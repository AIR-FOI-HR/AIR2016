package hr.example.treeapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.core.entities.Comment;
import com.example.core.entities.Post;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import com.example.core.entities.User;
import com.google.protobuf.StringValue;


public class SinglePostViewActivity extends AppCompatActivity implements OnMapReadyCallback {
    private String postId;
    private Post post;
    private  Bitmap image;
    private TextView username;
    private GetPostData getPostData;

    private ImageView postImage;
    private ImageView postComment;
    private RecyclerView commentRecyclerView;
    private CommentAdapter commentAdapter;
    private ImageView profilePicture;
    private TextView numberOfLikes;
    private TextView description;
    private TextView userPoints;
    private ImageView leafImage;
    private GoogleMap map;
    private boolean isBig = false;
    private boolean userLikedPictureFlag=false;
    LatLng treeLocation;
    private List<String> likesList;
    Context context=this;
    UserRepository userRepository= new UserRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_post_view);
        postId  = getIntent().getStringExtra("postId");
        postImage = findViewById(R.id.postImageView);
        getPostData = new GetPostData();

        commentRecyclerView = findViewById(R.id.commentRecycleView);
        username = findViewById(R.id.usernameText);
        profilePicture = findViewById(R.id.profile_image);
        numberOfLikes = findViewById(R.id.numberOfLeafsText);
        description = findViewById(R.id.treeDescriptionText);
        userPoints = findViewById(R.id.userPoints);
        leafImage = findViewById(R.id.leafIconButton);
        postComment = findViewById(R.id.postNewComment);
        leafImage.setImageResource(R.drawable.leaf_green);

        //tamnaPozadina= findViewById(R.id.tamnaPozadina);
        initMap();
        getPost();
        postImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!isBig){
                    ViewGroup.LayoutParams params = postImage.getLayoutParams();
                    params.height = image.getHeight();
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
        refreshLikeStatus();
        leafImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!userLikedPictureFlag)
                    getPostData.likePost(postId);
                if(userLikedPictureFlag)
                    getPostData.removeLikeOnPost(postId);
                refreshLikeStatus();
            }
        });
        initCommentRecycleView();

        getUsersLiked();

        
    }

    private void getUsersLiked() {

        getPostData.getUsersLiked(postId, new GetLikesForPostCallback() {
            @Override
            public void onCallback(List<String> listOfLikesByUserID) {
                List<String> lista=listOfLikesByUserID;
                Toast.makeText(context,lista.toString(), Toast.LENGTH_LONG).show();
            }
        });

        /*userRepository.getUser("LHAJZDU9gfXVzCD9ICJmj8YBZRN2", new UserCallback() {
            @Override
            public void onCallback(User user) {
                if(user==null)
                    Toast.makeText(context,"user.uid",Toast.LENGTH_LONG).show();
            }
        });*/
    }

    private void comment(){
        boolean userIsAnon = userRepository.isCurrentUserAnonymous();
        if(userRepository.isCurrentUserAnonymous())

    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.treeLocationMapView);
        mapFragment.getMapAsync(this);
    }
    private void initCommentRecycleView() {
        commentRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        getPostData.getPostComments(postId, new CommentCallback() {
            @Override
            public void onCallback(List<Comment> comment) {
                commentAdapter= new CommentAdapter(getApplicationContext(),comment);
                commentRecyclerView.setAdapter(commentAdapter);
            }
        });

    }

    private void refreshLikeStatus(){
        getPostData.hasUserLikedPost(postId, new CheckIfUserLikedPhotoCallback() {
            @Override
            public void onCallback(Boolean userLikedPhoto) {
                if(userLikedPhoto){
                    userLikedPictureFlag=true;
                    leafImage.setImageResource(R.drawable.leaf_full);
                }
                else{
                    userLikedPictureFlag=false;
                    leafImage.setImageResource(R.drawable.leaf_transparent);
                }

            }
        });
        likesList= new ArrayList<>();
        getPostData.getPostLikes(postId, new GetLikesForPostCallback() {
            @Override
            public void onCallback(List<String> listOfLikesByUserID) {
                likesList=listOfLikesByUserID;
                if(likesList.isEmpty())
                    numberOfLikes.setText("0");
                else{
                    String likes= String.valueOf(listOfLikesByUserID.size());
                    numberOfLikes.setText(likes);
                }

            }
        });
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
        Float zoomLvl = (float)15;
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

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.setMapType(GoogleMap.MAP_TYPE_HYBRID);
    }
}