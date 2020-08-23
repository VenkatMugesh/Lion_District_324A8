package com.example.leodistrict324a8.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leodistrict324a8.Adapters.ClubAdapter;
import com.example.leodistrict324a8.AdminLandingActivvity;
import com.example.leodistrict324a8.Model.Club;
import com.example.leodistrict324a8.R;
import com.example.leodistrict324a8.StartActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PeopleFragment extends Fragment {
    ImageView backArrow, frwdArrow, offImage, goverImage , leoBackArrow , leoFrontArrow , leoOffImage , dDDLCImage;
    TextView offName, offPost, goverName, leoOffName , leoOffPost , dDDLCText , logOut;
    RecyclerView recyclerView;
    DatabaseReference reference;
    List<Club> clubList;
    ClubAdapter mAdapter;
    int toggle = 1;
    int offToggle = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_people, container, false);

        backArrow = view.findViewById(R.id.back_arrow);
        frwdArrow = view.findViewById(R.id.left_button);
        offImage = view.findViewById(R.id.officer_image);
        offName = view.findViewById(R.id.officer_name);
        offPost = view.findViewById(R.id.officer_post);
        goverImage = view.findViewById(R.id.governor_image);
        goverName = view.findViewById(R.id.governor_name);
        leoBackArrow = view.findViewById(R.id.leo_back_arrow);
        leoFrontArrow = view.findViewById(R.id.leo_left_button);
        leoOffImage = view.findViewById(R.id.leo_officer_image);
        leoOffName = view.findViewById(R.id.leo_officer_name);
        leoOffPost = view.findViewById(R.id.leo_officer_post);
        recyclerView = view.findViewById(R.id.club_recycler_view);
        dDDLCImage = view.findViewById(R.id.ddlc_image);
        dDDLCText = view.findViewById(R.id.ddlc_name);
        logOut = view.findViewById(R.id.log_out);

        recyclerView.setHasFixedSize(true);

        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false);
        recyclerView.setLayoutManager(layoutManager);
        clubList = new ArrayList<>();
        mAdapter = new ClubAdapter(getContext() , clubList);
        recyclerView.setAdapter(mAdapter);

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("User").child("Member");
        userRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.child(user.getUid()).exists()){
                    logOut.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                auth.signOut();
                Intent i = new Intent(getContext(), StartActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);
            }
        });

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

        return view;
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