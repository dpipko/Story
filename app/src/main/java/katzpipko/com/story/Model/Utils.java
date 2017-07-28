package katzpipko.com.story.Model;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.provider.MediaStore;

/**
 * Created by User on 2017-07-26.
 */

public class Utils {

    public void Alert(Context context, String text)
    {
        Alert(context,text,"System Alert");
    }



    public void Alert(Context context, String text, String title)
    {
        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(context);
        dlgAlert.setMessage(text);
        dlgAlert.setTitle(title);
        dlgAlert.setPositiveButton("OK", null);
        dlgAlert.setCancelable(true);
        dlgAlert.create().show();


    }



}
