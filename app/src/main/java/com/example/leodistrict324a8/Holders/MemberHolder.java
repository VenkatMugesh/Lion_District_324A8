package com.example.leodistrict324a8.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leodistrict324a8.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class MemberHolder extends RecyclerView.ViewHolder {
    public TextView postText , nameText , addressText , emailText;
    public ImageView deleteIcon;
    public CardView memberCard;
    public MemberHolder(@NonNull View itemView) {
        super(itemView);

        postText = itemView.findViewById(R.id.post_member);
        nameText = itemView.findViewById(R.id.name_member);
        addressText = itemView.findViewById(R.id.address_member);
        emailText = itemView.findViewById(R.id.email_member);
        memberCard = itemView.findViewById(R.id.member_card);
        deleteIcon = itemView.findViewById(R.id.delete_member);
    }
}
