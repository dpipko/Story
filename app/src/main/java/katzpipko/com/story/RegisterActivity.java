package katzpipko.com.story;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseUser;

import katzpipko.com.story.Model.Model;
import katzpipko.com.story.Model.ModelFirebase;
import katzpipko.com.story.Model.User;

import static katzpipko.com.story.CreateStory.REQUEST_IMAGE_CAPTURE;

public class RegisterActivity extends Activity {



    private ProgressBar mainProgress;
    private Bitmap imageBitmap;
    private ImageView imageView;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText passwordVerify;
    private View currentView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);


        email = (EditText) findViewById(R.id.registerEmail);
        firstName = (EditText)findViewById(R.id.registerFirstName);
        lastName = (EditText)findViewById(R.id.registerLastName);
        password = (EditText)findViewById(R.id.registerPassword);
        passwordVerify = (EditText)findViewById(R.id.registerPasswordVerify);
        imageView = (ImageView) findViewById(R.id.registerProfilePic);
        mainProgress = (ProgressBar)findViewById(R.id.mainProgress);


        //Upload Image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Clicked Upload Image");
                dispatchTakePictureIntent();
            }
        });


        //Register Button
        final Button registerButton = (Button) findViewById(R.id.registerRegisterBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                currentView=v;

                if (imageBitmap==null)
                    sb.append("Please enter picture\n");



                if (!email.getText().toString().contains("@") || !email.getText().toString().contains("."))
                    sb.append("Email is not valid\n");

                if (password.getText().toString().length()<6)
                    sb.append("Password must be at least 6 long\n");

                if (!(password.getText().toString().equals(passwordVerify.getText().toString())))
                    sb.append("password not match\n");



                if (lastName.getText().toString().length()<2 || firstName.getText().toString().length()<2)
                    sb.append("Please enter valid full name\n");

                if (sb.length()>0)
                    Model.instace.utils.Alert(v.getContext(),sb.toString(),"Error");

                else
                {

                    final User newUser = new User();
                    newUser.setEmail(email.getText().toString());
                    newUser.setFirstName(firstName.getText().toString());
                    newUser.setLastName(lastName.getText().toString());


                    registerButton.setVisibility(View.GONE);
                    mainProgress.setVisibility(View.VISIBLE);



                    Model.instace.Register(newUser, password.getText().toString(), new ModelFirebase.CallbackRegisterInteface() {
                        @Override
                        public void OnComplete(final FirebaseUser currentUser) {

                            Log.d("TAG","Success = " + currentUser.getUid());
                            String name = currentUser.getUid();
                            Model.instace.saveImage(imageBitmap, name, "profileImages", new Model.SaveImageListener() {
                                @Override
                                public void complete(String url) {

                                    newUser.setProfileImage(url);
                                    newUser.setUid(currentUser.getUid());

                                    Model.instace.UpdateUserProfile(newUser, new ModelFirebase.CallBackGeneric() {
                                        @Override
                                        public void OnComplete(Object res) {
                                            Model.instace.setUserData(newUser);
                                            Log.d("TAG","Register + Upload Image + Update profile Sucess");

                                            Intent intent = new Intent(currentView.getContext(), MainStory.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        @Override
                                        public void OnError(Object res) {
                                            Model.instace.utils.Alert(currentView.getContext(),"Error Update User Profile","Server Error");

                                            registerButton.setVisibility(View.VISIBLE);
                                            mainProgress.setVisibility(View.GONE);

                                            currentUser.delete();

                                        }
                                    });

                                }

                                @Override
                                public void fail() {

                                    mainProgress.setVisibility(View.GONE);
                                    registerButton.setVisibility(View.VISIBLE);

                                    Model.instace.utils.Alert(currentView.getContext(),"Error Uploading Image","Info Error");
                                    currentUser.delete();
                                }

                            });
                        }

                        @Override
                        public void OnError(String exception) {
                            mainProgress.setVisibility(View.GONE);
                            registerButton.setVisibility(View.VISIBLE);

                            Model.instace.utils.Alert(currentView.getContext(),exception,"Server Error");
                             }
                    });


                }


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

            if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
                Bundle extras = data.getExtras();
                imageBitmap = (Bitmap) extras.get("data");
                imageView.setImageBitmap(imageBitmap);
            }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }
}
