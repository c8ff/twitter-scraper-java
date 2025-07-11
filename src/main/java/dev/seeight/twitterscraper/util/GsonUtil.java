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

package dev.seeight.twitterscraper.util;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.internal.bind.ReflectiveTypeAdapterFactory;
import dev.seeight.xeno.platform.PlatformExtensions;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class GsonUtil {
	public static <T> T createObject(@Nullable Gson gson, Class<T> objectClass) {
		if (gson != null) {
			TypeAdapter<T> adapter = gson.getAdapter(objectClass);
			if (adapter instanceof ReflectiveTypeAdapterFactory.Adapter<?, ?>) {
				ReflectiveTypeAdapterFactory.Adapter<T, Object> refAdapter = (ReflectiveTypeAdapterFactory.Adapter<T, Object>) adapter;

				try {
					Method method = ReflectiveTypeAdapterFactory.Adapter.class.getDeclaredMethod("createAccumulator");
					boolean ac = PlatformExtensions.Instance.impl.canAccessMethod(method, refAdapter);
					if (!ac) method.setAccessible(true);
					//noinspection unchecked
					T object = (T) method.invoke(refAdapter);
					if (!ac) method.setAccessible(false);
					return object;
				} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException ignored) {
				}
			}
		}

		try {
			Constructor<T> constructor = objectClass.getConstructor();
			return constructor.newInstance();
		} catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
			throw new RuntimeException(e);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(objectClass.getName() + " doesn't have an empty constructor.");
		}
	}
}
