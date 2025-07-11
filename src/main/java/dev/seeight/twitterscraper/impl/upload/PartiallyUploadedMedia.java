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
