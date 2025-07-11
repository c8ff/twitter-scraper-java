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

package dev.seeight.twitterscraper.impl.upload;

import dev.seeight.twitterscraper.config.interaction.ConfigCreateTextTweet;
import dev.seeight.twitterscraper.config.upload.ConfigUploadAppend;
import dev.seeight.twitterscraper.config.upload.ConfigUploadFinalize;
import dev.seeight.twitterscraper.config.upload.ConfigUploadStatus;
import org.jetbrains.annotations.Nullable;

public class PartiallyUploadedMedia {
	public long media_id;
	public String media_id_string;
	public long expires_after_secs;
	@Nullable
	public String media_key;

	public ConfigUploadAppend appendConfig(byte[] bytes, int segment) {
		return new ConfigUploadAppend(bytes, this.media_id_string, segment);
	}

	public ConfigUploadFinalize finalizeConfig(String originalMd5) {
		return new ConfigUploadFinalize(this.media_id_string, originalMd5);
	}

	public ConfigCreateTextTweet.Media asSingleEntityMedia() {
		return ConfigCreateTextTweet.Media.singleMedia(this.media_id_string);
	}

	public ConfigUploadStatus statusConfig() {
		return new ConfigUploadStatus(this.media_id_string);
	}
}
