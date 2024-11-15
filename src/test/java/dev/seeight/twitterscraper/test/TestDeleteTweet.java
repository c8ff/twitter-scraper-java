package dev.seeight.twitterscraper.test;

import dev.seeight.twitterscraper.Secret;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.config.interaction.ConfigDeleteTweet;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestDeleteTweet {
	public static void main(String[] args) {
		TwitterApi api = TwitterApi.newTwitterApi();
		Secret.defineFromFile(api);

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			boolean result = api.scrap(new ConfigDeleteTweet("1742783351495209136"), client);
			System.out.println(result);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
