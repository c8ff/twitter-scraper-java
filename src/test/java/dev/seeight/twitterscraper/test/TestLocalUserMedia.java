package dev.seeight.twitterscraper.test;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dev.seeight.twitterscraper.impl.user.UserMedia;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class TestLocalUserMedia {
	public static void main(String[] args) throws IOException {
		File file = new File("D:\\User\\Projects\\Java\\twitter\\twitter-scraper-java\\run\\samples\\UserMediaResponseSample.json");

		JsonElement p = JsonParser.parseReader(new FileReader(file, StandardCharsets.UTF_8));
		UserMedia userMedia = UserMedia.fromJson(null, p);

		System.out.println(userMedia);

		File save = new File(".\\UserMediaTest.json");
		try {
			Files.writeString(save.toPath(), new GsonBuilder().setPrettyPrinting().create().toJson(userMedia), StandardCharsets.UTF_8);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
