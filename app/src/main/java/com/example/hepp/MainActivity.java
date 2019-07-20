package com.example.hepp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.hepp.data.BookContract.BookEntry;
import com.example.hepp.data.BookDbHelper;

public class MainActivity extends AppCompatActivity {
    //Database helper that will provide us access to the database
    private BookDbHelper bookDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //link the main Activity with the edit activity
        Button createButton = findViewById(R.id.create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                startActivity(intent);
            }
        });

        bookDbHelper = new BookDbHelper(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayDatabaseInfo();
    }

    public void displayDatabaseInfo(){
        //To access the the database we need to instantiate our subclass of SQLiteOpenHelper
        //and pass the context which is our current Activity
        BookDbHelper bookDbHelper = new BookDbHelper(this);

        //create and/or open a database to read from it
        SQLiteDatabase db = bookDbHelper.getReadableDatabase();

        String[] projections = {
                BookEntry._ID,
                BookEntry.COLUMN_BOOK_TITLE,
                BookEntry.COLUMN_BOOK_AUTHOR,
                BookEntry.COLUMN_BOOK_ISBN,
                BookEntry.COLUMN_BOOK_PUBLISHER,
                BookEntry.COLUMN_AVAILABILITY
        };
//      query api helps in reading the content in the database
        Cursor cursor =  db.query(
                BookEntry.TABLE_NAME,
                projections,
                null,
                null,
                null,
                null,
                null
        );

        TextView displayRows = findViewById(R.id.num_of_rows);
        try {

            //Display the number of rows on the cursor(which reflects the number of rows in the db
            displayRows.setText("The number of rows in the database is: " + cursor.getCount() + "\n\n");
            //Append all the column names which will act as the header on our application
            displayRows.append("\n" +BookEntry._ID +
                    "-"+BookEntry.COLUMN_BOOK_TITLE +
                    "-" + BookEntry.COLUMN_BOOK_AUTHOR +
                    "-"+BookEntry.COLUMN_BOOK_ISBN +
                    "-" + BookEntry.COLUMN_BOOK_PUBLISHER +
                    "-" + BookEntry.COLUMN_AVAILABILITY + "\n\n");

//            figure out the column of each index the database
            int idIndex = cursor.getColumnIndex(BookEntry._ID);
            int titleIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_TITLE);
            int aurthoIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_AUTHOR);
            int isbnIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_ISBN);
            int publisherIndex = cursor.getColumnIndex(BookEntry.COLUMN_BOOK_PUBLISHER);
            int availabilityIndex = cursor.getColumnIndex(BookEntry.COLUMN_AVAILABILITY);

            while (cursor.moveToNext()){

                //use that string to extract the struing or int value of the word
                //at the current row the cursor is on
                int currentId = cursor.getInt(idIndex);
                String currentTitle = cursor.getString(titleIndex);
                String currentAurthor = cursor.getString(aurthoIndex);
                String currentIsbn = cursor.getString(isbnIndex);
                String currentPublisher = cursor.getString(publisherIndex);
                int availability = cursor.getInt(availabilityIndex);

                //display the values extracted from the rows by appending each row in the text view

                displayRows.append(currentId + " " + currentTitle + " " + currentAurthor + " " + currentIsbn
                + " " + currentPublisher + " " + availability + "\n\n");
            }
        }finally {
            //Always close the cursor when you are done with it.This releases all the resources and makes
            //it invalid
            cursor.close();
        }

    }

    public void insert(){
//        create and/or open a database  a writable mode
        SQLiteDatabase db = bookDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_TITLE, "Dropdown From the Sky");
        values.put(BookEntry.COLUMN_BOOK_AUTHOR, "George Kariuki");
        values.put(BookEntry.COLUMN_BOOK_ISBN, "1234671");
        values.put(BookEntry.COLUMN_BOOK_PUBLISHER, "Symphony");
        values.put(BookEntry.COLUMN_AVAILABILITY, "Available");

        //The insert method will return a long value and -1 if the data has not been inserted to the db
        long newRowId = db.insert(BookEntry.TABLE_NAME, null, values);

        Log.v("MainActivity", "This is a new row: " + newRowId);
    }


    //create the menu on the app bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu options form the res/menu/menu_main
        //This adds the menu items on the app bar
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        //user clicks on an option menu on the app bar overflow menu
        switch(item.getItemId()){
            //respond to a click "insert dummy data" on menu options
            case R.id.action_insert_dummy_data:
                insert();
                displayDatabaseInfo();
                return true;
                //respond to a click "delete all entries" on menu options
            case R.id.action_delete_all_entries:
                //do nothing for now
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
