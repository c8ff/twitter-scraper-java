package dev.seeight.twitterscraper.config.timeline;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
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

public class ConfigListOwnerships implements IConfigJsonTree<ConfigListOwnerships.ListOwnerships> {
    /**
     * From User ID (in most cases, self ID)
     */
    public final String userId;
    /**
     * Target User ID
     */
    public final String isListMemberTargetUserId;
    public int count = 20;

    public ConfigListOwnerships(String userId, String isListMemberTargetUserId) {
        this.userId = userId;
        this.isListMemberTargetUserId = isListMemberTargetUserId;
    }

    @Override
    public ListOwnerships fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return ListOwnerships.fromJson(gson, element, new JsonHelper(element));
    }

    @Override
    public String getBaseURL(GraphQLMap graphQL) {
        return graphQL.get("ListOwnerships").url;
    }

    @Override
    public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
        return builder
                .addParameter("variables", gson.toJson(this))
                .addParameter("features", gson.toJson(graphQL.get("ListOwnerships").features))
                .build();
    }

    public static class ListOwnerships extends Timeline {
        public static ListOwnerships fromJson(Gson gson, JsonElement element, JsonHelper h) {
            h.set(element);
            var l = new ListOwnerships();
            if (h.tryNext("data") && h.tryNext("user") && h.tryNext("result") && h.tryNext("timeline") && h.tryNext("timeline")) {
                l.instructions = Instruction.fromInstructionsJson(gson, h, h.array("instructions"));
            } else {
                l.instructions = Collections.emptyList();
            }
            return l;
        }
    }
}
