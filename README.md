# twitter-scraper-java
A Java API for scraping data from Twitter/X using the public Twitter API.

## Usage
Using the API is very simple. Here's an example. This example gets the information & replies of a specific tweet,
then, it writes the result into a JSON file.
```java
public static void main(String[] args) {
    // Create a new TwitterApi instance.
    TwitterApi api = TwitterApi.newTwitterApi();
    // Define secrets from the specified JSON file.
    Secret.defineFromFile(api, new File("./secret.json"));

    // Create a HttpClient to send and receive requests.
    try (CloseableHttpClient client = HttpClients.createDefault()) {
        // Create the configuration to process.
        ConfigTweetDetail config = new ConfigTweetDetail("161349024945942529", null);
        // Send the request and process the result.
        TweetDetail result = api.scrap(config, client);
        // Save the processed response into a 'tweet-detail.json' file.
        Files.writeString(new File("tweet-detail.json").toPath(), new Gson().toJson(result));
    } catch (IOException | URISyntaxException e) {
        e.printStackTrace();
    }
}
```
then, the file will then have a similar JSON structure to this:
```json
{
  "instructions": [
    {
      "entries": [
        {
          "bookmarks": 1177,
          "quotes": 376,
          "likes": 16162,
          "replies": 735,
          "retweets": 4918,
          "creationDate": 1327303579,
          "createdAt": "Mon Jan 23 07:26:19 +0000 2012",
          "id": "161349024945942529",
          "text": "I got 1,032,164 points while escaping from demon monkeys in Temple Run. Beat that! http://bit.ly/TempleRunGame",
          "rawText": "I got 1,032,164 points while escaping from demon monkeys in Temple Run. Beat that! http://t.co/bRr0HnMx",
          "language": "en",
          "publishDevice": "\u003ca href\u003d\"http://itunes.apple.com/us/app/temple-run/id420009108?mt\u003d8\u0026uo\u003d6\u0026partnerId\u003d30\u0026siteID\u003dx57HaVmLCLA\" rel\u003d\"nofollow\"\u003eTemple Run\u003c/a\u003e",
          "isQuoteStatus": false,
          "possiblySensitive": false,
          "possiblySensitiveEditable": true,
          "media": {
            "photos": [],
            "videos": [],
            "animatedGif": []
          },
          "user": ...,
          "liked": false,
          "retweeted": false,
          "bookmarked": false,
          "restricted": false,
          "displayTextRange": {
            "start": 0,
            "end": 103
          },
          "entities": ...,
          "hasBirdwatchNotes": false,
          "_tweetDisplayType": "Tweet",
          "entryId": "tweet-161349024945942529",
          "sortIndex": "9062023011908833278"
        }
      ],
      "type": "TimelineAddEntries"
    }
  ]
}
```
## Other stuff
This project uses the [TwitterInternalAPIDocument](https://github.com/fa0311/TwitterInternalAPIDocument) respository to access up-to-date GraphQL URLs. This behaviour can be changed by setting the GraphQLMap manually in a TwitterApi instance.