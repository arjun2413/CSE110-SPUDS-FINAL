package com.spuds.eventapp.EditProfile;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spuds.eventapp.R;

import java.util.ArrayList;
import java.util.List;


public class  EditProfileFragment extends Fragment implements AdapterView.OnItemSelectedListener{

    //interactable objects
    Button updateButton;
    ImageButton editProfilePictureButton;
    EditText editFullName;
    //EditText editCollege;
    //EditText editMajor;
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
        editProfilePictureButton = (ImageButton) view.findViewById(R.id.edit_profile_picture);
        editFullName = (EditText) view.findViewById(R.id.edit_full_name);
        //editCollege = (EditText) view.findViewById(R.id.edit_college);
        //editMajor = (EditText) view.findViewById(R.id.edit_major);
        editDescription = (EditText) view.findViewById(R.id.edit_description);

        //Set Custom Fonts
        Typeface custom_font = Typeface.createFromAsset(getActivity().getAssets(),  "raleway-light.ttf");
        editFullName.setTypeface(custom_font);
        editDescription.setTypeface(custom_font);



        /*TODO: here, we need to pull from the database the current information, and set the EditText hints to it.*/
        //TODO: FIREBASE SHIT

        editFullName.setHint("shit");
        //editCollege.setHint("shit");
        //editMajor.setHint("shit");
        editDescription.setHint("shit");



        //When update button is clicked, get the Text the user input, and send to Firebase.
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Firebase pull
                editFullName.getText().toString();      //Full Name to update to db
                //editCollege.getText().toString();       //College text to update to db
                //editMajor.getText().toString();         //Major text to update to db
                editDescription.getText().toString();   //description to update to db

                //exit this fragment when done
                getActivity().getSupportFragmentManager().beginTransaction().remove(editProfileFragment).commit();

            }
        });
// Spinner element
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner);

        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();
        categories.add("AM");
        categories.add("PM");


        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this.getActivity(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);

        //TODO: exit Edit Page and return to profile page.
        return view;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        ((TextView) parent.getChildAt(0)).setTextColor(Color.BLACK);
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();

    }
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
