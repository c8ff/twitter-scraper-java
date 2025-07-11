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

import org.jetbrains.annotations.Nullable;

public class UploadedMedia extends PartiallyUploadedMedia {
	public int size;
	@Nullable
	public Image image;
	@Nullable
	public Video video;
	@Nullable
	public ProcessingInfo processing_info;

	public static class ProcessingInfo {
		/**
		 * pending: processing upload
		 * in_progress: processing video as a whole
		 * so far 'pending', 'in_progress', 'succeeded', 'failed'
		 */
		public String state;
		public int check_after_secs;
		public int progress_percent;
		public @Nullable Error error;

		public static class Error {
			public String code;
			public String name;
			public String message;
			public String detail_message;
		}
	}

	public static class Video {
		public String video_type;
	}

	public static class Image {
		public String image_type;
		public int w;
		public int h;
	}
}
