package com.example.dell.farmersmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class CustomerActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer);


        Intent intent2=getIntent();
        username = intent2.getStringExtra("role2");

        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbarcust);
        toolbar.setTitle(username);


    }
}
