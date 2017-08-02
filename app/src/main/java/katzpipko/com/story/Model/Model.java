package katzpipko.com.story.Model;
import android.graphics.Bitmap;
import android.util.Log;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import katzpipko.com.story.MyApplication;

/**
 * Created by User on 2017-07-23.
 */

    public class Model {
    public final static Model instace = new Model();
    private ModelFirebase modelFirebase;
    public ModelSql modelSql;
    public ModelMem modelMem;

    public Utils utils;
    public static String UID = "";
    private  Integer StaticCounter = 1;

    public User getUserData() {
        return userData;
    }
    private User userData;

    public void setUserData(User userData) {
        this.userData = userData;
        Model.UID = userData.getUid();
    }

    public Model()
    {
        modelSql = new ModelSql(MyApplication.getMyContext());
        modelMem =  new ModelMem();
        modelFirebase = new ModelFirebase();
        utils = new Utils();
    }

    public void Login(String email, String password, final ModelFirebase.CallbackLoginInteface callbackLoginInteface)
    {
         modelFirebase.Login(email, password, new ModelFirebase.CallbackLoginInteface() {
             @Override
             public void OnComplete() {
                 UserSql.UpserUser(modelSql.getWritableDatabase(),getUserData());
                 callbackLoginInteface.OnComplete();
             }

             @Override
             public void OnError() {
                 callbackLoginInteface.OnError();

             }
         });
    }

    public void Register(final User user, String password, final Bitmap imageBitmap, final ModelFirebase.CallbackRegisterInteface callbackRegisterInteface)
    {
        modelFirebase.Register(user, password, new ModelFirebase.CallbackRegisterInteface() {
            @Override
            public void OnComplete(final FirebaseUser currentUser) {
                Log.d("TAG","Success = " + currentUser.getUid());
                String name = currentUser.getUid();
                Model.instace.saveImage(imageBitmap, name, "profileImages", new Model.SaveImageListener() {
                    @Override
                    public void complete(String url) {

                        user.setProfileImage(url);
                        user.setUid(currentUser.getUid());

                        Model.instace.UpdateUserProfile(user, new ModelFirebase.CallBackGeneric() {
                            @Override
                            public void OnComplete(Object res) {
                                Model.instace.setUserData(user);
                                Log.d("TAG","Register + Upload Image + Update profile Sucess");
                                //Update Local SQL
                                UserSql.UpserUser(Model.instace.modelSql.getWritableDatabase(),user);
                                callbackRegisterInteface.OnComplete(currentUser);//Success
                            }

                            @Override
                            public void OnError(Object res) {
                               Log.d("TAG","Error Update User Profile");
                                currentUser.delete();
                                callbackRegisterInteface.OnError("Error Update User Profile");
                            }
                        });

                    }

                    @Override
                    public void fail() {
                        Log.d("TAG","Error Uploading Image");
                        currentUser.delete();
                        callbackRegisterInteface.OnError("Error Uploading Image");
                    }
                });
            }
            @Override
            public void OnError(String exception) {
                callbackRegisterInteface.OnError(exception);
            }
        });
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


    public void UpdateUserProfile(User user,final ModelFirebase.CallBackGeneric callBackGeneric)
    {
        modelFirebase.UpdateUserProfile(user,callBackGeneric);
    }


    public void GetAllStoreisAndObserve(final ModelFirebase.CallBackGeneric callBackGeneric)
    {
        modelFirebase.GetAllStoreisAndObserve(new ModelFirebase.GetAllStoriesAndObserveCallback() {
            @Override
            public void onComplete(List<Story> list) {

                modelMem.SetNewStoryList(list,10);
                callBackGeneric.OnComplete(true);
            }

            @Override
            public void onCancel() {
                callBackGeneric.OnError(false);

            }
        });


    }

    public void saveImage(final Bitmap imageBmp, final String name, final SaveImageListener listener) {

        saveImage(imageBmp,name,"images",listener);

    }
    public void saveImage(final Bitmap imageBmp, final String name, final String pathLocation,final SaveImageListener listener) {
        modelFirebase.saveImage(imageBmp, name,pathLocation, new SaveImageListener() {
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


    //-------------------Interface---------
    public interface SaveImageListener {
        void complete(String url);
        void fail();
    }


}
