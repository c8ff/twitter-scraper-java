package dev.seeight.twitterscraper.impl;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.TwitterList;
import dev.seeight.twitterscraper.util.JsonHelper;
import dev.seeight.util.ListUtil;

import javax.annotation.processing.Generated;
import java.util.List;

@Generated("dev.seeight.twitterscraper.GenerateClassHelper")
public class Card {
    public String restId;
    public Legacy legacy;

    public static class Legacy {
        public List<BindingValues> bindingValues;
        public CardPlatform cardPlatform;
        public String name;
        public String url;

        public static class BindingValues {
            public String key;
            public Value value;

            public static class Value {
                public String scribeKey;
                public String stringValue;
                public String type;
                public Boolean booleanValue;
                public ImageValue imageValue;
                public ImageColorValue imageColorValue;

                public static class ImageColorValue {
                    public List<TwitterList.Palette> palette;

                    public static ImageColorValue fromJson(JsonHelper h, JsonObject z) {
                        h.set(z);
                        var a = new ImageColorValue();
                        a.palette = ListUtil.map(h.array("palette"), new ListUtil.Transformer<JsonElement, TwitterList.Palette>() {
                            @Override
                            public TwitterList.Palette transform(JsonElement jsonElement) {
                                return TwitterList.Palette.fromJson(jsonElement.getAsJsonObject(), h);
                            }
                        });
                        return a;
                    }
                }

                public static class ImageValue {
                    public int width;
                    public int height;
                    public String url;

                    public static ImageValue fromJson(JsonHelper h, JsonObject z) {
                        h.set(z);
                        var w = new ImageValue();
                        w.width = h.integer("width");
                        w.height = h.integer("height");
                        w.url = h.string("url");
                        return w;
                    }
                }

                public static Value fromJson(JsonHelper h, JsonObject z) {
                    var o = new Value();
                    h.set(z);
                    o.scribeKey = h.string("scribe_key", null);
                    o.stringValue = h.string("string_value", null);
                    o.type = h.string("type", null);
                    o.booleanValue = h.bool("boolean_value", false);
                    o.imageValue = h.has("image_value") ? ImageValue.fromJson(h, h.object("image_value")) : null;
                    o.imageColorValue = h.set(z).has("image_color_value") ? ImageColorValue.fromJson(h, h.object("image_color_value")) : null;
                    return o;
                }
            }
            public static BindingValues fromJson(JsonHelper h, JsonObject z) {
                var o = new BindingValues();
                h.set(z);
                o.key = h.string("key", null);
                o.value = Value.fromJson(h, h.object("value"));
                h.set(z);
                return o;
            }
        }

        public static class CardPlatform {
            public Platform platform;

            public static class Platform {
                public Audience audience;
                public Device device;

                public static class Audience {
                    public String name;
                    public static Audience fromJson(JsonHelper h, JsonObject z) {
                        var o = new Audience();
                        h.set(z);
                        o.name = h.string("name");
                        return o;
                    }
                }

                public static class Device {
                    public String name;
                    public String version;
                    public static Device fromJson(JsonHelper h, JsonObject z) {
                        var o = new Device();
                        h.set(z);
                        o.name = h.string("name");
                        o.version = h.string("version");
                        return o;
                    }
                }
                public static Platform fromJson(JsonHelper h, JsonObject z) {
                    var o = new Platform();
                    h.set(z);
                    o.audience = Audience.fromJson(h, h.object("audience"));
                    h.set(z);
                    o.device = Device.fromJson(h, h.object("device"));
                    h.set(z);
                    return o;
                }
            }
            public static CardPlatform fromJson(JsonHelper h, JsonObject z) {
                var o = new CardPlatform();
                h.set(z);
                o.platform = Platform.fromJson(h, h.object("platform"));
                h.set(z);
                return o;
            }
        }
        public static Legacy fromJson(JsonHelper h, JsonObject z) {
            var o = new Legacy();
            h.set(z);
            o.bindingValues = ListUtil.map(h.array("binding_values"), elm -> BindingValues.fromJson(h, elm.getAsJsonObject()));
            h.set(z);
            o.cardPlatform = CardPlatform.fromJson(h, h.object("card_platform"));
            h.set(z);
            o.name = h.string("name");
            o.url = h.string("url");
            return o;
        }
    }
    public static Card fromJson(JsonHelper h, JsonObject z) {
        var o = new Card();
        h.set(z);
        o.restId = h.string("rest_id");
        o.legacy = Legacy.fromJson(h, h.object("legacy"));
        h.set(z);
        return o;
    }
}