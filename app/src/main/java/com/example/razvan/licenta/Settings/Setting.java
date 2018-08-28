package com.example.razvan.licenta.Settings;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.example.razvan.licenta.Login.LoginActivity;
import com.example.razvan.licenta.R;

public class Setting extends Activity {
    RelativeLayout relativeLayout;

    @Override
    public void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);

        Button btnBack = (Button)findViewById(R.id.btn_settings_back);
        Button editExpense = (Button)findViewById(R.id.btn_settings_editExp);
        Button addCategory = (Button)findViewById(R.id.btn_settings_addCat);
        relativeLayout = (RelativeLayout) findViewById(R.id.content_main);
        final EditText editText = (EditText)findViewById(R.id.editText_settings);

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        editExpense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
                Intent i = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(i);
            }
        });

        addCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(),CategoryAdd.class);
                startActivity(i);
            }
        });


    }
}

