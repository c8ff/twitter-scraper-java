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
