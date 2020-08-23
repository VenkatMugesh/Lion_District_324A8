package com.example.leodistrict324a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.leodistrict324a8.Adapters.UserAdapter;
import com.example.leodistrict324a8.Model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewMemberActivity extends AppCompatActivity {

    RecyclerView memberRecycler , presidentRecycler;
    UserAdapter mAdapter , pAdapter;
    List<User> memberList , prezList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_member);

        memberRecycler = findViewById(R.id.member_recycler);
        presidentRecycler = findViewById(R.id.president_recycler);

        memberRecycler.setHasFixedSize(true);
        presidentRecycler.setHasFixedSize(true);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(this);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(this);
        layoutManager1.setReverseLayout(true);
        layoutManager1.setStackFromEnd(true);
        layoutManager2.setReverseLayout(true);
        layoutManager2.setStackFromEnd(true);
        memberRecycler.setLayoutManager(layoutManager1);
        presidentRecycler.setLayoutManager(layoutManager2);
        memberList = new ArrayList<>();
        prezList = new ArrayList<>();

        mAdapter = new UserAdapter(memberList , this);
        pAdapter = new UserAdapter(prezList , this);

        memberRecycler.setAdapter(mAdapter);
        presidentRecycler.setAdapter(pAdapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User");

        reference.child("Member").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                memberList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    memberList.add(user);
                }
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        reference.child("President").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                prezList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    prezList.add(user);
                }
                pAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}