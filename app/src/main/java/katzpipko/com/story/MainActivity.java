package katzpipko.com.story;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import katzpipko.com.story.Model.Model;
import katzpipko.com.story.Model.ModelFirebase;
import static katzpipko.com.story.CreateStory.REQUEST_IMAGE_CAPTURE;


public class MainActivity extends Activity {
     EditText email;
    EditText password;
    ProgressBar mainProgress;
    Button registerButton;
    private  View _v;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TAB","start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
          email = (EditText) findViewById(R.id.mainEmail);
          password = (EditText) findViewById(R.id.mainPassword);
        mainProgress = (ProgressBar) findViewById(R.id.mainProgress);
         registerButton = (Button)this.findViewById(R.id.mainRegister);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(v.getContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });
        final Button loginButton = (Button)this.findViewById(R.id.mainLoginBtn);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _v=v;
                mainProgress.setVisibility(View.VISIBLE);
                loginButton.setVisibility(View.GONE);
                registerButton.setVisibility(View.GONE);



                String emailtxt = email.getText().toString();
                String passwordtxt = password.getText().toString();
                Model.instace.Login(emailtxt, passwordtxt, new ModelFirebase.CallbackLoginInteface() {
                    @Override
                    public void OnComplete() {
                        Log.d("TAG","On Complete CallBack");
                        mainProgress.setVisibility(View.GONE);
                        loginButton.setVisibility(View.VISIBLE);
                        registerButton.setVisibility(View.GONE);
                        Intent intent = new Intent(_v.getContext(), MainStory.class);
                        startActivity(intent);
                        finish();

                    }

                    @Override
                    public void OnError() {
                        Log.d("TAG","On Error CallBack");

                        mainProgress.setVisibility(View.GONE);
                        loginButton.setVisibility(View.VISIBLE);
                        registerButton.setVisibility(View.GONE);
                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(_v.getContext());
                        dlgAlert.setMessage("Username or Password are wrong");
                        dlgAlert.setTitle("System Alert");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();


                    }
                });

            }
        });
    }




}
