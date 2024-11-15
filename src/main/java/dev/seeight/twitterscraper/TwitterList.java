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

package dev.seeight.twitterscraper;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class TwitterList {
	public long createdAt;
	public MediaInfo customBannerMedia;
	public MediaResult customBannerMediaResults;
	public MediaInfo defaultBannerMedia;
	public MediaResult defaultBannerMediaResults;
	public String description;
	public boolean following;
	public String id;
	public String idStr;
	public boolean isMember;
	public int memberCount;
	public String membersContext;
	public String mode;
	public boolean muting;
	public String name;
	public boolean pinning;
	public int subscriberCount;

	public User author;

	public static List<TwitterList> fromList(Gson gson, JsonArray array, JsonHelper h) {
		List<TwitterList> t = new ArrayList<>();

		for (JsonElement e : array) {
			if (!(e instanceof JsonObject j)) continue;

			String typeName = j.get("__typename").getAsString();
			if (typeName.equals("ListPinnedTimeline")) {
				t.add(TwitterList.fromJson(gson, h.set(j).object("list"), h));
			} else {
				System.out.println("dropping unknown pinned timeline: " + typeName);
			}
		}

		return t;
	}

	public static TwitterList fromJson(Gson gson, JsonObject root, JsonHelper h) {
		h.set(root);

		TwitterList s = new TwitterList();

		s.createdAt = h.asLong("created_at");
		s.description = h.string("description");
		s.following = h.bool("following");
		s.id = h.string("id");
		s.idStr = h.string("id_str");
		s.isMember = h.bool("is_member");
		s.memberCount = h.integer("member_count");
		s.membersContext = h.string("members_context");
		s.mode = h.string("mode");
		s.muting = h.bool("muting");
		s.name = h.string("name");
		s.pinning = h.bool("pinning");
		s.subscriberCount = h.integer("subscriber_count");

		s.customBannerMedia = MediaInfo.fromJson(h.set(root).next("custom_banner_media").object("media_info"), h);
		s.customBannerMediaResults = MediaResult.fromJson(h.set(root).next("custom_banner_media_results").object("result"), h);
		s.defaultBannerMedia = MediaInfo.fromJson(h.set(root).next("default_banner_media").object("media_info"), h);
		s.defaultBannerMediaResults = MediaResult.fromJson(h.set(root).next("default_banner_media_results").object("result"), h);

		s.author = User.fromJson(gson, h.set(root).next("user_results").object("result"), h);
		return s;
	}

	public static class MediaResult {
		public String id;
		public String mediaKey;
		public String mediaId;
		public MediaInfo mediaInfo;

		public static MediaResult fromJson(JsonObject o, JsonHelper h) {
			h.set(o);
			MediaResult r = new MediaResult();

			r.id = h.string("id");
			r.mediaKey = h.string("media_key");
			r.mediaId = h.string("media_id");
			r.mediaInfo = MediaInfo.fromJson(h.object("media_info"), h);

			return r;
		}
	}

	public static class MediaInfo {
		public String originalImgUrl;
		public int originalImgWidth;
		public int originalImgHeight;
		public SalientRect salientRect;
		public Palette[] palette;

		public static MediaInfo fromJson(JsonObject o, JsonHelper h) {
			h.set(o);

			MediaInfo m = new MediaInfo();
			m.originalImgUrl = h.string("original_img_url");
			m.originalImgWidth = h.integer("original_img_width");
			m.originalImgHeight = h.integer("original_img_height");
			m.salientRect = SalientRect.fromJson(h.set(o).object("salient_rect"), h);

			if (h.set(o).has("color_info")) {
				JsonArray object = h.set(o).next("color_info").array("palette");
				List<Palette> p = new ArrayList<>();
				for (JsonElement e : object) {
					if (!(e instanceof JsonObject j)) continue;
					p.add(Palette.fromJson(j, h));
				}
				m.palette = p.toArray(new Palette[0]);
			}

			return m;
		}
	}

	public static class SalientRect {
		public int left;
		public int top;
		public int width;
		public int height;

		public static SalientRect fromJson(JsonObject o, JsonHelper h) {
			h.set(o);
			SalientRect r = new SalientRect();
			r.height = h.integer("height");
			r.left = h.integer("left");
			r.width = h.integer("width");
			r.top = h.integer("top");
			return r;
		}
	}

	public static class Palette {
		public double percentage;
		public Rgb rgb;

		public static Palette fromJson(JsonObject o, JsonHelper h) {
			h.set(o);
			Palette t = new Palette();
			t.percentage = h.asDouble("percentage");
			t.rgb = Rgb.fromJson(h.set(o).object("rgb"), h);
			return t;
		}
	}

	public static class Rgb {
		public int blue;
		public int green;
		public int red;

		public static Rgb fromJson(JsonObject o, JsonHelper h) {
			h.set(o);
			Rgb r = new Rgb();
			r.red = h.integer("red");
			r.green = h.integer("green");
			r.blue = h.integer("blue");
			return r;
		}
	}
}
