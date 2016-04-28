package com.spuds.eventapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class change_password extends AppCompatActivity {

    private EditText current_pw;
    private EditText new_pw;
    private EditText confirm_pw;
    private Button change_pw;
    private TextView sys_message;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        current_pw = (EditText) findViewById(R.id.current_password);
        new_pw = (EditText) findViewById(R.id.new_password);
        confirm_pw = (EditText) findViewById(R.id.confirm_password);
        change_pw = (Button) findViewById(R.id.change_password);
        sys_message = (TextView) findViewById(R.id.system_message);

        change_pw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((current_pw.getText().length() > 0) && (new_pw.getText().length() >
                        0) && (confirm_pw.getText().length() > 0)) {
                    boolean is_error = false;
                    String user_pw = "a";
                    String first_pw = current_pw.getText().toString();
                    String second_pw = new_pw.getText().toString();
                    String third_pw = confirm_pw.getText().toString();
                    String message = "";
                    if(!(first_pw.equals(user_pw))){
                        String wrong_user_pw = "Error 1: incorrect current password";
                        message = message + wrong_user_pw;
                        is_error = true;
                    }
                    if(!(second_pw.equals(third_pw))){
                        String different_pw = "Error 2: new passwords do not match";
                        if(is_error){
                            message = message + "\n";
                        }
                        message = message + different_pw;
                        is_error = true;
                    }
                    if(!is_error) {
                        message = "Password Changed";
                    }
                    sys_message.setText(message);
                } else {
                    String incomplete_input = "Error 3: not all fields complete";
                    sys_message.setText(incomplete_input);
                }
            }
        });


    }
}
