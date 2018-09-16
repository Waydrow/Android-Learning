package com.waydrow.habittracker;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.waydrow.habittracker.adapter.HabitsAdapter;
import com.waydrow.habittracker.data.Habit;
import com.waydrow.habittracker.data.HabitContract;
import com.waydrow.habittracker.data.HabitContract.HabitEntry;
import com.waydrow.habittracker.data.HabitDbHelper;

import java.util.ArrayList;

public class HabitActivity extends AppCompatActivity {

    private HabitDbHelper mDbHelper;

    // adapter
    private HabitsAdapter mHabitsAdapter;

    // empty view for empty list
    private TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_habit);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HabitActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // get listview
        ListView habitsList = (ListView) findViewById(R.id.habits_list);

        // set empty view for listview
        mEmptyTextView = (TextView) findViewById(R.id.empty_view);
        habitsList.setEmptyView(mEmptyTextView);

        // Define habits adapter
        mHabitsAdapter = new HabitsAdapter(this, new ArrayList<Habit>());

        habitsList.setAdapter(mHabitsAdapter);

        mDbHelper = new HabitDbHelper(this);

        displayDatabaseInfo();

        habitsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // get current click habit
                Habit habit = (Habit) parent.getItemAtPosition(position);
                Intent intent = new Intent(HabitActivity.this, EditorActivity.class);
                // pass data to edit activity
                intent.putExtra("habit", habit);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // clear data before
        mHabitsAdapter.clear();
        displayDatabaseInfo();
    }


    /**
     * Temporary helper method to display information in the onscreen TextView about the state of
     * the habits database.
     */
    private void displayDatabaseInfo() {

        // Create and/or open a database to read from it
        SQLiteDatabase db = mDbHelper.getReadableDatabase();

        // Perform this raw SQL query "SELECT * FROM pets"
        // to get a Cursor that contains all rows from the pets table.
        String[] projection = {
                HabitEntry._ID,
                HabitEntry.COLUMN_HABIT_TITLE,
                HabitEntry.COLUMN_HABIT_LOCATION,
                HabitEntry.COLUMN_HABIT_TIMES};

        Cursor cursor = db.query(
                HabitEntry.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null);

        //TextView displayView = (TextView) findViewById(R.id.text_view_habit);
        try {
            int count = cursor.getCount();

            // Figure out the index of each column
            int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
            int titleColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_TITLE);
            int locationColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_LOCATION);
            int timesColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_TIMES);

            // Iterate through all the returned rows in the cursor
            while (cursor.moveToNext()) {
                // Use that index to extract the String or Int value of the word
                // at the current row the cursor is on.
                int currentID = cursor.getInt(idColumnIndex);
                String currentTitle = cursor.getString(titleColumnIndex);
                String currentLocation = cursor.getString(locationColumnIndex);
                int currentTimes = cursor.getInt(timesColumnIndex);

                Habit habit = new Habit(currentID, currentTitle, currentLocation, currentTimes);
                mHabitsAdapter.add(habit);
            }
        } finally {
            // Always close the cursor when you're done reading from it. This releases all its
            // resources and makes it invalid.
            cursor.close();
        }
    }

    /* delete all habits method */
    private void deleteAllHabits() {
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        int count = db.delete(HabitEntry.TABLE_NAME, null, null);

        if(count > 0) {
            mHabitsAdapter.clear();
            Toast.makeText(this, count + " habit deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error delete habit", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_habit, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete_all:
                deleteAllHabits();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
