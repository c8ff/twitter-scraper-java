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

package dev.seeight.twitterscraper.config.user;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.Tweet;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

// This rarely gets used, so it was hard to figure out its existence...
public class ConfigTweetResultsByRestIds implements IConfigJsonTree<ConfigTweetResultsByRestIds.TweetResults> {
	public final String[] tweetIds;
	public boolean includePromotedContent = true;
	public boolean withBirdwatchNotes = true;
	public boolean withVoice = true;
	public boolean withCommunity = true;

	public ConfigTweetResultsByRestIds(String[] tweetIds) {
		this.tweetIds = tweetIds;
	}

	@Override
	public TweetResults fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return TweetResults.fromJson(gson, new JsonHelper(element), element);
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("variables", gson.toJson(this))
			.addParameter("features", gson.toJson(graphQL.get("TweetResultsByRestIds").features))
			.build();
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return graphQL.get("TweetResultsByRestIds").url;
	}

	public static class TweetResults {
		public List<Tweet> tweets;

		public TweetResults() {
		}

		public TweetResults(List<Tweet> tweets) {
			this.tweets = tweets;
		}

		public static TweetResults fromJson(Gson gson, JsonHelper h, JsonElement z) {
			h.set(z);
			List<Tweet> tweets = new ArrayList<>();
			for (JsonElement e : h.next("data").array("tweetResult")) {
				Tweet t = Tweet.fromJson(gson, h.set(e).object("result"), h);
				tweets.add(t);
			}
			return new TweetResults(tweets);
		}
	}
}
