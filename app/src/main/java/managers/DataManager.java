package managers;

import android.content.Context;
import android.util.Log;

import com.example.core.DataPresenter;
import com.example.core.entities.Post;
import com.example.core.entities.User;
import com.example.timeline.PostItem;
import com.example.timeline.PostRecyclerAdapter;
import com.example.timeline.UserItem;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.LinearLayoutManager;
import hr.example.treeapp.AllPostsCallback;
import hr.example.treeapp.AllUsersCallback;
import hr.example.treeapp.GetPostData;
import hr.example.treeapp.UserRepository;

public class DataManager {
    private DataManager(){
    }
    private static DataManager instance = new DataManager();
    private GetPostData getPostData = new GetPostData();
    private UserRepository userRepository = new UserRepository();
    private boolean postsReady = false;
    private boolean usersReady = false;
    private DataPresenter presenter;
    private Context context;
    private List<Post> posts = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public static DataManager getInstance(){
        return instance;
    }

    public void loadData(DataPresenter presenter, Context context){
        this.presenter = presenter;
        this.context = context;

        getPostData.getAllPosts(new AllPostsCallback() {
            @Override
            public void onCallback(List<Post> postList) {
                if (postList != null) {
                    posts = postList;
                    postsReady = true;
                    sendDataToPresenter(presenter, context);
                }
                else{
                    Log.d("dokument", "Nema dokumenta objave.");
                }
            }
        });

        userRepository.getAllUsers(new AllUsersCallback() {
            @Override
            public void onCallback(List<User> userList) {
                if (userList != null) {
                    users = userList;
                    usersReady = true;
                    sendDataToPresenter(presenter, context);
                }
                else{
                    Log.d("dokument", "Nema dokumenta objave.");
                }
            }
        });
    }

    public void sendDataToPresenter(DataPresenter presenter, Context context){
        if(postsReady && usersReady){
            presenter.setData(posts, users);
        }
    }



}
