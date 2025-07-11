package dev.seeight.twitterscraper.config.upload;

public enum MediaCategory {
	TweetImage("tweet_image"),
	TweetVideo("tweet_video")
	;

	final String str;

	MediaCategory(String str) {
		this.str = str;
	}
}
