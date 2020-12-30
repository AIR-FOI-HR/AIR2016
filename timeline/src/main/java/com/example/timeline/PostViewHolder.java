package com.example.timeline;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.core.entities.User;
import com.example.core.entities.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class PostViewHolder extends RecyclerView.ViewHolder {
    ImageView postImage;
    ImageView profileImage;
    TextView username;
    TextView postDescription;
    RelativeLayout deleteButton;
    private View itemView;
    PostRecyclerAdapter.OnItemClicked onItemClicked;
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    public FirebaseUser user;

    public PostViewHolder(@NonNull View itemView, PostRecyclerAdapter.OnItemClicked onItemClicked) {
        super(itemView);
        postImage = itemView.findViewById(R.id.post_image);
        profileImage = itemView.findViewById(R.id.profile_image);
        username = itemView.findViewById(R.id.username);
        postDescription = itemView.findViewById(R.id.post_description);
        this.onItemClicked=onItemClicked;
        this.itemView = itemView;
        if(getCurrentUserRole()!=2){
            deleteButton=itemView.findViewById(R.id.admindelete);
            deleteButton.setVisibility(View.GONE);
        }
    }

    public void bindToData(Post post, User user){
        username.setText(user.korisnickoIme);
        postDescription.setText(post.getOpis());

        if(post.getURL_slike().contains("https://")) {
            Picasso.with(itemView.getContext())
                    .load(post.getURL_slike())
                    .into(postImage);
            postImage.setScaleType(ImageView.ScaleType.FIT_XY);
        } else {
            Glide.with(itemView.getContext())
                    .load(post.getSlika())
                    .into(postImage);
        }

        if(user.getProfilnaSlika().contains("https://")) {
            Picasso.with(itemView.getContext())
                    .load(user.getProfilnaSlika())
                    .into(profileImage);
        } else {
            Glide.with(itemView.getContext())
                    .load(user.getSlika())
                    .into(profileImage);
        }
    }

    public long getCurrentUserRole() {
        final long[] finali = new long[1];
        firebaseFirestore.collection("Korisnici")
                .document(firebaseAuth.getCurrentUser().getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        long i;
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                i= (long) document.get("Uloga_ID");
                                finali[0] =i;
                            }
                        } else {
                        }
                    }
                });
        return finali[0];
    }
}
