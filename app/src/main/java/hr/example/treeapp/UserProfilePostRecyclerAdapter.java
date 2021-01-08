package hr.example.treeapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.core.entities.Post;

import java.util.List;

public class UserProfilePostRecyclerAdapter extends RecyclerView.Adapter<UserProfilePostRecyclerAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Post post);
    }
    private List<Post> usersPostsList;
    private LayoutInflater mInflater;
    private OnItemClickListener listener;
    Context mContext;
    // data is passed into the constructor
    UserProfilePostRecyclerAdapter(Context context, List<Post> usersPostsList, OnItemClickListener listener) {
        this.mContext=context;
        this.mInflater = LayoutInflater.from(context);
        this.usersPostsList=usersPostsList;
        this.listener=listener;
    }

    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.user_profile_post_item, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each cell
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(usersPostsList.get(position), listener);
        String url=usersPostsList.get(position).getURL_slike();
        GetPostData getPostData=new GetPostData();
        getPostData.getPostImage(url, new PostImageCallback() {
            @Override
            public void onCallback(Bitmap slika) {
                if(slika!=null){
                    Glide.with(mContext).load(slika).into(holder.imageViewUserProfilePost);
                }
            }
        });
    }

    // total number of cells
    @Override
    public int getItemCount() {
        return usersPostsList.size();
    }


    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageViewUserProfilePost;
        ViewHolder(View itemView) {
            super(itemView);
            WindowManager wm=(WindowManager) mContext.getSystemService(mContext.WINDOW_SERVICE);
            Point size=new Point();
            Display display=wm.getDefaultDisplay();
            display.getSize(size);
            int width=size.x;
            Log.i("sirina", "ViewHolder: "+width);
            imageViewUserProfilePost=(ImageView)itemView.findViewById(R.id.imageViewUserProfilePost);
            imageViewUserProfilePost.setMinimumWidth(width/3);
            imageViewUserProfilePost.setMinimumHeight(100);
        }
        public void bind(Post post, OnItemClickListener listener) {
            itemView.setOnClickListener(new View.OnClickListener(){
                @Override public void onClick(View v) {
                    listener.onItemClick(post);
                }
            });
        }

    }

}
