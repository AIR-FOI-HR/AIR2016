package hr.example.treeapp;


import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;

import android.graphics.Point;
import android.os.Bundle;;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.PopupMenu;

import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.core.entities.Comment;
import com.example.core.entities.Post;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import com.example.core.entities.User;

import hr.example.treeapp.addTree.AddTreeLogic;


public class SinglePostViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String postId;
    private Post post;
    private  Bitmap image;
    private TextView username;
    private long bodovi;

    private GetPostData getPostData= new GetPostData();
    private String userID;
    private String userCommentPointsID;
    private String userLikePointsID;

    private ImageView postImage;
    private Button postComment;
    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private ReactionAdapter reactionAdapter;
    private ImageView profilePicture;
    private TextView numberOfLikes;
    private TextView description;
    private TextView userPoints;
    private ImageView leafImage;
    private TextView commentText;
    private GoogleMap map;
    private boolean isBig = false;
    private boolean userLikedPictureFlag=false;
    private RecyclerView reactionsRecyclerView;
    private List<String> likesList;
    private Context context=this;
    private UserRepository userRepository= new UserRepository();
    private AddTreeLogic addTreeLogic=new AddTreeLogic(this);
    private boolean userIsAnonymous;
    private boolean reactionsVisible =false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post_view);
        postId  = getIntent().getStringExtra("postId");
        postImage = findViewById(R.id.postImageView);

        commentRecyclerView = findViewById(R.id.commentRecycleView);
        username = findViewById(R.id.usernameText);
        profilePicture = findViewById(R.id.profile_image);
        numberOfLikes = findViewById(R.id.numberOfLeafsText);
        description = findViewById(R.id.treeDescriptionText);
        userPoints = findViewById(R.id.userPoints);

        leafImage = findViewById(R.id.leafIconButton);
        commentText = findViewById(R.id.newCommentTextBox);
        reactionsRecyclerView= findViewById(R.id.reactionsRecyclerView);

        postComment = findViewById(R.id.postNewComment);
        checkIfUserIsAnonymous();
        postComment.setEnabled(!userIsAnonymous);

        leafImage.setImageResource(R.drawable.leaf_green);

        initMap();


        //leafImage = findViewById(R.id.leafIconImageView);
        //leafImage.setImageResource(R.drawable.leaf_green);
        //tamnaPozadina= findViewById(R.id.tamnaPozadina);

        getPost();

        refreshLikeStatus();

        initCommentRecycleView();

        initClickListeners();

        reactionsRecyclerView.setLayoutManager(new LinearLayoutManager(context));

    }
    private void initClickListeners(){
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isBig){
                    Point displaySize = getDisplayWidth();
                    ViewGroup.LayoutParams params = postImage.getLayoutParams();
                    params.height = displaySize.x;
                    postImage.setScaleType(ImageView.ScaleType.FIT_XY);
                    postImage.setLayoutParams(params);
                    postImage.setImageBitmap(image);
                    isBig=true;
                }
                else{
                    ViewGroup.LayoutParams params = postImage.getLayoutParams();
                    final float scale = SinglePostViewActivity.this.getResources().getDisplayMetrics().density;
                    params.height = (int) (120*scale+0.5f);
                    postImage.setImageBitmap(image);
                    postImage.setScaleType(ImageView.ScaleType.CENTER_CROP);
                    postImage.setLayoutParams(params);
                    isBig=false;
                }

            }
        });

        leafImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userLikedPictureFlag) {
                    getPostData.likePost(postId);
                    userLikePointsID = post.getKorisnik_ID();
                    bodovi = 1;
                    userRepository.updatePointsForComment(userLikePointsID, bodovi);
                }

                if(userLikedPictureFlag){
                    getPostData.removeLikeOnPost(postId);
                    userLikePointsID=post.getKorisnik_ID();
                    bodovi=-1;
                    userRepository.updatePointsForComment(userLikePointsID, bodovi);
                    refreshLikeStatus();
                    }
            }
        });

        postComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(userIsAnonymous)
                    Toast.makeText(context, R.string.feature_not_available, Toast.LENGTH_LONG).show();
                else{
                    if(commentText.getText().length()==0)
                        Toast.makeText(context,"You have to write a comment first!", Toast.LENGTH_LONG).show();
                    else{
                        postComment();
                    }

                }
            }
        });
        numberOfLikes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!reactionsVisible){
                    getUsersLiked();
                    reactionsVisible=true;
                }
                else{
                    reactionsVisible=false;
                    reactionsRecyclerView.setVisibility(View.GONE);
                }
            }
        });
    }

    private Point getDisplayWidth() {
        Display display   = getWindowManager().getDefaultDisplay();
        Point displaySize = new Point();
        display.getSize(displaySize);
        return displaySize;
    }

    private void postComment() {
        String text=commentText.getText().toString();
        getPostData.postComent(postId,text);
        userCommentPointsID=post.getKorisnik_ID();
        bodovi=5;
        userRepository.updatePointsForComment(userCommentPointsID, bodovi);
        Toast.makeText(context, "Commented!", Toast.LENGTH_SHORT).show();
        updateCommentRecycleView(text);
    }

    private void updateCommentRecycleView(String text) {
        String currentUserID = userRepository.getCurrentUserID();
        Comment cmnt= new Comment(currentUserID,currentUserID,text, getString(R.string.now));
        commentText.setText(null);
        commentText.clearFocus();
        commentAdapter.mData.add(0,cmnt);
        commentAdapter.notifyItemInserted(0);
        commentAdapter.notifyDataSetChanged();
    }

    /**
     * Metoda koja provjerava je li korisnik gost/anoniman
     * uz pomoć calllback funkcije postavlja vrijednost u varijablu userIsAnonymous koja se koristi kod omoguććavanja dodavanja komentara
     */
    private void checkIfUserIsAnonymous(){
        userRepository.isCurrentUserAnonymous(new UserAnonymousCallback() {
            @Override
            public void onCallback(boolean isAnonymous) {
                userIsAnonymous=isAnonymous;
            }
        });
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.treeLocationMapView);
        mapFragment.getMapAsync(this);
    }

    private void initCommentRecycleView() {
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        getComments();

    }

    private void getComments() {
        getPostData.getPostComments(postId, new CommentCallback() {
            @Override
            public void onCallback(List<Comment> comment) {
                commentAdapter.postID=postId;
                commentAdapter= new CommentAdapter(getApplicationContext(),comment);
                commentRecyclerView.setAdapter(commentAdapter);
            }
        });
    }



    private void getUsersLiked() {

        getPostData.getUsersLiked(postId, new GetLikesForPostCallback() {
            @Override
            public void onCallback(List<String> listOfLikesByUserID) {
                reactionAdapter = new ReactionAdapter(getApplicationContext(), listOfLikesByUserID);
                reactionsRecyclerView.setAdapter(reactionAdapter);

            }
        });
        reactionsRecyclerView.setVisibility(View.VISIBLE);
    }


    private void refreshLikeStatus() {
        getPostData.hasUserLikedPost(postId, new CheckIfUserLikedPhotoCallback() {
            @Override
            public void onCallback(Boolean userLikedPhoto) {
                if (userLikedPhoto) {
                    userLikedPictureFlag = true;
                    leafImage.setImageResource(R.drawable.leaf_full);
                } else {
                    userLikedPictureFlag = false;
                    leafImage.setImageResource(R.drawable.leaf_transparent);
                }

            }
        });
        likesList = new ArrayList<>();
        getPostData.getPostLikes(postId, new GetLikesForPostCallback() {
            @Override
            public void onCallback(List<String> listOfLikesByUserID) {
                likesList = listOfLikesByUserID;
                if (likesList.isEmpty())
                    numberOfLikes.setText("0");
                else {
                    String likes = String.valueOf(listOfLikesByUserID.size());
                    numberOfLikes.setText(likes);
                }

            }
        });
    }

    public void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.postpopupmenu, popup.getMenu());
        getPostData.getCurrentUserRole(new CurrentUserRoleCallback() {
            @Override
            public void onCallback(int userRole) {
                if(userRole!=1  && !userRepository.getCurrentUserID().equals(userID)) {
                    popup.getMenu().findItem(R.id.postpopupdelete).setVisible(false);
                }
                else{
                    popup.getMenu().findItem(R.id.postpopupdelete).setVisible(true);
                }
            }
        });
        if(!userRepository.getCurrentUserID().equals(userID)){
            popup.getMenu().findItem(R.id.postpopupdescription).setVisible(false);
            popup.getMenu().findItem(R.id.postpopuplocation).setVisible(false);
        }
        else{
            popup.getMenu().findItem(R.id.postpopupdescription).setVisible(true);
            popup.getMenu().findItem(R.id.postpopuplocation).setVisible(true);
        }
        popup.setOnMenuItemClickListener (new PopupMenu.OnMenuItemClickListener ()
        {
            @Override
            public boolean onMenuItemClick (MenuItem item)
            {
                int id = item.getItemId();
                switch (id)
                {
                    case R.id.postpopupdelete: deletePost(); break;
                    case R.id.postpopupdescription: changeDescription(); break;
                    case R.id.postpopuplocation: changeLocation(); break;
                }
                return true;
            }
        });
        popup.show();
    }

    public void deletePost(){
        getPostData.deletePost(postId);
        finish();
    }

    private void getPost(){
        getPrimaryPostData();
    }

    private void getPrimaryPostData() {
        getPostData.getPost(postId, new PostCallback() {
            @Override
            public void onCallback(Post callbackPost) {
                if (callbackPost != null) {
                    post = callbackPost;
                    getPostImage();
                    getUser();
                    populatePostData();
                }
                else {
                    Toast.makeText(SinglePostViewActivity.this, R.string.Error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void populatePostData() {
        numberOfLikes.setText(Long.valueOf(post.getBroj_lajkova()).toString());
        description.setText(post.getOpis());

        final com.google.android.gms.maps.model.LatLng treeLocation =
                new com.google.android.gms.maps.model.LatLng(post.getLatitude(),
                        post.getLongitude());

        map.addMarker(new MarkerOptions()
                .position(treeLocation)
                .draggable(false)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));
        float zoomLvl = (float)15;
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(treeLocation,zoomLvl));
    }

    private void getPostImage(){
        getPostData.getPostImage(post.getURL_slike(), new PostImageCallback() {
            @Override
            public void onCallback(Bitmap slika) {
                if(slika != null) {
                    image= slika;
                    Glide.with(SinglePostViewActivity.this).load(image).into(postImage);
                }
                else
                    Toast.makeText(SinglePostViewActivity.this, R.string.Error_loading_picture, Toast.LENGTH_LONG).show();
            }
        });
    }

    private void getUser(){
        UserRepository userRepository = new UserRepository();
        userRepository.getUser(post.getKorisnik_ID(), new UserCallback() {
            @Override
            public void onCallback(User user) {
                userID=user.uid;
                username.setText(user.korisnickoIme);
                String bodovi = user.bodovi +" "+ getString(R.string.points);
                userPoints.setText(bodovi);
                getUserImageFromFirebase(userRepository, user);
            }
        });
    }

    private void getUserImageFromFirebase(UserRepository userRepository, User user) {
        userRepository.getUserImage(user.uid, new ProfileImageCallback() {
            @Override
            public void onCallbackList(UserImage userImage) {
                if(userImage.image!=null && userImage.url==null)
                    Glide.with(SinglePostViewActivity.this).load(userImage.image).into(profilePicture);
                else if (userImage.url!=null && userImage.image==null) {
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round);
                    Glide.with(SinglePostViewActivity.this).load(userImage.url).apply(options).into(profilePicture);
                }
            }
        });
    }


    private void changeLocation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_change_location, (ViewGroup) findViewById(android.R.id.content).getRootView(), false);
        Mapa mapa = new Mapa(post);

        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
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
            public void onClick(View v) {
                addTreeLogic.UpdatePostLocation(post.getID_objava(), mapa.marker.getPosition().latitude, mapa.marker.getPosition().longitude);
                Log.i("positive", "onClick: "+mapa.marker.getPosition().latitude);
                dialog.dismiss();

            }
        });
    }
