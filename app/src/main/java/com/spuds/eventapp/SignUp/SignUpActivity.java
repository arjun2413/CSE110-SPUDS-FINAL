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


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null)
            return false;

        return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {


        //("Create signup", "Creating right now");

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

        errorMessage = (TextView) findViewById(R.id.errorMessage);
        errorMessage.setTypeface(raleway_light);

        userCheck = new int[1];
        accountFirebase = new AccountFirebase();
        arrayMsg = new TextView[1];


        //Upon User clicking "Sign Up", convert editable text fields to Strings
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userCheck[0] = 0;
                arrayMsg[0] = errorMessage;


                if (signupName.getText().toString().equals("") || signupEmail.getText().toString().equals("") ||
                        signupPassword1.getText().toString().equals("") || signupPassword2.getText().toString().equals("")) {
                    errorMessage.setText(getString(R.string.errorEmptyFields));

                    error = false;
                }
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
                else{
                    //correct error flag to true, everything is good now
                    String message = "Loading...";
                    errorMessage.setText(message);
                    accountFirebase.checkEmail(signupEmail.getText().toString(), arrayMsg, "That email is already signed up.", true);
                    error = true;
                }

                //If everything is good, then proceed to database query
                if(error) {
                    //TODO Check if email is taken already with database

                    //TODO If email isn't taken already, go through with account creation
                    //HERE WE PLAY WITH MORE THREADS SIGH
                    class myThread implements Runnable{

                        @Override
                        public void run() {
                            int counter = 0;
                            System.out.println("thread start");
                            userCheck[0] = accountFirebase.getThreadCheck();

                            //wait for query
                            while (userCheck[0] == 0) {
                                if (counter > 200) {
                                    //things break
                                    break;
                                }
                                try {
                                    System.out.println("I slept for " + counter);
                                    Thread.sleep(100);
                                    counter++;
                                    userCheck[0] = accountFirebase.getThreadCheck();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (userCheck[0] == 2) {
                                accountFirebase.createAccount(signupEmail.getText().toString(),
                                        signupPassword1.getText().toString(), signupName.getText().toString());
                                //When done, leave this page and go to main screen.
                                //Make these console logs instead route to database.
                                //("signup_name", signupName.getText().toString());
                                //("signup_email", signupEmail.getText().toString());
                                //("signupPassword", signupPassword1.getText().toString());
                                //accountFirebase = new AccountFirebase();
                                startActivity(new Intent(SignUpActivity.this, LoginActivity.class));
                                //(AppCompatActivity)getActivity().getSupportFragmentManager().popBackStack();
                            }
                        }

                    }
                    Runnable r = new myThread();
                    Thread t = new Thread(r);
                    t.start();
                }
            }
        });
    }
}


