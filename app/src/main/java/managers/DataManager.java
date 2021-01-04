package managers;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;

import com.example.core.DataPresenter;
import com.example.core.entities.Post;
import com.example.core.entities.User;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import hr.example.treeapp.AllPostsCallback;
import hr.example.treeapp.AllUsersCallback;
import hr.example.treeapp.GetPostData;
import hr.example.treeapp.GetPostsFromLastID;
import hr.example.treeapp.PostImageCallback;
import hr.example.treeapp.UserCallback;
import hr.example.treeapp.UserImageCallback;
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
    private int numberOfPosts = 0;
    private int numberOfUsers = 0;
    private boolean postBitmapsReady = false;
    private boolean userBitmapsReady = false;
    private boolean newPostsReady = false;
    private boolean userpostoji=false;

    public static DataManager getInstance(){
        return instance;
    }

    public void loadData(DataPresenter presenter, Context context){
        this.presenter = presenter;
        this.context = context;
        numberOfPosts = 0;
        numberOfUsers = 0;
        postBitmapsReady = false;
        userBitmapsReady = false;
        postsReady = false;
        usersReady = false;

        newPostsReady = false;

        getPostData.getAllPosts(new AllPostsCallback() {
            @Override
            public void onCallback(List<Post> postList) {
                if (postList != null) {
                    posts = postList;
                    postsReady = true;
                    fillPostsWithBitmaps();
                    sendDataToPresenter(presenter);
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
                    fillUsersWithBitmaps();
                    sendDataToPresenter(presenter);
                }
                else{
                    Log.d("dokument", "Nema dokumenta objave.");
                }
            }
        });
    }

    public void sendDataToPresenter(DataPresenter presenter){
        if(postsReady && usersReady && postBitmapsReady && userBitmapsReady){
            presenter.setData(posts, users, true);
            postBitmapsReady = false;
            usersReady = false;
            userBitmapsReady = false;
        }
    }

    private void fillPostsWithBitmaps(){
        for (Post p : posts) {
            if (p.getSlika() == null) {
                getPostData.getPostImage(p.getURL_slike(), new PostImageCallback() {
                    @Override
                    public void onCallback(Bitmap slika) {
                        p.setSlika(slika);
                        numberOfPosts++;
                        if (numberOfPosts == posts.size()) {
                            postBitmapsReady = true;
                            sendDataToPresenter(presenter);
                            sendNewDataToPresenter(presenter);
                        }

                    }
                });
            }
        }
    }

    private void fillUsersWithBitmaps(){
        for (User u : users) {
            if (!(u.getProfilnaSlika().contains("https://"))) {
                userRepository.getUserImage(u.getProfilnaSlika(), new UserImageCallback() {
                    @Override
                    public void onCallback(Bitmap slika) {
                        u.setSlika(slika);
                        numberOfUsers++;
                        if (numberOfUsers == users.size()) {
                            userBitmapsReady = true;
                            sendDataToPresenter(presenter);
                        }

                    }
                });
            } else{
                numberOfUsers++;
                if (numberOfUsers == users.size()) {
                    userBitmapsReady = true;
                    sendDataToPresenter(presenter);
                }
            }
        }
    }

    public void GetPostsFromLastID(){
        getPostData.getPostsFromLastID(new GetPostsFromLastID() {
            @Override
            public void onCallback(List<Post> postList) {
                if (postList != null) {
                    posts = postList;
                    newPostsReady = true;
                    fillPostsWithBitmaps();
                    sendNewDataToPresenter(presenter);
                }
                else{
                    Log.d("dokument", "Nema dokumenta objave.");
                }
            }
        });
    }

    public void sendNewDataToPresenter(DataPresenter presenter){
        if(newPostsReady&&postBitmapsReady){
            presenter.setData(posts, users, false);
            newPostsReady = false;
            postBitmapsReady = false;
        }
    }

   }
