package com.example.vichat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vichat.Models.statusModel;
import com.example.vichat.Models.statusModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;

public class SendingImages extends AppCompatActivity {
    private static final int PICK_IMAGE =1 ;
    public Toolbar toolbar;
    public TextView mUsername,mEname,mTime,sendImage,openGallery;
    public CircularImageView friendsImage;
    public Intent intent;
    public String friendsId,caption;
    public EditText captionEd;
    public ImageView showImage;
    public  Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sending_images);
        toolbar=findViewById(R.id.messagingToolbar);
        mUsername=findViewById(R.id.messaginToolbarUsername);
        mEname=findViewById(R.id.messagingToolbarEname);
        mTime=findViewById(R.id.messagingToolbarTime);
        openGallery=findViewById(R.id.sOpenGallery);
        showImage=findViewById(R.id.sImageview);
        sendImage=findViewById(R.id.sendTextView);
        captionEd=findViewById(R.id.caption);


        friendsImage=findViewById(R.id.messagingToolbarImage);
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        intent=getIntent();
        friendsId=intent.getStringExtra("friendsId");
        toolbar=findViewById(R.id.messagingToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setHomeButtonEnabled(true);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Status").child(friendsId);
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statusModel myModel = snapshot.getValue(statusModel.class);
                assert myModel != null;
                if (myModel.getStatusUrl().equals("statusUrl")){
                    friendsImage.setImageResource(R.mipmap.app_icon);
                }
                else {
                    Glide.with(SendingImages.this)
                            .load(myModel.getStatusUrl())
                            .into(friendsImage);

                }

                mEname.setText(myModel.getEname());
                mUsername.setText(myModel.getUsername());
                mTime.setText(LocalDateTime.now().toString());


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });
        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "selectImage"), PICK_IMAGE);

            }
        });
        sendImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProgressDialog progressDialog = new ProgressDialog(SendingImages.this);
                progressDialog.setTitle("Sending...");
                progressDialog.setMessage("Wait till i complete my work please");
                progressDialog.show();
                // submit to storage
                caption=captionEd.getText().toString();

                StorageReference storageReference= FirebaseStorage.getInstance().getReference("Images").child(firebaseUser.getUid()+System.currentTimeMillis());
                storageReference.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri downloadeduri) {
                                DatabaseReference statusRef=FirebaseDatabase.getInstance().getReference("Status").child(firebaseUser.getUid());
                                statusRef.addValueEventListener(new ValueEventListener() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        statusModel model= snapshot.getValue(statusModel.class);
                                        HashMap <String,Object> hashMap= new HashMap<>();
                                        hashMap.put("statusUrl",model.getStatusUrl());
                                        hashMap.put("sendImages", downloadeduri.toString());
                                        hashMap.put("senderid",firebaseUser.getUid());
                                        hashMap.put("receiverid",friendsId);
                                        hashMap.put("caption",caption);
                                        hashMap.put("messages","");
                                        hashMap.put("time",LocalDateTime.now().toString());
                                        DatabaseReference messagesRef=FirebaseDatabase.getInstance().getReference("Messages");
                                        messagesRef.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    Toast.makeText(SendingImages.this,"Sent successfully",Toast.LENGTH_LONG).show();
                                                    finish();
                                                }

                                            }
                                        });





                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });

                                    }
                                });


                            }
                        });


                    }
                });





            }







    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        try {
             selectedImageUri = data.getData();
            Bitmap bitmap= MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
            showImage.setImageBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }




    }


}