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
import com.example.core.entities.Notification;
import com.example.core.entities.NotificationType;
import com.example.core.entities.User;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>{

    private Context mContext;
    private List<Notification> mData;
    private UserRepository userRepository = new UserRepository();

    public NotificationAdapter(Context mContext, List<Notification> mData) {
        this.mContext=mContext;
        this.mData=mData;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View row = LayoutInflater.from(mContext).inflate(R.layout.row_notification, parent, false);
        return new NotificationViewHolder(row);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder holder, int position) {
        String postId = mData.get(position).getPostId();
        String senderId = mData.get(position).getSenderId();
        Date timestamp =mData.get(position).getTimestamp();
        NotificationType type = mData.get(position).getType();

        if(type.equals(NotificationType.comment)){
            Glide.with(mContext).load(R.drawable.comment_notification).into(holder.notificationIcon);
            holder.notificationText.setText(R.string.comment_notification);
        }
        if(type.equals(NotificationType.leaf)){
            Glide.with(mContext).load(R.drawable.leaf_notification).into(holder.notificationIcon);
            holder.notificationText.setText(R.string.leaf_notification);
        }
        if(checkIfTimestampWasToday(timestamp))
            holder.timestamp.setText(timestamp.getHours()+":"+timestamp.getMinutes());
        else
            holder.timestamp.setText(timestamp.getDate()+"."+(timestamp.getMonth()+1)+"."+(timestamp.getYear()+1900)+".");

        userRepository.getUser(senderId, new UserCallback() {
            @Override
            public void onCallback(User user) {
                holder.username.setText(user.korisnickoIme);
                userRepository.getUserImage(user.uid, new ProfileImageCallback() {
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
            }
        });

        holder.notificationText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO: otvoriti aktivnosti SinglePostView
                /**
                Intent singlePostView = new Intent(mContext, SinglePostViewActivity.class);
                singlePostView.putExtra("postId",postId);
                View.
                 */
            }
        });
    }

    private boolean checkIfTimestampWasToday(Date timestamp) {
       Date today = Calendar.getInstance().getTime();
        return timestamp.getDate() == today.getDate() && timestamp.getMonth() == today.getMonth() && timestamp.getYear() == today.getYear();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class NotificationViewHolder extends RecyclerView.ViewHolder{
        ImageView userImage, notificationIcon;
        TextView username, notificationText, timestamp;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            userImage = itemView.findViewById(R.id.profile_image_notification);
            notificationIcon = itemView.findViewById(R.id.icon_notification);
            username=itemView.findViewById(R.id.username_notification);
            notificationText = itemView.findViewById(R.id.content_notification);
            timestamp = itemView.findViewById(R.id.timestamp_notification);
        }
    }
}
