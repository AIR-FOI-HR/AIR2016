package com.example.timeline;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

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

    boolean userScrolled=false;

    private boolean loading = true;
    int pastVisiblesItems, visibleItemCount, totalItemCount;

    LiveData liveData;

    public void implementScrollListener() {

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                        super.onScrollStateChanged(recyclerView, newState);
                        if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                            userScrolled = true;
                        }
                    }

                    @Override
                    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);
                        visibleItemCount = layoutManager.getChildCount();
                        totalItemCount = layoutManager.getItemCount();
                        pastVisiblesItems = layoutManager.findFirstVisibleItemPosition();

                        if (userScrolled && (visibleItemCount + pastVisiblesItems) == totalItemCount) {
                            userScrolled = false;
                            //RefreshData(oldestPostId);
                            //timelinePostCallbackAccept.UpdateLastPostNumber(5);
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
        moduleReady = true;

        tryToDisplayData();

    }

    @Override
    public void setData(List<Post> posts, List<User> users) {
        this.posts = posts;
        this.users = users;

        dataReady = true;
        tryToDisplayData();

    }

    private void tryToDisplayData(){
        if(moduleReady && dataReady) {
            List<PostItem> postItems = PostListViewModel.convertToPostItemList(posts);
            List<UserItem> userItems = UserListViewModel.convertToUserItemList(users);
            recyclerView.setAdapter(new PostRecyclerAdapter(postItems, userItems, context, this));
            recyclerView.setLayoutManager(layoutManager);
            liveData.UpdateLastPostNumber(5);
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
