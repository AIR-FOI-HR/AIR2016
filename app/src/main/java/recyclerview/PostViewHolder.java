package recyclerview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import auth.User;
import hr.example.treeapp.Post;
import hr.example.treeapp.R;

public class PostViewHolder extends RecyclerView.ViewHolder {
    ImageView postImage;
    ImageView profileImage;
    TextView username;
    TextView postDescription;
    private View itemView;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        postImage = itemView.findViewById(R.id.post_image);
        profileImage = itemView.findViewById(R.id.profile_image);
        username = itemView.findViewById(R.id.username);
        postDescription = itemView.findViewById(R.id.post_description);
        this.itemView = itemView;
    }

    public void bindToData(Post post, User user, StorageReference pictureReference){
        username.setText(user.korisnickoIme);
        postDescription.setText(post.getOpis());

        Picasso.with(itemView.getContext())
                .load(String.valueOf(pictureReference))
                .into(postImage);

        Picasso.with(itemView.getContext())
                .load(user.profilnaSlika)
                .into(profileImage);
    }
}
