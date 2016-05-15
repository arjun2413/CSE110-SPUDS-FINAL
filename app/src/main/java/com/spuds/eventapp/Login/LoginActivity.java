package com.spuds.eventapp.Login;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.spuds.eventapp.Firebase.AccountFirebase;
import com.spuds.eventapp.ForgotPasswordActivity.ForgotPasswordActivity;
import com.spuds.eventapp.R;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.SignUp.SignUpActivity;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "name_font.ttf");
        TextView tx = (TextView)findViewById(R.id.app_name);
        tx.setTypeface(custom_font);
        //a function to allow the user to sign in
        signInFunc();
        signUpFunc();
        forgotPassFunc();
    }


    public void signUpFunc(){
        //create a button for the sign up
        final Button signUp = (Button) findViewById(R.id.button);

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

    public void forgotPassFunc() {
        //create a button for the forgot password
        final Button forgotPass = (Button) findViewById(R.id.forgot_password);
        //set a on click listener to see when the button is clicked
        if (forgotPass != null) {
            forgotPass.setOnClickListener(new View.OnClickListener() {
                //what happens when the button is clicked
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));

                }
            });
        }
    }

    AccountFirebase obj = new AccountFirebase();
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

                    //make sure that something is entered for email and password
                    if (email.equals("") || password.equals("")) {
                        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                        if(errorMessage != null) {
                            String error = "Missing fields. Please try again.";
                            errorMessage.setText(error);
                        }
                    }
                    else if (!email.endsWith("@ucsd.edu")) {
                        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                        String message = "Please use a ucsd.edu email to log in.";
                        if(errorMessage != null) {
                            errorMessage.setText(message);
                        }
                    }
                    //check if email is in database
                /*else if(email.endsWith("@ucsd.edu") ){
                    TextView errorMessage = (TextView)findViewById(R.id.errorMessage);
                    errorMessage.setText("The email you've entered doesn't match any account.
                    Sign up for an account");
                }
                 */
                    //check if password is correct
                /*else if(password){
                    TextView errorMessage = (TextView)findViewById(R.id.errorMessage);
                    errorMessage.setText("The password you've entered is incorrect");
                }
                 */
                    //email and password match and are correct
                    //Switch to the Main Activity
                    //else{
                    //TODO:
                    //Pass through an id of the user [coding decision: should we pass image first and last name?]
                    else{
                        Object time = new Object();
                        Log.v("sadasdasdasd", email);
                        Log.v("asdasdasdasd", password);
                        obj.logIn(email.toString(), password.toString());



                            new Thread(new Runnable() {

                                @Override
                                public void run() {
                                    while (obj.status == 0) {
                                        try {
                                            Thread.sleep(1000);
                                        } catch (InterruptedException e) {
                                            e.printStackTrace();
                                        }

                                    }
                                    if (obj.status == 1) {

                                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    }

                                }
                            }).start();

                            Log.v("asdasdasdasd", String.valueOf(obj.status));

                        Log.v("asdasdasdasd", String.valueOf(obj.status));
obj.status = 0;
                    }
                }
            });

        }
    }
    public String getEmail(){
        EditText email = (EditText) findViewById(R.id.email);
        if(email != null) {
            return email.getText().toString();
        }
        return "";
    }
    public String getPassword() {
        EditText password = (EditText) findViewById(R.id.password);
        if(password != null) {
            return password.getText().toString();
        }
        return "";
    }


}