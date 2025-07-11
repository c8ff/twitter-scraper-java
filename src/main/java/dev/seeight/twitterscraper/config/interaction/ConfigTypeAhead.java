package dev.seeight.twitterscraper.config.interaction;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.IConfigJsonTree;
import dev.seeight.twitterscraper.graphql.GraphQLMap;
import dev.seeight.twitterscraper.impl.TwitterError;
import dev.seeight.twitterscraper.impl.TypeAhead;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.apache.hc.core5.net.URIBuilder;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

public class ConfigTypeAhead implements IConfigJsonTree<TypeAhead> {
    public int include_ext_is_blue_verified = 1;
    public int include_ext_verified_type = 1;
    public int include_ext_profile_image_shape = 1;
    public final String query;
    public String src = "search_box";
    public String result_type = "events,users,topics,lists";

    public ConfigTypeAhead(String query) {
        this.query = query;
    }

    @Override
    public TypeAhead fromJson(JsonElement element, Gson gson, List<TwitterError> errors) {
        return TypeAhead.fromJson(new JsonHelper(element), element.getAsJsonObject());
    }

    @Override
    public URI buildURI(Gson gson, URIBuilder builder, GraphQLMap graphQL) throws URISyntaxException {
        return builder
                .addParameter("include_ext_is_blue_verified", String.valueOf(this.include_ext_is_blue_verified))
                .addParameter("include_ext_verified_type", String.valueOf(this.include_ext_verified_type))
                .addParameter("include_ext_profile_image_shape", String.valueOf(this.include_ext_profile_image_shape))
                .addParameter("q", this.query)
                .addParameter("src", this.src)
                .addParameter("result_type", this.result_type)
                .build();
    }

    @Override
    public String getBaseURL(GraphQLMap graphQL) {
        return "https://x.com/i/api/1.1/search/typeahead.json";
    }
}
