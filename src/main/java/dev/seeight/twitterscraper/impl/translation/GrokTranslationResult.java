package dev.seeight.twitterscraper.impl.translation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.Tweet;
import dev.seeight.twitterscraper.util.JsonHelper;

public class GrokTranslationResult {
	public String content_type;
	public String text;
	public Tweet.TweetEntities entities;

	public static GrokTranslationResult fromJson(JsonHelper h, JsonObject o) {
		h.set(o);
		h.next("result");
		GrokTranslationResult e = new GrokTranslationResult();
		e.content_type = h.string("content_type");
		e.text = h.string("text");
		e.entities = Tweet.TweetEntities.fromJson(h.object("entities"), h);
		return e;
	}
}
