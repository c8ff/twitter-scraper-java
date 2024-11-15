package dev.seeight.twitterscraper.test;

import dev.seeight.twitterscraper.Secret;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.config.user.ConfigUserMedia;
import dev.seeight.twitterscraper.impl.user.UserMedia;
import dev.seeight.twitterscraper.util.JsonUtil;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class TestUserMedia {
	public static void main(String[] args) {
		TwitterApi api = TwitterApi.newTwitterApi();
		Secret.defineFromFile(api);

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			UserMedia result = api.scrap(new ConfigUserMedia("783214", null), client);
			System.out.println(result);
			Files.writeString(new File("latest-timeline.json").toPath(), JsonUtil.toJson(result));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}