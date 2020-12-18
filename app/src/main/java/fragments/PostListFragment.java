package fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import hr.example.treeapp.AllPostsCallback;
import hr.example.treeapp.Comment;
import hr.example.treeapp.CommentCallback;
import hr.example.treeapp.GetPostData;
import hr.example.treeapp.Post;
import hr.example.treeapp.R;
import recyclerview.PostItem;
import recyclerview.PostRecyclerAdapter;

public class PostListFragment extends Fragment {
    GetPostData getPostData;
    RecyclerView recyclerView;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getPostData = new GetPostData();
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        recyclerView = view.findViewById(R.id.main_recycler);
        context = this.getContext();
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        loadData();
    }

    private void loadData(){
      /*  getPostData.getPost("oEyhr7OjvnDKB5vuA8ie", new PostCallback() {
            @Override
            public void onCallback(Post post) {
                if (post != null) {

                    List<Post> postList = new ArrayList<>();
                    postList.add(post);

                    List<PostItem> postItems = new ArrayList<>();
                    for(Post p : postList){
                        postItems.add(new PostItem(post));
                    }
                    recyclerView.setAdapter(new PostRecyclerAdapter(postItems, context));
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                }
                else{
                    Log.d("dokument", "Nema dokumenta objave.");
                }
            }
        });*/

        getPostData.getAllPosts(new AllPostsCallback() {
            @Override
            public void onCallback(List<Post> postList) {
                if (postList != null) {
                    List<PostItem> postItems = new ArrayList<>();
                    for(Post p : postList){
                        postItems.add(new PostItem(p));
                    }
                    recyclerView.setAdapter(new PostRecyclerAdapter(postItems, context));
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                }
                else{
                    Log.d("dokument", "Nema dokumenta objave.");
                }
            }
        });

        getPostData.getPostComments("oEyhr7OjvnDKB5vuA8ie", new CommentCallback() {
            @Override
            public void onCallback(List<Comment> comment) {
                if (comment != null) {
                    for(Comment c : comment){
                        Log.d("komentar", "komentari:" + c.getTekst());
                    }
                }
                else{
                    Log.d("komentar", "Nema komentara.");
                }
            }
        });
    }
}
