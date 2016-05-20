package com.spuds.eventapp.CreateEvent;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.CategoryTextButton;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.library.SmoothCheckBox;


public class CreateEventFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ImageView eventImage;
    private EditText eventName;
    private EditText eventDate;
    private EditText eventLocation;
    private EditText eventDescription;
    private Button editEventDelete;
    private Button editEventDone;

    private ArrayList editEventFields;


    private List<CategoryTextButton> categories;
    public CreateEventRVAdapter adapter;

    private CreateEventForm makeForm(){
        return new CreateEventForm(eventName,eventDate,eventLocation,eventDescription);

    }

    protected void getEventDetails(View view) {
        eventImage = (ImageView) view.findViewById(R.id.eventImage);
        eventName = (EditText) view.findViewById(R.id.eventName);
        eventDate = (EditText) view.findViewById(R.id.eventDate);
        eventLocation = (EditText) view.findViewById(R.id.eventLocation);
        eventDescription = (EditText) view.findViewById(R.id.eventDescription);
        editEventDelete = (Button) view.findViewById(R.id.editEventDelete);
        editEventDone = (Button) view.findViewById(R.id.editEventDone);

        editEventFields = new ArrayList<String>();

    }


    protected void setupWindow() {
        final EventsFirebase eventsFirebase = new EventsFirebase(null, 0, null);
        editEventDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEventForm form = makeForm();

                boolean addImage = false;

                /* TODO get the image of the event
                Matrix uploadedEventImage = eventImage.getImageMatrix()

                if (uploadedEventImage != null) {
                    addImage = true;
                }
                */


                if (form.allFilled()) {
                    // TODO send to database the event details (in a method)
                    if (addImage) {
                        // TODO push to editEventFields array list
                    }
                    eventsFirebase.createEvent(form);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else {
                    // TODO return error
                }
            }
        });
    }

    public CreateEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        RecyclerView rv=(RecyclerView) view.findViewById(R.id.rv_categories);


        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);

        getEventDetails(view);

        setupWindow();


        final SmoothCheckBox scb = (SmoothCheckBox) view.findViewById(R.id.category_scb);

        /*
        // NULL POINTER ERROR
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));
            }
        });
        */


        categories = new ArrayList<>();

        categories.add(new CategoryTextButton("FOOD", scb));
        categories.add(new CategoryTextButton("SOCIAL", scb));
        categories.add(new CategoryTextButton("CONCERTS", scb));
        categories.add(new CategoryTextButton("SPORTS", scb));
        categories.add(new CategoryTextButton("CAMPUS ORGANIZATIONS", scb));
        categories.add(new CategoryTextButton("ACADEMIC", scb));
        categories.add(new CategoryTextButton("FREE", scb));


        /*
        // Is this necessary?
        // TODO: create multiple scbs
        categories.add(new CategoryTextButton("FOOD", food_scb));
        categories.add(new CategoryTextButton("SOCIAL", social_scb));
        categories.add(new CategoryTextButton("CONCERTS", concerts_scb));
        categories.add(new CategoryTextButton("SPORTS", sports_scb));
        categories.add(new CategoryTextButton("CAMPUS ORGANIZATIONS", campus_organizations_scb));
        categories.add(new CategoryTextButton("ACADEMIC", academic_scb));
        categories.add(new CategoryTextButton("FREE", free_scb));
        */

        adapter = new CreateEventRVAdapter(categories, this);
        rv.setAdapter(adapter);


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
