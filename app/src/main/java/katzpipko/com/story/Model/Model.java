package katzpipko.com.story.Model;

import android.graphics.Bitmap;
import android.support.v4.app.NavUtils;
import android.webkit.URLUtil;

/**
 * Created by User on 2017-07-23.
 */

public class Model {
    public final static Model instace = new Model();
    private ModelFirebase modelFirebase;
    public   Utils utils;
    public static  String UID = "6wiLf3wWksMYRx3hVmWorGKJbBt1";
    private  Integer StaticCounter = 1;

    public Model()
    {
        modelFirebase = new ModelFirebase();
        utils = new Utils();
    }

    public void Login(String email, String password, final ModelFirebase.CallbackLoginInteface callbackLoginInteface)
    {
         modelFirebase.Login(email,password,callbackLoginInteface);
    }



    public void  AddStory(Story story, final ModelFirebase.CallBackGeneric callBackGeneric)
    {
        modelFirebase.AddStory(story,callBackGeneric);
    }

    public void Logout()
    {
        modelFirebase.user=null;
        UID= null;
    }


    public String GetUniqueID()
    {
     return this.UID + "_" +   System.currentTimeMillis()/1000 + "_" + this.StaticCounter++;
    }

    public interface SaveImageListener {
        void complete(String url);
        void fail();
    }

    public void saveImage(final Bitmap imageBmp, final String name, final SaveImageListener listener) {
        modelFirebase.SaveImage(imageBmp, name, new SaveImageListener() {
            @Override
            public void complete(String url) {
                //String fileName = URLUtil.guessFileName(url, null, null);
               // saveImageToFile(imageBmp,fileName);
                listener.complete(url);
            }

            @Override
            public void fail() {
                listener.fail();
            }
        });


    }


    public interface GetImageListener{
        void onSuccess(Bitmap image);
        void onFail();
    }




}
