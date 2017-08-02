package katzpipko.com.story.Model;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by User on 2017-08-02.
 */

public class UserSql {


    private String uid;
    private String email;
    private String firstName;
    private String lastName;
    private String profileImage;
    private long createdTimeStamp;



    static final String USER_TABLE = "user";
    static final String USER_UID = "uid";
    static final String USER_EMAIL = "email";
    static final String USER_FIRST_NAME = "firstName";
    static final String USER_LAST_NAME = "lastName";
    static final String USER_PROFILE_IMAGE = "profileImage";
    static final String USER_CREATED_TIME_STAMP= "createdTimeStamp";


    public static void DeleteAllRows(SQLiteDatabase db)
    {
        db.execSQL("delete from "+ USER_TABLE);
    }

   public static void UpserUser(SQLiteDatabase db, User user) {
        ContentValues values = new ContentValues();
        values.put(USER_UID, user.getUid());
        values.put(USER_FIRST_NAME, user.getFirstName());
        values.put(USER_LAST_NAME, user.getLastName());
        values.put(USER_EMAIL, user.getEmail());
        values.put(USER_PROFILE_IMAGE, user.getProfileImage());
        values.put(USER_CREATED_TIME_STAMP, user.getCreatedTimeStamp());

       DeleteAllRows(db); //Clear Table
       db.insert(USER_TABLE, USER_UID, values);
    }

    static User getUser(SQLiteDatabase db)
    {
        Cursor cursor = db.query(USER_TABLE, null, null, null, null, null, null);
        if (cursor.getCount() ==0) return null;//No User yet
        cursor.moveToFirst();

        User user = new User();
        user.setUid(cursor.getString(cursor.getColumnIndex(USER_UID)));
        user.setFirstName(cursor.getString(cursor.getColumnIndex(USER_FIRST_NAME)));
        user.setLastName(cursor.getString(cursor.getColumnIndex(USER_LAST_NAME)));
        user.setProfileImage(cursor.getString(cursor.getColumnIndex(USER_PROFILE_IMAGE)));
        user.setEmail(cursor.getString(cursor.getColumnIndex(USER_EMAIL)));
        user.setCreatedTimeStamp(cursor.getLong(cursor.getColumnIndex(USER_CREATED_TIME_STAMP)));

        return user;
    }

    static public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + USER_TABLE +
                " (" +
                USER_UID + " TEXT PRIMARY KEY, " +
                USER_FIRST_NAME + " TEXT, " +
                USER_LAST_NAME + " TEXT, " +
                USER_PROFILE_IMAGE + " TEXT, " +
                USER_CREATED_TIME_STAMP + " UNSIGNED BIG INT, " +
                USER_EMAIL + " TEXT);");
    }

    static public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop " + USER_TABLE + ";");
        onCreate(db);
    }

}
