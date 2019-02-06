package com.example.dell.farmersmarket;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private  int SPLASH_TIME_OUT = 5;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        mAuth = FirebaseAuth.getInstance();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();
        logoLaunch logo = new logoLaunch();
        logo.start();

    }
    private class logoLaunch extends Thread {
        public void run() {
            try {
                sleep(1000*SPLASH_TIME_OUT);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

          Intent intent = (new Intent(MainActivity.this, HomeActivity.class));
            startActivity(intent);
            MainActivity.this.finish();

        }
    }
    //@Override
   /* public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser==null){
            startActivity(new Intent(MainActivity.this,HomeActivity.class));
            finish();
        }
        //updateUI(currentUser);
    }*/

}


