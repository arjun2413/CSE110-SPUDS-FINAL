package com.spuds.eventapp.ResetPassword;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.spuds.eventapp.Firebase.AccountFirebase;
import com.spuds.eventapp.Login.LoginActivity;
import com.spuds.eventapp.R;
import com.spuds.eventapp.SignUp.SignUpActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText input;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        input = (EditText)findViewById(R.id.email);
        send = (Button) findViewById(R.id.send_password);
        //TODO when errormessage is made
        //errorMessage = (TextView) findViewById(R.id.errorMessage);


        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountFirebase accountFirebase = new AccountFirebase();
                //TODO: move error checking logic to model file.
                //Logic:
                //      1. check if all fields are entered.
                //      2. check if first is an email
                //      3. check if first field exists in the database
                //      5. show snackbar on success or error message accordingly, but ignore number 4
                String message = "";
                if(input.getText().length() > 0){
                    String email = input.getText().toString();
                    String valid = "@ucsd.edu";
                    //TODO: ERROR MESSAGE IMPLEMENTATION
                    if(!email.endsWith(valid)){
                        message = "Please enter a valid @ucsd.edu email";
                    }
                    else{
                        //TODO: check database
                        accountFirebase.checkEmail(email);

                        if(true){
                            //Email is a valid email
                            //have firebase send email.
                            accountFirebase.resetPass(input.getText().toString());
                            startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                        }
                        else{
                            message = "That email was not found";
                        }
                    }

                }
                else{
                    message = "Please fill in all fields";
                }

                //TODO: display the message of success/failure somehow
            }
        });
    }
}
