/*
 * twitter-scraper-java.main
 * Copyright (C) 2025 c8ff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.seeight.twitterscraper.config.interaction;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.client5.http.classic.methods.HttpPost;
import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.apache.hc.core5.http.io.entity.StringEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigUpdate implements IConfigJsonTree<ConfigUpdate.Relationship> {
    public String include_profile_interstitial_type = "1";
    public String include_blocking = "1";
    public String include_blocked_by = "1";
    public String include_followed_by = "1";
    public String include_want_retweets = "1";
    public String include_mute_edge= "1";
    public String include_can_dm= "1";
    public String include_can_media_tag= "1";
    public String include_ext_is_blue_verified= "1";
    public String include_ext_verified_type= "1";
    public String include_ext_profile_image_shape= "1";
    public String skip_status = "1";
    public String cursor = "-1";
    public final String id;
    public Boolean retweets = null;
    public Boolean device = null;

    public ConfigUpdate(String id) {
        this.id = id;
    }

    @Override
    public Relationship fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return Relationship.fromJson(element, new JsonHelper(element));
    }

    @Override
    public String getBaseURL(GraphQLMap graphQL) {
        return "https://x.com/i/api/1.1/friendships/update.json";
    }

    @Override
    public HttpUriRequestBase createRequest(Gson gson, URI uri, GraphQLMap graphQL) throws URISyntaxException {
        HttpPost req = new HttpPost(uri);
        var b = new StringBuilder();
        for (Field field : ConfigUpdate.class.getFields()) {
            if ((field.getModifiers() & Modifier.TRANSIENT) != 0) continue;
            Object val;
            try {
                val = field.get(this);
            } catch (IllegalAccessException e) {
                continue;
            }

            if (val == null) continue;
            if (field.getType().isAssignableFrom(String.class) || field.getType().isAssignableFrom(Boolean.class)) {
                if (!b.isEmpty()) b.append("&");
                b.append(field.getName()).append('=').append(val);
            }
        }
        System.out.println(b.toString());
        StringEntity entity = new StringEntity(b.toString());
        req.setEntity(entity);
        req.addHeader("content-type", "application/x-www-form-urlencoded");
        return req;
    }

    public static class Relationship {
        public JsonObject source;
        public JsonObject target;

        public static Relationship fromJson(JsonElement element, JsonHelper h) {
            Relationship r = new Relationship();
            r.source = h.set(element).next("relationship").next("source").object();
            r.target = h.set(element).next("relationship").next("target").object();
            return r;
        }
    }
}
