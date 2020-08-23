package com.example.leodistrict324a8.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.leodistrict324a8.Holders.PostHolder;
import com.example.leodistrict324a8.Model.Post;
import com.example.leodistrict324a8.PostViewActivity;
import com.example.leodistrict324a8.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PostAdapter extends RecyclerView.Adapter<PostHolder> {

    Context c;
    List<Post> mList;

    public PostAdapter(Context c , List<Post> mList){
        this.c = c;
        this.mList = mList;
    }
    @NonNull
    @Override
    public PostHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(c).inflate(R.layout.post_item , parent , false);
        return new PostHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostHolder holder, int position) {
        final Post mPost = mList.get(position);
        holder.clubText.setText(mPost.getClub());
        holder.titleText.setText(mPost.getTitle());

        Picasso.get().load(mPost.getImageUrl()).into(holder.postImage);

        holder.postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String postId = mPost.getPostId();
                Intent i = new Intent(c , PostViewActivity.class);
                i.putExtra("postId" , postId);
                i.putExtra("image" , mPost.getImageUrl());
                c.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
