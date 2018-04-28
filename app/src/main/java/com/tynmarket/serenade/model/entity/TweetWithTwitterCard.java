package com.tynmarket.serenade.model.entity;

import com.twitter.sdk.android.core.models.Card;
import com.twitter.sdk.android.core.models.Coordinates;
import com.twitter.sdk.android.core.models.Place;
import com.twitter.sdk.android.core.models.Tweet;
import com.twitter.sdk.android.core.models.TweetEntities;
import com.twitter.sdk.android.core.models.User;

import java.util.List;

/**
 * Created by tynmarket on 2018/04/28.
 */
public class TweetWithTwitterCard extends Tweet {
    public TwitterCard twitterCard;

    public TweetWithTwitterCard(Tweet tweet, TwitterCard twitterCard) {
        super(
                tweet.coordinates,
                tweet.createdAt,
                tweet.currentUserRetweet,
                tweet.entities,
                tweet.extendedEntities,
                tweet.favoriteCount,
                tweet.favorited,
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
        this.twitterCard = twitterCard;
    }

    public TweetWithTwitterCard(Coordinates coordinates, String createdAt, Object currentUserRetweet,
                                TweetEntities entities, TweetEntities extendedEntities, Integer favoriteCount,
                                boolean favorited, String filterLevel, long id, String idStr,
                                String inReplyToScreenName, long inReplyToStatusId, String inReplyToStatusIdStr,
                                long inReplyToUserId, String inReplyToUserIdStr, String lang, Place place,
                                boolean possiblySensitive, Object scopes, long quotedStatusId, String quotedStatusIdStr,
                                Tweet quotedStatus, int retweetCount, boolean retweeted, Tweet retweetedStatus,
                                String source, String text, List<Integer> displayTextRange, boolean truncated,
                                User user, boolean withheldCopyright, List<String> withheldInCountries,
                                String withheldScope, Card card, TwitterCard twitterCard) {

        super(coordinates, createdAt, currentUserRetweet,
                entities, extendedEntities, favoriteCount,
                favorited, filterLevel, id, idStr,
                inReplyToScreenName, inReplyToStatusId, inReplyToStatusIdStr,
                inReplyToUserId, inReplyToUserIdStr, lang, place,
                possiblySensitive, scopes, quotedStatusId, quotedStatusIdStr,
                quotedStatus, retweetCount, retweeted, retweetedStatus,
                source, text, displayTextRange, truncated,
                user, withheldCopyright, withheldInCountries,
                withheldScope, card);
        this.twitterCard = twitterCard;
    }
}
