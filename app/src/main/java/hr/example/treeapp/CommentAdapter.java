package hr.example.treeapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.core.entities.Comment;
import com.example.core.entities.User;

import java.util.List;
import com.example.core.entities.Comment;


public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;
    private List<Comment> mData;

    public CommentAdapter(Context mContext, List<Comment> mData) {
        this.mContext = mContext;
        this.mData = mData;
    }
    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_comment, parent, false);
        return new CommentViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        String userId = mData.get(position).getKorisnik_ID();
        UserRepository userRepository = new UserRepository();
        userRepository.getUserImage(userId, new ProfileImageCallback() {
            @Override
            public void onCallbackList(UserImage userImage) {
                if(userImage.image!=null && userImage.url==null)
                    Glide.with(mContext).load(userImage.image).into(holder.userImage);
                if(userImage.url!=null && userImage.image==null){
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round);
                    Glide.with(mContext).load(userImage.url).apply(options).into(holder.userImage);
                }
            }
        });

        userRepository.getUser(userId, new UserCallback() {
            @Override
            public void onCallback(User user) {
                holder.username.setText(user.korisnickoIme);
            }
        });

        holder.commentContent.setText(mData.get(position).getTekst());
        holder.date.setText(mData.get(position).getDatum());

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class CommentViewHolder extends  RecyclerView.ViewHolder {

        ImageView userImage;
        TextView username, commentContent, date;


        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage=itemView.findViewById(R.id.profile_image);
            username = itemView.findViewById(R.id.commentUsername);
            commentContent= itemView.findViewById(R.id.commentText);
            date = itemView.findViewById(R.id.commentDate);
        }
    }
}
