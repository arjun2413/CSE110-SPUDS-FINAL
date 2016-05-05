package com.spuds.eventapp.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spuds.eventapp.ResetPassword.ResetPasswordActivity;
import com.spuds.eventapp.Shared.MainActivity;
import com.spuds.eventapp.R;
import com.spuds.eventapp.SignUp.SignUpActivity;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //a function to allow the user to sign in
        signInFunc();
        //a function for forget password use case
        forgotPassFunc();
        //a function for creating an account
        signUpFunc();

    }

    public void signInFunc() {
        //create a button for the sign in
        final Button signIn = (Button) findViewById(R.id.signIn);
        if(signIn != null) {
            //set a on click listener to see when the button is clicked
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
                        String message = "Missing fields. Please try again";
                        if(errorMessage != null) {
                            errorMessage.setText(message);
                        }
                    }
                    //check if valid ucsd.edu email
                    else if (!email.endsWith("@ucsd.edu")) {
                        TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
                        String message = "Please use a ucsd.edu email to log in.";
                        if(errorMessage != null) {
                            errorMessage.setText(message);
                        }
                    }
                    //check if email is in database
               /*else if(email ){
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
                    else{
                        //TODO:
                        //Pass through an id of the user [coding decision: should we pass image first and last name?]
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }
                }
            });
        }
    }

    public String getEmail() {
        EditText email = (EditText) findViewById(R.id.email);
        if (email == null) {
            return "";
        }
        return email.getText().toString();
    }

    public String getPassword() {
        EditText password = (EditText) findViewById(R.id.password);
        if (password == null) {
            return "";
        }
        return password.getText().toString();
    }

    public void forgotPassFunc() {
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

    public void signUpFunc() {
        //create a button for the sign in
        final Button signUp = (Button) findViewById(R.id.signup_button);
        //set a on click listener to see when the button is clicked
        if (signUp != null) {
            signUp.setOnClickListener(new View.OnClickListener() {
                //what happens when the button is clicked
                public void onClick(View v) {
                    startActivity(new Intent(LoginActivity.this, ResetPasswordActivity.class));

                }
            });
        }
    }
}
