package dev.seeight.twitterscraper.impl.item;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.impl.Tweet;
import dev.seeight.twitterscraper.impl.user.User;
import dev.seeight.twitterscraper.util.JsonHelper;
import dev.seeight.util.ListUtil;

import java.util.List;

public class NotificationF extends Entry {
    public String itemType;
    public String typename;
    public String id;
    public String notificationIcon;
    public RichMessage richMessage;
    public NotificationUrl notificationUrl;
    public Template template;
    public String timestampMs;

    public static class RichMessage {
        public Boolean rtl;
        public String text;
        public List<Entities> entities;

        public static class Entities {
            public Integer fromIndex;
            public Integer toIndex;
            public Ref ref;

            public static class Ref {
                public String type;
                public UserResults userResults;

                public static class UserResults {
                    public User result;

                    public static UserResults fromJson(JsonHelper h, JsonObject z) {
                        var o = new UserResults();
                        h.set(z);
                        // TODO: WHAT
                        o.result = User.fromJson(new Gson(), h.object("result"), h);
                        h.set(z);
                        return o;
                    }
                }

                public static Ref fromJson(JsonHelper h, JsonObject z) {
                    var o = new Ref();
                    h.set(z);
                    o.type = h.string("type", null);
                    o.userResults = UserResults.fromJson(h, h.object("user_results"));
                    h.set(z);
                    return o;
                }
            }

            public static Entities fromJson(JsonHelper h, JsonObject z) {
                var o = new Entities();
                h.set(z);
                o.fromIndex = h.integer("fromIndex", -1);
                o.toIndex = h.integer("toIndex", -1);
                o.ref = Ref.fromJson(h, h.object("ref"));
                h.set(z);
                return o;
            }
        }

        public static RichMessage fromJson(JsonHelper h, JsonObject z) {
            var o = new RichMessage();
            h.set(z);
            o.rtl = h.bool("rtl");
            o.text = h.string("text");
            o.entities = ListUtil.map(h.array("entities"), elm -> Entities.fromJson(h, elm.getAsJsonObject()));
            h.set(z);
            return o;
        }
    }

    public static class NotificationUrl {
        public String url;
        public String urlType;
        public UrtEndpointOptions urtEndpointOptions;

        public static class UrtEndpointOptions {
            public String cacheId;
            public String subtitle;
            public String title;
            public static UrtEndpointOptions fromJson(JsonHelper h, JsonObject z) {
                var o = new UrtEndpointOptions();
                h.set(z);
                o.cacheId = h.string("cacheId");
                o.subtitle = h.string("subtitle", null);
                o.title = h.string("title");
                return o;
            }
        }

        public static NotificationUrl fromJson(JsonHelper h, JsonObject z) {
            var o = new NotificationUrl();
            h.set(z);
            o.url = h.string("url");
            o.urlType = h.string("urlType");
            if (h.has("urtEndpointOptions"))
                o.urtEndpointOptions = UrtEndpointOptions.fromJson(h, h.object("urtEndpointOptions"));
            return o;
        }
    }

    public static class Template {
        public String typename;
        public List<TargetObjects> targetObjects;
        public List<FromUsers> fromUsers;

        public static class TargetObjects {
            public String typename;
            public TweetResults tweetResults;

            public static class TweetResults {
                public Tweet result;

                public static TweetResults fromJson(JsonHelper h, JsonObject z) {
                    var o = new TweetResults();
                    h.set(z);
                    // TODO: WHATT
                    o.result = Tweet.fromJson(new Gson(), h.object("result"), h);
                    h.set(z);
                    return o;
                }
            }

            public static TargetObjects fromJson(JsonHelper h, JsonObject z) {
                var o = new TargetObjects();
                h.set(z);
                o.typename = h.string("__typename", null);
                o.tweetResults = TweetResults.fromJson(h, h.object("tweet_results"));
                h.set(z);
                return o;
            }
        }

        public static class FromUsers {
            public String typename;
            public UserResults userResults;

            public static class UserResults {
                public User result;

                public static UserResults fromJson(JsonHelper h, JsonObject z) {
                    var o = new UserResults();
                    h.set(z);
                    // TODO: WHAT!!!
                    o.result = User.fromJson(new Gson(), h.object("result"), h);
                    h.set(z);
                    return o;
                }
            }

            public static FromUsers fromJson(JsonHelper h, JsonObject z) {
                var o = new FromUsers();
                h.set(z);
                o.typename = h.string("__typename", null);
                o.userResults = UserResults.fromJson(h, h.object("user_results"));
                h.set(z);
                return o;
            }
        }

        public static Template fromJson(JsonHelper h, JsonObject z) {
            var o = new Template();
            h.set(z);
            o.typename = h.string("__typename");
            o.targetObjects = ListUtil.map(h.array("target_objects"), elm -> TargetObjects.fromJson(h, elm.getAsJsonObject()));
            h.set(z);
            o.fromUsers = ListUtil.map(h.array("from_users"), elm -> FromUsers.fromJson(h, elm.getAsJsonObject()));
            return o;
        }
    }

    public static NotificationF fromJson(JsonHelper h, JsonObject z) {
        var o = new NotificationF();
        h.set(z);
        o.itemType = h.string("itemType");
        o.typename = h.string("__typename");
        o.id = h.string("id");
        o.notificationIcon = h.string("notification_icon");
        o.richMessage = RichMessage.fromJson(h, h.object("rich_message"));
        h.set(z);
        o.notificationUrl = NotificationUrl.fromJson(h, h.object("notification_url"));
        h.set(z);
        o.template = Template.fromJson(h, h.object("template"));
        h.set(z);
        o.timestampMs = h.string("timestamp_ms");
        return o;
    }
}
