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
                if ((current_pw.getText().length() > 0) && (new_pw.getText().length() >
                        0) && (confirm_pw.getText().length() > 0)) {
                    boolean is_error = false;
                    String user_pw = "a";
                    String first_pw = current_pw.getText().toString();
                    String second_pw = new_pw.getText().toString();
                    String third_pw = confirm_pw.getText().toString();
                    String message = "";
                    if(!(first_pw.equals(user_pw))){
                        String wrong_user_pw = "Error 1: incorrect current password";
                        message = message + wrong_user_pw;
                        is_error = true;
                    }
                    if(!(second_pw.equals(third_pw))){
                        String different_pw = "Error 2: new passwords do not match";
                        if(is_error){
                            message = message + "\n";
                        }
                        message = message + different_pw;
                        is_error = true;
                    }
                    if(!is_error) {

                        //TODO: need to test if snackbar works properly
                        Snackbar snackbar = Snackbar.make(view, "Password Changed", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    sys_message.setText(message);
                } else {
                    String incomplete_input = "Error 3: not all fields complete";
                    sys_message.setText(incomplete_input);
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
