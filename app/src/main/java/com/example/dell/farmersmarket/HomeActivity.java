package com.example.dell.farmersmarket;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;


public class HomeActivity extends AppCompatActivity {

    private EditText name, phone, email;
    private Spinner role;
    private Button submit;
    AwesomeValidation awesomeValidation;
    private FirebaseAuth mAuth;
    private EditText otp;
    ArrayAdapter<String> myadapter;
    String names[] = {"Select Role", "Farmer", "Agent", "Customer"};
    String urole = "",mVerificationId;
    private DatabaseReference databaseUser;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAuth = FirebaseAuth.getInstance();

        databaseUser = FirebaseDatabase.getInstance().getReference("user");

        awesomeValidation = new AwesomeValidation(ValidationStyle.BASIC);

        name = (EditText) findViewById(R.id.etName);
        phone = (EditText) findViewById(R.id.etPhone);
        email = (EditText) findViewById(R.id.etEmail);
        role = (Spinner) findViewById(R.id.Spin1);
        submit = (Button) findViewById(R.id.btnSubmit);
        otp = (EditText) findViewById(R.id.etOTP);
        toolbar = (Toolbar)findViewById(R.id.toolbar1);

        toolbar.setTitle("Farmer's Market");
        //toolbar.setTitleTextColor(0);

        myadapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, names);
        role.setAdapter(myadapter);


        role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;
                    case 1:
                        urole = "Farmer";
                        break;
                    case 2:
                        urole = "Agent";
                        break;
                    case 3:
                        urole = "Customer";
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Toast.makeText(HomeActivity.this, "Please select your role", Toast.LENGTH_SHORT).show();

            }
        });

       /* Spinner spinner = (Spinner)findViewById(R.id.Spin1);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Role,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);*/

        //String regexPassword = "(?=.*[a-z])(?=.*[A-Z])(?=.*[\\d])(?=.*[~`!@#\\$%\\^&\\*\\(\\)\\-_\\+=\\{\\}\\[\\]\\|\\;:\"<>,./\\?]).{8,}";
        awesomeValidation.addValidation(HomeActivity.this, R.id.etName, "[a-zA-Z\\s]+", R.string.nameerr);
        awesomeValidation.addValidation(HomeActivity.this, R.id.etPhone, "(^(?:(?:\\+|0{0,2})91(\\s*[\\ -]\\s*)?|[0]?)?[789]\\d{9}|(\\d[ -]?){10}\\d$)", R.string.phoneerr);
        awesomeValidation.addValidation(HomeActivity.this, R.id.etEmail, android.util.Patterns.EMAIL_ADDRESS, R.string.emailerr);
        // awesomeValidation.addValidation(HomeActivity.this,R.id.pass,regexPassword,R.string.passerr);
        //awesomeValidation.addValidation(HomeActivity.this,R.id.cnfpass,R.id.pass,R.string.cnfpasserr);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ph = phone.getText().toString();

                if (registrationValidate()) {
                    if (awesomeValidation.validate()) {
                        Toast.makeText(HomeActivity.this, "Verification code is sent to your phone", Toast.LENGTH_SHORT).show();
                        otp.setVisibility(View.VISIBLE);
                        sendVerificationCode(ph);
                        submit.setText("Verify Code");

                        findViewById(R.id.btnSubmit).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String code = otp.getText().toString().trim();
                                if (code.isEmpty() || code.length() < 6) {
                                    otp.setError("Enter valid code");
                                    otp.requestFocus();
                                    return;
                                }

                                //verifying the code entered manually
                                verifyVerificationCode(code);
                            }
                        });




                    }
                }
            }
        });

    }


    public boolean registrationValidate() {
        boolean result = true;
        String uname = name.getText().toString();
        String uphone = phone.getText().toString();
        String uemail = email.getText().toString();
        String Userrole = urole;

        if (uname.isEmpty()) {
            name.setError("Please enter name");
            result = false;
        } else if (uname.length() < 3) {
            name.setError("Please enter valid name");
            result = false;
        }
        if (uphone.isEmpty()) {
            phone.setError("Please enter Phone number");
            result = false;
        }
        if (uemail.isEmpty()) {
            email.setError("Please enter email");
            result = false;
        }
        if (Userrole != "Farmer" && Userrole != "Agent" && Userrole != "Customer") {
            Toast.makeText(this, "Please select your Role", Toast.LENGTH_SHORT).show();
            result = false;
        }
        return result;
    }


    private void sendVerificationCode(String mobile) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+91" + mobile,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallbacks);
    }


    //the callback to detect the verification status
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

            //Getting the code sent by SMS
            String code = phoneAuthCredential.getSmsCode();

            //sometime the code is not detected automatically
            //in this case the code will be null
            //so user has to manually enter the code
            if (code != null) {
                otp.setText(code);
                //verifying the code
                verifyVerificationCode(code);
            }
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(HomeActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);

            //storing the verification id that is sent to the user
            mVerificationId = s;
        }
    };


    private void verifyVerificationCode(String code) {
        //creating the credential
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId, code);

        //signing the user
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(HomeActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            //verification successful we will start the profile activity
                            String id = databaseUser.push().getKey();
                            String username = name.getText().toString();
                            String userphone = phone.getText().toString();
                            String useremail = email.getText().toString();
                            String userrole = urole;

                            User user = new User(id,username,userphone,useremail,userrole);

                            databaseUser.child(id).setValue(user);

                            switch(userrole){
                                case "Farmer":Intent intent = new Intent(HomeActivity.this, FarmerActivity.class);
                                                 intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                 intent.putExtra("role",username);
                                                 startActivity(intent);
                                                 break;

                                case "Agent":Intent intent1 = new Intent(HomeActivity.this,AgentActivity.class);
                                             intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                             intent1.putExtra("role1",username);
                                             startActivity(intent1);
                                             break;

                                case "Customer":Intent intent2 = new Intent(HomeActivity.this, CustomerActivity.class);
                                                intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                intent2.putExtra("role2",username);
                                                startActivity(intent2);
                                                break;
                            }


                        } else {

                            //verification unsuccessful.. display an error message

                            String message = "Somthing is wrong, we will fix it soon...";

                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                message = "Invalid code entered...";
                            }

                            Snackbar snackbar = Snackbar.make(findViewById(R.id.parent), message, Snackbar.LENGTH_LONG);
                            snackbar.setAction("Dismiss", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                }
                            });
                            snackbar.show();
                        }
                    }
                });
    }



}