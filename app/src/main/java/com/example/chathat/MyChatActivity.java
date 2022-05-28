package com.example.chathat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MyChatActivity extends AppCompatActivity {
    TextView textViewChat;
    ImageView back;
    EditText editTextMessage;
    FloatingActionButton fab;
    RecyclerView rvChat;
    String userName,otherName;
    FirebaseDatabase db;
    DatabaseReference ref;
    MessageAdapter adapter;
    List<ModelClass> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db=FirebaseDatabase.getInstance();
        ref=db.getReference();
        setContentView(R.layout.activity_my_chat);
        textViewChat=findViewById(R.id.textViewChat);
        back=findViewById(R.id.imageViewBack);
        fab=findViewById(R.id.fab);
        editTextMessage=findViewById(R.id.editMessage);
        rvChat=findViewById(R.id.rvChat);
        rvChat.setLayoutManager(new LinearLayoutManager(this));
        list = new ArrayList<>();
        userName=getIntent().getStringExtra("userName_key");
        otherName=getIntent().getStringExtra("otherName_key");
        textViewChat.setText(otherName);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MyChatActivity.this,chatActivity.class);
                startActivity(i);
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message=editTextMessage.getText().toString();
                if(!message.equals("")){
                    sendMessage(message);
                    editTextMessage.setText("");
                }
            }
        });
        getMessage();


    }

    public void getMessage() {
        ref.child("Messages").child(userName).child(otherName).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                ModelClass modelClass = snapshot.getValue(ModelClass.class);
                list.add(modelClass);
                adapter.notifyDataSetChanged();
                rvChat.scrollToPosition(list.size()-1);

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter=new MessageAdapter(list,userName);
        rvChat.setAdapter(adapter);
    }

    public void sendMessage(String message) {
        String key =ref.child("Messages").child(userName).child(otherName).push().getKey();
        Map<String,Object> messageMap = new HashMap<>();
        messageMap.put("message",message);
        messageMap.put("from",userName);
        ref.child("Messages").child(userName).child(otherName).child(key).setValue(messageMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    ref.child("Messages").child(otherName).child(userName).child(key).setValue(messageMap);
                }
            }
        });

    }
}