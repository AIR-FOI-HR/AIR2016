package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;
import auth.UsernameAvailabilityCallback;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import static android.content.ContentValues.TAG;

public class LoginTest extends AppCompatActivity {
    TextView userID;
    GetPostData getPostData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_test);
        userID=(TextView)findViewById(R.id.userid);
        FirebaseUser user=FirebaseAuth.getInstance().getCurrentUser();
        userID.setText(user.getUid());

        getPostData = new GetPostData();
    }

    public void logout(View view){
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        finish();
    }

    public void addTree (View view){
        /*Intent open = new Intent(LoginTest.this, AddTree.class);
        startActivity(open);*/

        getPostData.getPost("oEyhr7OjvnDKB5vuA8ie", new PostCallback() {
            @Override
            public void onCallback(Post post) {
                if (post != null) {
                    Log.d("dokument", "Opis dokumenta" + post.getOpis());
                }
                else{
                    Log.d("dokument", "Nema dokumenta.");
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
}