public class Mapa implements OnMapReadyCallback{
    private GoogleMap gMap;
    private Post post;
    public Marker marker;
    public Mapa(Post post){
        this.post=post;

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.treeLocationMapView2);
        mapFragment.getMapAsync(this);
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.gMap=googleMap;
        final com.google.android.gms.maps.model.LatLng treeLocation =
                new com.google.android.gms.maps.model.LatLng(post.getLatitude(),
                        post.getLongitude());
        gMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);


       marker=gMap.addMarker(new MarkerOptions()
                .position(treeLocation)
                .draggable(true)
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.location_marker)));

        float zoomLvl = (float)15;
        gMap.animateCamera(CameraUpdateFactory.newLatLngZoom(treeLocation,zoomLvl));
    }
}
    private void changeDescription() {
        final String[] error = new String[1];
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        TextView textView = new TextView(context);
        textView.setText(context.getString(R.string.changedescription));
        textView.setPadding(20, 30, 20, 30);
        textView.setTextSize(20F);
        textView.setBackgroundColor(getResources().getColor(R.color.baby_green));
        textView.setTextColor(getResources().getColor(R.color.tree_green));

        builder.setCustomTitle(textView);
        View viewInflated = LayoutInflater.from(this).inflate(R.layout.dialog_change_description, (ViewGroup) findViewById(android.R.id.content).getRootView(), false);
        final EditText changeDescription = (EditText) viewInflated.findViewById(R.id.changeDescription);
        changeDescription.setText(post.getOpis());
        changeDescription.requestFocus();
        builder.setView(viewInflated);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int which) {
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
            public void onClick(View v) {
                String newDescription = changeDescription.getText().toString();
                addTreeLogic.UpdatePostDescription(post.getID_objava(), newDescription);
                description.setText(newDescription);
                dialog.dismiss();
                /*finish();
                overridePendingTransition(0, 0);
                startActivity(getIntent());
                overridePendingTransition(0, 0);*/


            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}