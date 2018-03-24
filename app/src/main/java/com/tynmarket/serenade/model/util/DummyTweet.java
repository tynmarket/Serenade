package com.tynmarket.serenade.model.util;

import com.twitter.sdk.android.core.models.MediaEntity;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.UrlEntity;
import com.twitter.sdk.android.core.models.User;
import com.tynmarket.serenade.model.TwitterCard;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

/**
 * Created by tyn-iMarket on 2018/01/15.
 */

public class DummyTweet {
    public static final String CARD_SUMMARY_URL = "https://saruwakakun.com/life/recipe";
    public static final String CARD_SUMMARY_LARGE_IMAGE_URL = "http://diamond.jp/articles/-/155475";

    public static ArrayList<Tweet> tweets() {
        ArrayList<Tweet> tweets = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Tweet tweet;

            if (i == 1) {
                tweet = tweetWithRetweetedStatus(i);
            } else if (i == 2) {
                tweet = tweetWithQuotedStatus(i);
            } else if (i == 3) {
                tweet = tweetWithSlide(i);
            } else if (i == 4) {
                tweet = tweetWithCardSummary(i);
            } else if (i == 5) {
                tweet = tweetWithCardSummaryLarge(i);
            } else if (i == 6) {
                tweet = tweetWithFavoriteCount(tweet(i), 123456);
            } else {
                tweet = tweet(i);
            }
            tweets.add(tweet);
        }
        return tweets;
    }

    @SuppressWarnings("all")
    public static HashMap<String, TwitterCard> twitterCards() {
        HashMap<String, TwitterCard> cards = new HashMap<>();

        TwitterCard summary = new TwitterCard(
                TwitterCard.CARD_SUMMARY,
                "https://saruwakakun.com/wp-content/uploads/2017/05/nopowerban.png",
                "力尽きたときのためのレシピ 〜超簡単に作れるズボラ飯〜"
        );

        TwitterCard summaryLarge = new TwitterCard(
                TwitterCard.CARD_SUMMARY_LARGE_Image,
                "http://dol.ismcdn.jp/mwimgs/7/c/-/img_7cf57e479808fe68c759edf1cb35160567079.jpg",
                "三流リーダーは気前がよく、二流リーダーは単なるケチ、一流リーダーは「○○なケチ」である。 | 優れたリーダーはみな小心者である。 | ダイヤモンド・オンライン,"
        );

        cards.put(CARD_SUMMARY_URL, summary);
        cards.put(CARD_SUMMARY_LARGE_IMAGE_URL, summaryLarge);

        return cards;
    }

    private static Tweet tweet(int i) {
        return tweet(i, mediaEntities(), null, null, user(i));
    }

    private static Tweet tweet(int i, TweetEntities entities, Tweet retweetedStatus, Tweet quotedStatus, User user) {
        return new Tweet(null, "10時間", null, entities,
                null, 0, false, "filterLevel", i + 1,
                String.valueOf(i + 1), "inReplyToScreenName", 0,
                "inReplyToStatusIdStr", 0, "inReplyToUserIdStr",
                "lang", null, false, null, 0,
                "quotedStatusIdStr", quotedStatus, 0, false,
                retweetedStatus, "source", tweetText(i), null,
                false, user, false, null,
                "withheldScope", null);
    }

    private static Tweet tweetWithRetweetedStatus(int i) {
        Tweet retweetedStatus = tweet(i, null, null, null, user(i, "リツイートされた人"));
        return tweet(i, mediaEntities(), retweetedStatus, null, user(i));
    }

    private static Tweet tweetWithQuotedStatus(int i) {
        Tweet quotedStatus = tweet(i, mediaEntities(), null, null, user(i, "引用リツイートされた人"));
        return tweet(i, entities(), null, quotedStatus, user(i));
    }

    private static Tweet tweetWithSlide(int i) {
        return tweet(i, slideEntities(), null, null, user(i));
    }

    private static Tweet tweetWithCardSummary(int i) {
        return tweet(i, cardSummaryUrlEntities(), null, null, user(i));
    }

    private static Tweet tweetWithCardSummaryLarge(int i) {
        return tweet(i, cardSummaryLargeImageUrlEntities(), null, null, user(i));
    }

    private static String tweetText(int i) {
        return String.format("ツイート内容ツイート内容ツイート内容ツイート内容ツイート内容ツイート内容 %d", i + 1);
    }

    private static User user(int i) {
        return user(i, String.format("ティン＠iMarket管理人あああ %d", i + 1));
    }

    private static User user(int i, String name) {
        return new User(false, "createdAt", false,
                false, "description", "emailAddress",
                null, 0, false, 0,
                0, false, 0, "idStr", false,
                "lang", 0, "location", name,
                "profileBackgroundColor", "profileBackgroundImageUrl",
                "profileBackgroundImageUrlHttps", false,
                "profileBannerUrl", "profileImageUrl",
                "https://pbs.twimg.com/profile_images/742013491/06c940e6-s_200x200.png",
                "profileLinkColor",
                "profileSidebarBorderColor", "profileSidebarFillColor",
                "profileTextColor", false, false,
                String.format("tynmarket %d", i + 1), false, null, 0,
                "timeZone", "url", 0, false, null,
                "withheldScope");
    }

    private static TweetEntities entities() {
        return new TweetEntities(null, null, null, null, null);
    }

    private static TweetEntities mediaEntities() {
        return new TweetEntities(null, null, media("https://pbs.twimg.com/media/DVolgDHUMAIoU6l.jpg"), null, null);
    }

    private static TweetEntities slideEntities() {
        UrlEntity url = new UrlEntity("url", "https://speakerdeck.com/timakin/architecture-and-benefits-of-ab-test-allocation-system",
                "displayUrl", 0, 1);
        List<UrlEntity> urls = Arrays.asList(url);
        return new TweetEntities(urls, null, null, null, null);
    }

    private static TweetEntities cardSummaryUrlEntities() {
        UrlEntity url = new UrlEntity("url", CARD_SUMMARY_URL,
                "displayUrl", 0, 1);
        List<UrlEntity> urls = Arrays.asList(url);
        return new TweetEntities(urls, null, null, null, null);
    }

    private static TweetEntities cardSummaryLargeImageUrlEntities() {
        UrlEntity url = new UrlEntity("url", CARD_SUMMARY_LARGE_IMAGE_URL,
                "displayUrl", 0, 1);
        List<UrlEntity> urls = Arrays.asList(url);
        return new TweetEntities(urls, null, null, null, null);
    }

    private static List<MediaEntity> media(String mediaUrlHttps) {
        MediaEntity entity = new MediaEntity("url", "expandedUrl", "displayUrl",
                0, 1, 10, "idStr", "mediaUrl",
                mediaUrlHttps,
                null, 0, "sourceStatusIdStr",
                "photo", null, null);
        return Collections.singletonList(entity);
    }

    private static Tweet tweetWithFavoriteCount(Tweet tweet, int favoriteCount) {
        return new Tweet(
                tweet.coordinates,
                tweet.createdAt,
                tweet.currentUserRetweet,
                tweet.entities,
                tweet.extendedEntities,
                favoriteCount,
                true,
                tweet.filterLevel,
                tweet.id,
                tweet.idStr,
                tweet.inReplyToScreenName,
                tweet.inReplyToStatusId,
                tweet.inReplyToStatusIdStr,
                tweet.inReplyToUserId,
                tweet.inReplyToUserIdStr,
                tweet.lang,
                tweet.place,
                tweet.possiblySensitive,
                tweet.scopes,
                tweet.quotedStatusId,
                tweet.quotedStatusIdStr,
                tweet.quotedStatus,
                tweet.retweetCount,
                tweet.retweeted,
                tweet.retweetedStatus,
                tweet.source,
                tweet.text,
                tweet.displayTextRange,
                tweet.truncated,
                tweet.user,
                tweet.withheldCopyright,
                tweet.withheldInCountries,
                tweet.withheldScope,
                tweet.card
        );
    }
}
