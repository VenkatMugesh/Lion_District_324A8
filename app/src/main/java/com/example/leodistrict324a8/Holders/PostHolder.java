package com.example.leodistrict324a8.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leodistrict324a8.R;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class PostHolder extends RecyclerView.ViewHolder {
    public ImageView postImage;
    public TextView titleText, clubText;
    public CardView postCard;

    public PostHolder(@NonNull View itemView) {
        super(itemView);

        postImage = itemView.findViewById(R.id.activity_image);
        titleText = itemView.findViewById(R.id.title_text);
        clubText = itemView.findViewById(R.id.club_text);
        postCard = itemView.findViewById(R.id.post_card);
    }
}
