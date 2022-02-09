package com.example.vichat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.bumptech.glide.Glide;
import com.example.vichat.Models.statusModel;
import com.example.vichat.Models.users;
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
import java.util.HashMap;

public class PostVideo extends AppCompatActivity {
    public TextView openGallery, postTextView,toolbarEname;
    public ImageView myImageView;
    public EditText postED;
    public final int PICK_IMAGE = 1;
    public Uri selectedImageUri;
    public FirebaseAuth mAuth;
    public FirebaseDatabase firebaseDatabase;
    public DatabaseReference databaseReference;
    public ProgressDialog progressDialog;
    public FirebaseUser firebaseUser;
    public FirebaseStorage firebaseStorage;
    public StorageReference storageReference;
    public String downloadedimageurl, videoMessage;
    public Toolbar myToolbar;
    public CircularImageView toobarImage;
    public VideoView videoView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.posting_video);

        openGallery = findViewById(R.id.videoOpenGallery);
        postTextView = findViewById(R.id.videoTextview);

        videoView = findViewById(R.id.videoview);
        postED = findViewById(R.id.videoEd);
        firebaseStorage = FirebaseStorage.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        mAuth = FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        myToolbar = findViewById(R.id.firstToolbar);
        setSupportActionBar(myToolbar);
        toobarImage=findViewById(R.id.toolbarImage);
        toolbarEname=findViewById(R.id.toolbarEname);


        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        postTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                videoMessage=postED.getText().toString();
                if (TextUtils.isEmpty(videoMessage)){
                    Toast.makeText(PostVideo.this,"Please say something about your video",Toast.LENGTH_LONG).show();
                }
                else {
                    submitToStorage();

                }
            }
        });

        openGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pickImage();

            }
        });







        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Status").child(firebaseUser.getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statusModel myModel = snapshot.getValue(statusModel.class);
                assert myModel != null;

                if (myModel.getStatusUrl().equals("statusUrl")){
                    toobarImage.setImageResource(R.mipmap.app_icon);
                }
                else {
                    Glide.with(PostVideo.this)
                            .load(myModel.getUserid())
                            .into(toobarImage);

                }
                toolbarEname.setText(myModel.getEname());


            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }


        });






























    }

    private void submitToStorage() {
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Wait till i complete my work please");
        progressDialog.show();


        StorageReference vidoeRef= FirebaseStorage.getInstance().getReference("Videos").child(firebaseUser.
                getUid()+System.currentTimeMillis());
        vidoeRef.putFile(selectedImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                vidoeRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        DatabaseReference usersRef=FirebaseDatabase.getInstance().getReference("Users").child(firebaseUser.getUid());
                        usersRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                users myUsers=snapshot.getValue(users.class);
                                HashMap<String ,Object> hashMap= new HashMap<>();
                                DatabaseReference statusRef= FirebaseDatabase.getInstance().getReference("Status").child(firebaseUser.getUid());
                                statusRef.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        statusModel model= snapshot.getValue(statusModel.class);
                                        hashMap.put("statusUrl",model.getStatusUrl());
                                        hashMap.put("userid",firebaseUser.getUid());
                                        hashMap.put("username",myUsers.getUsername());
                                        hashMap.put("ename",myUsers.getEname());
                                        hashMap.put("videoUrl",uri.toString());
                                        hashMap.put("videoMessage",videoMessage);
                                        hashMap.put("counter","0");
                                        hashMap.put("lastSeen","lastSeen");
                                        hashMap.put("likeCount","likeCount");

                                        DatabaseReference reference=FirebaseDatabase.getInstance().getReference("Videos");
                                        reference.push().setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()){
                                                    progressDialog.dismiss();
                                                    Toast.makeText(PostVideo.this,"Upload succcess",Toast.LENGTH_LONG).show();
                                                    startActivity(new Intent(PostVideo.this,MainActivity.class));
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

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });



                    }
                });


            }
        });
    }


    public void pickImage() {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(
                Intent.createChooser(intent, "selectImage"), PICK_IMAGE);


    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        assert data != null;
        try {
            selectedImageUri=data.getData();
            videoView.setVisibility(View.VISIBLE);

            Bitmap bitmap=MediaStore.Images.Media.getBitmap(getContentResolver(),selectedImageUri);
            videoView.setVideoURI(selectedImageUri);
            videoView.seekTo(5);
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MediaController mediaController= new MediaController(PostVideo.this);
                    mediaController.setMediaPlayer(videoView);
                    videoView.requestFocus();

                    videoView.start();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }




    }

    ;
}





