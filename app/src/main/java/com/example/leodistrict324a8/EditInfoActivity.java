package com.example.leodistrict324a8;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leodistrict324a8.Adapters.ClubAdapter;
import com.example.leodistrict324a8.Model.Club;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditInfoActivity extends AppCompatActivity {

    ImageView backArrow, frwdArrow, offImage, goverImage , leoBackArrow , leoFrontArrow , leoOffImage , dDDLCImage , officerImage;
    TextView offName, offPost, goverName, leoOffName , leoOffPost , dDDLCText , changePhoto;
    EditText nameEdit , postEdit;
    Button uploadButton;
    RecyclerView recyclerView;
    DatabaseReference reference;
    List<Club> clubList;
    ClubAdapter mAdapter;
    int toggle = 1 ;
    int offToggle = 1;

    Dialog dialog;
    Uri imageUri;
    FirebaseStorage storage;
    StorageReference ref;
    DatabaseReference finalRef;


    public final static int PICK_IMAGE_REQUEST = 22;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_info);
        backArrow = findViewById(R.id.back_arrow);
        frwdArrow = findViewById(R.id.left_button);
        offImage = findViewById(R.id.officer_image);
        offName = findViewById(R.id.officer_name);
        offPost = findViewById(R.id.officer_post);
        goverImage = findViewById(R.id.governor_image);
        goverName = findViewById(R.id.governor_name);
        leoBackArrow = findViewById(R.id.leo_back_arrow);
        leoFrontArrow = findViewById(R.id.leo_left_button);
        leoOffImage = findViewById(R.id.leo_officer_image);
        leoOffName = findViewById(R.id.leo_officer_name);
        leoOffPost = findViewById(R.id.leo_officer_post);
        recyclerView = findViewById(R.id.club_recycler_view);
        dDDLCImage = findViewById(R.id.ddlc_image);
        dDDLCText = findViewById(R.id.ddlc_name);


        dialog = new Dialog(this);
        dialog.setContentView(R.layout.add_new_dialog);
        officerImage = dialog.findViewById(R.id.add_image);
        changePhoto = dialog.findViewById(R.id.change_photo);
        nameEdit = dialog.findViewById(R.id.name_member);
        postEdit = dialog.findViewById(R.id.post_member);
        uploadButton = dialog.findViewById(R.id.edit_button);

        storage = FirebaseStorage.getInstance();
        ref = storage.getReference("district officers");

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this , LinearLayoutManager.HORIZONTAL , false);
        recyclerView.setLayoutManager(layoutManager);
        clubList = new ArrayList<>();
        mAdapter = new ClubAdapter(EditInfoActivity.this , clubList);
        recyclerView.setAdapter(mAdapter);

        final DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Leoistic");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                clubList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Club club = dataSnapshot.getValue(Club.class);
                    clubList.add(club);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference = FirebaseDatabase.getInstance().getReference("DistrictOfficers");

        DatabaseReference personReference = FirebaseDatabase.getInstance().getReference("MainMembers");
        final DatabaseReference childReference = personReference.child("LionMembers");

        personReference.child("DCLC").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String name = snapshot.child("name").getValue(String.class);
                String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                Picasso.get().load(imageUrl).into(dDDLCImage);
                dDDLCText.setText(name);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        personReference.child("Governor").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {


                String name = snapshot.child("name").getValue(String.class);
                String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                Picasso.get().load(imageUrl).into(goverImage);
                goverName.setText(name);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("1").exists()) {
                    DataSnapshot dataSnapshot = snapshot.child("1");
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String post = dataSnapshot.child("post").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                    offName.setText(name);
                    offPost.setText(post);
                    Picasso.get().load(imageUrl).into(offImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        childReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child("1").exists()) {
                    DataSnapshot dataSnapshot = snapshot.child("1");
                    String name = dataSnapshot.child("name").getValue(String.class);
                    String post = dataSnapshot.child("post").getValue(String.class);
                    String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                    leoOffName.setText(name);
                    leoOffPost.setText(post);
                    Picasso.get().load(imageUrl).into(leoOffImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        leoOffImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditInfoActivity.this , EditExistActivity.class);
                i.putExtra("value" , offToggle);
                i.putExtra("child" , "LionMembers");
                startActivity(i);
            }
        });

        offImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditInfoActivity.this , EditExistActivity.class);
                i.putExtra("value" , toggle);
                i.putExtra("child" , "DistrictOfficers");
                startActivity(i);
            }
        });

        dDDLCImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditInfoActivity.this , EditExistActivity.class);
                i.putExtra("value" , toggle);
                i.putExtra("child" , "DCLC");
                startActivity(i);
            }
        });

        goverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EditInfoActivity.this , EditExistActivity.class);
                i.putExtra("value" , toggle);
                i.putExtra("child" , "Governor");
                startActivity(i);
            }
        });

        leoFrontArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (offToggle <= snapshot.getChildrenCount()) {
                            String val = String.valueOf(offToggle + 1);
                            if (snapshot.child(val).exists()) {
                                if (offToggle == snapshot.getChildrenCount()) {
                                    offToggle = 0;
                                }
                                setValue2(val , childReference);
                                //Toast.makeText(getContext(), val, Toast.LENGTH_SHORT).show();
                                offToggle += 1;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        leoBackArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                childReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (offToggle > 0) {
                            String val = String.valueOf(offToggle - 1);
                            if (snapshot.child(val).exists()) {
                                if (offToggle == 1) {
                                    offToggle = (int) snapshot.getChildrenCount() + 1;
                                }
                                setValue2(val , childReference);
                                // Toast.makeText(getContext(), val, Toast.LENGTH_SHORT).show();
                                offToggle -= 1;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });


        frwdArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (toggle <= snapshot.getChildrenCount()) {
                            String val = String.valueOf(toggle + 1);
                            if (snapshot.child(val).exists()) {
                                if (toggle == snapshot.getChildrenCount()) {
                                    toggle = 0;
                                }
                                setValue(val , reference);
                                //Toast.makeText(getContext(), val, Toast.LENGTH_SHORT).show();
                                toggle += 1;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (toggle > 0) {
                            String val = String.valueOf(toggle - 1);
                            if (snapshot.child(val).exists()) {
                                if (toggle == 1) {
                                    toggle = (int) snapshot.getChildrenCount() + 1;
                                }
                                setValue(val , reference);
                                // Toast.makeText(getContext(), val, Toast.LENGTH_SHORT).show();
                                toggle -= 1;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });





    }



    private void setValue2(String val , DatabaseReference reference) {

        reference.child(val).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String post = dataSnapshot.child("post").getValue(String.class);
                String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                leoOffName.setText(name);
                leoOffPost.setText(post);
                Picasso.get().load(imageUrl).into(leoOffImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setValue(String val , DatabaseReference reference) {


        reference.child(val).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.child("name").getValue(String.class);
                String post = dataSnapshot.child("post").getValue(String.class);
                String imageUrl = dataSnapshot.child("imageUrl").getValue(String.class);

                offName.setText(name);
                offPost.setText(post);
                Picasso.get().load(imageUrl).into(offImage);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }


}