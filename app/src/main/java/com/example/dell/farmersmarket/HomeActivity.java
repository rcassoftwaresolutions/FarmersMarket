package com.example.dell.farmersmarket;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.RegexTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class HomeActivity extends AppCompatActivity  {

    private EditText name,phone,email;
    private Spinner role;
    private Button submit;
    AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;
    ArrayAdapter<String> myadapter;
    String names[]={"Select Role","Farmer","Agent","Customer"};
    String urole="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        name = (EditText)findViewById(R.id.etName);
        phone = (EditText)findViewById(R.id.etPhone);
        email = (EditText)findViewById(R.id.etEmail);
        role = (Spinner)findViewById(R.id.Spin1);
        submit = (Button)findViewById(R.id.btnSubmit);
        myadapter=new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,names);
        role.setAdapter(myadapter);


        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch(position){
                    case 0:break;
                    case 1:urole="Farmer";break;
                    case 2:urole="Agent";break;
                    case 3:urole="Customer";break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(HomeActivity.this,"Please select your role",Toast.LENGTH_SHORT).show();

            }
        });

       /* Spinner spinner = (Spinner)findViewById(R.id.Spin1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Role,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);*/

        //String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(HomeActivity.this,R.id.etName, "[a-zA-Z\\s]+",R.string.nameerr);
        awesomeValidation.addValidation(HomeActivity.this,R.id.etPhone, "(^(?:(?:\\+|0{0,2})91(\\s*[\\ -]\\s*)?|[0]?)?[789]\\d{9}|(\\d[ -]?){10}\\d$)",R.string.phoneerr);
        awesomeValidation.addValidation(HomeActivity.this,R.id.etEmail,android.util.Patterns.EMAIL_ADDRESS,R.string.emailerr);
       // awesomeValidation.addValidation(HomeActivity.this,R.id.pass,regexPassword,R.string.passerr);
        //awesomeValidation.addValidation(HomeActivity.this,R.id.cnfpass,R.id.pass,R.string.cnfpasserr);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ph=phone.getText().toString();

                if(registrationValidate()) {
                    if (awesomeValidation.validate()) {
                        Toast.makeText(HomeActivity.this,"Verification code is sent to your phone",Toast.LENGTH_SHORT).show();
                        Intent intent=new Intent(HomeActivity.this, OTPActivity.class);
                        intent.putExtra("mobile", ph);
                        startActivity(intent);
                        HomeActivity.this.finish();
                    }
                }
            }
        });

    }


    public boolean registrationValidate(){
        boolean result=true;
        String uname = name.getText().toString();
        String uphone = phone.getText().toString();
        String uemail = email.getText().toString();
        String Userrole=urole;

        if(uname.isEmpty()){
            name.setError("Please enter name");
            result = false;
        }
        else if(uname.length()<3)
        {
            name.setError("Please enter valid name");
            result = false;
        }
        if(uphone.isEmpty()){
            phone.setError("Please enter Phone number");
            result = false;
        }
        if(uemail.isEmpty()){
            email.setError("Please enter email");
            result = false;
        }
        if(Userrole!="Farmer" && Userrole!="Agent" && Userrole!="Customer")
        {
            Toast.makeText(this,"Please select your Role",Toast.LENGTH_SHORT).show();
            result=false;
        }
        return result;
    }




}