/*
 * twitter-scraper-java
 * Copyright (C) 2024 c8ff
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package dev.seeight.twitterscraper;

import org.apache.hc.client5.http.classic.methods.HttpUriRequestBase;
import org.jetbrains.annotations.Nullable;

public class TwitterException extends RuntimeException {
	public final String message;
	public final int code;
	public final @Nullable HttpUriRequestBase request;

	public TwitterException(String message, int code, HttpUriRequestBase request) {
		super(message + " (" + code + ")");
		this.message = message;
		this.code = code;
		this.request = request;
	}

	public TwitterException(String message, int code) {
		super(message + " (" + code + ")");
		this.message = message;
		this.code = code;
		this.request = null;
	}
}
