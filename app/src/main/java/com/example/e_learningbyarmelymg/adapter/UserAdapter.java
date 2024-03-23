package com.example.e_learningbyarmelymg.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.GranularRoundedCorners;
import com.example.e_learningbyarmelymg.Chat;
import com.example.e_learningbyarmelymg.R;
import com.example.e_learningbyarmelymg.domain.User;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter <UserAdapter.Viewholder> {

    ArrayList<User> items;
    Context context;

    public UserAdapter(ArrayList<User> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public UserAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflator = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container_user, parent, false);
        context = parent.getContext();
        return new Viewholder(inflator);
    }

    @Override
    public void onBindViewHolder(@NonNull UserAdapter.Viewholder holder, @SuppressLint("RecyclerView") int position) {
        holder.email.setText(items.get(position).getEmail());
        holder.username.setText(items.get(position).getUsername());

        int drawableResourceId = holder.itemView.getResources().getIdentifier(items.get(position).getPhoto_url(), "drawable", holder.itemView.getContext().getPackageName());
        Glide.with(holder.itemView.getContext())
                .load(drawableResourceId)
                .transform(new GranularRoundedCorners(30, 30, 0, 0))
                .into(holder.photoUrl);

        // OnClickListener
        holder.constraintLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Récupérez les données de l'élément sur lequel vous avez cliqué
                String clickedData = items.get(position).getEmail();

                // Créez un Intent pour démarrer l'activité cible
                Intent intent = new Intent(context, Chat.class);

                // Ajoutez les données à l'Intent en utilisant putExtra()
                intent.putExtra("email_receiver", clickedData);
                intent.putExtra("username_receiver", items.get(position).getUsername());

                // Démarrez l'activité avec l'Intent
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {

        TextView email, username;
        ImageView photoUrl;

        ConstraintLayout constraintLayout;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            email = itemView.findViewById(R.id.textEmail);
            username = itemView.findViewById(R.id.textName);
            photoUrl = itemView.findViewById(R.id.imageProfile);

            constraintLayout = itemView.findViewById(R.id.user_container_layout);
        }
    }


    // <>
}
