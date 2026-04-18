package dev.seeight.twitterscraper.config.user;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;

import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class ConfigFollowersYouKnow implements IConfigJsonTree<ConfigFollowersYouKnow.Yo> {
	public String userId;
	public String cursor;
	public int count = 20;
	public boolean includePromotedContent = false;
	public boolean withGrokTranslatedBio = false;

	public ConfigFollowersYouKnow(String userId) {
		this.userId = userId;
	}

	public ConfigFollowersYouKnow(String userId, String cursor) {
		this.userId = userId;
		this.cursor = cursor;
	}

	@Override
	public Yo fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		var m = new ConfigFollowersYouKnow.Yo();

		JsonHelper h = new JsonHelper(element);
		h.next("data")
			.next("user")
			.next("result")
			.next("timeline");

		if (!h.has("timeline")) {
			m.instructions = Collections.emptyList();
			return m;
		}
		h.next("timeline");

		JsonArray instructions = h.array("instructions");
		m.instructions = Instruction.fromInstructionsJson(gson, h, instructions);
		return m;
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		var op = api.getGraphQLOperation("FollowersYouKnow");
		return op.getUrl(gson.toJson(this));
	}

	public static class Yo extends Timeline {
	}
}
