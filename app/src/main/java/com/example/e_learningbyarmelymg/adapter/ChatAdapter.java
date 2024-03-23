package com.example.e_learningbyarmelymg.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.e_learningbyarmelymg.R;
import com.example.e_learningbyarmelymg.domain.ChatMessage;
import com.example.e_learningbyarmelymg.domain.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter <ChatAdapter.Viewholder> {

    ArrayList<ChatMessage> items;
    Context context;

    private FirebaseAuth mAuth;

    public ChatAdapter(ArrayList<ChatMessage> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public ChatAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View inflator;
        if(viewType==1){
            inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.sent_message_item, parent, false);
        } else {
          inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.received_message_item, parent, false);
        }

        context = parent.getContext();
        return new Viewholder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatAdapter.Viewholder holder, int position) {
        holder.messageText.setText(items.get(position).getMessage());
        holder.dateReception.setText(items.get(position).getDateTime().toLocaleString());

        /*int drawableResourceId = holder.itemView.getResources().getIdentifier(items.get(position).getPhoto_url(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(holder.photoUrl);*/
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView messageText, dateReception;
        //ImageView photoUrl;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.message_txt);
            dateReception = itemView.findViewById(R.id.date_reception_txt);

            //photoUrl = itemView.findViewById(R.id.imageProfile);
        }
    }

    @Override
    public int getItemViewType(int position) {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(this.items.get(position).getSenderId().equals(user.getEmail())) {
            return  1;
        }
        return  0;
    }


    // <>
}
