package com.example.dell.farmersmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

public class AgentActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agent);

        Intent intent1=getIntent();
        username = intent1.getStringExtra("role1");

        toolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.toolbaragent);
        toolbar.setTitle(username);


    }
}
