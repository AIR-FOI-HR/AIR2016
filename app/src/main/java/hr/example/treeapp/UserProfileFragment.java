package hr.example.treeapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.core.entities.Post;
import com.example.core.entities.User;

import java.util.ArrayList;
import java.util.List;


public class UserProfileFragment extends Fragment {
    User selectedUser;
    String userID;
    UserRepository userRepository;
    TextView textViewName;
    TextView textViewUserName;
    ImageView imageViewProfil;
    TextView textViewPoints;
    TextView textViewPosts;
    GetPostData getPostData;
    RecyclerView myRecyclerView;
    UserProfilePostRecyclerAdapter userProfilePostRecyclerAdapter;
    public UserProfileFragment(User user) {
        this.selectedUser=user;
    }

    public UserProfileFragment(String userID) {
        this.userID=userID;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View inflatedView=inflater.inflate(R.layout.fragment_user_profile, container, false);
        myRecyclerView=(RecyclerView) inflatedView.findViewById(R.id.UserProfilePostsList);
        return inflatedView;
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        userInfo();
        getUsersPosts();

    }

    private void getUsersPosts() {
        //String[] data = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16", "17", "18", "19", "20", "21", "22", "23", "24", "25", "26", "27", "28", "29", "30", "31", "32", "33", "34", "35", "36", "37", "38", "39", "40", "41", "42", "43", "44", "45", "46", "47", "48"};
        int col=3;
        getPostData=new GetPostData();
        getPostData.getUsersPosts(userID, new UsersPostsCallback() {
            @Override
            public void onCallback(List<Post> usersPostsList) {
                if(usersPostsList!=null){
                    textViewPosts.setText(String.valueOf(usersPostsList.size()));
                    myRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), col));
                    userProfilePostRecyclerAdapter=new UserProfilePostRecyclerAdapter(getActivity(), usersPostsList, new UserProfilePostRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(Post post) {
                            Intent singlePostView = new Intent(getActivity(), SinglePostViewActivity.class);
                            singlePostView.putExtra("postId",post.getID_objava());
                            startActivity(singlePostView);
                        }
                    });
                    myRecyclerView.setAdapter(userProfilePostRecyclerAdapter);
                }
            }
        });



    }

    private void userInfo() {
        textViewName=(TextView)getView().findViewById(R.id.textViewName);
        textViewUserName=(TextView)getView().findViewById(R.id.textViewUserName);
        imageViewProfil=(ImageView)getView().findViewById(R.id.imageViewProfilePicture);
        textViewPoints=(TextView)getView().findViewById(R.id.textViewPoints);
        textViewPosts=(TextView)getView().findViewById(R.id.textViewPosts);
        userRepository=new UserRepository();
        //za UserProfileFragment(String userID);
        if(userID!=null){
            userRepository=new UserRepository();
            userRepository.getUser(userID, new UserCallback() {
                @Override
                public void onCallback(User user) {
                    selectedUser=user;
                    textViewName.setText(selectedUser.getIme()+" "+selectedUser.getPrezime());
                    textViewUserName.setText("@"+selectedUser.getKorisnickoIme());
                    textViewPoints.setText(Long.toString(selectedUser.getBodovi()));
                    userRepository.getUserImage(selectedUser.getUid(), new ProfileImageCallback() {
                        @Override
                        public void onCallbackList(UserImage userImage) {
                            imageViewProfil.setImageBitmap(userImage.image);
                        }
                    });

                }
            });
        }
        //za UserProfileFragment(User user)
        else{
            userRepository.getUserImage(selectedUser.uid, new ProfileImageCallback() {
                @Override
                public void onCallbackList(UserImage userImage) {
                    imageViewProfil.setImageBitmap(userImage.image);
                }
            });
            textViewName.setText(selectedUser.ime+" "+selectedUser.prezime);
            textViewUserName.setText("@"+selectedUser.korisnickoIme);
            textViewPoints.setText(Long.toString(selectedUser.bodovi));
        }
    }
}