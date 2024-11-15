package dev.seeight.twitterscraper.test;

import dev.seeight.twitterscraper.Secret;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.TwitterException;
import dev.seeight.twitterscraper.config.interaction.ConfigFavoriteTweet;
import dev.seeight.twitterscraper.config.interaction.ConfigUnFavoriteTweet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestFavoriteTweet {
	public static void main(String[] args) {
		TwitterApi api = TwitterApi.newTwitterApi();
		Secret.defineFromFile(api);

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			try {
				String result = api.scrap(new ConfigFavoriteTweet("1742761316022895009"), client);
				System.out.println(result);
			} catch (TwitterException e) {
				System.out.println("already liked tweet.");
			}
			try {
				String result = api.scrap(new ConfigUnFavoriteTweet("1742761316022895009"), client);
				System.out.println(result);
			} catch (TwitterException e) {
				System.out.println("already un favorite tweet.");
			}
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
