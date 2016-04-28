package com.spuds.eventapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class SignUpActivity extends AppCompatActivity {

    //Submit button, catalyst for any action on page
    Button signupButton;

    //User Entered Text
    EditText signupName;
    EditText signupEmail;
    EditText signupPassword1;
    EditText signupPassword2;

    //Error TextViews
    TextView signupPasswordMatch;
    TextView signupInvalidEmail;

    //I don't know but it seems important
    ImageView signup_logo;

    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Use findViewById for all variables
        //Define "Sign Up" button that user clicks, prompting al this information to be processed
        signupButton = (Button)findViewById(R.id.signupButton);

        //Fetch User Inputted "name" in String form
        signupName = (EditText)findViewById(R.id.signupName);

        //Fetch User's entered email address
        signupEmail = (EditText)findViewById(R.id.signupEmail);

        //Fetch User's entered new password, referred to as "signupPassword1"
        signupPassword1 = (EditText)findViewById(R.id.signupPassword1);

        //Fetch User's entered confirm password, referred to as "signupPassword2"
        signupPassword2 = (EditText)findViewById(R.id.signupPassword2);

        //Fetch invisible Password Warning Text
        signupPasswordMatch = (TextView)findViewById(R.id.signupPasswordMatch);

        //Fetch invisible Invalid Email text
        signupInvalidEmail = (TextView)findViewById(R.id.signupInvalidEmail);

        //Upon User clicking "Sign Up", convert editable text fields to Strings
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Make sure passwords are matching. If passwords are not equal, display popup
                 that says "Passwords must match.
                  */
                if (!signupPassword1.getText().toString().equals(signupPassword2.getText().toString())) {
                    //reveal Invalid Password Match text
                    signupPasswordMatch.setVisibility(View.VISIBLE);

                }
                //If email is not valid, user gets error popup
                else if (!isValidEmail(signupEmail.getText().toString())) {
                    //reveal Invalid Email text
                    signupInvalidEmail.setVisibility(View.VISIBLE);

                }
                //If everything is good, then proceed to database query
                else {
                    //TODO Check if email is taken already with database

                    //TODO If email isn't taken already, go through with account creation
                    //Make these console logs instead route to database.
                    Log.v("signupName", signupName.getText().toString());
                    Log.v("signupEmail", signupEmail.getText().toString());
                    Log.v("signupPassword", signupPassword1.getText().toString());
                }
            }
        });
    }
}


