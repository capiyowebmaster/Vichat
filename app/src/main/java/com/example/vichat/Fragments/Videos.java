package com.example.vichat.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.vichat.Adapters.videoAdapter;
import com.example.vichat.Models.videosModel;
import com.example.vichat.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;


public class Videos extends Fragment {
    public FirebaseAuth mAuth;
    public FirebaseDatabase firebaseDatabase;
    public FirebaseUser firebaseUser;
    public RecyclerView recyclerView;
    public videoAdapter videoAdapter;
    public List<videosModel> model;


    public Videos() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view=  inflater.inflate(R.layout.fragment_videos, container, false);
        mAuth=FirebaseAuth.getInstance();
        firebaseUser=mAuth.getCurrentUser();
        firebaseDatabase=FirebaseDatabase.getInstance();

        recyclerView = view.findViewById(R.id.videosRecyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        model= new ArrayList<>();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView,  int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (!recyclerView.canScrollVertically(1) && newState==RecyclerView.SCROLL_STATE_IDLE){
                    readPosts();
                }
            }
        });


        readPosts();
        return  view;

    }
    private void readPosts() {
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading...");
        progressDialog.setMessage("Wait till i complete my work please");
       // progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.show();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("Videos");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                model.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    videosModel myModel=dataSnapshot.getValue(videosModel.class);
                    assert myModel != null;
                    if (!myModel.getVideoUrl().equals("videoUrl")){
                        model.add(myModel);
                        progressDialog.dismiss();

                    }


                }
                videoAdapter=new videoAdapter(getContext(),model);
                recyclerView.setAdapter(videoAdapter);
                recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),DividerItemDecoration.HORIZONTAL));


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                progressDialog.dismiss();

            }
        });

    }


}