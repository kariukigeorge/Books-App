package com.example.hepp.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.hepp.data.BookContract.BookEntry;

public class BookDbHelper extends SQLiteOpenHelper {

    //Name of the database file
    private static final String DATABASE_NAME = "Books.db";
    //Database version. if you change the database schema, you must increment the database version
    private static  final int DATABASE_VERSION = 1;

    //constructor
    public BookDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create Table Books{id INTEGER PRIMARY KEY, title TEXT, author TEXT, isbn TEXT, publisher TEXT..
        //create a string the will contain the sql statement for the books table

        String SQL_CREATE_BOOKS_TABLE = "CREATE TABLE " + BookEntry.TABLE_NAME + "("
                + BookEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BookEntry.COLUMN_BOOK_TITLE + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_AUTHOR + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_ISBN + " TEXT NOT NULL, "
                + BookEntry.COLUMN_BOOK_PUBLISHER + " TEXT NOT NULL, "
                + BookEntry.COLUMN_AVAILABILITY + " INTEGER NOT NULL);";

        db.execSQL(SQL_CREATE_BOOKS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
