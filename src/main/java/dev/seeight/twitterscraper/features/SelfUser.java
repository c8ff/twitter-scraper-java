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

package dev.seeight.twitterscraper.features;

import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.api2.LegacyUser;
import dev.seeight.twitterscraper.util.JsonHelper;
import org.jetbrains.annotations.Nullable;

// TODO: ts incomplete but who cares
public class SelfUser {
    public Boolean defaultProfile;
    public Boolean defaultProfileImage;
    public String description;
    public LegacyUser.Entities entities;
    public Integer fastFollowersCount;
    public Integer favouritesCount;
    public Integer followersCount;
    public Integer friendsCount;
    public Boolean hasCustomTimelines;
    public Boolean isTranslator;
    public Integer listedCount;
    public Integer mediaCount;
    public Boolean needsPhoneVerification;
    public Integer normalFollowersCount;
    public Boolean possiblySensitive;
    public String profileInterstitialType;
    public Integer statusesCount;
    public String translatorType;
    public Boolean wantRetweets;
    public String name;
    public String screenName;
    public String idStr;
    public Boolean isProfileTranslatable;
    public String profileImageShape;
    public Integer creatorSubscriptionsCount;
    public String location;
    public Boolean isBlueVerified;
    public Boolean verified;
    public Boolean Protected;
    public String profileImageUrlHttps;
    public Boolean canDm;
    public Boolean canMediaTag;
    public Boolean blockedBy;
    public Boolean blocking;
    public Boolean following;
    public Boolean muting;
    public @Nullable Birthdate birthdate;
    public Boolean hasGraduatedAccess;
    public String createdAt;
    public String parodyCommentaryFanLabel;

    public static class Birthdate {
        public Integer day;
        public Integer month;
        public Integer year;
        public String visibility;
        public String yearVisibility;
        public static Birthdate fromJson(JsonHelper h, JsonObject z) {
            var o = new Birthdate();
            h.set(z);
            o.day = h.integer("day");
            o.month = h.integer("month");
            o.year = h.integer("year");
            o.visibility = h.string("visibility");
            o.yearVisibility = h.string("year_visibility");
            return o;
        }
    }
    public static SelfUser fromJson(JsonHelper h, JsonObject z) {
        var o = new SelfUser();
        h.set(z);
        o.defaultProfile = h.bool("default_profile");
        o.defaultProfileImage = h.bool("default_profile_image");
        o.description = h.string("description");
        o.entities = LegacyUser.Entities.fromJson(h, h.object("entities"));
        h.set(z);
        o.fastFollowersCount = h.integer("fast_followers_count");
        o.favouritesCount = h.integer("favourites_count");
        o.followersCount = h.integer("followers_count");
        o.friendsCount = h.integer("friends_count");
        o.hasCustomTimelines = h.bool("has_custom_timelines");
        o.isTranslator = h.bool("is_translator");
        o.listedCount = h.integer("listed_count");
        o.mediaCount = h.integer("media_count");
        o.needsPhoneVerification = h.bool("needs_phone_verification");
        o.normalFollowersCount = h.integer("normal_followers_count");
        o.possiblySensitive = h.bool("possibly_sensitive");
        o.profileInterstitialType = h.string("profile_interstitial_type");
        o.statusesCount = h.integer("statuses_count");
        o.translatorType = h.string("translator_type");
        o.wantRetweets = h.bool("want_retweets");
        o.name = h.string("name");
        o.screenName = h.string("screen_name");
        o.idStr = h.string("id_str");
        o.isProfileTranslatable = h.bool("is_profile_translatable");
        o.profileImageShape = h.string("profile_image_shape");
        o.creatorSubscriptionsCount = h.integer("creator_subscriptions_count");
        o.location = h.string("location");
        o.isBlueVerified = h.bool("is_blue_verified");
        h.set(z);
        o.verified = h.bool("verified");
        o.Protected = h.bool("protected");
        o.profileImageUrlHttps = h.string("profile_image_url_https");
        o.canDm = h.bool("can_dm");
        o.canMediaTag = h.bool("can_media_tag");
        o.blockedBy = h.bool("blocked_by");
        o.blocking = h.bool("blocking");
        o.following = h.bool("following");
        o.muting = h.bool("muting");
        o.birthdate = h.has("birtthdate") ? Birthdate.fromJson(h, h.object("birthdate")) : null;
        h.set(z);
        o.hasGraduatedAccess = h.bool("has_graduated_access");
        o.createdAt = h.string("created_at");
        o.parodyCommentaryFanLabel = h.string("parody_commentary_fan_label");
        return o;
    }
}