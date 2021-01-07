package hr.example.treeapp;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.core.entities.User;


public class UserProfileFragment extends Fragment {
    User selectedUser;
    String userID;
    UserRepository userRepository;
    TextView textViewName;
    TextView textViewUserName;
    ImageView imageViewProfil;
    TextView textViewPoints;
    TextView textViewPosts;
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
        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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