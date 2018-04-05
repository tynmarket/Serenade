package com.tynmarket.serenade.model.sqlite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.SparseArray;

/**
 * Created by tynmarket on 2018/03/31.
 */

public class TweetSQLiteHelper extends SQLiteOpenHelper {
    private static final String TABLE_NAME = "tweets";
    private static final String DB_NAME = TABLE_NAME + ".sqlite";
    private static final int VERSION = 1;

    public static final String INSERT_STATEMENT = "INSERT INTO " + TABLE_NAME + " (section_number, tweet) VALUES(?, ?);";
    public static final String DELETE_STATEMENT = "DELETE FROM " + TABLE_NAME + " WHERE section_number = ?;";
    public static final String SELECT_STATEMENT = "SELECT * FROM " + TABLE_NAME + " WHERE section_number = ? ORDER BY id ASC;";

    private static SparseArray<TweetSQLiteHelper> helpers = new SparseArray<>();

    private TweetSQLiteHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    public static void init(Context context, int sectionNumber) {
        if (helpers.get(sectionNumber) == null) {
            helpers.put(sectionNumber, new TweetSQLiteHelper(context));
        }
    }

    public static TweetSQLiteHelper getHelper(int sectionNumber) {
        return helpers.get(sectionNumber);
    }

    public static void clear(int sectionNumber) {
        helpers.delete(sectionNumber);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + " (id integer primary key, section_number integer, tweet text not null)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(db);
    }
}
