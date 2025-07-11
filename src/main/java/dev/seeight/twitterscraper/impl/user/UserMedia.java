/*
 * twitter-scraper-java
 * Copyright (C) 2024 c8ff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.seeight.twitterscraper.impl.user;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.SerializedName;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.impl.Range;
import dev.seeight.twitterscraper.impl.Url;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class UserMedia extends Timeline {
	public static UserMedia fromJson(Gson gson, JsonElement element) {
		JsonHelper h = new JsonHelper(element);

		h.next("data").next("user");

		UserMedia m = new UserMedia();
		if (!h.has("result")) {
			m.instructions = Collections.emptyList();
			return m;
		}

		h.next("result").next("timeline");
		if (!h.has("timeline")) {
			m.instructions = Collections.emptyList();
			return m;
		}

		JsonArray instructions = h.query("timeline", "instructions").getAsJsonArray();
		m.instructions = Instruction.fromInstructionsJson(gson, h, instructions);
		return m;
	}

	public static class MediaEntity {
		public Url url;
		@SerializedName("id_str")
		public String id;
		@SerializedName("media_key")
		@Nullable
		public String mediaKey;
		@SerializedName("media_url_https")
		public String mediaUrlHttps;
		public String type;
		public PhotoSize[] sizes;
		public OriginalInfo originalInfo;

		public Range indices;
		public boolean allowDownloadStatus;

		@Nullable
		public VideoInfo videoInfo = null;

		public static UserMedia.@NotNull MediaEntity fromJson(JsonObject object, JsonHelper helper) {
			UserMedia.MediaEntity e = new UserMedia.MediaEntity();

			e.url = Url.fromJson(object, helper);
			helper.set(object);
			e.indices = Range.fromArray(helper.array("indices"));
			e.id = helper.string("id_str");
			e.mediaKey = helper.string("media_key", null);
			e.mediaUrlHttps = helper.string("media_url_https");
			e.type = helper.string("type");
			if (helper.has("allow_download_status")) {
				if (object.get("allow_download_status").isJsonObject()) {
					e.allowDownloadStatus = helper.next("allow_download_status").bool("allow_download", false);
				} else {
					e.allowDownloadStatus = helper.bool("allow_download_status", false);
				}
			}

			helper.set(object).next("original_info");
			e.originalInfo = new UserMedia.OriginalInfo(
				helper.integer("width"),
				helper.integer("height"),
				!helper.has("focus_rects") ? Collections.emptyList() : helper.next("focus_rects").list(v -> {
					helper.set(v);
					return new UserMedia.FocusRect(
						helper.integer("x"),
						helper.integer("y"),
						helper.integer("w"),
						helper.integer("h")
					);
				})
			);

			helper.set(object);

			List<PhotoSize> sizesL = new ArrayList<>();
			JsonObject sizes = helper.object("sizes");
			for (Map.Entry<String, JsonElement> o : sizes.entrySet()) {
				sizesL.add(new UserMedia.PhotoSize(
					o.getKey(),
					helper.set(o.getValue()).integer("w"),
					helper.integer("h"),
					helper.string("resize", "fit")
				));
			}
			e.sizes = sizesL.toArray(new UserMedia.PhotoSize[0]);

			helper.set(object);
			if (helper.has("video_info")) {
				e.videoInfo = VideoInfo.fromJson(helper.object("video_info"), helper);
			}

			return e;
		}
	}

	public static class OriginalInfo {
		public int width;
		public int height;
		public List<FocusRect> focusRects;

		public OriginalInfo() {
		}

		public OriginalInfo(int width, int height, List<FocusRect> focusRects) {
			this.width = width;
			this.height = height;
			this.focusRects = focusRects;
		}
	}

	public static class FocusRect {
		public int x;
		public int y;
		@SerializedName("w")
		public int width;
		@SerializedName("h")
		public int height;

		public FocusRect() {
		}

		public FocusRect(int x, int y, int width, int height) {
			this.x = x;
			this.y = y;
			this.width = width;
			this.height = height;
		}
	}

	public static class VideoInfo {
		public int[] aspectRatio;
		public int durationMillis;
		public List<Variant> variants;

		public VideoInfo(int[] aspectRatio, int durationMillis, List<Variant> variants) {
			this.aspectRatio = aspectRatio;
			this.durationMillis = durationMillis;
			this.variants = variants;
		}

		public static @NotNull VideoInfo fromJson(JsonObject o, JsonHelper h) {
			h.set(o);
			return new UserMedia.VideoInfo(
				h.intArray("aspect_ratio"),
				h.integer("duration_millis", -1),
				h.next("variants").list(variant -> new Variant(
					h.set(variant).integer("bitrate", 0),
					h.string("content_type"),
					h.string("url")
				))
			);
		}
	}

	public static class Variant {
		public int bitrate;
		public String contentType;
		public String url;

		public Variant() {
		}

		public Variant(int bitrate, String contentType, String url) {
			this.bitrate = bitrate;
			this.contentType = contentType;
			this.url = url;
		}
	}

	public static class PhotoSize {
		public String name;
		public int width;
		public int height;
		public String resize;

		public PhotoSize() {
		}

		public PhotoSize(String name, int width, int height, String resize) {
			this.name = name;
			this.width = width;
			this.height = height;
			this.resize = resize;
		}
	}
}
