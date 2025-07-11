package dev.seeight.twitterscraper.impl;

import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.user.LegacyUser11;
import dev.seeight.twitterscraper.util.JsonHelper;
import dev.seeight.util.ListUtil;

import java.util.List;

public class TypeAhead {
    public Integer numResults;
    public List<LegacyUser11> users;
    public List<Topics> topics;
    public Integer completedIn;
    public String query;

    public static class Topics {
        public String topic;
        public Integer roundedScore;
        public Boolean inline;
        public static Topics fromJson(JsonHelper h, JsonObject z) {
            var o = new Topics();
            h.set(z);
            o.topic = h.string("topic", null);
            o.roundedScore = h.integer("rounded_score", -1);
            o.inline = h.bool("inline", false);
            return o;
        }
    }
    public static TypeAhead fromJson(JsonHelper h, JsonObject z) {
        var o = new TypeAhead();
        h.set(z);
        o.numResults = h.integer("num_results");
        o.users = ListUtil.map(h.array("users"), elm -> LegacyUser11.fromJson(h, elm.getAsJsonObject()));
        h.set(z);
        o.topics = ListUtil.map(h.array("topics"), elm -> Topics.fromJson(h, elm.getAsJsonObject()));
        h.set(z);
        o.completedIn = h.integer("completed_in");
        o.query = h.string("query");
        return o;
    }
}
