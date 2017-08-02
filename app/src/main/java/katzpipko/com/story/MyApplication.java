package katzpipko.com.story;

import android.app.Application;
import android.content.Context;

/**
 * Created by User on 2017-08-02.
 */

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getMyContext(){
        return context;
    }

}
