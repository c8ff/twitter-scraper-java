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
