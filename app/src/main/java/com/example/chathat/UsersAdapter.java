package com.example.chathat;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    List<User> list;
    Context mContext;
    String username;


    DatabaseReference reference;

    public UsersAdapter(List<User> list, Context mContext, String username) {
        this.list = list;
        this.mContext = mContext;
        this.username = username;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewUsers;
        private ImageView imageViewUsers;
        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewUsers = itemView.findViewById(R.id.textView);
            imageViewUsers = itemView.findViewById(R.id.imageViewUser);
            cardView = itemView.findViewById(R.id.cardView);

        }
    }

    @NonNull
    @Override
    public UsersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersAdapter.ViewHolder holder, int position) {
        String othername = list.get(position).getUsername();
        String imageURL = list.get(position).getImage();
        holder.textViewUsers.setText(othername);

        if (imageURL.equals("null")) {
            holder.imageViewUsers.setImageResource(R.drawable.pngaccount);
        } else {
            Picasso.get().load(imageURL).into(holder.imageViewUsers);
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, MyChatActivity.class);
                intent.putExtra("userName_key", username);
                intent.putExtra("otherName_key", othername);
                mContext.startActivity(intent);
            }
        });

    }



    @Override
    public int getItemCount() {
        return list.size();
    }
}


/*
reference.child("Users").child(list.get(position)).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                othername = snapshot.child("Username").getValue().toString();
                imageURL = snapshot.child("image").getValue().toString();
                holder.textViewUsers.setText(othername);
                if(imageURL.equals("null")){
                    holder.imageViewUsers.setImageResource(R.drawable.pngaccount);
                }
                else{
                    Picasso.get().load(imageURL).into(holder.imageViewUsers);
                }
                holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(mContext,MyChatActivity.class);
                        intent.putExtra("userName_key",username);
                        intent.putExtra("otherName_key",othername);
                        mContext.startActivity(intent);
                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
 */
