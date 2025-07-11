package dev.seeight.twitterscraper.config.timeline;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.Timeline;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;

public class ConfigRetweeters implements IConfigJsonTree<ConfigRetweeters.Retweeters> {
    public final String tweetId;
    public int count = 20;
    public String cursor;
    public boolean includePromotedContent = true;

    public ConfigRetweeters(String tweetId) {
        this.tweetId = tweetId;
    }

    public ConfigRetweeters(String tweetId, String cursor) {
        this.tweetId = tweetId;
        this.cursor = cursor;
    }

    @Override
    public Retweeters fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return Retweeters.fromJson(gson, new JsonHelper(element), element);
    }

    @Override
    public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
        return builder
                .addParameter("variables", gson.toJson(this))
                .addParameter("features", gson.toJson(graphQL.get("Retweeters").features))
                .build();
    }

    @Override
    public String getBaseURL(GraphQLMap graphQL) {
        return graphQL.get("Retweeters").url;
    }

    public static class Retweeters extends Timeline {
        public Retweeters() {
        }

        public Retweeters(List<Instruction> instructions) {
            this.instructions = instructions;
        }

        public static Retweeters fromJson(Gson gson, JsonHelper h, JsonElement z) {
            h.set(z).next("data").next("retweeters_timeline");
            if (!h.tryNext("timeline")) return new Retweeters(Collections.emptyList());
            return new Retweeters(Instruction.fromInstructionsJson(gson, h, h.array("instructions")));
        }
    }
}
