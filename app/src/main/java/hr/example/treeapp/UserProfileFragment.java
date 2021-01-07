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
    public UserProfileFragment(User user) {
        this.selectedUser=user;
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
        EditText editTextUsername=(EditText)getView().findViewById(R.id.editTextUserSearch);
        TextView textViewName=(TextView)getView().findViewById(R.id.textViewName);
        TextView textViewUserName=(TextView)getView().findViewById(R.id.textViewUserName);
        ImageView imageViewProfil=(ImageView)getView().findViewById(R.id.imageViewProfilePicture);
        TextView textViewPoints=(TextView)getView().findViewById(R.id.textViewPoints);
        TextView textViewPosts=(TextView)getView().findViewById(R.id.textViewPosts);
        UserRepository userRepository=new UserRepository();
        userRepository.getUserImage(selectedUser.uid, new ProfileImageCallback() {
            @Override
            public void onCallbackList(UserImage userImage) {
                imageViewProfil.setImageBitmap(userImage.image);
            }
        });
        textViewName.setText(selectedUser.ime+" "+selectedUser.prezime);
        textViewUserName.setText("@"+selectedUser.korisnickoIme);
        String b=Long.toString(selectedUser.bodovi);
        textViewPoints.setText(b);

    }
}