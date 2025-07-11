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

public class ConfigUnPinTimeline implements IConfigJsonTree<ConfigPinTimeline.PinTimelineResult> {
    public final String id;
    public final String pinnedTimelineType;

    public ConfigUnPinTimeline(String id, String pinnedTimelineType) {
        this.id = id;
        this.pinnedTimelineType = pinnedTimelineType;
    }

    @Override
    public ConfigPinTimeline.PinTimelineResult fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return ConfigPinTimeline.PinTimelineResult.fromJsonUnpin(gson, new JsonHelper(element), element.getAsJsonObject());
    }

    @Override
    public String getBaseURL(GraphQLMap graphQL) {
        return graphQL.get("UnpinTimeline").url;
    }

    @Override
    public HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
        GraphQLMap.Entry e = graphQL.get("UnpinTimeline");

        String variables = gson.toJson(new ConfigPinTimeline.Variables(new ConfigPinTimeline.Variables.PinnedTimelineItem(this.id, this.pinnedTimelineType)));
        String features = gson.toJson(e.features);
        String queryId = e.queryId;
        return TwitterApi.newJsonPostRequest(uri, "{\"variables\":" + variables + ",\"features\":" + features + ",\"queryId\":\"" + queryId + "\"}");
    }
}
