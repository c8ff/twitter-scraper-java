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
