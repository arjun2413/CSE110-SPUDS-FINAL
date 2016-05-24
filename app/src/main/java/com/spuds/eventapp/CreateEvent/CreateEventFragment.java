package com.spuds.eventapp.CreateEvent;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
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
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.CategoryTextButton;
import com.spuds.eventapp.Shared.MainActivity;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.library.SmoothCheckBox;


public class CreateEventFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private ImageView eventImage;
    private EditText eventName;
    private EditText eventDate;
    private EditText eventTime;
    private Spinner spinner;
    private EditText eventLocation;
    private EditText eventDescription;
    private Button editEventDelete;
    private Button editEventDone;

    private ArrayList editEventFields;


    private List<CategoryTextButton> categories;
    public CreateEventRVAdapter adapter;

    private CreateEventForm makeForm(){
        String result = UserFirebase.convert(getActivity(), ((MainActivity) getActivity()).picture);
        return new CreateEventForm(eventName,eventDate,eventTime, spinner, eventLocation,eventDescription, result);

    }

    protected void getEventDetails(View view) {
        eventImage = (ImageView) view.findViewById(R.id.eventImage);
        eventName = (EditText) view.findViewById(R.id.eventName);
        eventDate = (EditText) view.findViewById(R.id.eventDate);
        eventTime = (EditText) view.findViewById(R.id.eventTime);
        eventLocation = (EditText) view.findViewById(R.id.eventLocation);
        eventDescription = (EditText) view.findViewById(R.id.eventDescription);
        editEventDelete = (Button) view.findViewById(R.id.editEventDelete);
        editEventDone = (Button) view.findViewById(R.id.editEventDone);
        editEventFields = new ArrayList<String>();

    }


    protected void setupWindow() {
        final EventsFirebase eventsFirebase = new EventsFirebase(null, 0, null, null, null);
        editEventDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEventForm form = makeForm();

                boolean addImage = false;




                if (form.allFilled()) {
                    eventsFirebase.createEvent(form, adapter);
                    getActivity().getSupportFragmentManager().popBackStack();
                }
                else {
                    // TODO return error
                }
            }
        });


        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).picture = null;

                // For camera
                //UploadPictureDialogFragment dialogFragment = new UploadPictureDialogFragment();

                //dialogFragment.show(getFragmentManager(), "Add a Picture");


                ((MainActivity) getActivity()).pickImageWithoutCrop();

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (((MainActivity) getActivity()).picture == null) {
                            try {
                                Thread.sleep(75);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                eventImage.setImageURI(null);
                                eventImage.setImageURI(((MainActivity) getActivity()).picture);
                                eventImage.invalidate();


                            }
                        });

                    }
                }).start();
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

        categories.add(new CategoryTextButton("FOOD", false));
        categories.add(new CategoryTextButton("SOCIAL", false));
        categories.add(new CategoryTextButton("CONCERTS", false));
        categories.add(new CategoryTextButton("SPORTS", false));
        categories.add(new CategoryTextButton("STUDENT ORGS", false));
        categories.add(new CategoryTextButton("ACADEMIC", false));
        categories.add(new CategoryTextButton("FREE", false));


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
        spinner = (Spinner) view.findViewById(R.id.spinner);

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

        overrideFonts(view.getContext(),view);

        Typeface raleway_medium = Typeface.createFromAsset(getActivity().getAssets(),  "Raleway-Medium.ttf");

        //title font
        TextView upload = (TextView) view.findViewById(R.id.upload);
        upload.setTypeface(raleway_medium);

        TextView name = (TextView) view.findViewById(R.id.name);
        name.setTypeface(raleway_medium);

        TextView date = (TextView) view.findViewById(R.id.date);
        date.setTypeface(raleway_medium);

        TextView time = (TextView) view.findViewById(R.id.time);
        time.setTypeface(raleway_medium);

        TextView location = (TextView) view.findViewById(R.id.location);
        location.setTypeface(raleway_medium);

        TextView description = (TextView) view.findViewById(R.id.description);
        description.setTypeface(raleway_medium);

        TextView cat = (TextView) view.findViewById(R.id.event_categories);
        cat.setTypeface(raleway_medium);

        Button invite = (Button) view.findViewById(R.id.event_invite);
        invite.setTypeface(raleway_medium);

        Button done = (Button) view.findViewById(R.id.editEventDone);
        done.setTypeface(raleway_medium);

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

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }
        }
        catch (Exception e) {
        }
    }

}
