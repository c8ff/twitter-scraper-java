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
