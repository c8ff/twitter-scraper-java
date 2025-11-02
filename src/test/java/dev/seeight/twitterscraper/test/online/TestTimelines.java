package dev.seeight.twitterscraper.test.online;

import dev.seeight.twitterscraper.IConfig;
import dev.seeight.twitterscraper.config.interaction.ConfigBlockCreate;
import dev.seeight.twitterscraper.config.interaction.ConfigBlockDestroy;
import dev.seeight.twitterscraper.config.timeline.*;
import dev.seeight.twitterscraper.config.user.ConfigListByRestId;
import dev.seeight.twitterscraper.config.user.ConfigUserLikes;
import dev.seeight.twitterscraper.config.user.ConfigUserMedia;
import dev.seeight.twitterscraper.config.user.ConfigUserTweetsReplies;
import dev.seeight.twitterscraper.test.g.TestApiController;
import org.junit.jupiter.api.Test;

import java.util.function.Consumer;

public class TestTimelines {
	protected <T> void scrapDefault(IConfig<T> config, Consumer<T> consumer) throws Exception {
        TestApiController.insideContext((TestApiController.ContextConsumer<Void>) (api, client) -> {
            consumer.accept(api.scrap(config, client));
            return null;
        });
	}

	@Test
	public void configDetail() throws Exception {
		scrapDefault(new ConfigTweetDetail("999149383038971904", null), tweetDetail -> {
			System.out.println(tweetDetail);
		});
	}

	@Test
	public void configUserMedia() throws Exception {
		scrapDefault(new ConfigUserMedia("2455740283", null), media -> {
			System.out.println(media);
		});
	}

	@Test
	public void configUserTweetsReplies() throws Exception {
		scrapDefault(new ConfigUserTweetsReplies("2455740283"), media -> {
			System.out.println(media);
		});
	}

	@Test
	public void configLikes() throws Exception {
		scrapDefault(new ConfigUserLikes("2455740283", null), likes -> {
			System.out.println(likes);
		});
	}

	@Test
	public void configSearch() throws Exception {
		scrapDefault(new ConfigSearchTimeline("from:mrbeast", null), search -> {
			System.out.println(search);
		});
	}

	@Test
	public void configBookmarks() throws Exception {
		scrapDefault(new ConfigBookmarks(null), search -> {
			System.out.println(search);
		});
	}

	@Test
	public void configCommunityTweets() throws Exception {
		scrapDefault(new ConfigCommunityTweets("1577447698915024897", null), search -> {
			System.out.println(search);
		});
	}

	@Test
	public void configBlockDestroyTest() throws Exception {
		scrapDefault(new ConfigBlockDestroy("44196397"), search -> {
			System.out.println(search);
		});
	}

	@Test
	public void configBlockCreateTest() throws Exception {
		scrapDefault(new ConfigBlockCreate("44196397"), search -> {
			System.out.println(search);
		});
	}

	@Test
	public void configPinnedTimelines() throws Exception {
		scrapDefault(new ConfigPinnedTimelines(), search -> {
			System.out.println(search);
		});
	}

	@Test
	public void configListLatestTweetsTimelineByRestId() throws Exception {
		scrapDefault(new ConfigListTimeline("1803982053651755056", null), search -> {
			System.out.println(search);
		});
	}

	@Test
	public void configListByRestId() throws Exception {
		scrapDefault(new ConfigListByRestId("1803982053651755056"), search -> {
			System.out.println(search);
		});
	}

	@Test
	public void configTweetDetail() throws Exception {
		scrapDefault(new ConfigTweetDetail("1804264748189351999", null), search -> {
			System.out.println(search);
		});
	}
}
