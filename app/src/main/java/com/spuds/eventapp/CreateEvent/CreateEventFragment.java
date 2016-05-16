package com.spuds.eventapp.CreateEvent;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.spuds.eventapp.R;

import java.util.ArrayList;

import cn.refactor.library.SmoothCheckBox;


public class CreateEventFragment extends Fragment {

    private ImageView eventImage;
    private EditText eventName;
    private EditText eventDate;
    private EditText eventLocation;
    private EditText eventDescription;
    private Button editEventDelete;
    private Button editEventDone;

    private ArrayList editEventFields;

    protected CreateEventForm getEventDetails(View view) {
        eventImage = (ImageView) view.findViewById(R.id.eventImage);
        eventName = (EditText) view.findViewById(R.id.eventName);
        eventDate = (EditText) view.findViewById(R.id.eventDate);
        eventLocation = (EditText) view.findViewById(R.id.eventLocation);
        eventDescription = (EditText) view.findViewById(R.id.eventDescription);
        editEventDelete = (Button) view.findViewById(R.id.editEventDelete);
        editEventDone = (Button) view.findViewById(R.id.editEventDone);

        editEventFields = new ArrayList<String>();

        return new CreateEventForm(eventName,eventDate,eventLocation,eventDescription);
    }

    /*
    protected void setupWindow() {
        editEventDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEventForm form = getEventDetails(view);

                boolean addImage = false;

                /* TODO get the image of the event
                Matrix uploadedEventImage = eventImage.getImageMatrix()

                if (uploadedEventImage != null) {
                    addImage = true;
                }
                */

/*
                if (eventNameString == null | eventDateString == null |
                        eventLocationString == null | eventDescriptionString == null) {
                    // TODO return error
                }
                else {
                    // TODO send to database the event details (in a method)
                    if (addImage) {
                        // TODO push to editEventFields array list
                    }
                }
            }
        });
    }*/

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

        getEventDetails(view);
        editEventDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CreateEventForm form = getEventDetails(view);

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

                }
                else {
                    // TODO return error
                }
            }
        });

        final SmoothCheckBox scb = (SmoothCheckBox) view.findViewById(R.id.scb);
        scb.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                Log.d("SmoothCheckBox", String.valueOf(isChecked));
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
