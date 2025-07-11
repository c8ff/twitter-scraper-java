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

public class ConfigPinTimeline implements IConfigJsonTree<ConfigPinTimeline.PinTimelineResult> {
    public final String id;
    public final String pinnedTimelineType;

    public ConfigPinTimeline(String id, String pinnedTimelineType) {
        this.id = id;
        this.pinnedTimelineType = pinnedTimelineType;
    }

    @Override
    public PinTimelineResult fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return PinTimelineResult.fromJson(gson, new JsonHelper(element), element.getAsJsonObject());
    }

    @Override
    public String getBaseURL(GraphQLMap graphQL) {
        return graphQL.get("PinTimeline").url;
    }

    @Override
    public HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
        GraphQLMap.Entry e = graphQL.get("PinTimeline");

        String variables = gson.toJson(new Variables(new Variables.PinnedTimelineItem(this.id, this.pinnedTimelineType)));
        String features = gson.toJson(e.features);
        String queryId = e.queryId;
        return TwitterApi.newJsonPostRequest(uri, "{\"variables\":" + variables + ",\"features\":" + features + ",\"queryId\":\"" + queryId + "\"}");
    }

    protected static class Variables {
        public PinnedTimelineItem pinnedTimelineItem;

        public Variables(PinnedTimelineItem pinnedTimelineItem) {
            this.pinnedTimelineItem = pinnedTimelineItem;
        }

        public static class PinnedTimelineItem {
            public String id;
            public String pinned_timeline_type;

            public PinnedTimelineItem(String id, String pinned_timeline_type) {
                this.id = id;
                this.pinned_timeline_type = pinned_timeline_type;
            }
        }
    }

    public static class PinTimelineResult {
        public TwitterList list;

        public static PinTimelineResult fromJson(Gson gson, JsonHelper h, JsonObject o) {
            PinTimelineResult tl = new PinTimelineResult();
            tl.list = TwitterList.fromJson(gson, h.set(o).next("data").next("pin_timeline").next("updated_pinned_timeline").next("list").object(), h);
            return tl;
        }

        public static PinTimelineResult fromJsonUnpin(Gson gson, JsonHelper h, JsonObject o) {
            PinTimelineResult tl = new PinTimelineResult();
            tl.list = TwitterList.fromJson(gson, h.set(o).next("data").next("unpin_timeline").next("updated_pinned_timeline").next("list").object(), h);
            return tl;
        }
    }
}
