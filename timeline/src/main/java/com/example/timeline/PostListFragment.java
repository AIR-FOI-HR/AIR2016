package com.example.timeline;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.core.DataPresenter;
import com.example.core.LiveData.LiveData;
import com.example.core.entities.Post;
import com.example.core.entities.User;

public class PostListFragment extends Fragment implements DataPresenter, PostRecyclerAdapter.OnItemClicked {
    RecyclerView recyclerView;
    Context context;
    private List<Post> posts;
    private List<User> users;
    private boolean dataReady = false;
    private boolean moduleReady = false;
    private LinearLayoutManager layoutManager;
    PostRecyclerAdapter postRecyclerAdapter;

    private boolean loading = true;

    LiveData liveData;

    public void implementScrollListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (!recyclerView.canScrollVertically(1)&&newState == RecyclerView.SCROLL_STATE_IDLE) {
                            liveData.UpdateLastPostNumber(posts.get(posts.size()-1).getID_objava());
                            recyclerView.suppressLayout(true);
                        }
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_post_list, container, false);
        context = this.getContext();

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.main_recycler);
        layoutManager = new LinearLayoutManager(context);
        liveData = new LiveData();
        implementScrollListener();
        moduleReady = true;

        tryToDisplayData(false);

    }

    @Override
    public void setData(List<Post> posts, List<User> users, boolean isNewData) {
        recyclerView.suppressLayout(false);
        this.posts = posts;
        this.users = users;

        dataReady = true;
        tryToDisplayData(isNewData);
    }

    private void tryToDisplayData(boolean isNewData){
        if(moduleReady && dataReady) {
            List<PostItem> postItems = PostListViewModel.convertToPostItemList(posts);
            List<UserItem> userItems = UserListViewModel.convertToUserItemList(users);
            if(isNewData) {
                postRecyclerAdapter = new PostRecyclerAdapter(postItems, userItems, context, this);
                recyclerView.setAdapter(postRecyclerAdapter);
                recyclerView.setLayoutManager(layoutManager);
            }
            if(!isNewData){
                postRecyclerAdapter.postItemList = (ArrayList<PostItem>)postItems;
                postRecyclerAdapter.userItemList = (ArrayList<UserItem>)userItems;
                postRecyclerAdapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public String getModuleName(Context context) {
        return context.getString(R.string.module_name);
    }

    @Override
    public Fragment getFragment() {
        return this;
    }

    @Override
    public void onItemClick(int position) {
        liveData.UpdateSelectedPostId(posts.get(position).getID_objava());
    }
}
