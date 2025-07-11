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

package dev.seeight.xeno.platform;

import java.lang.reflect.Method;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class StandardExtension implements PlatformExtensions {
	@Override
	public boolean canAccessMethod(Method method, Object instance) {
		return method.canAccess(instance);
	}

	@Override
	public String encodeUriString(String str) {
		return URLEncoder.encode(str, StandardCharsets.UTF_8);
	}
}
