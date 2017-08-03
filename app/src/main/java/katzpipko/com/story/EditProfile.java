package katzpipko.com.story;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import katzpipko.com.story.Model.Model;
import katzpipko.com.story.Model.ModelFirebase;
import katzpipko.com.story.Model.User;
import katzpipko.com.story.Model.Utils;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditProfile.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditProfile#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditProfile extends Fragment {

    private ProgressBar editProgress;
    private View currentView;
    private Button editBtn;
    private ImageView imageView;
    private TextView email;
    private EditText firstName;
    private EditText lastName;
    private Bitmap imageBitmap;
    private  ReloadInterface reloadInterface;
    private OnFragmentInteractionListener mListener;

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    public EditProfile() {
        // Required empty public constructor
    }



    public interface ReloadInterface
    {
        public void OnReload();
    }


    // TODO: Rename and change types and number of parameters
    public static EditProfile newInstance(ReloadInterface reloadInterface) {
        EditProfile fragment = new EditProfile();
        Bundle args = new Bundle();
        fragment.reloadInterface = reloadInterface;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        email = (TextView) v.findViewById(R.id.viewEmail);
        email.setText(Model.instace.getUserData().getEmail());

        firstName = (EditText) v.findViewById(R.id.editFirstName);
        firstName.setText(Model.instace.getUserData().getFirstName());

        lastName = (EditText) v.findViewById(R.id.editLastName);
        lastName.setText(Model.instace.getUserData().getLastName());

        imageView = (ImageView) v.findViewById(R.id.editProfilePic);
        editBtn = (Button) v.findViewById(R.id.editBtn);
        editProgress = (ProgressBar) v.findViewById(R.id.editProgress);
        Picasso.with(v.getContext()).load(Model.instace.getUserData().getProfileImage()).into(imageView);




        //Upload Image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Clicked Upload Image");
                dispatchTakePictureIntent();
            }
        });

        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentView =v;

                editProgress.setVisibility(View.VISIBLE);
                editBtn.setVisibility(View.GONE);

                final User user = new User( Model.instace.getUserData());
                user.setFirstName(firstName.getText().toString());
                user.setLastName(lastName.getText().toString());
                if (imageBitmap!=null) //Image Updated
                {
                    String name = Model.instace.getUserData().getUid();
                    Model.instace.saveImage(imageBitmap, name, "profileImages", new Model.SaveImageListener() {
                        @Override
                        public void complete(String url) {
                            user.setProfileImage(url);
                            UpdateProfile(user);
                        }

                        @Override
                        public void fail() {
                            Model.instace.utils.Alert( currentView.getContext(), "Image Upload Faild","Server Error");
                        }
                    });
                    return; //Stop function from running
                }
                else if (user.equals(Model.instace.getUserData()))
                {

                        Model.instace.utils.Alert( currentView.getContext(), "Nothing Changed","System Alert");

                        editProgress.setVisibility(View.GONE);
                        editBtn.setVisibility(View.VISIBLE);
                        return;
                }

                UpdateProfile(user);
            }
        });

        return  v;
    }



    private void UpdateProfile(final User user)
    {

        Model.instace.UpdateUserProfile(user, new ModelFirebase.CallBackGeneric() {
            @Override
            public void OnComplete(Object res) {
                editBtn.setVisibility(View.VISIBLE);
                editProgress.setVisibility(View.GONE);


                Model.instace.setUserData(user); //cache updated
                Model.instace.utils.AlertWithCallBack(currentView.getContext(), "Update Successful", "System Alert", new Utils.AlertInteface() {
                    @Override
                    public void OnAlertFinish() {
                        reloadInterface.OnReload();

                    }
                });
            }
            @Override
            public void OnError(Object res) {
                Model.instace.utils.Alert( currentView.getContext(), "Oops something go wrong","Server Error");

                editBtn.setVisibility(View.VISIBLE);
                editProgress.setVisibility(View.GONE);

            }
        });

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }




    static final int REQUEST_IMAGE_CAPTURE = 1;

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            imageView.setImageBitmap(imageBitmap);
        }
    }

}
