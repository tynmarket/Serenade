package com.tynmarket.serenade.model.entity;

import com.twitter.sdk.android.core.models.Tweet;

/**
 * Created by tynmarket on 2018/04/28.
 */
public class TweetWithTwitterCard extends Tweet {
    @SuppressWarnings("all")
    private TwitterCard twitterCard;

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
}
