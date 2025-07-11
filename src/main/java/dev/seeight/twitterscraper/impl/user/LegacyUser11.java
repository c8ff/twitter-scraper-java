package dev.seeight.twitterscraper.impl.user;

import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.util.JsonHelper;

public class LegacyUser11 {
    public Integer id;
    public String idStr;
    public Boolean verified;
    public Boolean extIsBlueVerified;
    public Boolean isDmAble;
    public Boolean isSecretDmAble;
    public Boolean isBlocked;
    public Boolean canMediaTag;
    public String name;
    public String screenName;
    public String profileImageUrl;
    public String profileImageUrlHttps;
    public String location;
    public Boolean isProtected;
    public Integer roundedScore;
    public Integer socialProof;
    public Integer connectingUserCount;
    public SocialContext socialContext;
    public Boolean inline;
    public String extVerifiedType;

    public static class SocialContext {
        public Boolean following;
        public Boolean followedBy;

        public static SocialContext fromJson(JsonHelper h, JsonObject z) {
            var o = new SocialContext();
            h.set(z);
            o.following = h.bool("following", false);
            o.followedBy = h.bool("followed_by", false);
            return o;
        }
    }

    public static LegacyUser11 fromJson(JsonHelper h, JsonObject z) {
        var o = new LegacyUser11();
        h.set(z);
        o.id = h.integer("id", -1);
        o.idStr = h.string("id_str", null);
        o.verified = h.bool("verified", false);
        o.extIsBlueVerified = h.bool("ext_is_blue_verified", false);
        o.isDmAble = h.bool("is_dm_able", false);
        o.isSecretDmAble = h.bool("is_secret_dm_able", false);
        o.isBlocked = h.bool("is_blocked", false);
        o.canMediaTag = h.bool("can_media_tag", false);
        o.name = h.string("name", null);
        o.screenName = h.string("screen_name", null);
        o.profileImageUrl = h.string("profile_image_url", null);
        o.profileImageUrlHttps = h.string("profile_image_url_https", null);
        o.location = h.string("location", null);
        o.isProtected = h.bool("is_protected", false);
        o.roundedScore = h.integer("rounded_score", -1);
        o.socialProof = h.integer("social_proof", -1);
        o.connectingUserCount = h.integer("connecting_user_count", -1);
        o.socialContext = SocialContext.fromJson(h, h.object("social_context"));
        h.set(z);
        o.inline = h.bool("inline", false);
        o.extVerifiedType = h.string("ext_verified_type", null);
        return o;
    }
}