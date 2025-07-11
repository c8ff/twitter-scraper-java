package dev.seeight.twitterscraper.impl;

public abstract class TextEntity {
	public Range range;

	public abstract Type getType();

	public enum Type {
		URL,
		HASHTAG,
		USER_MENTION
	}
}
