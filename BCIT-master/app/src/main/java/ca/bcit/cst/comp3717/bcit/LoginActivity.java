package ca.bcit.cst.comp3717.bcit;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class LoginActivity extends AppCompatActivity {

    private String mongoURL = "https://api.mongolab.com/api/1/databases/bcitstudents/collections/courses";
    private String apiKey = "2fJXFYcpl5bf25wd1SLv7jVGYI5Hiw87";
    private String preApiKey = "'}&apiKey=";
    private String postURL = "?q={'studentNumber':'";
    private EditText studentNumber;
    private String enteredStudentNumber;
    private String jsonReturn;

    private JSONArray courses;

    private BCITDatabaseHelper helper;
    private SQLiteDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        studentNumber = (EditText)findViewById(R.id.studentNumber);
        helper = new BCITDatabaseHelper(this);
        database = helper.getWritableDatabase();
        helper.onCreate(database);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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

    public void login(final View view) {
        if (database == null)
            System.out.println("shit");
        else {
            System.out.println("pass");
        }
        accessMongolab mongoBongo = new accessMongolab();
        enteredStudentNumber = studentNumber.getText().toString();
        Intent intent = new Intent(this, StudentCoursesListActivity.class);
        try {
            jsonReturn = mongoBongo.execute(enteredStudentNumber).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        intent.putExtra("id", enteredStudentNumber);
        startActivity(intent);
    }

    private class accessMongolab extends AsyncTask<String, Void, String> {
        public String tmp = "";
        public String jsonString = "";
        public URL url;
        public HttpURLConnection httpConnection;
        @Override
        protected String doInBackground(String... num) {
            try {
                StringBuilder sb = new StringBuilder();
                sb.append(mongoURL);
                sb.append(postURL);
                sb.append(num[0]);
                sb.append(preApiKey);
                sb.append(apiKey);
                try {
                    url = new URL (sb.toString());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                try {
                    httpConnection = (HttpURLConnection) url.openConnection();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                httpConnection.setRequestMethod("GET");
                httpConnection.setRequestProperty("Accept", "application/json");

                BufferedReader br = new BufferedReader(new InputStreamReader((httpConnection.getInputStream())));
                while ((tmp = br.readLine()) != null) {
                    jsonString = tmp;
                }
                System.out.println(jsonString);
                jsonString = jsonString.substring(1, jsonString.length()-1);
                System.out.println(jsonString);
                JSONObject jason = new JSONObject(jsonString);
                String name = jason.getString("name");
                String sID = jason.getString("studentNumber");
                BCITDatabaseHelper.insertStudent(database, sID, name);
                System.out.println(name);
                courses = jason.getJSONArray("courses");
                for (int i = 0; i < courses.length(); i++) {
                    JSONObject aCourse = courses.getJSONObject(i);
                    String courseID = aCourse.getString("courseID");
                    System.out.println(courseID);
                    helper.insertStudentCourse(database, sID, courseID);
                }

            } catch (MalformedURLException e ) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return jsonString;
        }

    }
}
