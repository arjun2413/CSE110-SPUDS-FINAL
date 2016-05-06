package com.spuds.eventapp.ResetPassword;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.spuds.eventapp.R;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText input;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        input = (EditText)findViewById(R.id.email);
        send = (Button) findViewById(R.id.send_password);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: move error checking logic to model file.
                //Logic:
                //      1. check if all fields are entered.
                //      2. check if first is an email
                //      3. check if first field exists in the database
                //      5. show snackbar on success or error message accordingly.
                String message = "";
                if(input.getText().length() > 0){
                    int end = input.getText().length();
                    int eduCheck = end -9;
                    //Checks if end of string is "@ucsd.edu"
                    //NOTE: this does not properly check if email is from ucsd.
                    String emailCheck = (String)input.getText().subSequence( eduCheck,end);
                    String valid = "@ucsd.edu";
                    if(!emailCheck.equals(valid)){
                        message = "Please enter a valid @ucsd.edu email";
                    }
                    else{
                        //TODO: check database
                        if(true){
                            //Email is a valid email
                            //have firebase send email.

                        }
                        else{
                            message = "Email not found";
                        }
                    }

                }
                else{
                    message = "Please fill all fields";
                }

                //TODO: display the message of success/failure somehow
            }
        });
    }
}
