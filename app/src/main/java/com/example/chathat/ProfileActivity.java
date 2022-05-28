package com.example.chathat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends AppCompatActivity {
    private ImageView profileImage;
    private EditText username;
    private Button updateButton;
    FirebaseAuth auth;
    DatabaseReference ref;
    FirebaseUser firebaseUser;
    StorageReference storageReference;
    String image;

    boolean imageControl=false;
    Uri imageUri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        username=findViewById(R.id.usernameProfile);
        profileImage=findViewById(R.id.imageViewProfileActivity);
        updateButton=findViewById(R.id.buttonUpdate);
        ref= FirebaseDatabase.getInstance().getReference();
        auth=FirebaseAuth.getInstance();
        firebaseUser=auth.getCurrentUser();
        getUserInfo();
        storageReference=FirebaseStorage.getInstance().getReference();
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageChooser();
            }
        });
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });

    }

    public void updateProfile(){
        String userName = username.getText().toString();
        ref.child("Users").child(firebaseUser.getUid()).child("Username").setValue(userName);

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
                            ref.child("Users").child(auth.getUid()).child("image").setValue(filepath).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(ProfileActivity.this, "uploaded!", Toast.LENGTH_SHORT).show();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(ProfileActivity.this, "Error uploading !", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
            });

        }else{
            ref.child("Users").child(auth.getUid()).child("image").setValue(image);
        }
        Intent intent=new Intent(ProfileActivity.this,MainActivity.class);
        intent.putExtra("username_key",userName);
        startActivity(intent);
        finish();
    }

    public void getUserInfo() {
        ref.child("Users").child(firebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("Username").getValue().toString();
                image = snapshot.child("image").getValue().toString();
                username.setText(name);
                if(image.equals("null")) {
                    profileImage.setImageResource(R.drawable.googleg_disabled_color_18);
                }
                else{
                    Picasso.get().load(image).into(profileImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
            Picasso.get().load(imageUri).into(profileImage);
            imageControl=true;
        }
        else{
            imageControl=false;
        }
    }
}