package com.example.leodistrict324a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.leodistrict324a8.Model.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class PostViewActivity extends AppCompatActivity {

    TextView titleText , desText , clubText;
    ImageView postImage , deleteIcon;
    LinearLayout deleteLinear;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_view);

        String postId = getIntent().getExtras().get("postId").toString();
        final String image = getIntent().getExtras().get("image").toString();
        titleText = findViewById(R.id.title_text);
        desText = findViewById(R.id.desc_text);
        clubText = findViewById(R.id.club_text);
        postImage = findViewById(R.id.post_image);
        deleteIcon = findViewById(R.id.delete_post);
        deleteLinear = findViewById(R.id.delete_linear);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("User").child("Admin");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(user.getUid()).exists()){
                    deleteLinear.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Activity").child(postId);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String title = snapshot.child("title").getValue(String.class);
                String desc = snapshot.child("desc").getValue(String.class);
                String club = snapshot.child("club").getValue(String.class);
                String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                titleText.setText(title);
                desText.setText(desc);
                clubText.setText(club);

                Picasso.get().load(image).placeholder(R.drawable.leo_one).into(postImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        deleteIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Dialog dialog = new Dialog(PostViewActivity.this);
                dialog.setContentView(R.layout.splash_dialog);
                Button yesBtn = dialog.findViewById(R.id.yes_button);
                Button noBtn = dialog.findViewById(R.id.no_button);

                noBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });
                yesBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        reference.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                dialog.dismiss();
                                startActivity(new Intent(PostViewActivity.this , AddActivity.class));
                                finishAffinity();
                            }
                        });
                    }
                });
                dialog.show();
            }
        });


    }
}