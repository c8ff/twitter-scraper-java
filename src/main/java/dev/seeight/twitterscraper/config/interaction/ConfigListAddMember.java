package dev.seeight.twitterscraper.config.interaction;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.TwitterList;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigListAddMember implements IConfigJsonTree<TwitterList> {
    private final String listId;
    private final String userId;

    public ConfigListAddMember(String listId, String userId) {
        this.listId = listId;
        this.userId = userId;
    }

    @Override
    public TwitterList fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        JsonHelper h = new JsonHelper(element);
        h.next("data").next("list");
        return TwitterList.fromJson(gson, h.object(), h);
    }

    @Override
    public HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
        GraphQLMap.Entry q = graphQL.get("ListAddMember");

        JsonObject variables = new JsonObject();
        variables.addProperty("listId", this.listId);
        variables.addProperty("userId", this.userId);

        JsonObject o = new JsonObject();
        o.add("features", gson.toJsonTree(q.features));
        o.addProperty("queryId", q.queryId);
        o.add("variables", variables);

        return TwitterApi.newJsonPostRequest(uri, o.toString());
    }

    @Override
    public String getBaseURL(GraphQLMap graphQL) {
        return graphQL.get("ListAddMember").url;
    }
}
