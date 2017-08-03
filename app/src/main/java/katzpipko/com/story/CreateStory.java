package katzpipko.com.story;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
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

import katzpipko.com.story.Model.Model;
import katzpipko.com.story.Model.ModelFirebase;
import katzpipko.com.story.Model.Story;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CreateStory.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CreateStory#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateStory extends Fragment {


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }


    private Fragment currentFragment = null;
    private ProgressBar pBar;
    private View currentView = null;
    private Bitmap imageBitmap;
    private EditText createStoryTitle;
    private Button createStorySaveBtn;
    private ImageView imageView;
    private OnFragmentInteractionListener mListener;

    public CreateStory() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static CreateStory newInstance() {
        CreateStory fragment = new CreateStory();
        Bundle args = new Bundle();
        fragment.setArguments(args);

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

        View v =inflater.inflate(R.layout.fragment_create_story, container, false);
        currentFragment = this;
        imageView = (ImageView)v.findViewById(R.id.createStoryImage);
        createStoryTitle = (EditText) v.findViewById(R.id.createStoryTitle);
        createStorySaveBtn = (Button) v.findViewById(R.id.createStorySaveBtn);
        pBar  =(ProgressBar)v.findViewById(R.id.creatStoryProgress);


        //Upload Image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("TAG","Clicked Upload Image");
                dispatchTakePictureIntent();
            }
        });




        createStorySaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentView = v;
                if (imageBitmap==null)
                {
                    Model.instace.utils.Alert(currentView.getContext(),"Please Take an Image");
                    return;
                }


                pBar.setVisibility(View.VISIBLE);
                createStorySaveBtn.setVisibility(View.GONE);

                String name = Model.instace.GetUniqueID();

                Model.instace.saveImage(imageBitmap, name, new Model.SaveImageListener() {
                    @Override
                    public void complete(String url) {
                        Log.d("TAG","Upload Success - " + url);

                        //Add to database
                        Story newStory = new Story();
                        newStory.imageURL = url;
                        newStory.title =createStoryTitle.getText().toString();
                        newStory.timestamp = System.currentTimeMillis()/1000;
                        newStory.uid = Model.UID;
                        newStory.storyID = Model.instace.GetUniqueID();

                        Model.instace.AddStory(newStory, new ModelFirebase.CallBackGeneric() {
                            @Override
                            public void OnComplete(Object res) {

                                pBar.setVisibility(View.GONE);
                                createStorySaveBtn.setVisibility(View.VISIBLE);

                                Model.instace.utils.Alert(currentView.getContext(),"Story Created Succesfuly");


                                FragmentTransaction ft = getFragmentManager().beginTransaction();
                                ft.detach(currentFragment).attach(currentFragment).commit();

                                imageBitmap=null;
                                createStoryTitle.setText("");


                            }

                            @Override
                            public void OnError(Object res) {

                                pBar.setVisibility(View.GONE);
                                createStorySaveBtn.setVisibility(View.VISIBLE);


                                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(currentView.getContext());
                                dlgAlert.setMessage("Ops Something wrong");
                                dlgAlert.setTitle("System Alert");
                                dlgAlert.setPositiveButton("OK", null);
                                dlgAlert.setCancelable(true);
                                dlgAlert.create().show();

                            }
                        });
                    }

                    @Override
                    public void fail() {
                        Log.d("TAG","Faild Upload Success");

                        AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(currentView.getContext());
                        dlgAlert.setMessage("Ops Something Wrong With the Upload");
                        dlgAlert.setTitle("System Alert");
                        dlgAlert.setPositiveButton("OK", null);
                        dlgAlert.setCancelable(true);
                        dlgAlert.create().show();
                    }
                });
            }
        });




        return v;
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */



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
