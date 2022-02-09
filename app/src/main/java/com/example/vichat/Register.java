package com.example.vichat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class Register extends AppCompatActivity {
    public TextView submit;
    public EditText usernameEd,enameEd,passwordEd,confirmPassEd;
    public  String username,ename,password,confirmPassword;
    public FirebaseAuth firebaseAuth;
    public FirebaseUser firebaseUser;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        usernameEd=findViewById(R.id.regUsername);
        enameEd=findViewById(R.id.regEname);
        passwordEd=findViewById(R.id.regPassword);
        confirmPassEd=findViewById(R.id.regConpassword);


        //databaseReference=firebaseDatabase.getInstance().getReference("Users");


        submit=findViewById(R.id.submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Toast.makeText(getApplicationContext(),"at least  8 characters",Toast.LENGTH_LONG).show();

             validateCredentilas();
            }
        });
    }

    public void validateCredentilas() {
        username=usernameEd.getText().toString();
        ename=enameEd.getText().toString();
        password=passwordEd.getText().toString();
        confirmPassword=confirmPassEd.getText().toString();
       // Toast.makeText(getApplicationContext(),"at least  8 characters",Toast.LENGTH_LONG).show();
        // validate the credentials before submitting to database



        if (username.isEmpty() || ename.isEmpty() || password.isEmpty()
        || confirmPassword.isEmpty()){
            Toast.makeText(Register.this,"Please fill all fields",Toast.LENGTH_LONG).show();
        }
        else  if(password.length()<6 ){
            Toast.makeText(Register.this,"at least  8 characters",Toast.LENGTH_LONG).show();


        }
        else {
            // submit details to firebase


            ProgressDialog progressDialog  =new ProgressDialog(this);
            progressDialog.setTitle("Submitting...");
            progressDialog.setMessage("Please wait till i finish registration");
            progressDialog.show();
            firebaseAuth=FirebaseAuth.getInstance();

            firebaseAuth.createUserWithEmailAndPassword(ename,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        firebaseUser=firebaseAuth.getCurrentUser();
                        firebaseDatabase =FirebaseDatabase.getInstance();
                        HashMap <String ,String> map= new HashMap<>();
                        map.put("userid",firebaseUser.getUid());
                        map.put("username",username);
                        map.put("ename",ename);
                        map.put("password",password);
                        DatabaseReference Reference=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        Reference.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    HashMap <String ,String> myMap=new HashMap<>();
                                    myMap.put("username",username);
                                    myMap.put("ename",ename);
                                    myMap.put("userid",firebaseUser.getUid());
                                    myMap.put("statusUrl","statusUrl");
                                    DatabaseReference statusRef=FirebaseDatabase.getInstance().getReference("Status").child(firebaseUser.getUid());
                                    statusRef.setValue(myMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()){
                                                DatabaseReference videosRef=FirebaseDatabase.getInstance().getReference("Videos").child(firebaseUser.getUid());
                                                HashMap <String ,String> map=new HashMap<>();
                                                map.put("username",username);
                                                map.put("ename",ename);
                                                map.put("userid",firebaseUser.getUid());
                                                map.put("statusUrl","statusUrl");
                                                map.put("lastSeen","lastSeen");
                                                map.put("counter","counter");
                                                map.put("videoMessage","videoMessage");
                                                map.put("videoUrl","videoUrl");
                                                map.put("likeCount","likeCount");
                                                videosRef.setValue(map).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        progressDialog.dismiss();
                                                        Toast.makeText(getApplicationContext(),"Register success",Toast.LENGTH_LONG).show();
                                                        startActivity(new Intent(Register.this,MainActivity.class));

                                                    }
                                                });




                                                progressDialog.dismiss();
                                                Toast.makeText(getApplicationContext(),"Register success",Toast.LENGTH_LONG).show();
                                                startActivity(new Intent(Register.this,MainActivity.class));

                                            }

                                        }
                                    });



                                }






























                            }
                        });
                    }
                    else {
                        progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), "Upload fail check your connection", Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }




    }

}