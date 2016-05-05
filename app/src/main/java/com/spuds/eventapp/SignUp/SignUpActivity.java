package com.spuds.eventapp.SignUp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.spuds.eventapp.R;

public class SignUpActivity extends AppCompatActivity {

    //Submit button, catalyst for any action on page
    Button signup_button;

    //User Entered Text
    EditText signup_name;
    EditText signup_email;
    EditText signup_password_1;
    EditText signup_password_2;

    //Error TextViews
    TextView signupPasswordMatchError;
    TextView signupInvalidEmailError;

    //I don't know but it seems important
    ImageView signup_logo;

    //Error check, switch to FALSE if there exists any error
    Boolean error = true;

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
        signup_button = (Button)findViewById(R.id.signup_button);

        //Fetch User Inputted "name" in String form
        signup_name = (EditText)findViewById(R.id.signup_name);

        //Fetch User's entered email address
        signup_email = (EditText)findViewById(R.id.signup_email);

        //Fetch User's entered new password, referred to as "signup_password_1"
        signup_password_1 = (EditText)findViewById(R.id.signup_password_1);

        //Fetch User's entered confirm password, referred to as "signup_password_2"
        signup_password_2 = (EditText)findViewById(R.id.signup_password_2);

        //Fetch invisible Password Warning Text
        signupPasswordMatchError = (TextView)findViewById(R.id.signupPasswordMatchError);

        //Fetch invisible Invalid Email text
        signupInvalidEmailError = (TextView)findViewById(R.id.signupInvalidEmailError);

        //Upon User clicking "Sign Up", convert editable text fields to Strings
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Make sure passwords are matching. If passwords are not equal, display popup
                 that says "Passwords must match.
                  */
                if (!signup_password_1.getText().toString().equals(signup_password_2.getText().toString())) {
                    //reveal Invalid Password Match text
                    signupPasswordMatchError.setVisibility(View.VISIBLE);

                    //set error flag to FALSE since there is an error now
                    error = false;
                }
                else{
                    //hide it
                    signupPasswordMatchError.setVisibility(View.INVISIBLE);

                    //correct error flag to true, everything is good now
                    error = true;
                }
                //If email is not valid, user gets error popup
                if (!isValidEmail(signup_email.getText().toString())) {
                    //reveal Invalid Email text
                    signupInvalidEmailError.setVisibility(View.VISIBLE);

                    //set error flag to FALSE since there is an error now
                    error = false;

                }
                else{
                    //hide it
                    signupInvalidEmailError.setVisibility(View.INVISIBLE);

                    //correct error flag to true, everything is good now
                    error = true;
                }

                //If everything is good, then proceed to database query
                if(error) {
                    //TODO Check if email is taken already with database

                    //TODO If email isn't taken already, go through with account creation
                    //Make these console logs instead route to database.
                    Log.v("signup_name", signup_name.getText().toString());
                    Log.v("signup_email", signup_email.getText().toString());
                    Log.v("signupPassword", signup_password_1.getText().toString());
                }
            }
        });
    }
}


