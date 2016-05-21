package com.spuds.eventapp.SignUp;

import android.app.ActionBar;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

        //Hide Action Bar and Status Bar
        //View decorView = getWindow().getDecorView();
        // Hide the status bar.
        //int uiOptions = View.SYSTEM_UI_FLAG_FULLSCREEN;
        //decorView.setSystemUiVisibility(uiOptions);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "name_font.ttf");
        Typeface raleway_light = Typeface.createFromAsset(getAssets(),  "raleway-light.ttf");

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

        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
        errorMessage.setTypeface(raleway_light);

        //Upon User clicking "Sign Up", convert editable text fields to Strings
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (signupName.getText().toString().equals("") || signupEmail.getText().toString().equals("") ||
                        signupPassword1.getText().toString().equals("") || signupPassword2.getText().toString().equals("")) {
                    TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                    String message = "Missing fields. Please try again.";
                    errorMessage.setText(message);

                    error = false;
                }
                else if (!(signupPassword1.getText().toString()).equals(signupPassword2.getText().toString())) {
                    //reveal Invalid Password Match text
                    TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                    String message = "Passwords must match";
                    errorMessage.setText(message);
                    //set error flag to FALSE since there is an error now
                    error = false;
                }
                //If email is not valid, user gets error popup
                else if (!signupEmail.getText().toString().endsWith("@ucsd.edu")) {
                    TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                    String message = "Please use a ucsd.edu email to log in.";
                    errorMessage.setText(message);
                    //set error flag to FALSE since there is an error now
                    error = false;
                }
                else{
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
                            signupPassword1.getText().toString(), signupName.getText().toString());
                    //When done, leave this page and go to main screen.
                    startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                }
            }
        });
    }
}


