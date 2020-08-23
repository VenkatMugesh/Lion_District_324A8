package com.example.leodistrict324a8.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leodistrict324a8.ClubEditActivity;
import com.example.leodistrict324a8.ClubMembersActivity;
import com.example.leodistrict324a8.Holders.ClubHolder;
import com.example.leodistrict324a8.Holders.MemberHolder;
import com.example.leodistrict324a8.Model.Club;
import com.example.leodistrict324a8.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearSmoothScroller;
import androidx.recyclerview.widget.RecyclerView;

public class ClubAdapter extends RecyclerView.Adapter<ClubHolder> {
    Context c;
    List<Club> mClubList;
    FirebaseUser user;

    public ClubAdapter(Context c , List<Club> mClubList){
        this.c = c;
        this.mClubList = mClubList;
    }
    @NonNull
    @Override
    public ClubHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.club_item , parent , false);
        return new ClubHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ClubHolder holder, int position) {
        final Club mClub = mClubList.get(position);
        holder.clubName.setText(mClub.getName());
        holder.clubAddress.setText(mClub.getAddress());
        user = FirebaseAuth.getInstance().getCurrentUser();
        final DatabaseReference reference = FirebaseDatabase.getInstance().getReference("User").child("Admin");

        Picasso.get().load(mClub.getImageUrl()).into(holder.clubImage);

        holder.clubImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                reference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.child(user.getUid()).exists()){
                            Intent i = new Intent(c , ClubEditActivity.class);
                            i.putExtra("key"  , mClub.getId());
                            c.startActivity(i);
                        }else{

                            Intent i = new Intent(c , ClubMembersActivity.class);
                            i.putExtra("key" , mClub.getId());
                            c.startActivity(i);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

            }
        });

    }

    @Override
    public int getItemCount() {
        return mClubList.size();
    }
}
