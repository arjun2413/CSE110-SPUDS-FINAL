package com.spuds.eventapp.ResetPassword;

import android.accounts.Account;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.spuds.eventapp.Firebase.AccountFirebase;
import com.spuds.eventapp.Login.LoginActivity;
import com.spuds.eventapp.R;
import com.spuds.eventapp.SignUp.SignUpActivity;

public class ResetPasswordActivity extends AppCompatActivity {

    private EditText input;
    private Button send;
    private TextView errorMessage;
    private int error;
    private String message;
    private TextView[] fun;


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
        setContentView(R.layout.activity_forgot_password);

        //import typefaces
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "name_font.ttf");
        Typeface raleway_light = Typeface.createFromAsset(getAssets(),  "raleway-light.ttf");

        TextView tx = (TextView)findViewById(R.id.app_name);
        tx.setTypeface(custom_font);

        input = (EditText)findViewById(R.id.email);
        send = (Button) findViewById(R.id.send_password);
        errorMessage = (TextView) findViewById(R.id.errorMessage);
        fun = new TextView[1];
        fun[0] = errorMessage;


        //TODO when errormessage is made
        //final TextView errorMessage = (TextView) findViewById(R.id.errorMessage);
        errorMessage.setTypeface(raleway_light);
        input.setTypeface(raleway_light);
        send.setTypeface(raleway_light);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error = 0;
                final AccountFirebase accountFirebase = new AccountFirebase();
                //TODO: move error checking logic to model file.
                //Logic:
                //      1. check if all fields are entered.
                //      2. check if first is an email
                //      3. check if first field exists in the database
                //      5. show snackbar on success or error message accordingly, but ignore number 4
                String message = "";
                if(input.getText().length() > 0){
                    final int[] check = new int[1];
                    check[0] = 0;

                    final String email = input.getText().toString();
                    final String valid = "@ucsd.edu";

                    //TODO: ERROR MESSAGE IMPLEMENTATION

                    //TODO: here we are playing with threads!
                    class myThread2 implements Runnable{
                        public String emailCheck;
                        public String isValid;
                        public AccountFirebase af;

                        public myThread2 (String one, String two, AccountFirebase red){
                            emailCheck = one;
                            isValid = two;
                            af = red;
                        }
                        @Override
                        public void run() {
                            int counter = 0;
                            System.out.println("thread2 start");
                            check[0] = accountFirebase.getThreadCheck();
                            //wait for query
                            while (check[0] == 0) {
                                if (counter > 200) {
                                    error = 4;
                                    break;
                                }
                                try {
                                    System.out.println("I slept for " + counter);
                                    Thread.sleep(100);
                                    counter++;
                                    check[0] = accountFirebase.getThreadCheck();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }
                            if (emailCheck.endsWith(isValid) && error == 0){
                                if (check[0] == 1) {
                                    System.out.println("Checking");
                                    af.resetPass(emailCheck);
                                    startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                                }
                                else {
                                    error = 3;
                                }
                            }
                            System.out.println("int error status is: " + error);
                            System.out.println("thread2 has finished");
                        }
                    }


                    Runnable r2 = new myThread2(email, valid, accountFirebase);
                    Thread cool2 = new Thread(r2);

                    if (email.endsWith(valid)) {
                        accountFirebase.checkEmail(email, fun);
                        cool2.start();
                    }
                    else {
                        error = 2;
                    }

                }
                else {
                    error = 1;
                }

                System.out.println("Error status is: " + error);
                switch(error) {
                    case 1:
                        message = "Fill out the field bruh.";
                        if(errorMessage != null) {
                            errorMessage.setText(message);
                        }
                        break;
                    case 2:
                        message = "Please enter a ucsd.edu email.";
                        if (errorMessage != null) {
                            errorMessage.setText(message);
                        }
                        break;
                    case 3:
                        message = "That email is not signed up.";
                        if (errorMessage != null) {
                            errorMessage.setText(message);
                        }
                        break;
                    case 4:
                        message = "Something went wrong. Please try again.";
                        if (errorMessage != null) {
                            errorMessage.setText(message);
                        }
                        break;
                    default:
                        message = "Please Wait...";
                        if (errorMessage != null) {
                            errorMessage.setText(message);
                        }
                        break;

                }



            }//end of onClick
        });
    }
    /*public void setMessage(TextView text, String message){
        if (text != null) {
            text.setText(message);
        }
    }*/
}
