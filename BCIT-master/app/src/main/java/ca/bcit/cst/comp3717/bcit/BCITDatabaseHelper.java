package ca.bcit.cst.comp3717.bcit;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class BCITDatabaseHelper
    extends SQLiteOpenHelper
{
    public static final String DB_NAME = "bcit";
    public static final int DB_VERSION = 1;

    public static final String COURSE_TABLE = "COURSE";
    public static final String COURSE_NAME = "COURNAME";
    public static final String COURSE_DESCRIPTION = "DESCRIPTION";

    public static final String STUDENT_TABLE = "STUDENT";
    public static final String STUDENT_NUMBER = "NUMBER";
    public static final String STUDENT_NAME = "STUNAME";

    public static final String STU_COUR_TABLE = "STUDENTCOURSE";

    public BCITDatabaseHelper(final Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(final SQLiteDatabase database)
    {
        database.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + STU_COUR_TABLE);

        database.execSQL("CREATE TABLE " + COURSE_TABLE + " (" +
                         "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                         COURSE_NAME + " TEXT, " +
                         COURSE_DESCRIPTION + " TEXT)");

        System.out.println("CREATE TABLE " + COURSE_TABLE + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COURSE_NAME + " TEXT, " +
                COURSE_DESCRIPTION + " TEXT)");

        database.execSQL("CREATE TABLE IF NOT EXISTS " + STUDENT_TABLE + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STUDENT_NUMBER + " TEXT, " +
                STUDENT_NAME + " TEXT)");

        System.out.println("CREATE TABLE IF NOT EXISTS " + STUDENT_TABLE + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STUDENT_NUMBER + " TEXT, " +
                STUDENT_NAME + " TEXT)");

        database.execSQL("CREATE TABLE IF NOT EXISTS " + STU_COUR_TABLE + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STUDENT_NUMBER + " TEXT, " +
                COURSE_NAME + " TEXT)");

        System.out.println("CREATE TABLE IF NOT EXISTS " + STU_COUR_TABLE + " (" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                STUDENT_NUMBER + " TEXT, " +
                COURSE_NAME + " TEXT)");

        insertCourse(database, "COMP 3717", "Android");
        insertCourse(database, "COMP 3512", "C++");
        insertCourse(database, "COMP 3711", "OOAD");
        insertCourse(database, "COMP 3721", "Datacomm");
        insertCourse(database, "COMP 3760", "Algorithms");
        insertCourse(database, "COMP 3900", "Projects");
    }

    public void insertCourse(final SQLiteDatabase database,
                             final String name,
                             final String description)
    {
        final ContentValues courseValues;

        courseValues = new ContentValues();
        courseValues.put(COURSE_NAME, name);
        courseValues.put(COURSE_DESCRIPTION, description);
        database.insert(COURSE_TABLE, null, courseValues);
    }

    public static void insertStudent(final SQLiteDatabase database,
                             final String id,
                             final String name)
    {
        final ContentValues courseValues;

        courseValues = new ContentValues();
        courseValues.put(STUDENT_NUMBER,id);
        courseValues.put(STUDENT_NAME, name);
        database.insert(STUDENT_TABLE, null, courseValues);
    }

    public static void insertStudentCourse(final SQLiteDatabase database,
                             final String id,
                             final String courseName)
    {
        final ContentValues courseValues;

        courseValues = new ContentValues();
        courseValues.put(STUDENT_NUMBER,id);
        courseValues.put(COURSE_NAME, courseName);
        database.insert(STU_COUR_TABLE, null, courseValues);
    }

    @Override
    public void onUpgrade(final SQLiteDatabase database,
                          final int            oldVersion,
                          final int            newVersion)
    {
        /*database.execSQL("DROP TABLE IF EXISTS " + COURSE_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + STUDENT_TABLE);
        database.execSQL("DROP TABLE IF EXISTS " + STU_COUR_TABLE);

        onCreate(database); */
    }
}
