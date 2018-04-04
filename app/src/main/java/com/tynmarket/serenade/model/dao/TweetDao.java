package com.tynmarket.serenade.model.dao;

import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.google.gson.Gson;
import com.twitter.sdk.android.core.models.Tweet;
import com.tynmarket.serenade.model.sqlite.TweetSQLiteHelper;
import com.tynmarket.serenade.model.util.DisposableHelper;

import java.util.List;

import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by tynmarket on 2018/03/31.
 */

public class TweetDao {
    public static void replaceTweets(List<Tweet> tweets, int sectionNumber) {
        TweetSQLiteHelper helper = TweetSQLiteHelper.getHelper();
        SQLiteDatabase db = helper.getWritableDatabase();

        Disposable disposable = Observable
                .create(emitter -> {
                    emitter.onNext(tweets);
                })
                .subscribeOn(Schedulers.io())
                .subscribe(o -> {
                    db.beginTransaction();

                    db.execSQL(TweetSQLiteHelper.DELETE_STATEMENT, new String[]{String.valueOf(sectionNumber)});

                    SQLiteStatement stmt = db.compileStatement(TweetSQLiteHelper.INSERT_STATEMENT);
                    Gson gson = new Gson();

                    for (int i = 0; i < tweets.size(); i++) {
                        Tweet tweet = tweets.get(i);
                        String json = gson.toJson(tweet);
                        stmt.bindLong(1, sectionNumber);
                        stmt.bindString(2, json);
                        stmt.executeInsert();
                    }
                    stmt.close();

                    db.setTransactionSuccessful();
                    db.endTransaction();
                }, throwable -> {
                    db.endTransaction();
                });

        DisposableHelper.add(disposable, sectionNumber);
    }
}
