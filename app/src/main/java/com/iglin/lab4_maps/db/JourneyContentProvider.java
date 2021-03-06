package com.iglin.lab4_maps.db;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.iglin.lab4_maps.R;
import com.iglin.lab4_maps.model.Journey;
import com.iglin.lab4_maps.model.Picture;
import com.iglin.lab4_maps.model.Point;
import com.iglin.lab4_maps.db.JourneyDbContract.*;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

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

    public Point insertPoint(Point point) {
        deletePoint(point.getLat(), point.getLng());

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(PointTable.COLUMN_NAME_ICON, bitmapToBytesArray(point.getIcon()));
        values.put(PointTable.COLUMN_NAME_TITLE, point.getTitle());
        values.put(PointTable.COLUMN_NAME_DESCRIPTION, point.getDescription());
        values.put(PointTable.COLUMN_NAME_LAT, point.getLat());
        values.put(PointTable.COLUMN_NAME_LNG, point.getLng());

        int id = (int) db.insert(
                PointTable.TABLE_NAME,
                null,
                values);
        point.setId(id);

        if (point.getPics() != null && !point.getPics().isEmpty()) {
            for (Picture pic : point.getPics()) {
                values = new ContentValues();
                values.put(PictureTable.COLUMN_NAME_POINT, id);
                values.put(PictureTable.COLUMN_NAME_PICTURE, bitmapToBytesArray(pic.getPicture()));

                int picId = (int) db.insert(
                        PictureTable.TABLE_NAME,
                        null,
                        values);
                pic.setId(picId);
            }
        }

        db.close();
        return point;
    }

    public Journey insertJourney(Journey journey) {
        if (readJourney(journey.getStartPoint(), journey.getEndPoint()) != null ||
                readJourney(journey.getEndPoint(), journey.getStartPoint()) != null) {
            return null;
        }
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(JourneyTable.COLUMN_NAME_TITLE, journey.getName());
        values.put(JourneyTable.COLUMN_NAME_START, journey.getStartPoint().getId());
        values.put(JourneyTable.COLUMN_NAME_END, journey.getEndPoint().getId());

        int id = (int) db.insert(
                JourneyTable.TABLE_NAME,
                null,
                values);
        journey.setId(id);
        db.close();
        return journey;
    }

    public List<Journey> readJourneys() {
        String[] projection = {
                JourneyTable._ID,
                JourneyTable.COLUMN_NAME_TITLE,
                JourneyTable.COLUMN_NAME_START,
                JourneyTable.COLUMN_NAME_END
        };

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                JourneyTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List<Journey> result = new ArrayList<>();
        while (cursor.moveToNext()) {
            Journey journey = new Journey();
            int index = cursor.getColumnIndex(JourneyTable._ID);
            journey.setId(cursor.getInt(index));
            index = cursor.getColumnIndex(JourneyTable.COLUMN_NAME_TITLE);
            journey.setName(cursor.getString(index));
            index = cursor.getColumnIndex(JourneyTable.COLUMN_NAME_START);
            journey.setStartPoint(readPoint(cursor.getInt(index)));
            index = cursor.getColumnIndex(JourneyTable.COLUMN_NAME_END);
            journey.setEndPoint(readPoint(cursor.getInt(index)));

            result.add(journey);
        }
        cursor.close();
        db.close();
        return result;
    }

    public Journey readJourney(Point startPoint, Point endPoint) {
        String selection = JourneyTable.COLUMN_NAME_START + " = ? AND " + JourneyTable.COLUMN_NAME_END + " = ? ";
        String[] selectionArgs = { String.valueOf(startPoint.getId()), String.valueOf(endPoint.getId()) };

        String[] projection = {
                JourneyTable._ID,
                JourneyTable.COLUMN_NAME_TITLE,
                JourneyTable.COLUMN_NAME_START,
                JourneyTable.COLUMN_NAME_END
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
        if (cursor.moveToNext()) {
            Journey journey = new Journey(startPoint, endPoint);
            int index = cursor.getColumnIndex(JourneyTable._ID);
            journey.setId(cursor.getInt(index));
            index = cursor.getColumnIndex(JourneyTable.COLUMN_NAME_TITLE);
            journey.setName(cursor.getString(index));
            cursor.close();
            db.close();
            return journey;
        } else {
            cursor.close();
            db.close();
            return null;
        }
    }

    public Point readPoint(double lat, double lng) {
        String selection = PointTable.COLUMN_NAME_LAT + " = ? AND " + PointTable.COLUMN_NAME_LNG + " = ? ";
        String[] selectionArgs = { String.valueOf(lat), String.valueOf(lng) };

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

        if (cursor.moveToNext()) {
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
            point.setIcon(bytesArrayToBitmap(cursor.getBlob(index)));
            cursor.close();

            cursor = readPics(point.getId());
            while (cursor.moveToNext()) {
                Picture picture = new Picture();
                index = cursor.getColumnIndex(PictureTable._ID);
                picture.setId(cursor.getInt(index));
                index = cursor.getColumnIndex(PictureTable.COLUMN_NAME_PICTURE);
                picture.setPicture(bytesArrayToBitmap(cursor.getBlob(index)));

                point.addPic(picture);
            }

            db.close();
            return point;
        } else {
            db.close();
            return null;
        }
    }

    public List<Point> readPointsForMap() {
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
                null,                                // The columns for the WHERE clause
                null,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );

        List<Point> result = new ArrayList<>();
        while (cursor.moveToNext()) {
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
            //Integer icon = cursor.getInt(index);
            point.setIcon(bytesArrayToBitmap(cursor.getBlob(index)));
            result.add(point);
        }
        cursor.close();
        return result;
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
        point.setIcon(bytesArrayToBitmap(cursor.getBlob(index)));
        cursor.close();

        cursor = readPics(id);
        while (cursor.moveToNext()) {
            Picture picture = new Picture();
            index = cursor.getColumnIndex(PictureTable._ID);
            picture.setId(cursor.getInt(index));
            index = cursor.getColumnIndex(PictureTable.COLUMN_NAME_PICTURE);
            picture.setPicture(bytesArrayToBitmap(cursor.getBlob(index)));

            point.addPic(picture);
        }

        db.close();
        return point;
    }

    public Cursor readPics(int pointId) {
        String selection = PictureTable.COLUMN_NAME_POINT + " = ?";
        String[] selectionArgs = { String.valueOf(pointId) };

        String[] projection = {
                PictureTable._ID,
                PictureTable.COLUMN_NAME_PICTURE,
                PictureTable.COLUMN_NAME_POINT
        };

        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        return db.query(
                PictureTable.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                null                                 // The sort order
        );
    }

    public void deletePoint(double lat, double lng) {
        Point point = readPoint(lat, lng);

        if (point == null) return;

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = JourneyTable.COLUMN_NAME_START + " = ? OR " + JourneyTable.COLUMN_NAME_END + " = ? ";
        String[] selectionArgs = new String[] { String.valueOf(point.getId()), String.valueOf(point.getId()) };
        db.delete(JourneyTable.TABLE_NAME, selection, selectionArgs);

        selection = PictureTable.COLUMN_NAME_POINT + " = ? ";
        selectionArgs = new String[] { String.valueOf(point.getId()) };
        db.delete(PictureTable.TABLE_NAME, selection, selectionArgs);

        selection = PointTable.COLUMN_NAME_LAT + " = ? AND " + PointTable.COLUMN_NAME_LNG + " = ? ";
        selectionArgs = new String[] { String.valueOf(lat), String.valueOf(lng) };
        db.delete(PointTable.TABLE_NAME, selection, selectionArgs);

        db.close();
    }

    private byte[] bitmapToBytesArray(Bitmap bitmap) {
        if (bitmap == null) return null;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 0, stream);
        return stream.toByteArray();
    }

    private Bitmap bytesArrayToBitmap(byte[] image) {
        if (image == null) return null;
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }


}
