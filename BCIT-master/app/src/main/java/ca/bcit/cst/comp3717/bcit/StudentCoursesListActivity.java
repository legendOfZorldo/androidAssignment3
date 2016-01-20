package ca.bcit.cst.comp3717.bcit;

import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentCoursesListActivity extends ListActivity {

    final public static String TABLE_NAME = "STUDENTCOURSE";

    private Intent intent;
    private String studentID;
    private SQLiteDatabase database;
    private Cursor cursor;
    private SQLiteOpenHelper helper;

    private ArrayAdapter<String> courseAdapter;
    private ArrayList<String> listItems=new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        courseAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
        setListAdapter(courseAdapter);
        ListView courseList;
        courseList = getListView();
        intent = getIntent();
        studentID = intent.getStringExtra("id");
        try
        {

            helper   = new BCITDatabaseHelper(this);
            database = helper.getReadableDatabase();
            String s = "SELECT * FROM " + TABLE_NAME  +" WHERE NUMBER = '" + studentID + "'";
            System.out.println(s);
            cursor   = database.rawQuery("SELECT * FROM " + TABLE_NAME  +" WHERE NUMBER = '"
                    + studentID + "'", null);
            System.out.println(cursor.getCount());
            while (cursor.moveToNext()) {
                String course = cursor.getString(cursor.getColumnIndex("COURNAME"));
                listItems.add(course);
                courseAdapter.notifyDataSetChanged();
            }
            courseList.setAdapter(courseAdapter);
        }
        catch(final SQLiteException ex)
        {
            Toast.makeText(this,
                    "Database unavailable",
                    Toast.LENGTH_LONG).show();
            Log.e("CourseActivity",
                    "Database error",
                    ex);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_student_courses_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
