package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import auth.UsernameAvailabilityCallback;
import recyclerview.PostItem;
import recyclerview.PostRecyclerAdapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.MapView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

import static android.content.ContentValues.TAG;

public class LoginTest extends AppCompatActivity {
    TextView userID;
    GetPostData getPostData;
    RecyclerView recyclerView;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);
        userID=(TextView)findViewById(R.id.userid);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        userID.setText(user.getUid());
        context = this.context;

        getPostData = new GetPostData();

        recyclerView = findViewById(R.id.main_recycler);
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void addTree (View view){
        /*Intent open = new Intent(LoginTest.this, AddTree.class);
            startActivity(open);*/

        loadData();
    }

    private void loadData(){
        getPostData.getPost("oEyhr7OjvnDKB5vuA8ie", new PostCallback() {
            @Override
            public void onCallback(Post post) {
                if (post != null) {

                    List<Post> postList = new ArrayList<>();
                    postList.add(post);

                    List<PostItem> postItems = new ArrayList<>();
                    for(Post p : postList){
                        postItems.add(new PostItem(post));
                    }
                    recyclerView.setAdapter(new PostRecyclerAdapter(postItems, context));
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                }
                else{
                    Log.d("dokument", "Nema dokumenta objave.");
                }
            }
        });

        getPostData.getPostComments("oEyhr7OjvnDKB5vuA8ie", new CommentCallback() {
            @Override
            public void onCallback(List<Comment> comment) {
                if (comment != null) {
                    for(Comment c : comment){
                        Log.d("komentar", "komentari:" + c.getTekst());
                    }
                }
                else{
                    Log.d("komentar", "Nema komentara.");
                }
            }
        });
    }


    public void mapView (View view){
        Intent openMapview = new Intent(
                LoginTest.this,
                PostMapView.class
        );
        startActivity(openMapview);
    }
}