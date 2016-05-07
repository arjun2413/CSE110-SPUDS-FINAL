package com.spuds.eventapp.ChangePassword;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spuds.eventapp.R;

public class ChangePasswordFragment extends Fragment {


    //parts of the fragment
    private EditText current_pw;
    private EditText new_pw;
    private EditText confirm_pw;
    private Button change_pw;
    private TextView sys_message;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initializing parts of the fragment
        final View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        current_pw = (EditText) view.findViewById(R.id.current_password);
        new_pw = (EditText) view.findViewById(R.id.new_password);
        confirm_pw = (EditText) view.findViewById(R.id.confirm_password);
        change_pw = (Button) view.findViewById(R.id.change_password);
        sys_message = (TextView) view.findViewById(R.id.system_message);

        change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: move error checking logic to model file.
                //Logic:
                //      1. check if all fields are entered.
                //      2. check if first field matches user password
                //      3. check if second field fulfills all password requirements
                //      4. check if second and third fields match
                //      5. show snackbar on success or error message accordingly.


                //Checks if all fields have been entered
                if ((current_pw.getText().length() > 0) && (new_pw.getText().length() >
                        0) && (confirm_pw.getText().length() > 0)) {

                    //checks if an error has been found
                    boolean is_error = false;

                    //default comparison password to user password
                    String user_pw = "a";

                    //gets all entered strings to perform logic
                    String first_pw = current_pw.getText().toString();
                    String second_pw = new_pw.getText().toString();
                    String third_pw = confirm_pw.getText().toString();

                    //default string set to append error to
                    String message = "";

                    //checks if first field matches user current password
                    if(!(first_pw.equals(user_pw))){

                        //get error message and set that an error was found
                        message = message + getString(R.string.incorrect_password_error);
                        is_error = true;
                    }

                    //checks if second and third fields are the same
                    if(!(second_pw.equals(third_pw))){

                        //append to error message if one already found
                        if(is_error){
                            message = message + "\n";
                        }
                        message = message + getString(R.string.mismatched_fields_error);
                        is_error = true;
                    }

                    //pops up a snackbar with successful change message if no error was found
                    if(!is_error) {

                        //TODO: need to test if snackbar works properly
                        Snackbar snackbar = Snackbar.make
                                (view,getString(R.string.password_change_success),
                                        Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    else {

                        //show concatinated error messages
                        sys_message.setText(message);
                    }
                }
                //Tells the user that they must fill all fields
                else {
                    sys_message.setText(getString(R.string.missing_fields_error));
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
