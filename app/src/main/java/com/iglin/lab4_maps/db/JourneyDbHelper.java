package com.iglin.lab4_maps.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.iglin.lab4_maps.db.JourneyDbContract.*;

/**
 * Created by user on 21.02.2017.
 */

public class JourneyDbHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Journey.db";

    private static final String TEXT_TYPE = "TEXT";
    private static final String INT_TYPE = "INTEGER";
    private static final String REAL_TYPE = "REAL";
    private static final String NUMERIC_TYPE = "NUMERIC";
    private static final String BLOB_TYPE = "BLOB";

    private static final String SQL_CREATE_TABLE_POINT =
            "CREATE TABLE " + PointTable.TABLE_NAME + " (" +
                    PointTable._ID + " INTEGER PRIMARY KEY, " +
                    PointTable.COLUMN_NAME_TITLE + " " + TEXT_TYPE + ", " +
                    PointTable.COLUMN_NAME_DESCRIPTION + " " + TEXT_TYPE + ", " +
                    PointTable.COLUMN_NAME_LAT + " " + REAL_TYPE + ", " +
                    PointTable.COLUMN_NAME_LNG + " " + REAL_TYPE + ", " +
                    PointTable.COLUMN_NAME_ICON + " " + INT_TYPE +
                    " )";
    private static final String SQL_DROP_TABLE_POINT = "DROP TABLE IF EXISTS " + PointTable.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_PICTURE =
            "CREATE TABLE " + PictureTable.TABLE_NAME + " (" +
                    PictureTable._ID + " INTEGER PRIMARY KEY, " +
                    PictureTable.COLUMN_NAME_POINT + " " + INT_TYPE + ", " +
                    PictureTable.COLUMN_NAME_PICTURE + " " + BLOB_TYPE + ", " +
                    " FOREIGN KEY(" + PictureTable.COLUMN_NAME_POINT + ") REFERENCES "
                    + PointTable.TABLE_NAME + "(" + PointTable._ID + ") " +
                    " )";
    private static final String SQL_DROP_TABLE_PICTURE = "DROP TABLE IF EXISTS " + PictureTable.TABLE_NAME;

    private static final String SQL_CREATE_TABLE_JOURNEY =
            "CREATE TABLE " + JourneyTable.TABLE_NAME + " (" +
                    JourneyTable._ID + " INTEGER PRIMARY KEY, " +
                    JourneyTable.COLUMN_NAME_TITLE + " " + TEXT_TYPE + ", " +
                    JourneyTable.COLUMN_NAME_START + " " + INT_TYPE + ", " +
                    JourneyTable.COLUMN_NAME_END + " " + INT_TYPE + ", " +
                    " FOREIGN KEY(" + JourneyTable.COLUMN_NAME_START + ") REFERENCES "
                    + PointTable.TABLE_NAME + "(" + PointTable._ID + "), " +
                    " FOREIGN KEY(" + JourneyTable.COLUMN_NAME_END + ") REFERENCES "
                    + PointTable.TABLE_NAME + "(" + PointTable._ID + ") " +
                    " )";
    private static final String SQL_DROP_TABLE_JOURNEY = "DROP TABLE IF EXISTS " + JourneyTable.TABLE_NAME;

    JourneyDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_POINT);
        db.execSQL(SQL_CREATE_TABLE_PICTURE);
        db.execSQL(SQL_CREATE_TABLE_JOURNEY);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // This database is only a cache for online data, so its upgrade policy is
        // to simply to discard the data and start over
        db.execSQL(SQL_DROP_TABLE_PICTURE);
        db.execSQL(SQL_DROP_TABLE_JOURNEY);
        db.execSQL(SQL_DROP_TABLE_POINT);
        onCreate(db);
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
