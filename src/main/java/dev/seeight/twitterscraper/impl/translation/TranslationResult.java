package dev.seeight.twitterscraper.impl.translation;

import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.impl.Tweet;
import dev.seeight.twitterscraper.util.JsonHelper;

public class TranslationResult {
	public String id;
	public String id_str;
	public String translation;
	public Tweet.TweetEntities entities;
	public String translationState;
	public String sourceLanguage;
	public String localizedSourceLanguage;
	public String destinationLanguage;
	public String translationSource;

	public static TranslationResult fromJson(JsonElement element, JsonHelper h) {
		TranslationResult r = new TranslationResult();
		h.set(element);
		r.id = h.string("id");
		r.id_str = h.string("id_str");
		r.translation = h.string("translation");
		r.entities = Tweet.TweetEntities.fromJson(h.object("entities"), h);
		h.set(element);
		r.translationState = h.string("translationState");
		r.sourceLanguage = h.string("sourceLanguage");
		r.localizedSourceLanguage = h.string("localizedSourceLanguage");
		r.destinationLanguage = h.string("destinationLanguage");
		r.translationSource = h.string("translationSource");
		return r;
	}
}
