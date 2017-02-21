package com.iglin.lab4_maps.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.iglin.lab4_maps.model.Point;
import com.iglin.lab4_maps.db.JourneyDbContract.*;

/**
 * Created by user on 21.02.2017.
 */

public class JourneyContentProvider {
    private JourneyDbHelper mDbHelper;

    public JourneyContentProvider(Context context) {
        mDbHelper = new JourneyDbHelper(context);
    }

    private JourneyContentProvider() {
        throw new AssertionError("Use parametrized constructor instead!");
    }

    public Point insertRecord(Point point) {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PointTable.COLUMN_NAME_ICON, point.getIconId());
        values.put(PointTable.COLUMN_NAME_TITLE, point.getTitle());
        values.put(PointTable.COLUMN_NAME_DESCRIPTION, point.getDescription());
        values.put(PointTable.COLUMN_NAME_LAT, point.getLat());
        values.put(PointTable.COLUMN_NAME_LNG, point.getLng());

        int id = (int) db.insert(
                PointTable.TABLE_NAME,
                null,
                values);
        point.setId(id);

        db.close();
        return point;
    }

    public Point readPoint(int id) {
        String selection = PointTable._ID + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        String[] projection = {
                PointTable._ID,
                PointTable.COLUMN_NAME_TITLE,
                PointTable.COLUMN_NAME_DESCRIPTION,
                PointTable.COLUMN_NAME_LAT,
                PointTable.COLUMN_NAME_LNG,
                PointTable.COLUMN_NAME_ICON
        };

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                PointTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
        Point point = new Point();

        int index = cursor.getColumnIndex(PointTable._ID);
        point.setId(cursor.getInt(index));
        index = cursor.getColumnIndex(PointTable.COLUMN_NAME_TITLE);
        point.setTitle(cursor.getString(index));
        index = cursor.getColumnIndex(PointTable.COLUMN_NAME_DESCRIPTION);
        point.setDescription(cursor.getString(index));
        index = cursor.getColumnIndex(PointTable.COLUMN_NAME_LAT);
        point.setLat(cursor.getDouble(index));
        index = cursor.getColumnIndex(PointTable.COLUMN_NAME_LNG);
        point.setLng(cursor.getDouble(index));
        index = cursor.getColumnIndex(PointTable.COLUMN_NAME_ICON);
        point.setIconId(cursor.getInt(index));

        cursor.close();
        db.close();
        return point;
    }

    public void deletePoint(double lat, double lng) {
        // TODO check if point used in journeys

        String selection = PointTable.COLUMN_NAME_LAT + " = ? AND " + PointTable.COLUMN_NAME_LNG + " = ? ";
        String[] selectionArgs = { String.valueOf(lat), String.valueOf(lng) };
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        db.delete(PointTable.TABLE_NAME, selection, selectionArgs);
        db.close();
    }
}
