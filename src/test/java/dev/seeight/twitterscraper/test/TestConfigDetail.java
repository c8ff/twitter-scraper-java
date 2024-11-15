package dev.seeight.twitterscraper.test;

import dev.seeight.twitterscraper.Secret;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.config.timeline.ConfigTweetDetail;
import dev.seeight.twitterscraper.impl.timeline.TweetDetail;
import dev.seeight.twitterscraper.util.JsonUtil;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class TestConfigDetail {
	public static void main(String[] args) {
		TwitterApi api = TwitterApi.newTwitterApi();
		Secret.defineFromFile(api);

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			TweetDetail result = api.scrap(new ConfigTweetDetail("999149383038971904", null), client);
			System.out.println(result);
			Files.writeString(new File("tweet-detail.json").toPath(), JsonUtil.toJson(result));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}