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
