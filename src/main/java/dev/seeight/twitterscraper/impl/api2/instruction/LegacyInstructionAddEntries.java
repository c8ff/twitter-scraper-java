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

package dev.seeight.twitterscraper.impl.api2.instruction;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.api2.LegacyInstruction;
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class LegacyInstructionAddEntries extends LegacyInstruction {
    public List<LegacyEntry> entries;

    public static LegacyInstructionAddEntries fromJson(JsonHelper h, JsonObject z) {
        h.set(z);
        var i = new LegacyInstructionAddEntries();
        i.entries = new ArrayList<>();
        for (JsonElement e : h.array("entries")) {
            i.entries.add(LegacyEntry.fromJson(h, e));
        }
        return i;
    }
}
