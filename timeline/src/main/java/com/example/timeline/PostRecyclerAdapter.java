package com.example.timeline;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.core.entities.User;
import com.example.core.entities.Post;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private ArrayList<PostItem> postItemList;
    private ArrayList<UserItem> userItemList;
    private Context context;

    public PostRecyclerAdapter(@NonNull List<PostItem> postItemList, List<UserItem> userItemList, Context context) {
        this.postItemList = (ArrayList<PostItem>) postItemList;
        this.userItemList = (ArrayList<UserItem>) userItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.post_list_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        final Post post = postItemList.get(position);
        User user = new User();

        for(User u : userItemList){
            if(u.uid.equals(post.getKorisnik_ID()))
                user = u;
        }

        holder.bindToData(post, user);
    }

    @Override
    public int getItemCount() {
        return postItemList == null? 0: postItemList.size();
    }
}