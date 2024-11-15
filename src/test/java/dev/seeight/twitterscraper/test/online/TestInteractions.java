package dev.seeight.twitterscraper.test.online;

import dev.seeight.twitterscraper.IConfig;
import dev.seeight.twitterscraper.Secret;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.config.interaction.ConfigFollowCreate;
import dev.seeight.twitterscraper.config.interaction.ConfigFollowDestroy;
import dev.seeight.twitterscraper.config.interaction.ConfigRemoveFollower;
import org.apache.hc.client5.http.impl.classic.CloseableHttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.function.Consumer;

public class TestInteractions {
	protected <T> void scrapDefault(IConfig<T> config, Consumer<T> consumer) throws Exception {
		TwitterApi api = TwitterApi.newTwitterApi();
		Secret.defineFromFile(api, new File("D:\\dev\\Java\\c8ff\\twitter\\xeno.web\\run\\secret.json"));

		try (CloseableHttpClient client = HttpClients.createDefault()) {
			consumer.accept(api.scrap(config, client));
		}
	}

	@Test
	public void followCreate() throws Exception {
		scrapDefault(new ConfigFollowCreate("3023961057"), System.out::println);
	}

	@Test
	public void followDestroy() throws Exception {
		scrapDefault(new ConfigFollowDestroy("3023961057"), System.out::println);
	}
	@Test
	public void removeFollower() throws Exception {
		scrapDefault(new ConfigRemoveFollower("2342563641235"), System.out::println);
	}
}
