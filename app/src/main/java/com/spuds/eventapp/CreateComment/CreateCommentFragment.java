package com.spuds.eventapp.CreateComment;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.spuds.eventapp.R;


public class CreateCommentFragment extends Fragment {

    //parts of the fragment
    private EditText comment;
    private Button submit;

    public CreateCommentFragment() {
        //required empty constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize parts of the fragment
        final View view = inflater.inflate(R.layout.fragment_create_comment, container, false);
        comment = (EditText) view.findViewById(R.id.comment_input);
        submit = (Button) view.findViewById(R.id.comment_submit);

        // TODO: Migrate logic to model
        //     1. Check if all fields are filled.
        //     2. Show snackbar if successful.

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Checks if all fields have been entered
                if ((comment.getText().length() > 0) ) {

                    Snackbar snackbar = Snackbar.make
                            (view,getString(R.string.comment_submit_success),
                                    Snackbar.LENGTH_LONG);
                    snackbar.show();

                    //TODO: submit comment to database
                }
                //Tells the user that they must fill all fields
                else {

                    Snackbar snackbar = Snackbar.make
                            (view,getString(R.string.missing_fields_error),
                                    Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
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
