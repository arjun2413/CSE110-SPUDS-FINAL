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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
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
    private Button buttonInvite;

    private ArrayList editEventFields;


    private List<CategoryTextButton> categories;
    public CreateEventRVAdapter adapter;

    private CreateEventForm makeForm(){
        String result = "";
        if (((MainActivity) getActivity()).picture != null)
        result = UserFirebase.convert(getActivity(), ((MainActivity) getActivity()).picture);
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
        fieldMessage = (TextView) view.findViewById(R.id.missingMessage);
        fieldMessage.setVisibility(View.INVISIBLE);
        dateMessage = (TextView) view.findViewById(R.id.dateErrorMessage);
        timeMessage = (TextView) view.findViewById(R.id.timeErrorMessage);
        scrollView = (ScrollView) view.findViewById(R.id.scrollView);
        editEventFields = new ArrayList<String>();
        buttonInvite = (Button) view.findViewById(R.id.event_invite);
    }


    protected void setupWindow() {
        buttonInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).addSearchToolbar();
                InvitePeopleFragment invitePeopleFragment = new InvitePeopleFragment();
                // Add Event Details Fragment to fragment manager
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_frame_layout, invitePeopleFragment)
                        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                        .addToBackStack("fragment_invite_people")
                        .commit();
            }
        });

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

                if (!form.allFilled() || eventTime.getText().toString().equals("") || eventDate.getText().toString().equals("")) {
                    fieldMessage.setVisibility(View.VISIBLE);
                    check = FILLED;

                    System.out.println("Fill out all the forms");
                }
                if (check != FILLED) {
                    dateCheck = form.correctDate();
                    timeCheck = form.correctTime();
                }


                if (check != 0 || dateCheck != 0 || timeCheck!= 0) {
                    System.out.println("Date format is wrong");
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
                    switch (timeCheck){
                        case 0:
                            time = "";
                            break;
                        case 4:
                            //TODO: Reggie, specify event time format in strings.xml file
                            time = getString(R.string.errorTimeFormat);
                            break;
                        case 5:
                            time = getString(R.string.errorIntegerInput);
                            break;
                        case 6:
                            time = getString(R.string.errorInvalidTime);
                            break;
                    }
                    dateMessage.setText(date);
                    timeMessage.setText(time);
                    scrollView.fullScroll(ScrollView.FOCUS_UP);
                }
                else {
                    final String eventId = eventsFirebase.createEvent(form, adapter);
                    //("createevent:", "eventid: " + eventId);

                    EventsFirebase ef = new EventsFirebase();
                    ef.getEventDetails(eventId);


                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            while (!EventsFirebase.detailsThreadCheck) {
                                try {
                                    //("sleepingthread","fam");

                                    Thread.sleep(Integer.parseInt(getString(R.string.sleepTime)));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

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

                // For camera
                //UploadPictureDialogFragment dialogFragment = new UploadPictureDialogFragment();

                //dialogFragment.show(getFragmentManager(), "Add a Picture");


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
                                /*eventImage.setImageURI(null);
                                eventImage.setImageURI(((MainActivity) getActivity()).picture);
                                eventImage.invalidate();*/
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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((MainActivity) getActivity()).picture = null;


    }

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

        final SmoothCheckBox scb = (SmoothCheckBox) view.findViewById(R.id.category_scb);

        /*
        // NULL POINTER ERROR
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                //("SmoothCheckBox", String.valueOf(isChecked));
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
        TextView child = ((TextView) parent.getChildAt(0));
        if (child != null) {
            child.setTextColor(Color.BLACK);
        }
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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).removeSearchToolbar();
    }

}
