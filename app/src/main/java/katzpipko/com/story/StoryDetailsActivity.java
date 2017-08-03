package katzpipko.com.story;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.sql.Timestamp;
import java.text.DateFormat;

import katzpipko.com.story.Model.Model;
import katzpipko.com.story.Model.ModelFirebase;
import katzpipko.com.story.Model.Story;
import katzpipko.com.story.Model.User;
import katzpipko.com.story.Model.Utils;


public class StoryDetailsActivity extends Activity  {


    private  TextView sDstoryCreatedTime;
    private  ImageView sDprofilePic;
    private  TextView sDuserFullName;
    private  ImageView sDstoryImage;
    private  TextView sDstoryTitle;
    private  Story currentStory = null;
    private  User userCreator = null;

    private View currentView=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_details);

        Intent myIntent = getIntent(); // gets the previously created intent
        currentStory = (Story) myIntent.getExtras().getSerializable("story");

        sDprofilePic  = (ImageView) findViewById(R.id.SDprofilePic);
        sDuserFullName = (TextView) findViewById(R.id.SDuserFullName);
        sDstoryImage = (ImageView) findViewById(R.id.SDstoryImage);
        sDstoryTitle = (TextView) findViewById(R.id.SDstoryTitle);
        sDstoryCreatedTime = (TextView) findViewById(R.id.SDstoryCreatedTime);

        this.setTitle(currentStory.title);

        Model.instace.GetUserProfileByUid(currentStory.uid, new ModelFirebase.CallBackGeneric() {
            @Override
            public void OnComplete(Object res) {
                userCreator = (User)res;

                Picasso.with(getBaseContext()).load(currentStory.imageURL).into(sDstoryImage);
                Picasso.with(getBaseContext()).load(userCreator.getProfileImage()).transform(new CircleTransform()).into(sDprofilePic);
                sDuserFullName.setText( userCreator.getFullName());
                sDstoryTitle.setText( currentStory.title);

                Timestamp st = new Timestamp(currentStory.timestamp*1000);
                java.util.Date dt = new java.util.Date(st.getTime());
                DateFormat df = DateFormat.getTimeInstance(DateFormat.SHORT);
                sDstoryCreatedTime.setText(df.format(dt));
            }

            @Override
            public void OnError(Object res) {

                Log.d("TAG","Error story");
                finish();
            }
        });


        Log.d("TAG",currentStory.storyID);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Intent myIntent = getIntent(); // gets the previously created intent
        Story st = (Story) myIntent.getExtras().getSerializable("story");

        if (!st.uid.equals(Model.UID)) return super.onCreateOptionsMenu(menu); //Only current User


        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_story_details, menu);
        return super.onCreateOptionsMenu(menu);

    }


    public void   CloseActivity()
    {
        finish();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {


        switch(item.getItemId())
        {
            case R.id.actionStoryDetailsRemove:


                Model.instace.utils.ConfirmWithCallBack(StoryDetailsActivity.this, "Are you sure", "System Alert", new Utils.ConfirmInterface() {
                    @Override
                    public void Yes() {

                        Model.instace.RemoveStory(currentStory, new ModelFirebase.CallBackGeneric() {
                            @Override
                            public void OnComplete(Object res) {

                                Model.instace.utils.AlertWithCallBack(StoryDetailsActivity.this, "Remove Successful", "Server Info", new Utils.AlertInteface() {
                                            @Override
                                            public void OnAlertFinish() {
                                                CloseActivity();
                                            }
                                        });
                            }

                            @Override
                            public void OnError(Object res) {
                                Model.instace.utils.Alert(StoryDetailsActivity.this,"Oops something wrong","Server Error");

                            }
                        });


                    }

                    @Override
                    public void No() {
                    //nothing
                    }
                });


                break;

            default:
                return true;
        }

    return true;
    }


}
