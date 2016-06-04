package com.spuds.eventapp.SignUp;

import android.app.ActionBar;
import android.app.Activity;
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
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.spuds.eventapp.Firebase.AccountFirebase;
import com.spuds.eventapp.Login.LoginActivity;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.Shared.RegistrationService;

//Created by youngjinyun

public class SignUpActivity extends AppCompatActivity {

    //Submit button, catalyst for any action on page
    Button signupButton;

    //User Entered Text
    EditText signupName;
    EditText signupEmail;
    EditText signupPassword1;
    EditText signupPassword2;
    TextView errorMessage;

    //I don't know but it seems important
    ImageView signup_logo;

    //Error check, switch to FALSE if there exists any error
    Boolean error = true;

    // email checking things
    int[] userCheck;
    AccountFirebase accountFirebase;
    TextView[] arrayMsg;

    /*---------------------------------------------------------------------------
       Function Name:       isValidEmail
       Description:         Checks to make sure email follows email address format
       Input:               CharSequence
       Output:              boolean
       ---------------------------------------------------------------------------*/
    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    /*---------------------------------------------------------------------------
   Function Name:       onCreate
   Description:         Sets up Firebase, fonts typefaces, and flags
   Input:               Bundle
   Output:              none
   ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //get the Activity window, add params to WindowManager
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        //set up Firebase tokens
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Set up typefaces for different fonts
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "name_font.ttf");
        Typeface raleway_light = Typeface.createFromAsset(getAssets(), "raleway-light.ttf");

        //Initialize header TextView from View and set it to custom font
        TextView header = (TextView)findViewById(R.id.signup_header);
        header.setTypeface(custom_font);


        //Use findViewById for all variables
        //Define "Sign Up" button that user clicks, prompting al this information to be processed
        signupButton = (Button)findViewById(R.id.signup_button);
        signupButton.setTypeface(raleway_light);

        //Fetch User Inputted "name" in String form
        signupName = (EditText)findViewById(R.id.signup_name);
        signupName.setTypeface(raleway_light);

        //Fetch User's entered email address
        signupEmail = (EditText)findViewById(R.id.signup_email);
        signupEmail.setTypeface(raleway_light);

        //Fetch User's entered new password, referred to as "signup_password_1"
        signupPassword1 = (EditText)findViewById(R.id.signup_password_1);
        signupPassword1.setTypeface(raleway_light);

        //Fetch User's entered confirm password, referred to as "signup_password_2"
        signupPassword2 = (EditText)findViewById(R.id.signup_password_2);
        signupPassword2.setTypeface(raleway_light);

        //Change Error Message TextView's font to custom font
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        errorMessage.setTypeface(raleway_light);

        //Initialize flag array
        userCheck = new int[1];
        //initialize a new account in Firebase
        accountFirebase = new AccountFirebase();
        //initialize array for textview for setting to error message
        arrayMsg = new TextView[1];


        //Upon User clicking "Sign Up", convert editable text fields to Strings
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //default initializations
                userCheck[0] = 0;
                arrayMsg[0] = errorMessage;

                //Make sure that user-entered name, email, password, confirm password are not blank
                if (signupName.getText().toString().equals("") || signupEmail.getText().toString().equals("") ||
                        signupPassword1.getText().toString().equals("") || signupPassword2.getText().toString().equals("")) {
                    errorMessage.setText(getString(R.string.errorEmptyFields));

                    error = false;
                }
                //Make sure that password and confirmed password match
                else if (!(signupPassword1.getText().toString()).equals(signupPassword2.getText().toString())) {
                    //reveal Invalid Password Match text
                    errorMessage.setText(getString(R.string.errorPassMismatch));
                    //set error flag to FALSE since there is an error now
                    error = false;
                }
                //If email is not valid, user gets error popup
                else if (!signupEmail.getText().toString().endsWith("@ucsd.edu")) {
                    errorMessage.setText(getString(R.string.errorInvalidEmail));
                    //set error flag to FALSE since there is an error now
                    error = false;
                }
                //if there are no local errors, check for database error
                else{
                    //correct error flag to true, everything is good now
                    String message = "Loading...";
                    //set message to email already in use error
                    errorMessage.setText(message);
                    accountFirebase.checkEmail(signupEmail.getText().toString(), arrayMsg, "That email is already signed up.", true);
                    error = true;
                }

                //If everything is good, then proceed to database query
                if(error) {
                    //Create a new thread for the purpose of querying Firebase for account information.
                    class myThread implements Runnable{

                        @Override
                        //run the thread
                        public void run() {
                            int counter = 0;
                            userCheck[0] = accountFirebase.getThreadCheck();

                            //wait for query
                            while (userCheck[0] == 0) {
                                if (counter > 200) {
                                    //break after 200 ms
                                    break;
                                }
                                try {
                                    //sleep for 100 ms
                                    Thread.sleep(100);
                                    //increment counter
                                    counter++;
                                    //set flag to result of accountFirebase method
                                    userCheck[0] = accountFirebase.getThreadCheck();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            //if no errors, create an account in Firebase
                            if (userCheck[0] == 2) {
                                accountFirebase.createAccount(signupEmail.getText().toString(),
                                        signupPassword1.getText().toString(), signupName.getText().toString());
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                            }
                        }

                    }
                    //set up and start threads
                    Runnable r = new myThread();
                    Thread t = new Thread(r);
                    t.start();
                }
            }
        });
    }
}


