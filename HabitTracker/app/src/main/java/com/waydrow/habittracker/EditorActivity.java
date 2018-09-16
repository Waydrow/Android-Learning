package com.waydrow.habittracker;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.waydrow.habittracker.data.Habit;
import com.waydrow.habittracker.data.HabitContract.HabitEntry;
import com.waydrow.habittracker.data.HabitDbHelper;

import static android.R.attr.inset;

public class EditorActivity extends AppCompatActivity {

    // title EditText
    private EditText mTitleEditText;

    // location EditText
    private EditText mLocationEditText;

    // times Spinner
    private Spinner mTimesSpinner;

    // times of the habit, default is 1
    private int mTimes = 1;

    /*
    * flag for insert or update
    * insert = 0
    * update or delete = 1
    */
    private int flag = 0;

    // id for getIntent()'s id to update habit
    private int mId;

    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        mTitleEditText = (EditText) findViewById(R.id.title);
        mLocationEditText = (EditText) findViewById(R.id.location);
        mTimesSpinner = (Spinner) findViewById(R.id.spinner_times);

        // setup Spinner
        setupSpinner();

        // new dbhelper
        mDbHelper = new HabitDbHelper(this);

        // get data from habit activity
        Intent intent = getIntent();
        Habit habit = (Habit) intent.getSerializableExtra("habit");
        if(habit != null) {
            // update or delete
            flag = 1;

            // set activity label
            setTitle(getString(R.string.update_habit_label));

            mTitleEditText.setText(habit.getTitle());
            mLocationEditText.setText(habit.getLocation());
            mTimes = habit.getTimes();
            mTimesSpinner.setSelection(mTimes - 1);

            mId = habit.getId();
        }

    }

    private void setupSpinner() {

        // set adapter for spinner
        ArrayAdapter timesSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_times_options, android.R.layout.simple_spinner_item);

        timesSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        mTimesSpinner.setAdapter(timesSpinnerAdapter);

        // set select listener for spinner
        mTimesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if(!TextUtils.isEmpty(selection)) {
                    if(selection.equals(getString(R.string.times_1))) {
                        mTimes = HabitEntry.TIMES_1;
                    } else if(selection.equals(getString(R.string.times_2))) {
                        mTimes = HabitEntry.TIMES_2;
                    } else {
                        mTimes = HabitEntry.TIMES_3;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mTimes = HabitEntry.TIMES_1;
            }
        });
    }

    /* insert one habit */
    private void insertHabit() {
        String title = mTitleEditText.getText().toString().trim();
        String location = mLocationEditText.getText().toString().trim();

        if(title.equals("") || location.equals("")) {
            return;
        }

        // to write mod
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_TITLE, title);
        values.put(HabitEntry.COLUMN_HABIT_LOCATION, location);
        values.put(HabitEntry.COLUMN_HABIT_TIMES, mTimes);

        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        if(newRowId == -1) {
            Toast.makeText(this, "Error with saving habit", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    /* update one habit */
    private void upateHabit(int _id) {
        String title = mTitleEditText.getText().toString().trim();
        String location = mLocationEditText.getText().toString().trim();

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_TITLE, title);
        values.put(HabitEntry.COLUMN_HABIT_LOCATION, location);
        values.put(HabitEntry.COLUMN_HABIT_TIMES, mTimes);

        // where clause
        String selection = HabitEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(_id)};

        // row count returns
        int count = db.update(
                HabitEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
        if(count > 0) {
            Toast.makeText(this, count + " habit saved", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error update habit", Toast.LENGTH_SHORT).show();
        }
    }

    /* delete one habit */
    private void deleteHabit(int _id) {

        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        String selection = HabitEntry._ID + " = ?";
        String[] selectionArgs = {Integer.toString(_id)};

        int count = db.delete(HabitEntry.TABLE_NAME, selection, selectionArgs);
        if(count > 0) {
            Toast.makeText(this, count + " habit deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error delete habit", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_save:
                // insert
                if(flag == 0) {
                    insertHabit();
                } else {
                    // update
                    upateHabit(mId);
                }
                // back to parent activity
                finish();
                return true;
            case R.id.action_delete:
                // delete habit
                deleteHabit(mId);
                finish();
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
