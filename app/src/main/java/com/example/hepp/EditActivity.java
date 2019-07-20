package com.example.hepp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.hepp.data.BookContract.BookEntry;
import com.example.hepp.data.BookDbHelper;

/**
 * Allows a user to create a new book
 */

public class EditActivity extends AppCompatActivity {
    //The title of the book
    private EditText mTitle;
    //The name ot the author
    private EditText mAuthor;
    //The isbn number of the book
    private EditText mIsbnNumber;
    //The name of the publisher
    private EditText mPublisher;
    //Edit text field to enter the availability of the book
    private Spinner mAvailabilitySpinner;

    /**
     * Availability of the book has two options.
     * 0 for not available
     * 1 for available
     * 2 for invalid
     */
    private int mAvailable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        //find the relevant views that we will use
        mTitle = findViewById(R.id.title_of_the_book);
        mAuthor = findViewById(R.id.author);
        mIsbnNumber = findViewById(R.id.isbn);
        mPublisher = findViewById(R.id.publisher);
        mAvailabilitySpinner = findViewById(R.id.spinner_available);

        setupSpinner();

        Button addButton = findViewById(R.id.button1);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void setupSpinner() {
        //create a spinner options from the string array
        //The spinner will use the default layout
        ArrayAdapter availabilitySpinnerAdapter = ArrayAdapter.createFromResource(getApplicationContext(),
                R.array.array_availability_options, android.R.layout.simple_spinner_dropdown_item);
        //specify the dropdown style for the spinner-simple list view with one item in a line
        availabilitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        //Apply the adapter to the spinner
        mAvailabilitySpinner.setAdapter(availabilitySpinnerAdapter);

        mAvailabilitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                String selection = (String) adapterView.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals("Available")) {
                        mAvailable = BookEntry.AVAILABILITY_AVAILABLE; //Not available
                    } else if (selection.equals("NOT Available")) {
                        mAvailable = BookEntry.AVAILABILITY_NOT_AVAILABLE; //Available
                    } else {
                        mAvailable = BookEntry.AVAILABILITY_INVALID; //invalid
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                mAvailable = BookEntry.AVAILABILITY_INVALID;
            }
        });

    }

    public void insertBook() {
        //Read from the input fields
        //use trim() to eliminate leading or trailing white space
        String title = mTitle.getText().toString().trim();
        String author = mAuthor.getText().toString().trim();
        String isbn = mIsbnNumber.getText().toString().trim();
        String publisher = mPublisher.getText().toString().trim();

//        ContentValues allows creation of key/pair values. in this context
//        the keys are the names of the columns while the values are the data
//        passes to the columns
        ContentValues values = new ContentValues();
        values.put(BookEntry.COLUMN_BOOK_TITLE, title);
        values.put(BookEntry.COLUMN_BOOK_AUTHOR, author);
        values.put(BookEntry.COLUMN_BOOK_ISBN, isbn);
        values.put(BookEntry.COLUMN_BOOK_PUBLISHER, publisher);
        values.put(BookEntry.COLUMN_AVAILABILITY, mAvailable);

        //Helper object to enable us to access the database
        BookDbHelper bookDbHelper = new BookDbHelper(this);
        //Create and/or open the database in write mode
        SQLiteDatabase db = bookDbHelper.getWritableDatabase();
        //Insert method returns -1 when it is unsuccessful otherwise row inserted successfully
        // The first argument is the name of the table
        //if the second argument is set tu null the the framework will not insert in date if there
        //are no values provided
        //The third argument are the values to insert in the database
        long newRow = db.insert(BookEntry.TABLE_NAME, null, values);

        if (newRow == -1) {
            Toast.makeText(this, "Error saving the pet", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "pet saved successfully" + newRow, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //inflate the menu options on the res/menu/menu_edit file
        //This adds the menu items to the app bar
        getMenuInflater().inflate(R.menu.menu_edit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            //Respond to a click "DELETE" the menu options
            case R.id.action_delete:
                return true;
            //Respond to a click "save" on the app bar
            case R.id.action_save:
                //save book to the database
                insertBook();
                //Exit Activity
                finish();
                return true;

            case android.R.id.home:
                //Navigate back to the parent activity
                NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
