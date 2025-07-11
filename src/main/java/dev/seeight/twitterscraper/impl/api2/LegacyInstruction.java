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

package dev.seeight.twitterscraper.impl.api2;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.impl.api2.instruction.LegacyInstructionAddEntries;
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.ArrayList;
import java.util.List;

public class LegacyInstruction {
    // ??? who knows bro
    public static List<LegacyInstruction> fromJsonArray(JsonHelper h, JsonArray z) {
        var a = new ArrayList<LegacyInstruction>();
        for (JsonElement e : z) {
            h.set(e);
            if (h.has("addEntries")) {
                a.add(LegacyInstructionAddEntries.fromJson(h, h.object("addEntries")));
            }
        }
        return a;
    }
}
