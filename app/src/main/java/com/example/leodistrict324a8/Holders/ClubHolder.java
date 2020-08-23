package com.example.leodistrict324a8.Holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.leodistrict324a8.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ClubHolder extends RecyclerView.ViewHolder {

    public TextView clubName , clubAddress;
    public ImageView clubImage;
    public ClubHolder(@NonNull View itemView) {
        super(itemView);

        clubAddress = itemView.findViewById(R.id.leo_address);
        clubName = itemView.findViewById(R.id.leo_name);
        clubImage = itemView.findViewById(R.id.logo);
    }
}
