package com.spuds.eventapp.Login;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.spuds.eventapp.MainActivity;
import com.spuds.eventapp.R;

public class LoginActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //a function to allow the user to sign in
        signInFunc();

    }

    public void signInFunc(){
        //create a button for the sign in
        final Button signIn = (Button) findViewById(R.id.signIn);
        //set a on click listener to see when the button is clicked
        signIn.setOnClickListener(new View.OnClickListener(){
            //what happens when the button is clicked
            public void onClick(View v){
                //get the user input for email field
                String email = getEmail();
                //get user input for password field
                String password = getPassword();

                //make sure that something is entered for email and password
                if(email == "" || password == "" ){
                    TextView errorMessage = (TextView)findViewById(R.id.errorMessage);
                    errorMessage.setText("Missing fields. Please try again.");
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
                //else{
                    //TODO:
                    //Pass through an id of the user [coding decision: should we pass image first and last name?]
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                //}
            }
        });
    }
    public String getEmail(){
        EditText email = (EditText) findViewById(R.id.email);
        return email.getText().toString();
    }
    public String getPassword() {
        EditText password = (EditText) findViewById(R.id.password);
        return password.getText().toString();
    }


}
