package com.spuds.eventapp.SignUp;

import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.spuds.eventapp.Firebase.AccountFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;

public class SignUpActivity extends AppCompatActivity {

    //Submit button, catalyst for any action on page
    Button signupButton;

    //User Entered Text
    EditText signupName;
    EditText signupEmail;
    EditText signupPassword1;
    EditText signupPassword2;

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
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "name_font.ttf");
        TextView header = (TextView)findViewById(R.id.signup_header);
        header.setTypeface(custom_font);


        //Use findViewById for all variables
        //Define "Sign Up" button that user clicks, prompting al this information to be processed
        signupButton = (Button)findViewById(R.id.signup_button);

        //Fetch User Inputted "name" in String form
        signupName = (EditText)findViewById(R.id.signup_name);

        //Fetch User's entered email address
        signupEmail = (EditText)findViewById(R.id.signup_email);

        //Fetch User's entered new password, referred to as "signup_password_1"
        signupPassword1 = (EditText)findViewById(R.id.signup_password_1);

        //Fetch User's entered confirm password, referred to as "signup_password_2"
        signupPassword2 = (EditText)findViewById(R.id.signup_password_2);

        //Fetch invisible Password Warning Text
        //signupPasswordMatchError = (TextView)findViewById(R.id.signupPasswordMatchError);

        //Fetch invisible Invalid Email text
        //signupInvalidEmailError = (TextView)findViewById(R.id.signupInvalidEmailError);

        //Upon User clicking "Sign Up", convert editable text fields to Strings
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*Make sure passwords are matching. If passwords are not equal, display popup
                 that says "Passwords must match.
                  */
                if (!signupPassword1.getText().toString().equals(signupPassword2.getText().toString())) {
                    //reveal Invalid Password Match text
                    //signupPasswordMatchError.setVisibility(View.VISIBLE);

                    //set error flag to FALSE since there is an error now
                    error = false;
                }
                else{
                    //hide it
                    //signupPasswordMatchError.setVisibility(View.INVISIBLE);

                    //correct error flag to true, everything is good now
                    error = true;
                }
                //If email is not valid, user gets error popup
                if (!isValidEmail(signupEmail.getText().toString())) {
                    //reveal Invalid Email text
                    //signupInvalidEmailError.setVisibility(View.VISIBLE);

                    //set error flag to FALSE since there is an error now
                    error = false;

                }
                else{
                    //hide it
                    //signupInvalidEmailError.setVisibility(View.INVISIBLE);

                    //correct error flag to true, everything is good now
                    error = true;
                }

                //If everything is good, then proceed to database query
                if(error) {
                    //TODO Check if email is taken already with database

                    //TODO If email isn't taken already, go through with account creation
                    //Make these console logs instead route to database.
                    Log.v("signup_name", signupName.getText().toString());
                    Log.v("signup_email", signupEmail.getText().toString());
                    Log.v("signupPassword", signupPassword1.getText().toString());
                    AccountFirebase accountFirebase = new AccountFirebase();
                    accountFirebase.createAccount(signupEmail.getText().toString(),
                            signupPassword1.getText().toString());
                    //When done, leave this page and go to main screen.
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                }
            }
        });
    }
}


