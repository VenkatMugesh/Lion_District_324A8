package com.example.leodistrict324a8;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.MemoryFile;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.leodistrict324a8.Adapters.MemberAdapter;
import com.example.leodistrict324a8.Model.Member;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ClubMembersActivity extends AppCompatActivity {

    TextView clubText;
    ImageView logoImage;
    RecyclerView clubRecycler;
    List<Member> mMembers;
    MemberAdapter memberAdapter;
    String val = "1";

    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_members);

        clubText = findViewById(R.id.club_name);
        logoImage = findViewById(R.id.club_logo);
        clubRecycler = findViewById(R.id.member_recycler);

        val = getIntent().getExtras().get("key").toString();
        reference = FirebaseDatabase.getInstance().getReference("Leoistic").child(val);

        clubRecycler.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager =new GridLayoutManager(this , 1);
        clubRecycler.setLayoutManager(linearLayoutManager);
        mMembers = new ArrayList<>();
        memberAdapter = new MemberAdapter(this , mMembers);
        clubRecycler.setAdapter(memberAdapter);

        reference.child("members").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mMembers.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()){
                    Member member = dataSnapshot.getValue(Member.class);
                    mMembers.add(member);
                }
                memberAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String name = snapshot.child("name").getValue(String.class);
                String imageUrl = snapshot.child("imageUrl").getValue(String.class);

                clubText.setText(name);
                Picasso.get().load(imageUrl).into(logoImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}