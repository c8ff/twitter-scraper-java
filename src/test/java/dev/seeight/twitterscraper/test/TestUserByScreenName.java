package dev.seeight.twitterscraper.test;

import dev.seeight.twitterscraper.Secret;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.config.user.ConfigUserByScreenName;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.twitterscraper.util.JsonUtil;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;

public class TestUserByScreenName {
	public static void main(String[] args) {
		TwitterApi api = TwitterApi.newTwitterApi();
		Secret.defineFromFile(api);

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			User result = api.scrap(new ConfigUserByScreenName("X"), client);
			System.out.println(result);
			Files.writeString(new File("user_by_screen_name.json").toPath(), JsonUtil.toJson(result));
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}