package dev.seeight.twitterscraper.config.user;

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
import java.util.List;

public class ConfigListMembers implements IConfigJsonTree<ConfigListMembers.ListMembers> {
    public final String listId;
    public String cursor;
    public int count = 20;

    public ConfigListMembers(String listId) {
        this.listId = listId;
    }

    @Override
    public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
        var v = new JsonObject();
        v.addProperty("listId", this.listId);
        v.addProperty("count", this.count);
        if (cursor != null) v.addProperty("cursor", this.cursor);
        return builder
                .addParameter("variables", v.toString())
                .addParameter("features", gson.toJson(graphQL.get("ListMembers").features))
                .build();
    }

    @Override
    public ListMembers fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return ListMembers.fromJson(gson, new JsonHelper(element), element.getAsJsonObject());
    }

    @Override
    public String getBaseURL(GraphQLMap graphQL) {
        return graphQL.get("ListMembers").url;
    }

    public static class ListMembers extends Timeline {
        public static ListMembers fromJson(Gson gson, JsonHelper h, JsonObject o) {
            ListMembers m = new ListMembers();
            m.instructions = Instruction.fromInstructionsJson(gson, h, h.set(o).next("data").next("list").next("members_timeline").next("timeline").array("instructions"));
            return m;
        }
    }
}
