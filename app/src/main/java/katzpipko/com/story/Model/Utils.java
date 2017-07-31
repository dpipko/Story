package katzpipko.com.story.Model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;


/**
 * Created by User on 2017-07-26.
 */

public class Utils {

    public interface AlertInteface
    {
        public void OnAlertFinish();
    }

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

    public void AlertWithCallBack(Context context, String text, String title, final AlertInteface alertInteface)
    {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true);
        builder.setMessage(text);

        builder.setTitle(title);
        builder.setInverseBackgroundForced(true);
        builder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.dismiss();
                        alertInteface.OnAlertFinish();
                    }
                });

        AlertDialog alert = builder.create();
        alert.show();
    }








}
