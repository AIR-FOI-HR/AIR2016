package com.example.timeline;

import android.content.Context;
import android.os.Bundle;
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

import com.example.core.DataPresenter;
import com.example.core.entities.Post;
import com.example.core.entities.User;

public class PostListFragment extends Fragment implements DataPresenter {
    RecyclerView recyclerView;
    Context context;
    private List<Post> posts;
    private List<User> users;
    private boolean dataReady = false;
    private boolean moduleReady = false;

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
        moduleReady = true;
        tryToDisplayData();

        //testni podaci
        dataReady = true;
        posts = new ArrayList<>();
        posts.add(new Post("fdsdf", "fsdfs", "fsd", 846, 8456, "fsd", "https://i.pinimg.com/564x/d9/56/9b/d9569bbed4393e2ceb1af7ba64fdf86a.jpg", 8546));
        posts.add(new Post("aaaa", "fsdfs", "fsd", 846, 8456, "aaaa", "https://i.pinimg.com/564x/d9/56/9b/d9569bbed4393e2ceb1af7ba64fdf86a.jpg", 8546));
        posts.add(new Post("aaaa", "fsdfs", "fsd", 846, 8456, "aaaa", "https://i.pinimg.com/564x/d9/56/9b/d9569bbed4393e2ceb1af7ba64fdf86a.jpg", 8546));
        posts.add(new Post("aaaa", "fsdfs", "fsd", 846, 8456, "aaaa", "https://i.pinimg.com/564x/d9/56/9b/d9569bbed4393e2ceb1af7ba64fdf86a.jpg", 8546));
        posts.add(new Post("aaaa", "fsdfs", "fsd", 846, 8456, "aaaa", "https://i.pinimg.com/564x/d9/56/9b/d9569bbed4393e2ceb1af7ba64fdf86a.jpg", 8546));
        posts.add(new Post("aaaa", "fsdfs", "fsd", 846, 8456, "aaaa", "https://i.pinimg.com/564x/d9/56/9b/d9569bbed4393e2ceb1af7ba64fdf86a.jpg", 8546));


        users = new ArrayList<>();
        users.add(new User("fsdfs", "f", "gfd", "gf", "https://i.pinimg.com/564x/d9/56/9b/d9569bbed4393e2ceb1af7ba64fdf86a.jpg", 48564, "fsdfs", "tr", 45));
        users.add(new User("aaaaaa", "f", "gfd", "gf", "https://i.pinimg.com/564x/d9/56/9b/d9569bbed4393e2ceb1af7ba64fdf86a.jpg", 48564, "aaaaa", "tr", 45));
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
            recyclerView.setAdapter(new PostRecyclerAdapter(postItems, userItems, context));
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
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
}
