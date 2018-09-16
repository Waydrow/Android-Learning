package com.waydrow.habittracker.data;

import android.provider.BaseColumns;

/**
 * Created by Waydrow on 2016/11/18.
 */

public class HabitContract {

    private HabitContract() {
    }

    public static final class HabitEntry implements BaseColumns {
        /**
                * Name of database table for habits
        */
        public final static String TABLE_NAME = "habits";

        /**
         * Unique ID number for the habit (only for use in the database table).
         * <p>
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * title of the habit.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_HABIT_TITLE = "title";

        /**
         * location of the habit.
         * <p>
         * Type: TEXT
         */
        public final static String COLUMN_HABIT_LOCATION = "location";

        /**
         * times of the habit.
         * Type: INTEGER
         */
        public final static String COLUMN_HABIT_TIMES = "times";


        /**
         * Possible values for the times of the habit in a week.
         */
        public static final int TIMES_1= 1;
        public static final int TIMES_2 = 2;
        public static final int TIMES_3 = 3;
    }
}
