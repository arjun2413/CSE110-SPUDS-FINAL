package com.spuds.eventapp.ChangePassword;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spuds.eventapp.Firebase.AccountFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;

/*---------------------------------------------------------------------------
   Class Name: ChangePasswordFragment
   Description: Contains methods related to the change password functionality
---------------------------------------------------------------------------*/
public class ChangePasswordFragment extends Fragment {

    //parts of the fragment
    private EditText current_pw;
    private EditText new_pw;
    private EditText confirm_pw;
    private Button changeButton;
    private TextView sys_message;

    private String error_string;
    private Boolean thread_running = false;

    private Boolean getThread_running(){
        return thread_running;
    }

    private void setThread_running(Boolean bool){
        thread_running = bool;
    }

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /*---------------------------------------------------------------------------
         Function Name: onCreate
         Description: sets up the change password screen
         Input: Bundle savedInstanceState
         Output: None
    ---------------------------------------------------------------------------*/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /*---------------------------------------------------------------------------
         Function Name: getUserInputs
         Description: gets the user inputs from the fields
         Input: AccountFirebase af - reference to the accountFirebase class
         Output: ChangePasswordForm - a form containing all inputs
    ---------------------------------------------------------------------------*/
    private ChangePasswordForm getUserInputs( AccountFirebase af ){
        String email = af.getUserEmail();
        return new ChangePasswordForm(email,current_pw,new_pw,confirm_pw);
    }

    /*---------------------------------------------------------------------------
         Function Name: appendError
         Description: adds to the error string
         Input: String error - the error message to be added
         Output: None
    ---------------------------------------------------------------------------*/
    private void appendError(String error){
        error_string = error_string + error;
    }

    /*---------------------------------------------------------------------------
         Function Name: restartError
         Description: resets the error string
         Input: None
         Output: None
    ---------------------------------------------------------------------------*/
    private void restartError(){
        error_string = "";
        sys_message.setText("");
    }

    //getter method for error message
    private String getError(){
        return error_string;
    }

    /*---------------------------------------------------------------------------
         Function Name: onCreateView
         Description: sets up the change password screen, containing logic checks
         Input: LayoutInflater, ViewGroup, Bundle
         Output: View
    ---------------------------------------------------------------------------*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //initializing parts of the fragment
        final View view = inflater.inflate(R.layout.fragment_change_password, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Change Password");
        overrideFonts(view.getContext(),view);

        Typeface raleway_medium = Typeface.createFromAsset(getActivity().getAssets(),  "Raleway-Medium.ttf");

        //title font
        Button change = (Button) view.findViewById(R.id.change_button);
        change.setTypeface(raleway_medium);

        //xml elements
        current_pw = (EditText) view.findViewById(R.id.current_password);
        new_pw = (EditText) view.findViewById(R.id.new_password);
        confirm_pw = (EditText) view.findViewById(R.id.confirm_password);
        changeButton = (Button) view.findViewById(R.id.change_button);
        sys_message = (TextView) view.findViewById(R.id.system_message);

        //ref to accountfirebase class
        final AccountFirebase af = new AccountFirebase();

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Logic:
                //      1. check if first field matches user password
                //      2. check if second field fulfills all password requirements
                //      3. show snackbar on success or error message accordingly.

                ChangePasswordForm form = getUserInputs(af);

                //default string set to append error to
                restartError();

                //checks if an error has been found
                boolean is_error = false;

                if(!form.allFilled()) {
                    appendError(getString(R.string.errorEmptyFields));
                    is_error = true;
                }
                //checks if second and third fields are the same
                if(!form.matchingPw()){
                    if(is_error) {
                        appendError("\n");
                    }
                    appendError(getString(R.string.errorPassMismatch));
                    is_error = true;
                }
                else if(!form.diffPw()){    //check if new pass different from old pass
                    if(is_error){
                        appendError("\n");
                    }
                    appendError(getString(R.string.errorSamePass));
                    is_error = true;
                }

                //pops up a snackbar with successful change message if no error was found
                if(!is_error) {
                    af.changePass(form);

                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            setThread_running(true);
                            while (af.getThreadCheck() == 0) {
                                try {
                                    Thread.sleep(Integer.parseInt(getString(R.string.sleepTime)));
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }

                            if (af.getThreadCheck() == 1) {
                                Snackbar snackbar = Snackbar.make
                                        (view, getString(R.string.password_change_success),
                                                Snackbar.LENGTH_LONG);
                                snackbar.show();
                                getActivity().getSupportFragmentManager().popBackStack();
                            }
                            else{
                                appendError("Incorrect Password");
                                getActivity().runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        sys_message.setText(error_string);
                                    }
                                });

                            }
                            setThread_running(false);
                        }
                    }).start();
                    while(getThread_running()){
                        //stalls until thread above ends so user can't click
                    }


                }
                else {
                    sys_message.setText(getError());
                }
                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
            }
        });

        return view;
    }


    //required super method calls
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    /*---------------------------------------------------------------------------
         Function Name: overrideFonts
         Description: overrides the fonts on the page
         Input: context, view
         Output: none
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
         Function Name: onResume
         Description: super call to onResume, removes search toolbar
         Input: none
         Output: none
    ---------------------------------------------------------------------------*/
    @Override
    public void onResume() {
        super.onResume();
        ((MainActivity)getActivity()).removeSearchToolbar();
    }

}
