package com.example.vichat;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.vichat.Adapters.messagesAdapter;
import com.example.vichat.Models.messageModel;
import com.example.vichat.Models.statusModel;
import com.example.vichat.Models.users;
import com.example.vichat.Adapters.messagesAdapter;
import com.example.vichat.Models.messageModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Messaging extends AppCompatActivity {
    public Toolbar toolbar;
    public TextView mUsername,mEname,mTime,sendMessage,openGallery;
    public CircularImageView friendsImage;
    public Intent intent;
    public String friendsId,message;
    public EditText replyMessage;
    public  FirebaseUser firebaseUser;
    public RecyclerView messagesRecyclerview;
    public messagesAdapter messagesAdapter;
    public  List <messageModel> messageModelList;
    public  List <statusModel> statusModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messaging);
        intent=getIntent();

        toolbar=findViewById(R.id.messagingToolbar);
        mUsername=findViewById(R.id.messaginToolbarUsername);
        mEname=findViewById(R.id.messagingToolbarEname);
        mTime=findViewById(R.id.messagingToolbarTime);
        friendsImage=findViewById(R.id.messagingToolbarImage);
         firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        friendsId=intent.getStringExtra("friendsId");
        replyMessage=findViewById(R.id.replyText);
        replyMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessages();
            }
        });
        sendMessage=findViewById(R.id.sendMessage);
        openGallery=findViewById(R.id.sendImages);
         sendMessage.setOnClickListener(new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                sendMessages();

                                            }
                                        });


         openGallery.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 Intent intent = new Intent(Messaging.this,SendingImages.class);
                 intent.putExtra("friendsId", friendsId);
                 startActivity(intent);


             }
         });
        messagesRecyclerview=findViewById(R.id.messaginRecyclerView);
        messagesRecyclerview.setHasFixedSize(true);
       LinearLayoutManager linearLayoutManager= new LinearLayoutManager(getApplicationContext());
       linearLayoutManager.setStackFromEnd(true);
       messagesRecyclerview.setLayoutManager(linearLayoutManager);
        statusModels=new ArrayList<>();
        messageModelList=new ArrayList<>();







        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Status").child(friendsId);
        reference.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                statusModels.clear();
                statusModel myModel = snapshot.getValue(statusModel.class);
                assert myModel != null;
                if (myModel.getStatusUrl().equals("statusUrl")){
                    friendsImage.setImageResource(R.mipmap.app_icon);
                }
                else {
                    Glide.with(Messaging.this)
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
        readMessages();

    }

    private void readMessages() {
        DatabaseReference messageRef=FirebaseDatabase.getInstance().getReference("Messages");
        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageModelList.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    messageModel myModel=dataSnapshot.getValue(messageModel.class);
                    assert myModel != null;
                    if (myModel.getReceiverid().equals(firebaseUser.getUid()) && myModel.getSenderid().equals(friendsId) ||
                        myModel.getReceiverid().equals(friendsId)   && myModel.getSenderid().equals(firebaseUser.getUid()) ){
                        messageModelList.add(myModel);

                    }
                     messagesAdapter=new messagesAdapter(Messaging.this,messageModelList);
                    messagesRecyclerview.setAdapter(messagesAdapter);


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }

    private void sendMessages() {
        message = replyMessage.getText().toString();

        if (TextUtils.isEmpty(message)) {
            Toast.makeText(Messaging.this,"Please reply message",Toast.LENGTH_LONG).show();
        } else {
            DatabaseReference statusRef = FirebaseDatabase.getInstance().getReference("Status").child(firebaseUser.getUid());
            statusRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    statusModel model = snapshot.getValue(statusModel.class);
                    HashMap<String, Object> hashMap = new HashMap<>();
                    hashMap.put("statusUrl", model.getStatusUrl());
                    hashMap.put("senderid",firebaseUser.getUid());
                    hashMap.put("time",LocalDateTime.now().toString());
                    hashMap.put("receiverid", friendsId);
                    hashMap.put("messages", message);
                    hashMap.put("caption","");
                    hashMap.put("sendImages","");
                    DatabaseReference messagesref = FirebaseDatabase.getInstance().getReference("Messages");
                    messagesref.push().setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            replyMessage.setText("");
                        }
                    });


                }





                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });



        }






            // get Image url and Captions


        }










    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //super.onCreateOptionsMenu(menu);
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.messaging_menu, menu);

        return true;
    }




}