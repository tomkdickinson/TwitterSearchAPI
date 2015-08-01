package uk.co.tomkdickinson.twitter.search;

import com.google.gson.Gson;
import org.apache.http.client.utils.URIBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

public abstract class TwitterSearch {

    public TwitterSearch() {

    }

    public abstract boolean saveTweets(List<Tweet> tweets);

    public void search(final String query, final long rateDelay) throws InvalidQueryException {
        TwitterResponse response;
        URL url = constructURL(query, null);
        boolean continueSearch = true;
        String minTweet = null;
        while((response = executeSearch(url))!=null && continueSearch && !response.getTweets().isEmpty()) {
            if(minTweet==null) {
                minTweet = response.getTweets().get(0).getId();
            }
            continueSearch = saveTweets(response.getTweets());
            String maxTweet = response.getTweets().get(response.getTweets().size()-1).getId();
            if(!minTweet.equals(maxTweet)) {
                try {
                    Thread.sleep(rateDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String maxPosition = "TWEET-" + maxTweet + "-" + minTweet;
                url = constructURL(query, maxPosition);
            }

        }
    }

    public static TwitterResponse executeSearch(final URL url) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            Gson gson = new Gson();
            return gson.fromJson(reader, TwitterResponse.class);
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                reader.close();
            } catch(NullPointerException | IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public final static String TYPE_PARAM = "f";
    public final static String QUERY_PARAM = "q";
    public final static String SCROLL_CURSOR_PARAM = "max_position";
    public final static String TWITTER_SEARCH_URL = "https://twitter.com/i/search/timeline";

    public static URL constructURL(final String query, final String maxPosition) throws InvalidQueryException {
        if(query==null || query.isEmpty()) {
            throw new InvalidQueryException(query);
        }
        try {
            URIBuilder uriBuilder;
            uriBuilder = new URIBuilder(TWITTER_SEARCH_URL);
            uriBuilder.addParameter(QUERY_PARAM, query);
            uriBuilder.addParameter(TYPE_PARAM, "tweets");
            if (maxPosition != null) {
                uriBuilder.addParameter(SCROLL_CURSOR_PARAM, maxPosition);
            }
            return uriBuilder.build().toURL();
        } catch(MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
            throw new InvalidQueryException(query);
        }
    }
}