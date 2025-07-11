/*
 * Xeno
 * Copyright (C) 2025 c8ff
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

package dev.seeight.xeno;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import dev.seeight.twitterscraper.impl.Entry;
import dev.seeight.twitterscraper.impl.inst.Instruction;
import dev.seeight.twitterscraper.impl.inst.TimelineAddEntries;
import dev.seeight.twitterscraper.impl.inst.TimelineAddToModule;
import dev.seeight.twitterscraper.impl.inst.TimelineReplaceEntry;

public class TwitterUtil {
    public static void forEachEntry(List<Instruction> instructions, Consumer<Entry> consumer) {
        for (Instruction instruction : instructions) {
            if (instruction instanceof TimelineAddEntries t) {
                for (Entry entry : t.entries) {
                    consumer.accept(entry);
                }
            } else if (instruction instanceof TimelineReplaceEntry t) {
                consumer.accept(t.entry);
            } else if (instruction instanceof TimelineAddToModule t) {
                for (Entry entry : t.entries) {
                    consumer.accept(entry);
                }
            }
        }
    }

    public static List<byte[]> splitIntoParts(byte[] data) {
        int maxBytesPerPart = 8387584;
        List<byte[]> parts = new ArrayList<>();
        if (data.length > maxBytesPerPart) {
            int start = 0;
            int remaining = data.length;
            while (remaining > maxBytesPerPart) {
                parts.add(Arrays.copyOfRange(data, start, start + maxBytesPerPart));
                start += maxBytesPerPart;
                remaining -= maxBytesPerPart;
            }
            parts.add(Arrays.copyOfRange(data, start, start + remaining));
        } else {
            parts.add(data);
        }
        return parts;
    }

    /**
     * @param b Consumer of a buffer. Warning: This byte array is changed after it is consumed.
     */
    public static void splitIntoParts(byte[] data, PartConsumer b) {
        int maxBytesPerPart = 8387584;
        if (data.length > maxBytesPerPart) {
            int index = 0;
            int start = 0;
            int remaining = data.length;
            byte[] buffer = new byte[maxBytesPerPart];
            while (remaining > maxBytesPerPart) {
                System.arraycopy(data, start, buffer, 0, maxBytesPerPart);
                b.consume(buffer, index++);
                start += maxBytesPerPart;
                remaining -= maxBytesPerPart;
            }
            buffer = new byte[remaining];
            System.arraycopy(data, start, buffer, 0, remaining);
            b.consume(buffer, index);
        } else {
            b.consume(data, 0);
        }
    }

    public interface PartConsumer {
        void consume(byte[] buffer, int index);
    }
}
