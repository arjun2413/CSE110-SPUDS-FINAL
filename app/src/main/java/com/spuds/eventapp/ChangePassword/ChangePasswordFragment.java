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

import com.spuds.eventapp.Firebase.AccountFirebase;
import com.spuds.eventapp.R;

public class ChangePasswordFragment extends Fragment {


    //parts of the fragment
    private EditText current_pw;
    private EditText new_pw;
    private EditText confirm_pw;
    private Button changeButton;
    private TextView sys_message;

    private String error_string;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ChangePasswordForm getUserInputs( AccountFirebase af ){
        String email = af.getEmail();
        return new ChangePasswordForm(email,current_pw,new_pw,confirm_pw);
    }

    private void appendError(String error){
        error_string = error_string + error;
    }

    private void restartError(){
        error_string = "";
    }

    private String getError(){
        return error_string;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initializing parts of the fragment
        final View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        current_pw = (EditText) view.findViewById(R.id.current_password);
        new_pw = (EditText) view.findViewById(R.id.new_password);
        confirm_pw = (EditText) view.findViewById(R.id.confirm_password);
        changeButton = (Button) view.findViewById(R.id.change_button);
        sys_message = (TextView) view.findViewById(R.id.system_message);

        final AccountFirebase af = new AccountFirebase();

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: move error checking logic to model file.
                //Logic:
                //      2. check if first field matches user password
                //      3. check if second field fulfills all password requirements
                //      5. show snackbar on success or error message accordingly.

                ChangePasswordForm form = getUserInputs(af);

                //default string set to append error to
                restartError();

                if(form.allFilled()){

                    //checks if an error has been found
                    boolean is_error = false;

                    //checks if second and third fields are the same
                    if(!form.matchingPw()){
                        appendError(getString(R.string.mismatched_fields_error));
                        is_error = true;
                    }

                    //pops up a snackbar with successful change message if no error was found
                    if(!is_error) {

                        af.changePass(form);

                        // Pop this fragment from backstack
                        getActivity().getSupportFragmentManager().popBackStack();

                        //TODO: need to test if snackbar works properly
                        Snackbar snackbar = Snackbar.make
                                (view,getString(R.string.password_change_success),
                                        Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                    else {
                        //show concatinated error messages
                        sys_message.setText(getError());
                    }
                }
                //Tells the user that they must fill all fields
                else {
                    appendError(getString(R.string.missing_fields_error));
                    sys_message.setText(getError());
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
