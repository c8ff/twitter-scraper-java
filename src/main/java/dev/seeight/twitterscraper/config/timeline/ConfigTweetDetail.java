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
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.timeline.TweetDetail;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.core5.net.URIBuilder;
import org.jetbrains.annotations.Nullable;

import java.net.URI;
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
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("TweetDetail").url;
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("variables", gson.toJson(this))
			.addParameter("features", gson.toJson(graphQL.get("TweetDetail").features))
			.addParameter("fieldToggles", "{\"withArticleRichContentState\":true,\"withArticlePlainText\":false,\"withGrokAnalyze\":false}")
			.build();
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
