package com.example.leodistrict324a8.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leodistrict324a8.Holders.UserHolder;
import com.example.leodistrict324a8.Model.User;
import com.example.leodistrict324a8.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserAdapter extends RecyclerView.Adapter<UserHolder> {

    List<User> userList;
    Context c;

    public UserAdapter(List<User> userList, Context c) {
        this.userList = userList;
        this.c = c;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(c).inflate(R.layout.reg_item , parent , false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {

        User mUser = userList.get(position);

        String name = mUser.getName();
        String email = mUser.getEmail();
        String post = mUser.getPost();
        String club = mUser.getClub();

        holder.emailText.setText(email);
        holder.postText.setText(post);
        holder.clubText.setText(club);
        holder.nameText.setText(name);

    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
