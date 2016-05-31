package com.spuds.eventapp.CreateComment;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;


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

        // If bundle is null then it is a root comment
        // If bundle has parent then it is a reply comment
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initialize parts of the fragment
        final View view = inflater.inflate(R.layout.fragment_create_comment, container, false);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Comment");

        overrideFonts(view.getContext(),view);

        Typeface raleway_medium = Typeface.createFromAsset(getActivity().getAssets(),  "Raleway-Medium.ttf");

        //title font
        Button submitbutton = (Button) view.findViewById(R.id.comment_submit);
        submitbutton.setTypeface(raleway_medium);

        comment = (EditText) view.findViewById(R.id.comment_input);
        submit = (Button) view.findViewById(R.id.comment_submit);

        // TODO (M): Migrate logic to model
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

                    // Pop this fragment from backstack
                    getActivity().getSupportFragmentManager().popBackStack();

                    // TODO (M): submit comment to database
                }
                //Tells the user that they must fill all fields
                else {

                    Snackbar snackbar = Snackbar.make
                            (view,getString(R.string.errorEmptyFields),
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

    private void overrideFonts(final Context context, final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    overrideFonts(context, child);
                }
            } else if (v instanceof TextView) {
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
