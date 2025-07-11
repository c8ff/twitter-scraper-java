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

import com.google.gson.JsonObject;
import dev.seeight.twitterscraper.impl.api2.instruction.LegacyEntry;
import dev.seeight.twitterscraper.util.JsonHelper;

import java.util.List;

public class LegacyTimeline {
    public String id;
    public List<LegacyInstruction> instructions;

    public LegacyTimeline() {
    }

    public LegacyTimeline(String id, List<LegacyInstruction> instructions) {
        this.id = id;
        this.instructions = instructions;
    }

    public static LegacyTimeline fromJson(JsonHelper h, JsonObject z) {
        h.set(z);
        return new LegacyTimeline(
                h.string("id"),
                LegacyInstruction.fromJsonArray(h, h.array("instructions"))
        );
    }
}
