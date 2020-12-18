package recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import auth.User;
import hr.example.treeapp.Post;
import hr.example.treeapp.PostCallback;
import hr.example.treeapp.R;
import hr.example.treeapp.UserCallback;
import hr.example.treeapp.UserRepository;

public class PostRecyclerAdapter extends RecyclerView.Adapter<PostViewHolder> {
    private ArrayList<PostItem> postItemList;
    private Context context;
    UserRepository userRepository;

    public PostRecyclerAdapter(@NonNull List<PostItem> postItemList, Context context) {
        this.postItemList = (ArrayList<PostItem>) postItemList;
        this.context = context;
        userRepository = new UserRepository();
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());

        View view = layoutInflater.inflate(R.layout.post_list_item, parent, false);
        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        final Post post = postItemList.get(position);
        userRepository.getUser(post.getKorisnik_ID(), new UserCallback() {
            @Override
            public void onCallback(User user, StorageReference pictureReference) {
                if (user != null) {
                    holder.bindToData(post, user, pictureReference);
                }
                else{
                    Log.d("greska", "Nema kreiranja nove objave.");
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return postItemList == null? 0: postItemList.size();
    }
}
