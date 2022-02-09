package com.example.vichat.Adapters;


import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import com.example.vichat.LoginActivity;
import com.example.vichat.Messaging;
import com.example.vichat.Models.videosModel;
import com.example.vichat.PostVideo;
import com.example.vichat.R;
import com.example.vichat.SendingImages;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.like.LikeButton;
import com.like.OnLikeListener;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.HashMap;
import java.util.List;

public class videoAdapter extends RecyclerView.Adapter<videoAdapter.MyViewHolder> {


    private final List<videosModel>  videosModels;
    private DatabaseReference mUserReference;
    public String friendsId;
    public  static int counter=0;

    private final Context mContext;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;

    public videoAdapter(Context context, List<videosModel> models) {
        this.mContext = context;
        this.videosModels = models;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_items, parent, false);
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        return new MyViewHolder(view);

    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        videosModel model = videosModels.get(position);
        holder.usernameTv.setText(model.getUsername());
        holder.enameTv.setText(model.getEname());
        if (model.getStatusUrl().equals("statusUrl")){
            holder.accountimage.setImageResource(R.mipmap.app_icon);

        }
        else {
            Glide.with(mContext).load(model.getStatusUrl()).into(holder.accountimage);
        }

         if (!model.getVideoUrl().equals("videoUrl")){
             holder.videoMessage.setText(model.getVideoMessage());
             holder.videoView.setVideoURI(Uri.parse(model.getVideoUrl()));
             MediaController mediaController= new MediaController(mContext);
             mediaController.setMediaPlayer(holder.videoView);
             holder.videoView.requestFocus();
             holder.videoView.seekTo(2);
             holder.videoView.start();


         }
         holder.videoView.seekTo(2);
         holder.videoView.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 holder.videoView.start();

             }
         });

        



        holder.likeButton.setOnLikeListener(new OnLikeListener() {
            int counter=0;


            @Override
            public void liked(LikeButton likeButton) {
                //MediaPlayer mediaPlayer = MediaPlayer.create(mContext, R.raw.like_button);
               // mediaPlayer.start();
                DatabaseReference postsref=FirebaseDatabase.getInstance().getReference("Videos");
                Query query=postsref.orderByChild("videoUrl").equalTo(model.getVideoUrl());
               query.addListenerForSingleValueEvent(new ValueEventListener() {
                   @Override
                   public void onDataChange(@NonNull DataSnapshot snapshot) {
                       for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                           videosModel checkCounter=dataSnapshot.getValue(videosModel.class);
                           counter++;
                           assert checkCounter != null;
                               HashMap <String ,Object> hashMap= new HashMap<>();
                               hashMap.put("likeCount",counter);
                               dataSnapshot.getRef().updateChildren(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                   @Override
                                   public void onSuccess(Void unused) {
                                       holder.likeCount.setText(checkCounter.getLikeCount());
                                   }
                               });






                           }




                       }



                   @Override
                   public void onCancelled(@NonNull DatabaseError error) {

                   }
               });


            }


            @Override
            public void unLiked(LikeButton likeButton) {

            }
        });
        holder.likeCount.setText(model.getLikeCount());



        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        assert firebaseUser != null;
        if (firebaseUser.getUid().equals(model.getUserid())){
            holder.openMessaging.setVisibility(View.INVISIBLE);
        }
        else {
            holder.openMessaging.setVisibility(View.VISIBLE);
        }



        holder.openMessaging.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, Messaging.class);
                intent.putExtra("friendsId", model.getUserid());
                mContext.startActivity(intent);


            }

        });


    }






















    @Override
    public int getItemCount() {
        return videosModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView usernameTv, enameTv;
        public TextView openMessaging, videoMessage;
        public CircularImageView accountimage;
        public VideoView videoView;
        public  TextView likeCount;
        public LikeButton likeButton;



        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameTv=itemView.findViewById(R.id.videoItemsUsername);
            enameTv=itemView.findViewById(R.id.videoItemsEname);
            videoView=itemView.findViewById(R.id.videoview);
            accountimage=itemView.findViewById(R.id.accountImageview);
            videoMessage=itemView.findViewById(R.id.videoMessage);
            openMessaging=itemView.findViewById(R.id.openMessaging);
            likeCount=itemView.findViewById(R.id.counter);
            likeButton=itemView.findViewById(R.id.likeButton);




        }
    }


}