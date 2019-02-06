package com.example.dell.farmersmarket;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class LoginActivity extends AppCompatActivity {

    private RadioButton role;
    private RadioGroup grole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        grole=findViewById(R.id.cRole);


    }

    public void checkRole(View v){

        switch(v.getId()){
            case R.id.rdFarmer :
            startActivity(new Intent(LoginActivity.this,FarmerActivity.class));
            LoginActivity.this.finish();
            break;

            case R.id.rdAgent :
                startActivity(new Intent(LoginActivity.this,AgentActivity.class));
                LoginActivity.this.finish();
                break;

            case R.id.rdCustomer :
                startActivity(new Intent(LoginActivity.this,CustomerActivity.class));
                LoginActivity.this.finish();
                break;
        }


    }
}
