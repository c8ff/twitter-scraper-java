package dev.seeight.twitterscraper.graphql;

import com.google.gson.Gson;
import com.google.gson.JsonElement;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class GraphQL {
	public List<Entry> entries;

	public static GraphQL fromURL(Gson gson) throws IOException {
		var stream = new URL("https://raw.githubusercontent.com/fa0311/TwitterInternalAPIDocument/refs/heads/develop/docs/json/GraphQL.json").openStream();
		ByteArrayOutputStream result = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		for (int length; (length = stream.read(buffer)) != -1; ) {
			result.write(buffer, 0, length);
		}
		stream.close();
		//noinspection CharsetObjectCanBeUsed (cuz of compat issues with android)
		var s = result.toString(StandardCharsets.UTF_8.name());
		return gson.fromJson("{\"entries\":" + s + "}", GraphQL.class);
	}

	public static GraphQL fromURL(Gson gson, String url) throws IOException {
		return gson.fromJson(new InputStreamReader(new URL(url).openStream()), GraphQL.class);
	}

	public static class Entry {
		public int n;
		public Exports exports;

		public static class Exports {
			public String queryId;
			public String operationName;
			public String operationType;
			public Metadata metadata;

			public static class Metadata {
				public List<String> featureSwitches;
				public List<String> fieldToggles;
				public Map<String, JsonElement> featureSwitch;
			}
		}
	}
}
