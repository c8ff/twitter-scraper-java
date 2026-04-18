package dev.seeight.twitterscraper.config.translate;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.translation.GrokTranslationResult;
import dev.seeight.twitterscraper.util.JsonHelper;
import okhttp3.HttpUrl;
import okhttp3.Request;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigGrokTranslateTweet implements IConfigJsonTree<GrokTranslationResult> {
	public String content_type = "POST";
	public String dst_lang;
	public final String id;

	public ConfigGrokTranslateTweet(String id) {
		this.id = id;
		this.dst_lang = "en";
	}

	public ConfigGrokTranslateTweet(String id, String dst_lang) {
		this.id = id;
		this.dst_lang = dst_lang;
	}

	@Override
	public GrokTranslationResult fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return GrokTranslationResult.fromJson(new JsonHelper(element), element.getAsJsonObject());
	}

	@Override
	public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
		return HttpUrl.get("https://api.x.com/2/grok/translation.json");
	}

	@Override
	public Request.Builder createRequest(Gson gson, HttpUrl url, TwitterApi api) throws URISyntaxException, MalformedURLException {
		return TwitterApi.jsonPostReq(url, "{\"content_type\":\"" + content_type + "\",\"dst_lang\":\"" + dst_lang + "\",\"id\":\"" + id + "\"}");
	}
}
