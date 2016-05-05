package com.spuds.eventapp.ResetPassword;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.spuds.eventapp.R;

public class ResetPasswordActivity extends AppCompatActivity {
    private EditText emailTextField;
    private Button getPassButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        getPageElements();
        setupWindow();
    }

    protected void getPageElements() {
        emailTextField = (EditText) findViewById(R.id.email);
        getPassButton = (Button) findViewById(R.id.sign_in);
    }

    protected void setupWindow() {
        getPassButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailString = emailTextField.getText().toString();

                if (isValidEmail(emailString)) {
                    // TODO send emailString to database to see if it exists
                    // if true, call sendEmail()
                    // else, say email not found in database

                    // TODO below only if true in this block
                    sendEmail(emailString);
                }
                else {
                    // TODO return invalid email error
                }
            }
        });
    }

    // TODO set up sending email and page for user to reset password
    // http://stackoverflow.com/questions/28546703/how-to-code-using-android-studio-to-send-an-email
    protected void sendEmail(String email) {
        String[] TO = {email};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Eventory Password Reset");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Hi! Click this link to reset your password");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
        } catch (android.content.ActivityNotFoundException ex) {
            // TODO can't connect and need error message
            /*
            Toast.makeText(MainActivity.this,
                    "There is no email client installed.", Toast.LENGTH_SHORT).show();
            */
        }
    }

    // determines if a string contains the @ucsd.edu extension
    private boolean isValidEmail(String email) {
        if (!email.toLowerCase().contains("@ucsd.edu")) {
            return false;
        }
        return true;
    }


}