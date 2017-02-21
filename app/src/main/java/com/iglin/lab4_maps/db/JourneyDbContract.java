package com.iglin.lab4_maps.db;

import android.provider.BaseColumns;

/**
 * Created by user on 21.02.2017.
 */

public class JourneyDbContract {
    private JourneyDbContract() {
    throw new AssertionError("Do not instantiate contract class!");
}

    public static abstract class PointTable implements BaseColumns {
        public static final String TABLE_NAME = "point";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_DESCRIPTION = "descr";
        public static final String COLUMN_NAME_LNG = "lng";
        public static final String COLUMN_NAME_LAT = "lat";
        public static final String COLUMN_NAME_ICON = "icon";
    }

    public static abstract class PictureTable implements BaseColumns {
        public static final String TABLE_NAME = "picture";
        public static final String COLUMN_NAME_POINT = "point";
        public static final String COLUMN_NAME_PICTURE = "picture";
    }

    public static abstract class JourneyTable implements BaseColumns {
        public static final String TABLE_NAME = "journey";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_START = "start";
        public static final String COLUMN_NAME_END = "end";
    }
}
