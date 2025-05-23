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

package dev.seeight.twitterscraper.util;

import com.google.gson.*;
import dev.seeight.twitterscraper.JsonFormatException;
import org.jetbrains.annotations.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

/**
 * This class is meant to manage annoying Json structures that don't change.
 *
 * @author C8FF
 */
public class JsonHelper {
	@NotNull
	private JsonElement element;

	public JsonHelper(@NotNull JsonElement element) {
		this.element = element;
	}

	@Contract(value = "_ -> this")
	public JsonHelper set(@NotNull JsonElement element) {
		this.element = element;
		return this;
	}

	/**
	 * Sets {@link #element} to the element {@code name} inside {@link #element}.
	 *
	 * @param name The member name to be searched in {@link #element}.
	 * @throws ClassCastException If {@link #element} is not a {@link JsonObject}.
	 * @throws RuntimeException   If {@code name} was not found on {@link #element}.
	 */
	@Contract(value = "_ -> this")
	public JsonHelper next(@NotNull String name) throws NullPointerException, ClassCastException {
		if (!(this.element instanceof JsonObject object)) {
			throw new JsonFormatException("Expected JsonObject but got " + this.getElementClassName());
		}

		for (Map.Entry<String, JsonElement> entry : object.asMap().entrySet()) {
			if (entry.getKey().equals(name)) {
				this.element = entry.getValue();
				return this;
			}
		}

		throw new JsonFormatException("Element doesn't contain member '" + name + "'.");
	}

	/**
	 * Sets the element to any {@link JsonElement} that is on the current {@link #element}'s array at {@code arrayIndex}.
	 *
	 * @param arrayIndex The index of the {@link #element}'s array.
	 * @return this
	 * @throws ClassCastException             If {@link #element} is not a {@link JsonArray}.
	 * @throws ArrayIndexOutOfBoundsException If {@code arrayIndex} is outside bounds.
	 */
	@Contract(value = "_ -> this")
	public JsonHelper get(@Range(from = 0, to = Integer.MAX_VALUE) int arrayIndex) {
		if (!(this.element instanceof JsonArray array)) {
			throw new JsonFormatException("Expected JsonArray but got " + this.getElementClassName());
		}

		if (arrayIndex >= array.size()) {
			throw new ArrayIndexOutOfBoundsException(arrayIndex);
		}

		this.element = array.get(arrayIndex);
		return this;
	}

	/**
	 * Meant to reduce extra code.
	 * Replaces:
	 * <pre>
	 * JsonObject object = helper.next("foo").object();
	 * String bar = object.get("bar");</pre>
	 * with:
	 * <pre>
	 * String bar = helper.next("foo").stringOrDefault("bar", null);</pre>
	 *
	 * @return {@code defaultValue} parameter, in case that {@code memberName} was not found on {@link #element}.
	 * @throws ClassCastException If {@link #element} is not a {@link JsonObject}.
	 * @deprecated Use {@link #string(String, String)} instead.
	 */
	@Deprecated
	@Nullable
	public String stringOrDefault(String memberName, @Nullable String defaultValue) {
		if (!(this.element instanceof JsonObject object)) {
			throw new JsonFormatException("Expected JsonObject but got " + this.element.getClass().getName());
		}

		JsonElement jsonElement = object.get(memberName);
		if (jsonElement == null) return null;
		if (jsonElement.isJsonNull()) return null;
		if (!(jsonElement instanceof JsonPrimitive p) || !p.isString()) {
			return defaultValue;
		}

		return p.getAsString();
	}

