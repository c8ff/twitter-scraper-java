/*
 * twitter-scraper-java.main
 * Copyright (C) 2025 c8ff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.seeight.twitterscraper.config.timeline;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.timeline.TweetDetail;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;
import org.jetbrains.annotations.Nullable;

import java.net.URISyntaxException;
import java.util.List;

public class ConfigTweetDetail implements IConfigJsonTree<TweetDetail> {
	@SerializedName("focalTweetId")
	public final String tweetId;
	@Nullable
	public final String cursor;
	public RankingMode rankingMode = null;
	public String referrer = "tweet";
	public String controller_data = null;
	public boolean with_rux_injections = false;
	public boolean includePromotedContent = true;
	public boolean withCommunity = true;
	public boolean withQuickPromoteEligibilityTweetFields = true;
	public boolean withBirdwatchNotes = true;
	public boolean withVoice = true;
	public boolean withV2Timeline = true;

	public ConfigTweetDetail(String tweetId, @Nullable String cursor) {
		this.tweetId = tweetId;
		this.cursor = cursor;
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		var op = api.getGraphQLOperation("TweetDetail");
		return op.getUrl(gson.toJson(this));
	}

	@Override
	public TweetDetail fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return TweetDetail.fromJson(gson, new JsonHelper(element), element);
	}

	public enum RankingMode {
		Relevance,
		Recency,
		Likes
	}
}
