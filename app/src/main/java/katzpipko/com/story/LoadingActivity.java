package katzpipko.com.story;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import katzpipko.com.story.Model.Model;
import katzpipko.com.story.Model.User;

public class LoadingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

         User user =  Model.instace.CheckAndGetStoreUserData();
        if (user==null)
        {
            Intent intent = new Intent(this.getBaseContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        else
        {

            Model.instace.setUserData(user);//Add user to model
            Intent intent = new Intent(this.getBaseContext(), MainStory.class);
            startActivity(intent);
            finish();
        }


     }
}
