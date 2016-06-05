package com.spuds.eventapp.ResetPassword;

import android.content.Intent;
import android.graphics.Typeface;
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

/*---------------------------------------------------------------------------
   Class Name: ResetPasswordActivity
   Description: Contains all methods that contribute to the reset
            password functionality
---------------------------------------------------------------------------*/
public class ResetPasswordActivity extends AppCompatActivity {

    private EditText input;
    private Button send;
    private TextView errorMessage;
    private int error;
    private String message;
    private TextView[] textArray;


    /*---------------------------------------------------------------------------
     Function Name: onCreate
     Description: Sets up the reset password page
     Input: Bundle savedInstanceState -
     Output: None
    ---------------------------------------------------------------------------*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        Firebase.setAndroidContext(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        //import typefaces
        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "name_font.ttf");
        Typeface raleway_light = Typeface.createFromAsset(getAssets(),  "raleway-light.ttf");

        TextView tx = (TextView)findViewById(R.id.app_name);
        tx.setTypeface(custom_font);

        //xml elements
        input = (EditText)findViewById(R.id.email);
        send = (Button) findViewById(R.id.send_password);
        errorMessage = (TextView) findViewById(R.id.errorMessage);

        //errormessage array to use in checkEmail
        textArray = new TextView[1];
        textArray[0] = errorMessage;

        errorMessage.setTypeface(raleway_light);
        input.setTypeface(raleway_light);
        send.setTypeface(raleway_light);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                error = 0;
                final AccountFirebase accountFirebase = new AccountFirebase();
                //Logic:
                //      1. check if all fields are entered.
                //      2. check if email ends in @ucsd.edu
                //      3. check if email exists in database
                //      4. if email exists, call Firebase's resetPassword, which sends email
                String message = "";
                if(input.getText().length() > 0){
                    final int[] check = new int[1];
                    check[0] = 0;

                    final String email = input.getText().toString();
                    final String valid = "@ucsd.edu";

                    //here we are playing with threads!
                    class myThread2 implements Runnable{
                        public static final int TIMEDOUT = 4;
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
                            check[0] = accountFirebase.getThreadCheck();
                            //wait for query
                            while (check[0] == 0) {
                                if (counter > 100) {
                                    error = TIMEDOUT;
                                    break;
                                }
                                try {
                                    Thread.sleep(100);
                                    counter++;
                                    check[0] = accountFirebase.getThreadCheck();

                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                            if (emailCheck.endsWith(isValid) && error == 0){
                                if (check[0] == 1) {    //success, email exists
                                    af.resetPass(emailCheck);
                                    startActivity(new Intent(ResetPasswordActivity.this, LoginActivity.class));
                                }
                                else {
                                    error = 3;
                                }
                            }
                            else if (error == TIMEDOUT) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        String message = "Something went wrong. Possible network error.";
                                        errorMessage.setText(message);
                                    }
                                });
                            }
                        }
                    }

                    Runnable r2 = new myThread2(email, valid, accountFirebase);
                    Thread cool2 = new Thread(r2);

                    if (email.endsWith(valid)) {
                        accountFirebase.checkEmail(email, textArray, "That email is not signed up.", false);
                        cool2.start();
                    }
                    else {
                        error = 2;
                    }
                }
                else {
                    error = 1;
                }

                switch(error) {
                    case 1: //not all fields filled
                        if(errorMessage != null) {
                            errorMessage.setText(getString(R.string.errorEmptyFields));
                        }
                        break;
                    case 2: //invalid email
                        if (errorMessage != null) {
                            errorMessage.setText(getString(R.string.errorInvalidEmail));
                        }
                        break;
                    case 3: //email not found
                        if (errorMessage != null) {
                            errorMessage.setText(getString(R.string.errorNoEmail));
                        }
                        break;
                    case 4: //timed out error
                        if (errorMessage != null) {
                            errorMessage.setText(getString(R.string.errorFatal));
                        }
                        break;
                    default:    //this will show if query is not fast enough
                        message = getString(R.string.loading);
                        if (errorMessage != null) {
                            errorMessage.setText(message);
                        }
                        break;
                }
            }//end of onClick
        });
    }
}
