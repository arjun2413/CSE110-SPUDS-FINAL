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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private ChangePasswordForm getUserInputs( AccountFirebase af ){
        String email = af.getUserEmail();
        return new ChangePasswordForm(email,current_pw,new_pw,confirm_pw);
    }

    private void appendError(String error){
        error_string = error_string + error;
    }

    private void restartError(){
        error_string = "";
        sys_message.setText("");
    }

    private String getError(){
        return error_string;
    }

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

                //checks if an error has been found
                boolean is_error = false;

                Log.v("is_error 1:" , String.valueOf(is_error));
                if(!form.allFilled()) {
                    appendError(getString(R.string.errorEmptyFields));
                    is_error = true;
                    Log.v("is_error 2:" , String.valueOf(is_error));
                }

                //checks if second and third fields are the same
                if(!form.matchingPw()){
                    if(is_error) {
                        appendError("\n");
                    }
                    appendError(getString(R.string.errorPassMismatch));
                    is_error = true;
                    Log.v("is_error 3:" , String.valueOf(is_error));
                }

                else if(!form.diffPw()){
                    if(is_error){
                        appendError("\n");
                    }
                    appendError(getString(R.string.errorSamePass));
                    is_error = true;
                    Log.v("is_error 4:" , String.valueOf(is_error));
                }

                //pops up a snackbar with successful change message if no error was found
                if(!is_error) {
                    Log.v("is_error 4:" , String.valueOf(is_error));
                    af.changePass(form);

                    // Pop this fragment from backstack
                    //getActivity().getSupportFragmentManager().popBackStack();


                    //TODO: need to test if snackbar works properly
                    new Thread(new Runnable() {

                        @Override
                        public void run() {
                            setThread_running(true);
                            while (af.getThreadCheck() == 0) {
                                try {
                                    Thread.sleep(75);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }


                            if (af.getThreadCheck() == 1) {
                                Log.v("password: ", "matches top");
                                Snackbar snackbar = Snackbar.make
                                        (view, getString(R.string.password_change_success),
                                                Snackbar.LENGTH_LONG);
                                snackbar.show();
                                getActivity().getSupportFragmentManager().popBackStack();
                                Log.v("password: ", "matches bottom");
                            }
                            else{
                                Log.v("password: ", "does not match top");
                                Snackbar snackbar = Snackbar.make
                                        (view, getString(R.string.errorWrongPass), Snackbar.LENGTH_LONG);
                                snackbar.show();
                                Log.v("password: ", "does not match bottom");
                            }
                            setThread_running(false);
                        }
                    }).start();
                    while(getThread_running()){
                        //stalls until thread above ends so user can't click
                    }

                    Log.v("finished: ", "both threads");


                }
                else {
                    //show concatinated error messages

                    sys_message.setText(getError());
                }

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

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
            } else if (v instanceof TextView ) {
                ((TextView) v).setTypeface(Typeface.createFromAsset(context.getAssets(), "raleway-regular.ttf"));
            }
        }
        catch (Exception e) {
        }
    }


}
