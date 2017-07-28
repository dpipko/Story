package katzpipko.com.story;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import static katzpipko.com.story.CreateStory.REQUEST_IMAGE_CAPTURE;

public class RegisterActivity extends Activity {




    private Bitmap imageBitmap;
    private ImageView imageView;
    private EditText email;
    private EditText firstName;
    private EditText lastName;
    private EditText password;
    private EditText passwordVerify;

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



        //Upload Image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Clicked Upload Image");
                dispatchTakePictureIntent();
            }
        });

        Button registerButton = (Button) findViewById(R.id.registerRegisterBtn);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
