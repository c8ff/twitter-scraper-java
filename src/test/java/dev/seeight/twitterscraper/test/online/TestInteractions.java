package dev.seeight.twitterscraper.test.online;

import dev.seeight.twitterscraper.config.interaction.*;
import dev.seeight.twitterscraper.config.user.ConfigTweetResultsByRestIds;
import dev.seeight.twitterscraper.test.g.TestApiController;
import org.junit.jupiter.api.Test;

public class TestInteractions {
	@Test
	public void favoriteTweet() {
        var result = TestApiController.insideContext((api, client) -> api.scrap(new ConfigFavoriteTweet("1773948632758206695"), client));
		System.out.println(result);
	}

	@Test
	public void unFavoriteTweet() {
        var result = TestApiController.insideContext((api, client) -> api.scrap(new ConfigUnFavoriteTweet("1773948632758206695"), client));
        System.out.println(result);
	}

	@Test
	public void followCreate() throws Exception {
        var result = TestApiController.insideContext((api, client) -> api.scrap(new ConfigFollowCreate("3023961057"), client));
        System.out.println(result);
	}

	@Test
	public void followDestroy() throws Exception {
        var result = TestApiController.insideContext((api, client) -> api.scrap(new ConfigFollowDestroy("3023961057"), client));
        System.out.println(result);
	}
	@Test
	public void removeFollower() throws Exception {
        var result = TestApiController.insideContext((api, client) -> api.scrap(new ConfigRemoveFollower("2342563641235"), client));
        System.out.println(result);
	}

	@Test
	public void pinTimeline() throws Exception {
        var result = TestApiController.insideContext((api, client) -> api.scrap(new ConfigPinTimeline("1873316187834110397", "List"), client));
        System.out.println(result);
	}
	@Test
	public void unpinTimeline() throws Exception {
        var result = TestApiController.insideContext((api, client) -> api.scrap(new ConfigUnPinTimeline("1873316187834110397", "List"), client));
        System.out.println(result);
	}
	@Test
	public void adasdsa() throws Exception {
        var result = TestApiController.insideContext((api, client) -> api.scrap(new ConfigTweetResultsByRestIds(new String[] {
                "1901699498704867423",
                "1928521879028891997",
                "1939025587563094424",
        }), client));
        System.out.println(result);
	}
}
