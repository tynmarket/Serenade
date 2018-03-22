package com.tynmarket.serenade.model;

import com.twitter.sdk.android.core.models.Tweet;

/**
 * Created by tynmarket on 2018/03/22.
 */

public class TweetMapper {
    public static Tweet withFavorited(Tweet tweet, boolean favorited) {
        return new Tweet(
                tweet.coordinates,
                tweet.createdAt,
                tweet.currentUserRetweet,
                tweet.entities,
                tweet.extendedEntities,
                tweet.favoriteCount,
                favorited,
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
