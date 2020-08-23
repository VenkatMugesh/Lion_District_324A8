package com.example.leodistrict324a8.Holders;

import android.view.View;
import android.widget.TextView;

import com.example.leodistrict324a8.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class UserHolder extends RecyclerView.ViewHolder {
    public TextView nameText , clubText , postText , emailText;
    public UserHolder(@NonNull View itemView) {
        super(itemView);

        nameText = itemView.findViewById(R.id.name_member);
        clubText = itemView.findViewById(R.id.club_member);
        postText = itemView.findViewById(R.id.post_member);
        emailText = itemView.findViewById(R.id.email_member);

    }
}
