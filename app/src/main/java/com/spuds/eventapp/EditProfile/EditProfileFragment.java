package com.spuds.eventapp.EditProfile;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.spuds.eventapp.R;


public class  EditProfileFragment extends Fragment {

    //interactable objects
    Button updateButton;
    ImageButton editProfilePicture;
    EditText editFullName;
    EditText editCollege;
    EditText editMajor;
    EditText editDescription;
    Fragment editProfileFragment;

    public EditProfileFragment() {
        //this is to leave this fragment when done
        editProfileFragment = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {super.onCreate(savedInstanceState);}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        updateButton = (Button) view.findViewById(R.id.update_button);
        editProfilePicture = (ImageButton) view.findViewById(R.id.edit_profile_picture);
        editFullName = (EditText) view.findViewById(R.id.edit_full_name);
        editCollege = (EditText) view.findViewById(R.id.edit_college);
        editMajor = (EditText) view.findViewById(R.id.edit_major);
        editDescription = (EditText) view.findViewById(R.id.edit_description);

        /*TODO: here, we need to pull from the database the current information, and set the EditText hints to it.*/
        //TODO: FIREBASE SHIT

        editFullName.setHint("shit");
        editCollege.setHint("shit");
        editMajor.setHint("shit");
        editDescription.setHint("shit");

        //When update button is clicked, get the Text the user input, and send to Firebase.
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Firebase pull
                editFullName.getText().toString();      //Full Name to update to db
                editCollege.getText().toString();       //College text to update to db
                editMajor.getText().toString();         //Major text to update to db
                editDescription.getText().toString();   //description to update to db

                //exit this fragment when done
                getActivity().getSupportFragmentManager().beginTransaction().remove(editProfileFragment).commit();

            }
        });


        //TODO: exit Edit Page and return to profile page.
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
