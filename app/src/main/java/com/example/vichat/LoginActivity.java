package com.example.vichat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText enameEd,passwordEd;
    public TextView loginTextview,openRegister;
    public  String ename,password;
    public FirebaseAuth mAuth;
    public FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();

        initializeWidgets();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        if(firebaseUser!=null){
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
        }
    }



    public void initializeWidgets() {

        enameEd=findViewById(R.id.enamelogin);
        passwordEd=findViewById(R.id.passwordlogin);
        loginTextview=findViewById(R.id.login);
        openRegister=findViewById(R.id.openRegister);
        mAuth=FirebaseAuth.getInstance();
        openRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(LoginActivity.this,Register.class));
                //login out current users to erase credentials

            }
        });
        loginTextview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validatecredentials();
            }
        });
    }

    public void validatecredentials() {
        ename=enameEd.getText().toString();
        password=passwordEd.getText().toString();
        if(ename.isEmpty() || password.isEmpty()){
            Toast.makeText(getApplicationContext(),"Please fill All fields",Toast.LENGTH_LONG).show();
        }
        else {
            mAuth.signInWithEmailAndPassword(ename,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        enameEd.setText("");
                        passwordEd.setText("");
                        startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    }
                    else {
                        Toast.makeText(getApplicationContext(),"Login failed",Toast.LENGTH_LONG).show();
                    }

                }
            });

        }
    }
}