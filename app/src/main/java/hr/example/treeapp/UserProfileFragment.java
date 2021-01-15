package hr.example.treeapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.core.entities.Post;
import com.example.core.entities.User;

import java.util.ArrayList;
import java.util.List;

import auth.RegistrationRepository;
import auth.UsernameAvailabilityCallback;


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
    Context context;
    ImageButton imageButton;
    String test;
    private RegistrationRepository registrationRepository = new RegistrationRepository(context);
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
        context=getContext();
        userInfo();
        getUsersPosts();
        imageButton=view.findViewById(R.id.popUpButtonProfile);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPopupProfile(view);
            }
        });

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

    public void showPopupProfile(View v) {
        PopupMenu popup = new PopupMenu(context, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.userprofilepopupmenu, popup.getMenu());
        if(!getPostData.getCurrentUserID().equals(userID)) {
            popup.getMenu().findItem(R.id.changeprofilepic).setVisible(false);
            popup.getMenu().findItem(R.id.changeuserdata).setVisible(false);
            popup.getMenu().findItem(R.id.changepassword).setVisible(false);
        }
        else{
            popup.getMenu().findItem(R.id.changeprofilepic).setVisible(true);
            popup.getMenu().findItem(R.id.changeuserdata).setVisible(true);
            popup.getMenu().findItem(R.id.changepassword).setVisible(true);
        }
        popup.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener ()
        {
            @Override
            public boolean onMenuItemClick (MenuItem item)
            {
                int id = item.getItemId();
                switch (id)
                {
                    case R.id.changeprofilepic:  break;
                    case R.id.changeuserdata: changeUserData(); break;
                    case R.id.changepassword: changeUserPassword(); break;
                }
                return true;
            }
        });
        popup.show();
    }

    private void changeUserData(){
        userRepository.getCurrentUser(new UserCallback() {
            @Override
            public void onCallback(User user) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                TextView textView = new TextView(context);
                textView.setText(context.getString(R.string.changeuserdata));
                textView.setPadding(20, 30, 20, 30);
                textView.setTextSize(20F);
                textView.setBackgroundColor(getResources().getColor(R.color.baby_green));
                textView.setTextColor(getResources().getColor(R.color.tree_green));

                builder.setCustomTitle(textView);
                View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_userdata, (ViewGroup) getView(), false);

                final EditText changeuserdataName = (EditText) viewInflated.findViewById(R.id.changeuserdataName);
                changeuserdataName.setText(user.ime);
                final EditText changeuserdataSurname = (EditText) viewInflated.findViewById(R.id.changeuserdataSurname);
                changeuserdataSurname.setText(user.prezime);
                final EditText changeuserdataUsername = (EditText) viewInflated.findViewById(R.id.changeuserdataUsername);
                changeuserdataUsername.setText(user.korisnickoIme);

                builder.setView(viewInflated);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v)
                    {
                        String ime=changeuserdataName.getText().toString();
                        String prezime=changeuserdataSurname.getText().toString();
                        String korime=changeuserdataUsername.getText().toString();
                        if(changeuserdataName!=null && changeuserdataSurname!=null && changeuserdataUsername!=null) {
                            if(!user.korisnickoIme.equals(korime)){
                                registrationRepository.checkUsernameAvailability(korime, new UsernameAvailabilityCallback() {
                                    @Override
                                    public void onCallback(String value) {
                                        if (value.equals("Dostupno")) {
                                            user.ime = ime;
                                            user.prezime = prezime;
                                            user.korisnickoIme = korime;
                                            userRepository.changeUserDataFirebase(user);
                                            userRepository.getCurrentUser(new UserCallback() {
                                                @Override
                                                public void onCallback(User user) {
                                                    textViewName.setText(user.ime + " " + user.prezime);
                                                    textViewUserName.setText(user.korisnickoIme);
                                                    dialog.dismiss();
                                                }
                                            });
                                        } else
                                            changeuserdataUsername.setError(context.getString(R.string.username_taken));
                                    }
                                });
                            }
                            else {
                                user.ime = ime;
                                user.prezime = prezime;
                                user.korisnickoIme = korime;
                                userRepository.changeUserDataFirebase(user);
                                userRepository.getCurrentUser(new UserCallback() {
                                    @Override
                                    public void onCallback(User user) {
                                        textViewName.setText(user.ime + " " + user.prezime);
                                        textViewUserName.setText(user.korisnickoIme);
                                        dialog.dismiss();
                                    }
                                });
                            }
                        }
                    }
                });
            }
        });
    }

    private void changeUserPassword(){
        final String[] error = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        TextView textView = new TextView(context);
        textView.setText(context.getString(R.string.changepassword));
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(getResources().getColor(R.color.baby_green));
        textView.setTextColor(getResources().getColor(R.color.tree_green));

        builder.setCustomTitle(textView);
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.dialog_change_password, (ViewGroup) getView(), false);
        final EditText changeuserdataNewPassword = (EditText) viewInflated.findViewById(R.id.changeuserdataNewPassword);
        final EditText changeuserdataNewPasswordRepeat = (EditText) viewInflated.findViewById(R.id.changeuserdataNewPasswordRepeat);

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
                //asd
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        final AlertDialog dialog = builder.create();
        dialog.show();

        dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                String newPass =changeuserdataNewPassword.getText().toString();
                String repeatPass=changeuserdataNewPasswordRepeat.getText().toString();
                if(!newPass.equals("") && !repeatPass.equals("")) {
                    if (registrationRepository.passwordNotCorrectFormat(newPass)==false) {
                        if (newPass.equals(repeatPass)) {
                            userRepository.changeUserPasswordFirebase(repeatPass);
                            dialog.dismiss();
                        } else {
                            changeuserdataNewPasswordRepeat.setError(context.getString(R.string.invalid_password_repeat));
                        }
                    } else {
                        changeuserdataNewPassword.setError(context.getString(R.string.invalid_password));
                    }
                }
            }
        });
    }


}