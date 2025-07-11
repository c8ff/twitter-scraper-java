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

package dev.seeight.twitterscraper.graphql;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public interface GraphQLMap {
	@NotNull
	default Entry get(String queryId) {
		Entry entry = this.getNullable(queryId);
		if (entry == null) throw new IllegalStateException("Could not find graphql mapping for '" + queryId + "'.");
		return entry;
	}

	@Nullable
	Entry getNullable(String queryId);

	class Root {
		public Map<String, Entry> graphql;
	}

	class Entry {
		public String url;
		public String queryId;
		public String method;
		public Map<String, Boolean> features;
	}
}
