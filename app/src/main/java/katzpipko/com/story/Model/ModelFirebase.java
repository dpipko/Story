package katzpipko.com.story.Model;

import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;

import static android.content.ContentValues.TAG;

/**
 * Created by User on 2017-07-23.
 */

public class ModelFirebase {

    private FirebaseAuth mAuth;
    public FirebaseUser user;
// ...

    public ModelFirebase()
    {
        mAuth = FirebaseAuth.getInstance();


    }



    public void Login(String email, String password, final CallbackLoginInteface callbackLoginInteface)
    {

         mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
             @Override
             public void onComplete(@NonNull Task<AuthResult> task) {

                Model.UID="";

                 if (task.isSuccessful()) {

                     Model.UID = task.getResult().getUser().getUid();

                     GetUserProfileByUid(task.getResult().getUser().getUid(), new CallBackGeneric() {
                         @Override
                         public void OnComplete(Object res) {
                             callbackLoginInteface.OnComplete();
                         }

                         @Override
                         public void OnError(Object res) {

                             Log.d("TAG","No Profile");
                             Model.instace.Logout();
                             callbackLoginInteface.OnError();

                         }
                     });


                 }
                 else
                 {
                     Log.d("TAG","Error");
                     Model.instace.Logout();
                     callbackLoginInteface.OnError();

                 }
             }
         });

    }

    public void Register(User user,String password, final CallbackRegisterInteface callbackRegisterInteface)
    {

        mAuth.createUserWithEmailAndPassword(user.getEmail(), password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    Log.d("TAG","Register Success");
                    Model.UID = task.getResult().getUser().getUid();
                    callbackRegisterInteface.OnComplete(task.getResult().getUser());
                }
                else {
                //wait

                   String exception =  task.getException().getLocalizedMessage();
                    callbackRegisterInteface.OnError(exception);

                }
            }

        });


     }


    public void saveImage(final Bitmap imageBmp, final String name, final String pathLocation,final Model.SaveImageListener listener) {


        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imagesRef = storage.getReference().child(pathLocation).child(name);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        imageBmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();
        UploadTask uploadTask = imagesRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception exception) {
                listener.fail();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                @SuppressWarnings("VisibleForTests") Uri downloadUrl = taskSnapshot.getDownloadUrl();
                listener.complete(downloadUrl.toString());
            }
        });

    }


    public void GetUserProfileByUid(String uid,final ModelFirebase.CallBackGeneric callBackGeneric)

    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");

        myRef.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user =  dataSnapshot.getValue(User.class);
                Model.instace.setUserData(user);
                callBackGeneric.OnComplete(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                callBackGeneric.OnError(false);
            }
        });
    }




    public void UpdateUserProfile(User user,final ModelFirebase.CallBackGeneric callBackGeneric)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("user");


        myRef.child(user.getUid()).setValue(user);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callBackGeneric.OnComplete(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBackGeneric.OnError(false);
            }
        });

    }


    public void AddStory(Story story, final ModelFirebase.CallBackGeneric callBackGeneric)
    {
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("story");


        myRef.child(story.uid).child(story.storyID).setValue(story);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                callBackGeneric.OnComplete(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callBackGeneric.OnError(false);
            }
        });
    }


    public interface CallbackLoginInteface
    {
        public void OnComplete();
        public void  OnError();
    }


    public interface CallbackRegisterInteface
    {
        public void OnComplete(FirebaseUser currentUser);
        public void  OnError(String exception);
    }

    public interface CallBackGeneric
    {
        public void OnComplete(Object res);
        public void OnError(Object res);

    }
}
