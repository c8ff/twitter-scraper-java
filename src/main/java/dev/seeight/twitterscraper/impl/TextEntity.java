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

package dev.seeight.twitterscraper.impl;

public abstract class TextEntity implements Cloneable {
	public Range range;

	public abstract Type getType();

	@Override
	public TextEntity clone() {
		try {
			TextEntity clone = (TextEntity) super.clone();
			clone.range = clone.range.clone();
			return clone;
		} catch (CloneNotSupportedException e) {
			throw new AssertionError();
		}
	}

	public enum Type {
		URL,
		HASHTAG,
		USER_MENTION
	}
}
