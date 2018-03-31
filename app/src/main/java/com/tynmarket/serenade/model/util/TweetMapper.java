package com.tynmarket.serenade.model.util;

import android.support.annotation.Nullable;

import com.twitter.sdk.android.core.models.Tweet;

/**
 * Created by tynmarket on 2018/03/22.
 */

public class TweetMapper {
    public static Tweet withFavorited(Tweet tweet, boolean favorited) {
        int favoriteCount = tweet.favoriteCount;

        if (tweet.retweetedStatus != null) {
            favoriteCount = tweet.retweetedStatus.favoriteCount;
        }

        if (favorited) {
            favoriteCount += 1;
        } else if (favoriteCount > 0) {
            favoriteCount -= 1;
        }

        Tweet retweetedStatus = withFavoriteCount(tweet.retweetedStatus, favoriteCount);

        return new Tweet(
                tweet.coordinates,
                tweet.createdAt,
                tweet.currentUserRetweet,
                tweet.entities,
                tweet.extendedEntities,
                favoriteCount,
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
                retweetedStatus,
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

    public static Tweet withRetweeted(Tweet tweet, boolean retweeted) {
        int retweetCount = tweet.retweetCount;

        if (retweeted) {
            retweetCount += 1;
        } else if (retweetCount > 0) {
            retweetCount -= 1;
        }


        return new Tweet(
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
                retweetCount,
                retweeted,
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

    @Nullable
    private static Tweet withFavoriteCount(@Nullable Tweet tweet, int favoriteCount) {
        if (tweet == null) {
            return null;
        }

        return new Tweet(
                tweet.coordinates,
                tweet.createdAt,
                tweet.currentUserRetweet,
                tweet.entities,
                tweet.extendedEntities,
                favoriteCount,
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
    }
}
