package dev.seeight.twitterscraper.config.timeline;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.api2.GlobalObjects;
import dev.seeight.twitterscraper.impl.api2.LegacyTimeline;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigApi2View implements IConfigJsonTree<ConfigApi2View.Api2View> {
    public final String url;

    public ConfigApi2View(String url) {
        this.url = url;
    }

    @Override
    public Api2View fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return Api2View.fromJson(new JsonHelper(element), element);
    }

    @Override
    public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
        return new URI(this.url);
    }

    @Override
    public String getBaseURL(GraphQLMap graphQL) {
        return "";
    }

    public static class Api2View {
        public GlobalObjects objects;
        public LegacyTimeline timeline;

        public Api2View() {
        }

        public Api2View(GlobalObjects objects, LegacyTimeline timeline) {
            this.objects = objects;
            this.timeline = timeline;
        }

        public static Api2View fromJson(JsonHelper h, JsonElement z) {
            h.set(z);
            return new Api2View(GlobalObjects.fromJson(h, h.object("globalObjects")), LegacyTimeline.fromJson(h, h.set(z).object("timeline")));
        }
    }
}
