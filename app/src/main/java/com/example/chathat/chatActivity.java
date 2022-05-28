package com.example.chathat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class chatActivity extends AppCompatActivity {
    FirebaseAuth auth;
    RecyclerView rv;
    FirebaseUser user;
    DatabaseReference reference;
    String q;
    List<User> list;
    UsersAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        rv=findViewById(R.id.rv);
        list=new ArrayList<User>();

        rv.setLayoutManager(new LinearLayoutManager(chatActivity.this));

        getUsers();

    }
    public void getUsers(){
        reference= FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        user= auth.getCurrentUser();

        reference.child("Users").addValueEventListener(new ValueEventListener() {
//            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                list.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    String key = ds.getKey();
                    User user1 = ds.getValue(User.class);
                    if(!user.getUid().equals(key)){
                        list.add(user1);
                    }else{
                        q = ds.child("Username").getValue().toString();
                    }

                    adapter=new UsersAdapter(list,chatActivity.this,q);
                    rv.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater menuInflater=getMenuInflater();
        menuInflater.inflate(R.menu.chat_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        if(item.getItemId()==R.id.action_profile){
            startActivity(new Intent(chatActivity.this,ProfileActivity.class));
        }
        else if(item.getItemId()==R.id.action_signout){
            auth.signOut();
            startActivity(new Intent(chatActivity.this,MainActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}