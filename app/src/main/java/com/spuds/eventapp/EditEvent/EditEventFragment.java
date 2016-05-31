package com.spuds.eventapp.EditEvent;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
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
import com.spuds.eventapp.Shared.Event;
import com.spuds.eventapp.Shared.EventDate;
import com.spuds.eventapp.Shared.MainActivity;

import java.util.ArrayList;
import java.util.List;

import cn.refactor.library.SmoothCheckBox;

public class EditEventFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    private ImageView eventImage;
    private EditText eventName;
    private EditText eventDate;
    private Spinner spinner;
    private EditText eventTime;
    private EditText eventLocation;
    private EditText eventDescription;
    private Button editEventDelete;
    private Button editEventDone;
    private ArrayList editEventFields;
    private SmoothCheckBox scb;
    private RecyclerView rv;
    private EventDate eD;
    private Event event;
    private TextView errorMissingMessage;
    private TextView errorDateMessage;
    private TextView errorTimeMessage;




    private List<CategoryTextButton> categories;
    public EditEventRVAdapter adapter;

    public EditEventFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ((MainActivity) getActivity()).picture = null;

        Bundle extras = getArguments();
        event = (Event) extras.getSerializable(getString(R.string.event_details));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_event, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Edit Event");
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

        Button delete = (Button) view.findViewById(R.id.editEventDelete);
        delete.setTypeface(raleway_medium);


        eD = new EventDate(event.getDate());
        Log.d("edvalue", String.valueOf(eD.hour));
        Log.d("edvalue", String.valueOf(eD.year));
        Log.d("edvalue", String.valueOf(eD.month));
        Log.d("edvalue", String.valueOf(eD.day));

        getPageElements(view);
        setupWindow();

        setupEditTime(view);

        return view;
    }

    void setupEditTime(View view) {
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

        if (eD.getAMPM().equals("PM"))
            spinner.setSelection(1);

    }
    protected void getPageElements(View view) {
        eventImage = (ImageView) view.findViewById(R.id.eventImage);
        eventName = (EditText) view.findViewById(R.id.eventName);
        eventDate = (EditText) view.findViewById(R.id.eventDate);
        eventTime = (EditText) view.findViewById(R.id.eventTime);
        eventLocation = (EditText) view.findViewById(R.id.eventLocation);
        eventDescription = (EditText) view.findViewById(R.id.eventDescription);
        editEventDelete = (Button) view.findViewById(R.id.editEventDelete);
        editEventDone = (Button) view.findViewById(R.id.editEventDone);
        rv =(RecyclerView) view.findViewById(R.id.rv_categories);
        scb = (SmoothCheckBox) view.findViewById(R.id.category_scb);
        editEventFields = new ArrayList<String>();

        errorMissingMessage = (TextView) view.findViewById(R.id.missingMessage);
        errorDateMessage = (TextView) view.findViewById(R.id.dateErrorMessage);
        errorTimeMessage = (TextView) view.findViewById(R.id.timeErrorMessage);

        errorMissingMessage.setVisibility(View.INVISIBLE);
        errorDateMessage.setVisibility(View.INVISIBLE);
        errorTimeMessage.setVisibility(View.INVISIBLE);

        // TODO (M): Picasso for picture
        eventName.setText(event.getEventName());

        String originalString = event.getDate();
        char[] c = originalString.toCharArray();
        char temp = c[0];
        c[0] = c[6];
        c[6] = temp;
        char temp1 = c[1];
        c[1] = c[7];
        c[7] = temp1;
        char temp2 = c[0];
        c[0] = c[3];
        c[3] = temp2;
        char temp3 = c[1];
        c[1] = c[4];
        c[4] = temp3;
        String swappedString = new String(c);
        String tempString = swappedString.substring(11, swappedString.length());
        String sub = tempString.substring(0, tempString.indexOf(':'));
        String col = tempString.substring(tempString.indexOf(':'), tempString.length());
        int numb = Integer.parseInt(sub);
        //PM
        if(Integer.parseInt(sub) >= 12 && Integer.parseInt(sub) < 24) {
            if(numb != 12) {
                numb -= 12;
            }
            sub = numb + col;
        }
        //AM
        else{
            if(numb == 0) {
                numb += 12;
            }
            sub = numb + col;
        }
        swappedString = swappedString.substring(0, 11) + sub;

        eventDate.setText(swappedString.substring(0,8));
        eventTime.setText(swappedString.substring(11, swappedString.length()));

        eventLocation.setText(event.getLocation());
        eventDescription.setText(event.getDescription());

        //an arraylist of category text buttons
        categories = new ArrayList<>();


        categories.add(new CategoryTextButton("FOOD", false));
        categories.add(new CategoryTextButton("SOCIAL", false));
        categories.add(new CategoryTextButton("CONCERTS", false));
        categories.add(new CategoryTextButton("SPORTS", false));
        categories.add(new CategoryTextButton("STUDENT ORGS", false));
        categories.add(new CategoryTextButton("ACADEMIC", false));
        categories.add(new CategoryTextButton("FREE", false));

        //existing categories on this event is good.
        ArrayList<String> existingCateg = event.getCategories();
        //("size", "size: " + event.getCategories().size());

        for (int i = 0; i < existingCateg.size(); ++i) {

            //("category", "category: " + existingCateg.get(i));


            switch(existingCateg.get(i)) {

                case "Food":
                    categories.get(0).setCheckedBoolean(true);
                    break;
                case "Social":
                    categories.get(1).setCheckedBoolean(true);
                    break;
                case "Concerts":
                    categories.get(2).setCheckedBoolean(true);
                    break;
                case "Sports":
                    categories.get(3).setCheckedBoolean(true);
                    break;
                case "Student Orgs":
                    categories.get(4).setCheckedBoolean(true);
                    break;
                case "Academic":
                    categories.get(5).setCheckedBoolean(true);
                    break;
                case "Free":
                    categories.get(6).setCheckedBoolean(true);
                    break;
            }
        }

        LinearLayoutManager llm = new LinearLayoutManager(view.getContext());
        rv.setLayoutManager(llm);

        adapter = new EditEventRVAdapter(categories, this, existingCateg);
        rv.setAdapter(adapter);


        editEventDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(getActivity())
                        .setTitle("Delete Event")
                        .setMessage("Are you sure you want delete this event?")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO (M): Delete event
                                // TODO (C): Big issue, must refresh all feeds or else will get null
                                // TODO      pointer exceptions when clicking the event again when it doesn't
                                // TODO      even exist

                                EventsFirebase eventsFirebase = new EventsFirebase();
                                eventsFirebase.deleteEvent(event.getEventId());

                                // Pop this fragment from backstack
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        //.setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    protected void setupWindow() {
        editEventDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String eventNameString = eventName.getText().toString();
                String eventDateString = eventDate.getText().toString();
                String eventLocationString = eventLocation.getText().toString();
                String eventDescriptionString = eventDescription.getText().toString();

                boolean addImage = false;

                /* TODO get the image of the event
                Matrix uploadedEventImage = eventImage.getImageMatrix()

                if (uploadedEventImage != null) {
                    addImage = true;
                }
                */

                if (eventNameString == null | eventDateString == null |
                        eventLocationString == null | eventDescriptionString == null) {
                    // TODO return error
                }
                else {
                    // TODO send to database the event details (in a method)


                    EventsFirebase eventsFirebase = new EventsFirebase();
                    String result = "";
                    if (((MainActivity) getActivity()).picture != null)
                        result = UserFirebase.convert(getActivity(), ((MainActivity) getActivity()).picture);

                    EditEventForm form = new EditEventForm(eventName,eventDate,eventTime, spinner, eventLocation,eventDescription, result, event.getEventId());

                    if (!form.allFilled()) {
                        //TODO: form not all filled error
                        //("ERROR", getString(R.string.errorEmptyFields));
                        errorMissingMessage.setVisibility(View.VISIBLE);
                    }
                    else if (!form.correctDate()) {
                        //TODO: date incorrect format error
                        //("ERROR", getString(R.string.errorInvalidTime));
                        errorTimeMessage.setVisibility(View.VISIBLE);
                    }
                    else {
                        eventsFirebase.updateEvent(form, adapter);
                        getActivity().getSupportFragmentManager().popBackStack();
                        errorMissingMessage.setVisibility(View.INVISIBLE);
                        errorTimeMessage.setVisibility(View.INVISIBLE);

                    }
                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }


        });

        if (event.getPicture() != null || event.getPicture() != "") {
            String imageFile = event.getPicture();

            Bitmap src = null;
            try {
                byte[] imageAsBytes = Base64.decode(imageFile, Base64.DEFAULT);
                src = BitmapFactory.decodeByteArray(imageAsBytes, 0, imageAsBytes.length);
            } catch(OutOfMemoryError e) {
                System.err.println(e.toString());
            }

            if (src != null)
                eventImage.setImageBitmap(src);
        }

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

    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).removeSearchToolbar();
    }

}