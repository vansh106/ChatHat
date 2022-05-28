package com.example.chathat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText editEmail,editPassword;
    Button signIn,signUp;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editEmail=findViewById(R.id.emailEdit);
        editPassword=findViewById(R.id.passwordEdit);
        signIn=findViewById(R.id.button2);
        signUp=findViewById(R.id.button3);
        auth=FirebaseAuth.getInstance();
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Signin(editEmail.getText().toString(),editPassword.getText().toString());
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent in = new Intent(MainActivity.this, SignUpActivity.class);
                    startActivity(in);
                }catch(Exception e){
                    Toast.makeText(MainActivity.this, "nahi hua", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void Signin(String email,String password) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Toast.makeText(MainActivity.this, "SignIn Successful!", Toast.LENGTH_SHORT).show();
                    try {
                        Intent in = new Intent(MainActivity.this, chatActivity.class);
                        startActivity(in);
                    }catch(Exception e){
                        Toast.makeText(MainActivity.this, "nahi hua", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(MainActivity.this,"Invalid Credentials!",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}