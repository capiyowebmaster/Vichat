package com.example.vichat.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.vichat.Models.messageModel;
import com.example.vichat.R;
import com.example.vichat.Models.messageModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.util.List;

public class messagesAdapter extends RecyclerView.Adapter<messagesAdapter.MyViewHolder> {


    private final List<messageModel> myModel;

    private final Context mContext;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private  static final  int MSG_LEFT=0;
    private  static final int MSG_RIGHT=1;

    public messagesAdapter(Context context, List<messageModel> myModel) {
        this.mContext = context;
        this.myModel =myModel;

    }

    @NonNull
    @Override
    public  MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==MSG_RIGHT){
            View view=LayoutInflater.from(mContext).inflate(R.layout.message_right,parent,false);
            return  new  MyViewHolder(view);
        }
        else {
            View view=LayoutInflater.from(mContext).inflate(R.layout.message_left,parent,false);
            return  new  MyViewHolder(view);

        }


    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        messageModel model=myModel.get(position);


        if (!model.getMessages().equals("")){
            holder.displayMessage.setVisibility(View.VISIBLE);

            holder.displayMessage.setText(model.getMessages());

        }
        else {
           holder.displayMessage.setVisibility(View.GONE);
        }



        //holder.accountimage.setImageResource(R.mipmap.account_avatar_foreground);

        if (!model.getSendImages().equals("")){
            holder.displayImage.setVisibility(View.VISIBLE);
            Glide.with(mContext).load(model.getSendImages()).into(holder.displayImage);

        }
        else {
            holder.displayImage.setVisibility(View.GONE);
        }


        if (!model.getCaption().equals("")){
            holder.displayCaption.setVisibility(View.VISIBLE);
            holder.displayCaption.setText(model.getCaption());
        } else {
            holder.displayCaption.setVisibility(View.GONE);
        }



        if (model.getStatusUrl().equals("statusUrl")){
            holder.accountimage.setImageResource(R.mipmap.app_icon);
        }
        else {
            Glide.with(mContext).load(model.getStatusUrl()).into(holder.accountimage);
        }


















    }



    @Override
    public int getItemCount() {
        return myModel.size();
    }

    public class  MyViewHolder extends RecyclerView.ViewHolder {
        public TextView displayMessage, displayCaption;
        public CircularImageView accountimage;
        public  ImageView displayImage;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            displayMessage=itemView.findViewById(R.id.showMessage);

            accountimage=itemView.findViewById(R.id.showStatus);
            displayImage=itemView.findViewById(R.id.displayImage);
            displayCaption=itemView.findViewById(R.id.caption);


        }
    }

    @Override
    public int getItemViewType(int position) {
        firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if (myModel.get(position).getSenderid().equals(firebaseUser.getUid())){
            return MSG_RIGHT;
        }else {
            return  MSG_LEFT;
        }
    }
}