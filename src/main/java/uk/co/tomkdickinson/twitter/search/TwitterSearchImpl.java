package uk.co.tomkdickinson.twitter.search;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class TwitterSearchImpl extends TwitterSearch {

    private final AtomicInteger counter = new AtomicInteger();

    @Override
    public boolean saveTweets(List<Tweet> tweets) {
        if(tweets!=null) {
            for (Tweet tweet : tweets) {
                System.out.println(counter.getAndIncrement() + 1 + "[" + tweet.getCreatedAt() + "] - " + tweet.getText());
                if (counter.get() >= 500) {
                    return false;
                }
            }
        }
        return true;
    }

    public static void main(String[] args) throws InvalidQueryException {
        TwitterSearch twitterSearch = new TwitterSearchImpl();
        twitterSearch.search("babylon 5", 2000);
    }
}