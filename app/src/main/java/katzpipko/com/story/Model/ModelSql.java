package katzpipko.com.story.Model;

 import android.content.Context;
 import android.database.sqlite.SQLiteDatabase;
 import android.database.sqlite.SQLiteOpenHelper;



/**
 * Created by menachi on 17/05/2017.
 */

public class ModelSql extends SQLiteOpenHelper {
    ModelSql(Context context) {
        super(context, "database.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        UserSql.onCreate(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        UserSql.onUpgrade(db, oldVersion, newVersion);
    }


}
