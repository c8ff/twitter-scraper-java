package dev.seeight.twitterscraper.test;

import dev.seeight.twitterscraper.Secret;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.config.ConfigFollowers;
import dev.seeight.twitterscraper.config.ConfigFollowing;
import dev.seeight.twitterscraper.impl.item.ItemList;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;

public class TestConfigFollowers {
	@Test
	public void followers() {
		TwitterApi api = TwitterApi.newTwitterApi();
		Secret.defineFromFile(api);

		try (CloseableHttpClient c = HttpClients.createDefault()) {
			ItemList s = api.scrap(new ConfigFollowers("833838358996611073"), c);
			System.out.println(s);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void following() {
		TwitterApi api = TwitterApi.newTwitterApi();
		Secret.defineFromFile(api);

		try (CloseableHttpClient c = HttpClients.createDefault()) {
			ItemList s = api.scrap(new ConfigFollowing("833838358996611073", "1779700983428914983|1773539578955694010"), c);
			System.out.println(s);
		} catch (IOException | URISyntaxException e) {
			e.printStackTrace();
		}
	}
}
