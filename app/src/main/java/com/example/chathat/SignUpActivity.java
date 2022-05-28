package com.example.chathat;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class SignUpActivity extends AppCompatActivity {
    EditText name , email,pass;
    Button signUp;
    ImageView imageView;
    boolean imageControl=false;
    Uri imageUri;
    FirebaseAuth auth;
    DatabaseReference reference;
    StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        name=findViewById(R.id.editTextTextPersonName);
        email=findViewById(R.id.editTextTextEmailAddress);
        pass=findViewById(R.id.editTextTextPassword);
        signUp=findViewById(R.id.button);


        auth=FirebaseAuth.getInstance();
        reference= FirebaseDatabase.getInstance().getReference();
        storageReference=FirebaseStorage.getInstance().getReference();
        imageView=findViewById(R.id.imageViewUser);
        imageView.setImageResource(R.drawable.googleg_standard_color_18);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });

        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mail=email.getText().toString();
                String password=pass.getText().toString();
                String username=name.getText().toString();
                if(!mail.equals("") && !password.equals("") && !username.equals("")){
                    signup(mail,password,username);
                }

            }
        });

    }

    public void signup(String email,String pass, String name) {
        auth.createUserWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    reference.child("Users").child(auth.getUid()).child("Username").setValue(name);
                    Toast.makeText(SignUpActivity.this, "User registered successfully!", Toast.LENGTH_SHORT).show();
                    if(imageControl){
                        UUID randomId = UUID.randomUUID();
                        String imageName ="image/"+randomId+".jpg";
                        storageReference.child(imageName).putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                StorageReference storageReference2 = FirebaseStorage.getInstance().getReference(imageName);
                                storageReference2.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {
                                         String filepath=uri.toString();
                                         reference.child("Users").child(auth.getUid()).child("image").setValue(filepath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                             @Override
                                             public void onSuccess(Void unused) {
                                                 Toast.makeText(SignUpActivity.this, "uploaded!", Toast.LENGTH_SHORT).show();
                                             }
                                         }).addOnFailureListener(new OnFailureListener() {
                                             @Override
                                             public void onFailure(@NonNull Exception e) {
                                                 Toast.makeText(SignUpActivity.this, "Error uploading !", Toast.LENGTH_SHORT).show();
                                             }
                                         });
                                    }
                                });
                            }
                        });

                    }else{
                        reference.child("Users").child(auth.getUid()).child("image").setValue("null");
                    }
                    Intent intent=new Intent(SignUpActivity.this,MainActivity.class);
                    intent.putExtra("username_key",name);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }

    public void imageChooser() {
        Intent i=new Intent();
        i.setType("image/*");
        i.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(i,1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1 && resultCode==RESULT_OK && data != null){
            imageUri=data.getData();
            Picasso.get().load(imageUri).into(imageView);
            imageControl=true;

        }
        else{
            imageControl=false;
        }
    }
}