	/**
	 * @return {@link #element} as a {@link String}.
	 */
	public String string() {
		return element.getAsString();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as a {@link String}.
	 */
	@Nullable
	public String string(@NotNull String name) {
		JsonElement value = this.getValue(name);
		if (value.isJsonNull()) return null;
		return value.getAsString();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as a {@link String},
	 * or the default value if the member doesn't exist.
	 */
	@Nullable
	public String string(@NotNull String name, @Nullable String defaultValue) {
		JsonElement v = this.getValueNullable(name);
		if (v == null || v.isJsonNull()) {
			return defaultValue;
		}
		return v.getAsString();
	}

	/**
	 * @return {@link #element} as a {@link JsonArray}.
	 */
	public JsonArray array() {
		return element.getAsJsonArray();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as a {@link JsonArray}
	 */
	public JsonArray array(@NotNull String name) {
		return this.getValue(name).getAsJsonArray();
	}

	/**
	 * @return {@link #element} as a {@link JsonObject}.
	 */
	public JsonObject object() {
		return element.getAsJsonObject();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as a {@link JsonObject}
	 */
	public JsonObject object(@NotNull String name) {
		return this.getValue(name).getAsJsonObject();
	}

	/**
	 * @return {@link #element} as an {@code int}.
	 */
	public int integer() {
		return element.getAsInt();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as an {@code int}
	 */
	public int integer(@NotNull String name) {
		return this.getValue(name).getAsInt();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as an {@code int},
	 * or the default value if the member doesn't exist.
	 */
	public int integer(@NotNull String name, int defaultValue) {
		JsonElement v = this.getValueNullable(name);
		if (v == null) {
			return defaultValue;
		}
		return v.getAsInt();
	}

	/**
	 * @return {@link #element} as an {@code long}.
	 */
	public long asLong() {
		return element.getAsLong();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as an {@code long}
	 */
	public long asLong(@NotNull String name) {
		return this.getValue(name).getAsLong();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as an {@code long},
	 * or the default value if the member doesn't exist.
	 */
	public long asLong(@NotNull String name, long defaultValue) {
		JsonElement v = this.getValueNullable(name);
		if (v == null) return defaultValue;
		return v.getAsLong();
	}

	/**
	 * @return {@link #element} as a {@code double}.
	 */
	public double asDouble() {
		return element.getAsDouble();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as a {@code double}
	 */
	public double asDouble(@NotNull String name) {
		return this.getValue(name).getAsDouble();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as a {@code double},
	 * or the default value if the member doesn't exist.
	 */
	public double asLong(@NotNull String name, double defaultValue) {
		JsonElement v = this.getValueNullable(name);
		if (v == null) return defaultValue;
		return v.getAsDouble();
	}

	/**
	 * @return {@link #element} as a {@code boolean}.
	 */
	public boolean bool() {
		return element.getAsBoolean();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as a {@code boolean}
	 */
	public boolean bool(@NotNull String name) {
		JsonElement value = this.getValue(name);
		if (value.isJsonNull()) return false;
		return value.getAsBoolean();
	}

	/**
	 * This doesn't change the underlying element.
	 *
	 * @return The member from the current element as a {@code boolean},
	 * or the default value if the member doesn't exist.
	 */
	public boolean bool(@NotNull String name, boolean defaultValue) {
		JsonElement v = this.getValueNullable(name);
		if (v == null || v.isJsonNull()) {
			return defaultValue;
		}
		return v.getAsBoolean();
	}

	public boolean equals(String objectName, String value) {
		if (!(this.element instanceof JsonObject e)) {
			return false;
		}

		JsonElement elm = e.get(objectName);
		return elm != null && value.equals(elm.getAsString());
	}

	public String[] stringArray(String name, String[] defaultValue) {
		JsonElement e = this.getValueNullable(name);
		if (e == null) return defaultValue;
		return stringArray((JsonArray) e);
	}

	public String[] stringArray(String name) {
		return stringArray(this.array(name));
	}

	/**
	 * @return Casts {@link #element} to a {@link JsonArray} and then converts it into a {@code String} array.
	 */
	public String[] stringArray() {
		return stringArray(this.array());
	}

	private String[] stringArray(JsonArray array) {
		if (array.isEmpty()) return new String[0];

		String[] arr = new String[array.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = array.get(i).getAsString();
		}

		return arr;
	}

	/**
	 * Converts the current element into a list of {@link T} objects.
	 *
	 * @param transformer A function that converts a {@link JsonElement} into a {@link T} object. If this function returns null, it won't be added to the list.
	 * @return The transformed list.
	 */
	public <T> T[] array(Function<JsonElement, T> transformer) {
		JsonArray array = this.array();
		if (array.isEmpty()) //noinspection unchecked
			return (T[]) new Object[0];

		@SuppressWarnings("unchecked")
		T[] arr = (T[]) new Object[array.size()];
		for (int i = 0; i < array.size(); i++) {
			arr[i] = transformer.apply(array.get(i));
		}

		return arr;
	}

	/**
	 * @return Casts {@link #element} to a {@link JsonArray} and then converts it into a {@code String} list.
	 */
	public List<String> stringList() {
		JsonArray array = this.array();
		if (array.isEmpty()) return Collections.emptyList();
		return List.of(this.stringArray());
	}

	/**
	 * Converts the current element into a list of {@link T} objects.
	 *
	 * @param transformer A function that converts a {@link JsonElement} into a {@link T} object. If this function returns null, it won't be added to the list.
	 * @return The transformed list.
	 */
	public <T> List<T> list(Function<JsonElement, T> transformer) {
		JsonArray array = this.array();
		if (array.isEmpty()) return Collections.emptyList();

		List<T> l = new ArrayList<>();
		for (int i = 0; i < array.size(); i++) {
			l.add(transformer.apply(array.get(i)));
		}
		return l;
	}

	/**
	 * @return Casts {@link #element} to a {@link JsonArray} and then converts it into a {@code int} array.
	 */
	public int[] intArray() {
		return this.intArray(this.array());
	}

	public int[] intArray(String name) {
		return this.intArray(this.array(name));
	}

	private int[] intArray(JsonArray array) {
		if (array.isEmpty()) return new int[0];

		int[] arr = new int[array.size()];
		for (int i = 0; i < arr.length; i++) {
			arr[i] = array.get(i).getAsInt();
		}

		return arr;
	}

	/**
	 * Searches through {@link #element} based on the instructions. This doesn't change the underlying element.
	 *
	 * @param instructions The instructions to navigate through {@link #element}.
	 *                     A string is the equivalent to {@link #next(String)} and an integer is the equivalent to {@link #get(int)},
	 *                     but with the difference that this function does not set {@link #element}.
	 * @return The found {@link JsonElement} from the search.
	 */
	@NotNull
	public JsonElement query(Object @NotNull ... instructions) throws IllegalArgumentException {
		return query(this.element, instructions);
	}

	/**
	 * Searches through the start element based on the instructions. This doesn't change the underlying element.
	 *
	 * @param start        The element to start the instructions from
	 * @param instructions The instructions to navigate through the {@code start} element.
	 *                     A string is the equivalent to {@link #next(String)} and an integer is the equivalent to {@link #get(int)},
	 *                     but with the difference that this function does not set {@link #element}.
	 * @return The found {@link JsonElement} from the search.
	 */
	public @NotNull JsonElement query(JsonElement start, Object @NotNull ... instructions) throws IllegalArgumentException {
		JsonElement elm = start;

		for (Object instruction : instructions) {
			if (elm == null) {
				throw new JsonFormatException("Element is now null. Current instruction '" + instruction + "'.");
			}

			if (instruction.getClass().isAssignableFrom(Integer.class)) {
				elm = elm.getAsJsonArray().get((Integer) instruction);
			} else if (instruction instanceof String s) {
				elm = elm.getAsJsonObject().get(s);
			} else {
				throw new IllegalArgumentException("Unknown class type '" + instruction.getClass().getName() + "'.");
			}
		}

		return elm;
	}

	/**
	 * Gets the value from {@link #element}.
	 *
	 * @param key The name of the member to get.
	 * @return The found element.
	 * @throws RuntimeException   If the member was not found.
	 * @throws ClassCastException If {@link #element} is not a JsonObject.
	 */
	public @NotNull JsonElement getValue(@NotNull String key) throws RuntimeException, ClassCastException {
		JsonElement v = getValueNullable(key);
		if (v == null)
			throw new JsonFormatException("Member '" + key + "' not found.");
		return v;
	}

	/**
	 * Gets the value from the {@link #element}.
	 *
	 * @param key The name of the member to get.
	 * @return The found element, or null if it wasn't found.
	 * @throws ClassCastException If {@link #element} is not a JsonObject.
	 */
	public @Nullable JsonElement getValueNullable(@NotNull String key) throws ClassCastException {
		if (!(this.element instanceof JsonObject o)) {
			throw new JsonFormatException("Expected JsonObject but got " + this.element.getClass().getName() + ".");
		}
		return o.get(key);
	}

	private String getElementClassName() {
		//noinspection ConstantValue
		if (this.element == null) {
			return "null";
		}

		return this.element.getClass().getSimpleName();
	}

	public boolean has(String property) {
		return this.element instanceof JsonObject s && s.has(property);
	}

	public boolean tryNext(String property) {
		var result = this.has(property);
		if (result) this.next(property);
		return result;
	}
}