package com.example.hepp.data;

import android.provider.BaseColumns;


public final class BookContract {

    public static abstract class BookEntry implements BaseColumns{

        public static final String TABLE_NAME = "Books";

        public static final String _ID = BaseColumns._ID;
        public static  final String COLUMN_BOOK_TITLE = "title";
        public static final String COLUMN_BOOK_AUTHOR = "author";
        public static final String COLUMN_BOOK_ISBN = "isbn";
        public static final String COLUMN_BOOK_PUBLISHER = "publisher";
        public static final String COLUMN_AVAILABILITY = "availability";


        /**
         * possible values for the availability of the book
         */
        public static final int AVAILABILITY_NOT_AVAILABLE = 0;
        public static final int AVAILABILITY_AVAILABLE = 1;
        public static final int AVAILABILITY_INVALID = 2;
    }
}
