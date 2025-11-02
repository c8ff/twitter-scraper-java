/*
 * twitter-scraper-java.main
 * Copyright (C) 2025 c8ff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.seeight.twitterscraper.features;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.seeight.twitterscraper.JsonFormatException;
import dev.seeight.twitterscraper.util.JsonHelper;
import dev.seeight.util.MiscUtil;
import kotlin.text.Charsets;
import okhttp3.*;
import org.jetbrains.annotations.Nullable;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.time.Duration;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Utility class to fetch Twitter data needed to send requests.
 * @author c8ff
 */
public class FeatureFetcher {
	@SuppressWarnings("RegExpRedundantEscape") // android smh
	private static final Pattern startPattern = Pattern.compile("return [A-z]\\}\\,m\\.unparse=[A-z],e\\.exports=[A-z]\\},");
	private static final Pattern endPattern = Pattern.compile("[0-9]*:function\\([A-z],[A-z]\\)\\{");
	private static final Pattern entryPattern = Pattern.compile("[0-9]*:e=>\\{e\\.exports=\\{queryId:");

	public static class TwitterPage {
		public SelfUser user;
		private Map<String, JsonElement> features;
		private Map<String, JsonElement> u_features;

		public Map<String, JsonElement> getFeatures() {
			if (u_features == null) u_features = Collections.unmodifiableMap(features);
			return u_features;
		}
	}

	// TODO: add customization options for user agent and such
	/**
	 * Gets the featureSwitches, logged-in user data and fieldToggles. Fetches both 'x.com/home' and the main script.
	 *
	 * @param cookie A Twitter valid cookie list
	 */
	public static TwitterPage fetchTwitterPage(String cookie) {
        var client = new OkHttpClient.Builder().callTimeout(Duration.ofSeconds(20)).build();
        try {
            return fetchTwitterPage(cookie, client, null);
        } finally {
            client.dispatcher().executorService().shutdown();
            client.connectionPool().evictAll();
            try {
                client.cache().close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
    /**
     * Gets the featureSwitches, logged-in user data and fieldToggles. Fetches both 'x.com/home' and the main script.
     *
     * @param cookie A Twitter valid cookie list
     */
	public static TwitterPage fetchTwitterPage(String cookie, OkHttpClient client, @Nullable DocumentReceiver consumer) {
		var twitterPage = new TwitterPage();

        // fetch main page
        var url = HttpUrl.get("https://x.com/home");
        var req = new Request.Builder()
                .url(url)
                .header("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
                .header("Accept-Language", "en,en-US;q=0.5")
                .header("Connection", "keep-alive")
                .header("Cookie", cookie)
                .header("Host", "x.com")
                .header("Set-Fetch-Dest", "document")
                .header("Set-Fetch-Mode", "navigate")
                .header("Set-Fetch-Site", "cross-site")
                .header("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:140.0) Gecko/20100101 Firefox/140.0")
                .build();

		try (var response = client.newCall(req).execute()) {
			// parse the main page
            String rawDoc;

            try {
                rawDoc = response.body().string();
            } catch (Exception e) {
                MiscUtil.sneakyThrow(e);
                throw new RuntimeException();
            }

            var doc = Jsoup.parse(rawDoc);

            try {
                if (consumer != null) consumer.consume(rawDoc, doc);
            } catch (Exception e) {
                e.printStackTrace();
            }

			// get information inside JS initialization code
			for (Element e : doc.getElementsByTag("script")) {
				var data = e.data();
				if (!data.startsWith("window.__INITIAL_STATE__")) continue;

				// let's hope that no JS is actually on here...
				var cleanedUp = data.substring(data.indexOf('=') + 1, data.indexOf(";window.__META_DATA__")).trim();

				// TODO: Might be good to check the settings, user instance and session returned here also
				try {
					var a = JsonParser.parseString(cleanedUp);
					var featureSwitch = a.getAsJsonObject().get("featureSwitch");
					var config = featureSwitch.getAsJsonObject().get("user").getAsJsonObject().get("config");
					var user = a.getAsJsonObject().getAsJsonObject("entities").getAsJsonObject("users").getAsJsonObject("entities").asMap().values().iterator().next(); // dumbass

					Map<String, JsonElement> m = new HashMap<>();
					config.getAsJsonObject().asMap().forEach((s1, element) -> {
						if (element.isJsonObject()) m.put(s1, element.getAsJsonObject().get("value"));
					});
					twitterPage.features = m;
					twitterPage.user = SelfUser.fromJson(new JsonHelper(user), user.getAsJsonObject());
				} catch (JsonFormatException ex) {
					// TODO: Have a fallback here...
					throw new RuntimeException(ex);
				}
				// if (!searchForScripts) break;
			}
		} catch (Throwable e) {
			throw new RuntimeException(e);
		}

		return twitterPage;
	}

    public interface DocumentReceiver {
        void consume(String raw, Document doc) throws Exception;
    }
}
