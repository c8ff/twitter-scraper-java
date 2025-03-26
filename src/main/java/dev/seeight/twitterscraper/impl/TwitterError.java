package dev.seeight.twitterscraper.impl;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class TwitterError {
	public static final int ALREADY_RETWEETED = 327;
	public static final int ALREADY_FAVORITED = 139;
	public static final int ALREADY_UNFAVORITED = 144;
	public static final int TIMEOUT_UNSPECIFIED = 29;

	public String message;
	public String[] path;
	public int code;
	public String kind;
	public String name;
	public String source;

	public static TwitterError fromJson(JsonHelper h, JsonObject errorObj) {
		h.set(errorObj);

		TwitterError error = new TwitterError();
		error.message = h.string("message");
		error.code = h.integer("code");
		error.path = h.stringArray("path", new String[0]);
		error.kind = h.string("kind", null);
		error.name = h.string("name", null);
		error.source = h.string("source", null);
		return error;
	}

	public static List<TwitterError> fromJsonArray(JsonHelper h, JsonArray arr) {
		List<TwitterError> errors = new ArrayList<>();
		fromJsonArray(h, arr, errors);
		return errors;
	}

	public static void fromJsonArray(JsonHelper h, JsonArray arr, List<TwitterError> errors) {
		for (JsonElement e : arr) {
			h.set(e);
			errors.add(fromJson(h, e.getAsJsonObject()));
		}
	}
}
