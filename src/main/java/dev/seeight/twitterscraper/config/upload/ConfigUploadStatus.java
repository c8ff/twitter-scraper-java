package dev.seeight.twitterscraper.config.upload;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterException;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.upload.UploadedMedia;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.net.URIBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigUploadStatus implements IConfigJsonTree<UploadedMedia> {
	public final Command command = Command.STATUS;
	public final String mediaId;

	public ConfigUploadStatus(String mediaId) {
		this.mediaId = mediaId;
	}

	@Override
	public UploadedMedia resolve(HttpClient client, HttpUriRequestBase request, Gson gson) throws IOException, TwitterException {
		ConfigUploadInit.stripHeaders(request);
		return IConfigJsonTree.super.resolve(client, request, gson);
	}

	@Override
	public UploadedMedia fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
		return gson.fromJson(element, UploadedMedia.class);
	}

	@Override
	public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
		return builder
			.addParameter("command", this.command.name())
			.addParameter("media_id", this.mediaId)
			.build();
	}

	@Override
	public String getBaseURL(GraphQLMap graphQL) {
		return "https://upload.x.com/i/media/upload.json";
	}
}
