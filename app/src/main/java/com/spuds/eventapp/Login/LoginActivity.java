package com.spuds.eventapp.Login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.spuds.eventapp.Firebase.AccountFirebase;
import com.spuds.eventapp.Firebase.UserFirebase;
import com.spuds.eventapp.R;
import com.spuds.eventapp.ResetPassword.ResetPasswordActivity;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.SignUp.SignUpActivity;

/*---------------------------------------------------------------------------
   Class Name: LoginActivity
   Description: Contains all methods related to login functionality
---------------------------------------------------------------------------*/
public class LoginActivity extends AppCompatActivity {
    private AccountFirebase accountFirebase;
    private TextView errorMessage;

    /*---------------------------------------------------------------------------
     Function Name: onCreate
     Description: Sets up the login page and all the buttons
     Input: Bundle savedInstanceState
     Output: None
    ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Firebase.setAndroidContext(this);
        setContentView(R.layout.activity_login);

        //Typefaces for two different fonts
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "name_font.ttf");
        Typeface raleway_light = Typeface.createFromAsset(getAssets(),  "raleway-light.ttf");

        //title font
        TextView tx = (TextView)findViewById(R.id.app_name);
        tx.setTypeface(custom_font);

        //all other text font is raleway-light
        EditText enterEmail = (EditText)findViewById(R.id.email);
        enterEmail.setTypeface(raleway_light);

        EditText enterPassword = (EditText)findViewById(R.id.password);
        enterPassword.setTypeface(raleway_light);

        errorMessage = (TextView) findViewById(R.id.errorMessage);
        errorMessage.setTypeface(raleway_light);

        Button signInButton = (Button) findViewById(R.id.signIn);
        signInButton.setTypeface(raleway_light);

        Button forgotPassword = (Button) findViewById(R.id.forgot_password);
        forgotPassword.setTypeface(raleway_light);

        Button registerButton = (Button) findViewById(R.id.register);
        registerButton.setTypeface(raleway_light);

        accountFirebase = new AccountFirebase();
        signInFunc();       //sets up sign in functionality
        signUpFunc();       //sets up sign up functionality
        resetPassFunc();    //sets up forgot pass functionality
    }

    /*---------------------------------------------------------------------------
     Function Name: signUpFunc
     Description: Sets up the sign up button, which when clicked leads to the
                sign up page
     Input: None
     Output: None
    ---------------------------------------------------------------------------*/
    public void signUpFunc(){
        //create a button for the sign up
        final Button signUp = (Button) findViewById(R.id.register);
        if(signUp != null) {
            //set a on click listener to see when the button is clicked
            signUp.setOnClickListener(new View.OnClickListener() {
                //what happens when the button is clicked
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
                }
            });
        }
    }

    /*---------------------------------------------------------------------------
     Function Name: resetPassFunc
     Description: Sets up the forgotpassword button, which leads to the
                resetpassword page
     Input: None
     Output: None
    ---------------------------------------------------------------------------*/
    public void resetPassFunc() {
        //create a button for the forgot password
        final Button forgotPass = (Button) findViewById(R.id.forgot_password);
        //set a on click listener to see when the button is clicked
        if (forgotPass != null) {
            forgotPass.setOnClickListener(new View.OnClickListener() {
                //what happens when the button is clicked
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));
                }
            });
        }
    }

    /*---------------------------------------------------------------------------
     Function Name: signInFunc
     Description: Sets up the sign in functionality with all the logic
     Input: None
     Output: None
    ---------------------------------------------------------------------------*/
    public void signInFunc() {
        //create a button for the sign in
        final Button signIn = (Button) findViewById(R.id.signIn);

        //set a on click listener to see when the button is clicked
        if (signIn != null) {
            signIn.setOnClickListener(new View.OnClickListener() {
                //what happens when the button is clicked
                public void onClick(View v) {
                    //get the user input for email field
                    String email = getEmail();
                    //get user input for password field
                    String password = getPassword();

                    /*  STEPS:
                     *  1. Check if fields are filled
                     *  2. Check if email is valid ucsd email
                     *  3. Calls login method from accountfirebase
                     *  4. Returns success or prints error messages
                     */

                    //make sure that something is entered for email and password
                    if (email.equals("") || password.equals("")) {
                        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                        if(errorMessage != null) {
                            errorMessage.setText(getString(R.string.errorEmptyFields));
                        }
                    }
                    else if (!email.endsWith("@ucsd.edu")) {
                        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                        if(errorMessage != null) {
                            errorMessage.setText(getString(R.string.errorInvalidEmail));
                        }
                    }
                    else{
                        errorMessage.setText(getString(R.string.loading));

                        class myThread implements Runnable{
                            public String email;
                            public String password;

                            public myThread (String one, String two){
                                email = one;
                                password = two;
                            }
                            @Override
                            public void run() {
                                accountFirebase.logIn(email, password);
                                int counter = 0;
                                while (accountFirebase.status == 0) {
                                    try {
                                        counter++;
                                        Thread.sleep(75);
                                        if (counter == 100) {
                                            accountFirebase.status = 3;
                                        }
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        if(accountFirebase.status == 2) {
                                            errorMessage.setText(getString(R.string.errorLoginPass));
                                        }
                                        else if (accountFirebase.status == 3) {
                                            String message = "Network error.";
                                            errorMessage.setText(message);
                                        }
                                    }
                                });
                                if (accountFirebase.status == 1) {
                                    getUserDetails();
                                }
                            }
                        }
                        Runnable r = new myThread(email, password);
                        Thread t = new Thread(r);
                        t.start();
                        accountFirebase.status = 0;
                    }
                }
            });
        }
    }

    /*---------------------------------------------------------------------------
     Function Name: getEmail
     Description: getter method for email field
     Input: None
     Output: String - email
    ---------------------------------------------------------------------------*/
    public String getEmail(){
        EditText email = (EditText) findViewById(R.id.email);
        if(email != null) {
            return email.getText().toString();
        }
        return "";
    }

    /*---------------------------------------------------------------------------
     Function Name: getPassword
     Description: getter method for password field
     Input: None
     Output: String - password
    ---------------------------------------------------------------------------*/
    public String getPassword() {
        EditText password = (EditText) findViewById(R.id.password);
        if(password != null) {
            return password.getText().toString();
        }
        return "";
    }

    UserFirebase userFirebase = new UserFirebase();

    /*---------------------------------------------------------------------------
     Function Name: getUserDetails
     Description: gets the user details to log in with
     Input: None
     Output: None
    ---------------------------------------------------------------------------*/
    void getUserDetails() {
        userFirebase.getMyAccountDetails();
        new Thread(new Runnable() {

            @Override
            public void run() {
                while (!userFirebase.threadCheck) {
                    try {
                        Thread.sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                userFirebase.threadCheck = false;
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        }).start();
        accountFirebase.status = 0;
    }
}
