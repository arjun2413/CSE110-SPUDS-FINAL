package com.spuds.eventapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import java.util.ArrayList;

/**
 * Created by Jonathan on 4/27/16.
 */
public class CreateEditEvent extends AppCompatActivity {

    private ImageView eventImage;
    private EditText eventName;
    private EditText eventDate;
    private EditText eventLocation;
    private EditText eventDescription;
    private Button editEventDelete;
    private Button editEventDone;

    private ArrayList editEventFields;

    protected void getEventDetails() {
        eventImage = (ImageView) findViewById(R.id.eventImage);
        eventName = (EditText) findViewById(R.id.eventName);
        eventDate = (EditText) findViewById(R.id.eventDate);
        eventLocation = (EditText) findViewById(R.id.eventLocation);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        editEventDelete = (Button) findViewById(R.id.editEventDelete);
        editEventDone = (Button) findViewById(R.id.editEventDone);

        editEventFields = new ArrayList<String>();
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
                    if (addImage) {
                        // TODO push to editEventFields array list
                    }
                }
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_edit_event);

        getEventDetails();
        setupWindow();

    }
}
