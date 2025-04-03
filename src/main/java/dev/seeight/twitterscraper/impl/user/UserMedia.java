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
import com.google.gson.annotations.SerializedName;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.impl.Url;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;

public class UserMedia extends Timeline {
	@Deprecated
	public static class Media {
		public Photo[] photos;
		public Video[] videos;
		public Video[] animatedGif;
	}

	@Deprecated
	public static class Photo {
		public String url;
		public String id;
		public String format;
		public PhotoSize[] sizes;
	}

	@Deprecated
	public static class Video {
		public String thumbnail;
		public String url;
		public String contentType;
		public int viewCount;
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

		@Nullable
		public VideoInfo videoInfo = null;
	}

	public static class OriginalInfo {
		public int width;
		public int height;
		public FocusRect[] focusRects;
	}

	public static class FocusRect {
		public int x;
		public int y;
		@SerializedName("w")
		public int width;
		@SerializedName("h")
		public int height;
	}

	public static class VideoInfo {
		public int[] aspectRatio;
		public int durationMillis;
		public Variant[] variants;
	}

	public static class Variant {
		public int bitrate;
		public String contentType;
		public String url;
	}

	public static class PhotoSize {
		public String name;
		public int width;
		public int height;
		public String resize;
	}

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
}
