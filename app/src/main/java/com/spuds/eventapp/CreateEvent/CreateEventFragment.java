package com.spuds.eventapp.CreateEvent;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.spuds.eventapp.EventDetails.EventDetailsFragment;
import com.spuds.eventapp.Firebase.EventsFirebase;
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.InvitePeople.InvitePeopleFragment;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.CategoryTextButton;
import com.spuds.eventapp.Shared.MainActivity;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.library.SmoothCheckBox;
/*---------------------------------------------------------------------------
Class Name:                CreateEventFragment
Description:               Contains information about CreateEventFragment
---------------------------------------------------------------------------*/
public class CreateEventFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    public static final int FILLED = 213;
    private ImageView eventImage;
    private EditText eventName;
    private EditText eventDate;
    private EditText eventTime;
    private Spinner spinner;
    private EditText eventLocation;
    private EditText eventDescription;
    private Button editEventDelete;
    private Button editEventDone;
    private TextView fieldMessage;
    private TextView dateMessage;
    private TextView timeMessage;
    private ScrollView scrollView;
    private ImageButton uploadButton;
    private TextView uploadText;

    private ArrayList editEventFields;


    private List<CategoryTextButton> categories;
    public CreateEventCategoryRVAdapter adapter;

    /*---------------------------------------------------------------------------
    Function Name:                makeForm()
    Description:                  instantiates a CreateEventForm for new event
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    private CreateEventForm makeForm(){
        String result = "";
        if (((MainActivity) getActivity()).picture != null)
        result = UserFirebase.convert(getActivity(), ((MainActivity) getActivity()).picture);
        return new CreateEventForm(eventName,eventDate,eventTime, spinner, eventLocation,eventDescription, result);

    }

    /*---------------------------------------------------------------------------
    Function Name:                getEventDetails()
    Description:                  Instantiating instance variables from xml file
    Input:                        View view
    Output:                       None.
    ---------------------------------------------------------------------------*/
    protected void getEventDetails(View view) {
        eventImage = (ImageView) view.findViewById(R.id.eventImage);
        eventName = (EditText) view.findViewById(R.id.eventName);
        eventDate = (EditText) view.findViewById(R.id.eventDate);
        eventTime = (EditText) view.findViewById(R.id.eventTime);
        eventLocation = (EditText) view.findViewById(R.id.eventLocation);
        eventDescription = (EditText) view.findViewById(R.id.eventDescription);
        editEventDelete = (Button) view.findViewById(R.id.editEventDelete);
        editEventDone = (Button) view.findViewById(R.id.editEventDone);
        fieldMessage = (TextView) view.findViewById(R.id.missingMessage);
        fieldMessage.setVisibility(View.INVISIBLE);
        dateMessage = (TextView) view.findViewById(R.id.dateErrorMessage);
        timeMessage = (TextView) view.findViewById(R.id.timeErrorMessage);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        editEventFields = new ArrayList<String>();
        uploadButton = (ImageButton) view.findViewById(R.id.image);
        uploadText = (TextView) view.findViewById(R.id.upload);
    }

    /*---------------------------------------------------------------------------
    Function Name:                setupWindow()
    Description:                  Sets up window for items to be clicked/filled out and error messages
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    protected void setupWindow() {


        //ref to eventsfirebase class
        //initialize EventsFirebase object
        final EventsFirebase eventsFirebase = new EventsFirebase(null, 0, null, null, null);
        editEventDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEventForm form = makeForm();
                fieldMessage.setVisibility(View.INVISIBLE);
                boolean addImage = false;
                int check = 0;
                int timeCheck = 0;
                int dateCheck = 0;

                String date = "";
                String time = "";

                //check if form is all filled out properly
                if (!form.allFilled() || eventTime.getText().toString().equals("") || eventDate.getText().toString().equals("")) {
                    fieldMessage.setVisibility(View.VISIBLE);
                    check = FILLED;
                }
                if (check != FILLED) {
                    dateCheck = form.correctDate();
                    timeCheck = form.correctTime();
                }

                //error messages for date
                if (check != 0 || dateCheck != 0 || timeCheck!= 0) {
                    switch (dateCheck) {
                        case 0:
                            date = "";
                                break;
                        case 1:
                            date = getString(R.string.errorDateFormat);
                            break;
                        case 2:
                            date = getString(R.string.errorIntegerInput);
                            break;
                        case 3:
                            date = getString(R.string.errorInvalidDate);
                            break;
                    }
                    //error messages for time
                    switch (timeCheck){
                        case 0:
                            time = "";
                            break;
                        case 4:
                            time = getString(R.string.errorTimeFormat);
                            break;
                        case 5:
                            time = getString(R.string.errorIntegerInput);
                            break;
                        case 6:
                            time = getString(R.string.errorInvalidTime);
                            break;
                    }
                    //set date and time
                    dateMessage.setText(date);
                    timeMessage.setText(time);
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
                //creates event and inputs in database using eventsFirebase method
                else {
                    final String eventId = eventsFirebase.createEvent(form, adapter);

                    EventsFirebase ef = new EventsFirebase();
                    ef.getEventDetails(eventId);

                    //new thread to put app to sleep and give time for event details to be updated
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            while (!EventsFirebase.detailsThreadCheck) {
                                try {
                                    Thread.sleep(Integer.parseInt(getString(R.string.sleepTime)));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            //set Event ID for specific event created
                            EventsFirebase.eventDetailsEvent.setEventId(eventId);

                            EventDetailsFragment eventDetailsFragment = new EventDetailsFragment();
                            Bundle bundle = new Bundle();
                            bundle.putSerializable(getString(R.string.event_details), EventsFirebase.eventDetailsEvent);
                            eventDetailsFragment.setArguments(bundle);

                            getActivity().getSupportFragmentManager().beginTransaction()
                                    .replace(R.id.fragment_frame_layout, eventDetailsFragment)
                                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                                    .addToBackStack(getString(R.string.event_details_fragment))
                                    .commit();

                        }
                    }).start();

                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });


        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).picture = null;

                ((MainActivity) getActivity()).pickImage(false);

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        while (((MainActivity) getActivity()).picture == null) {
                            try {
                                Thread.sleep(Integer.parseInt(getString(R.string.sleepTime)));
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                        }

                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                String imageFile = UserFirebase.convert(getActivity(), ((MainActivity) getActivity()).picture);

                                Bitmap src = null;
                                try {
                                    byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                                    src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
                                } catch(OutOfMemoryError e) {
                                    System.err.println(e.toString());
                                }

                                if (src != null) {
                                    eventImage.setImageBitmap(src);
                                    uploadButton.setVisibility(View.GONE);
                                    uploadText.setVisibility(View.GONE);
                                }

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

    /*---------------------------------------------------------------------------
    Function Name:                onCreate()
    Description:                  Called each time fragment is created
    Input:                        Bundle savedInstanceState
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        ((MainActivity) getActivity()).picture = null;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateView()
    Description:                  Inflates View layout and sets fonts programmatically
    Input:                        LayoutInflater inflater - inflates layout
                                  ViewGroup container - parent view group
                                  Bundle savedInstanceState
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_create_event, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Create Event");

        RecyclerView rv=(RecyclerView) view.findViewById(R.id.rv_categories);


        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);

        getEventDetails(view);

        setupWindow();
        //create categories list
        categories = new ArrayList<>();

        categories.add(new CategoryTextButton("FOOD", false));
        categories.add(new CategoryTextButton("SOCIAL", false));
        categories.add(new CategoryTextButton("CONCERTS", false));
        categories.add(new CategoryTextButton("SPORTS", false));
        categories.add(new CategoryTextButton("STUDENT ORGS", false));
        categories.add(new CategoryTextButton("ACADEMIC", false));
        categories.add(new CategoryTextButton("FREE", false));

        adapter = new CreateEventCategoryRVAdapter(categories, this);
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



        Button done = (Button) view.findViewById(R.id.editEventDone);
        done.setTypeface(raleway_medium);

        return view;
    }

    /*---------------------------------------------------------------------------
    Function Name:                onCreateOptionsMenu()
    Description:                  creates the stuff on the toolbar
    Input:                        Menu menu
                                  MenuInflater inflater - inflates layout
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.removeItem(R.id.action_create_event);
        inflater.inflate(R.menu.create_event_blank, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();
        TextView child = ((TextView) parent.getChildAt(0));
        if (child != null) {
            child.setTextColor(Color.BLACK);
        }
        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    public void onNothingSelected(AdapterView<?> arg0) {

    }

    /*---------------------------------------------------------------------------
    Function Name:                overrideFonts()
    Description:                  Sets fonts for all TextViews
    Input:                        final Context context
                                  final View v
    Output:                       View to be inflated
    ---------------------------------------------------------------------------*/
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


    /*---------------------------------------------------------------------------
    Function Name:                onResume()
    Description:                  called every time the fragment is resumed
    Input:                        None.
    Output:                       None.
    ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).removeSearchToolbar();
    }

}
