package com.spuds.eventapp.EditProfile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.RoundedBitmapDrawable;
import android.support.v4.graphics.drawable.RoundedBitmapDrawableFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.User;


public class  EditProfileFragment extends Fragment {

    //interactable objects
    Button updateButton;
    ImageButton editProfilePictureButton;
    EditText editFullName;
    //EditText editCollege;
    //EditText editMajor;
    EditText editDescription;
    Fragment editProfileFragment;

    User user;

    public EditProfileFragment() {
        //this is to leave this fragment when done
        editProfileFragment = this;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getArguments();
        user = (User) extras.get(getString(R.string.user_details));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);

        updateButton = (Button) view.findViewById(R.id.update_button);
        editProfilePictureButton = (ImageButton) view.findViewById(R.id.edit_profile_picture);
        editFullName = (EditText) view.findViewById(R.id.edit_full_name);
        editDescription = (EditText) view.findViewById(R.id.edit_description);


        Bitmap src = BitmapFactory.decodeResource(this.getResources(), R.id.edit_profile_picture);
        RoundedBitmapDrawable dr =
                RoundedBitmapDrawableFactory.create(this.getResources(), src);
        dr.setCornerRadius(Math.max(src.getWidth(), src.getHeight()) / 2.0f);
        editProfilePictureButton.setImageDrawable(dr);

        //Set Custom Fonts
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "raleway-light.ttf");
        editFullName.setTypeface(custom_font);
        editDescription.setTypeface(custom_font);



        editFullName.setText(user.name);
        editDescription.setText(user.description);



        //When update button is clicked, get the Text the user input, and send to Firebase.
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO (M): Push this data
                editFullName.getText().toString();      //Full Name to update to db
                //editCollege.getText().toString();       //College text to update to db
                //editMajor.getText().toString();         //Major text to update to db
                editDescription.getText().toString();   //description to update to db

                // TODO (C): Refresh after updating profile
                // Pop this fragment from backstack
                getActivity().getSupportFragmentManager().popBackStack();

            }
        });

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
