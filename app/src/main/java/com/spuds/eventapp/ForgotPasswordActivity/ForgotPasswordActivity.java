package com.spuds.eventapp.ForgotPasswordActivity;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.spuds.eventapp.R;

public class ForgotPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "name_font.ttf");
        TextView header = (TextView)findViewById(R.id.app_name);
        header.setTypeface(custom_font);
    }
}
