package com.example.timeline;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.core.entities.User;
import com.example.core.entities.Post;

public class PostViewHolder extends RecyclerView.ViewHolder {
    ImageView postImage;
    ImageView profileImage;
    TextView username;
    TextView postDescription;
    private View itemView;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        postImage = itemView.findViewById(R.id.post_image);
        profileImage = itemView.findViewById(R.id.profile_image);
        username = itemView.findViewById(R.id.username);
        postDescription = itemView.findViewById(R.id.post_description);
        this.itemView = itemView;
    }

    public void bindToData(Post post, User user){
        username.setText(user.korisnickoIme);
        postDescription.setText(post.getOpis());

        Glide.with(itemView.getContext())
                .load(post.getSlika())
                .into(postImage);

        Glide.with(itemView.getContext())
                .load(user.getSlika())
                .into(profileImage);
    }
}
