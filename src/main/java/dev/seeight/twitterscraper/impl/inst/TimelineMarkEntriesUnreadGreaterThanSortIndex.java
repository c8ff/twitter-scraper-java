package dev.seeight.twitterscraper.impl.inst;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import dev.seeight.twitterscraper.util.GsonUtil;
import dev.seeight.twitterscraper.util.JsonHelper;

// It's not my fault ok...
public class TimelineMarkEntriesUnreadGreaterThanSortIndex extends Instruction {
    public String sortIndex;

    public static TimelineMarkEntriesUnreadGreaterThanSortIndex fromJson(Gson gson, JsonHelper h, JsonElement element) {
        var a1 = GsonUtil.createObject(gson, TimelineMarkEntriesUnreadGreaterThanSortIndex.class);

        a1.sortIndex = h.set(element).string("sort_index");
        return a1;
    }
}
