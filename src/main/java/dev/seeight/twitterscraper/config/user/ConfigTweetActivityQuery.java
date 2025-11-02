package dev.seeight.twitterscraper.config.user;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.TwitterApi;
import dev.seeight.twitterscraper.impl.TwitterError;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.util.JsonHelper;
import dev.seeight.util.ListUtil;
import okhttp3.HttpUrl;

import java.net.URISyntaxException;
import java.util.List;

public class ConfigTweetActivityQuery implements IConfigJsonTree<ConfigTweetActivityQuery.JoeSigma> {
    public final String restId;
    public final String from_time;
    public final String to_time;
    public final String first_48_hours_time;
    public String[] requested_organic_metrics = new String[] {"DetailExpands", "Engagements", "Follows", "Impressions", "LinkClicks", "ProfileVisits"};
    public String[] requested_promoted_metrics = new String[] {"DetailExpands", "Engagements", "Follows", "Impressions", "LinkClicks", "ProfileVisits", "CostPerFollower"};

    public ConfigTweetActivityQuery(String restId, String fromTime, String toTime, String first48HoursTime) {
        this.restId = restId;
        this.from_time = fromTime;
        this.to_time = toTime;
        this.first_48_hours_time = first48HoursTime;
    }

    @Override
    public JoeSigma fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        JsonHelper h = new JsonHelper(element);
        return JoeSigma.fromJson(h, h.next("data").next("tweet_result_by_rest_id").object("result"));
    }

    @Override
    public HttpUrl getUrl(Gson gson, TwitterApi api) throws URISyntaxException {
        // Not available in internal twitter document
        return HttpUrl.get("https://x.com/i/api/graphql/vnwexpl0q33_Bky-SROVww/TweetActivityQuery").newBuilder()
                .addQueryParameter("variables", gson.toJson(this))
                .addQueryParameter("features", "{\"responsive_web_tweet_analytics_m3_enabled\":false}")
                .build();
    }

    public static class JoeSigma {
        public String typename;
        public List<DatapointsGrid> datapointsGrid;
        public List<DatapointsGrid> promotionInfo;
        public List<Video> video;
        public String id;

        public static class DatapointsGrid {
            public String metricType;
            public Integer metricValue;
            public static DatapointsGrid fromJson(JsonHelper h, JsonObject z) {
                var o = new DatapointsGrid();
                h.set(z);
                o.metricType = h.string("metric_type", null);
                o.metricValue = h.integer("metric_value", -1);
                return o;
            }
        }

        public static class Video {
            public String metricType;
            public Integer metricValue;
            public static Video fromJson(JsonHelper h, JsonObject z) {
                var o = new Video();
                h.set(z);
                o.metricType = h.string("metric_type", null);
                o.metricValue = h.integer("metric_value", -1);
                return o;
            }
        }
        public static JoeSigma fromJson(JsonHelper h, JsonObject z) {
            var o = new JoeSigma();
            h.set(z);
            o.typename = h.string("__typename");
            o.datapointsGrid = ListUtil.map(h.array("datapoints_grid"), elm -> DatapointsGrid.fromJson(h, elm.getAsJsonObject()));
            h.set(z);
            o.promotionInfo = ListUtil.map(h.array("promotion_info"), elm -> DatapointsGrid.fromJson(h, elm.getAsJsonObject()));
            h.set(z);
            o.video = ListUtil.map(h.array("video"), elm -> Video.fromJson(h, elm.getAsJsonObject()));
            h.set(z);
            o.id = h.string("id");
            return o;
        }
    }
}
