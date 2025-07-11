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

package dev.seeight.twitterscraper.impl.inst;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class Instruction {
	public String type;

	public static List<Instruction> fromInstructionsJson(Gson gson, JsonHelper h, JsonArray instructions) {
		List<Instruction> ins = new ArrayList<>();

		for (JsonElement _instruction : instructions) {
			if (!(_instruction instanceof JsonObject instruction)) continue;

			Instruction i = fromInstructionJson(gson, instruction, h);
			if (i != null) ins.add(i);
		}

		return ins;
	}

	public static Instruction fromInstructionJson(Gson gson, JsonObject instruction, JsonHelper h) {
		String type = h.set(instruction).string("type");
		Instruction inst;
		switch (type) {
			case "TimelineAddEntries" -> inst = TimelineAddEntries.fromJson(gson, h, instruction);
			case "TimelineReplaceEntry" -> inst = TimelineReplaceEntry.fromJson(gson, h, instruction);
			case "TimelineTerminateTimeline" -> inst = TimelineTerminateTimeline.fromJson(h, instruction);
			case "TimelinePinEntry" -> inst = TimelinePinEntry.fromJson(gson, h, instruction);
			case "TimelineAddToModule" -> inst = TimelineAddToModule.fromJson(gson, h, instruction);
			case "TimelineClearCache" -> inst = new TimelineClearCache();
			case "TimelineClearEntriesUnreadState" -> inst = new TimelineClearEntriesUnreadState();
			case "TimelineMarkEntriesUnreadGreaterThanSortIndex" ->
				inst = TimelineMarkEntriesUnreadGreaterThanSortIndex.fromJson(gson, h, instruction);
			default -> {
				System.out.println("dropping unknown type: " + type);
				return null;
			}
		}

		inst.type = type;
		return inst;
	}
}
