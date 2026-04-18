package dev.seeight.twitterscraper.config.translate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.translation.TranslationResult;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;

import java.net.URISyntaxException;
import java.util.List;

public class ConfigGoogleTranslateTweet implements IConfigJsonTree<TranslationResult> {
	public final String tweetId;
	public final String destinationLanguage;
	public String translationSource = "Some(Google)";
	public String feature = "None";
	public String timeout = "None";
	public String onlyCached = "None";

	public ConfigGoogleTranslateTweet(String tweetId) {
		this.tweetId = tweetId;
		this.destinationLanguage = "None";
	}

	public ConfigGoogleTranslateTweet(String tweetId, String destinationLanguage) {
		this.tweetId = tweetId;
		this.destinationLanguage = destinationLanguage;
	}

	@Override
	public TranslationResult fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return TranslationResult.fromJson(element, new JsonHelper(element));
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		// What the fuck is this URL man lmao
		return HttpUrl.get("https://x.com/i/api/1.1/strato/column/None/tweetId=%s,destinationLanguage=%s,translationSource=%s,feature=%s,timeout=%s,onlyCached=%s/translation/service/translateTweet"
			.formatted(tweetId, destinationLanguage, translationSource, feature, timeout, onlyCached));
	}
}
