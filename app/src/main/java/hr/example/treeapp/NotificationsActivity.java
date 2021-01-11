package hr.example.treeapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.example.core.entities.Notification;

import java.util.ArrayList;
import java.util.List;

public class NotificationsActivity extends AppCompatActivity implements NotificationAdapter.OnItemClicked {
    private RecyclerView notificationRecyclerView;
    private NotificationAdapter notificationAdapter;
    private UserRepository userRepository = new UserRepository();
    private RelativeLayout noNotifications;
    private List<Notification> notifications = new ArrayList<Notification>();
    private Button refreshButton;
    private ImageButton clearButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        notificationRecyclerView = findViewById(R.id.notificationRecyclerView);
        noNotifications = findViewById(R.id.noNotificationsRelativeLayout);
        refreshButton = findViewById(R.id.refreshButton);
        clearButton = findViewById(R.id.clearNotificationsButton);
        //createMockData();
        fetchNotificatons();

        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fetchNotificatons();
            }
        });
    }

    private void setNotificationsAdapter(List<Notification> notificationsList) {
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter= new NotificationAdapter(getApplicationContext(), notificationsList,this::onItemClick);
        notificationRecyclerView.setAdapter(notificationAdapter);
    }

    private void fetchNotificatons() {
        userRepository.getCurrentUserNotifications(new NotificationsCallback() {
            @Override
            public void onCallback(List<Notification> notificationList) {
                if(notificationList!=null){
                    setNotificationsAdapter(notificationList);
                    notifications=notificationList;
                    noNotifications.setVisibility(View.GONE);
                    notificationRecyclerView.setVisibility(View.VISIBLE);
                    clearButton.setVisibility(View.VISIBLE);
                }
            }
        });
    }
    @Override
    public void onItemClick(int position) {
        String postId = notifications.get(position).getPostId();
        Intent singlePostView = new Intent(NotificationsActivity.this, SinglePostViewActivity.class);
        singlePostView.putExtra("postId",postId);
        startActivity(singlePostView);
    }
   /** private void createMockData() {
        Date date = new Date();
        date.setTime(
                122221321
        );
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "okp3IrFw5iP52sdFg209uok3T4I2","26MOeG0FV8CWp07qqGyD",
                NotificationType.comment,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "3Xw4gd2CkCdPgddM9tjQtdmmpEA2","26MOeG0FV8CWp07qqGyD",
                NotificationType.leaf,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "93TUiDBhxATivWObkowFkKWC0ku1","26MOeG0FV8CWp07qqGyD",
                NotificationType.leaf,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "ARvJOct6v1bj9YZB5LrEpdZ0COk1","26MOeG0FV8CWp07qqGyD",
                NotificationType.comment,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "B725sSarQLQlGBop5JvNaS9b5cu1","26MOeG0FV8CWp07qqGyD",
                NotificationType.leaf,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "BiU29vEAKNNs4nE9a41I8ritBZ02","26MOeG0FV8CWp07qqGyD",
                NotificationType.comment,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "PjLr4Bjec8ftJkRKapfkQfE9PqB3","26MOeG0FV8CWp07qqGyD",
                NotificationType.leaf,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "aTQjGfPj47M0EKwxvXsw07je9qe2","26MOeG0FV8CWp07qqGyD",
                NotificationType.leaf,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "bKuvnY0Qn4UVrJDDVZtDHEpCf4X2","26MOeG0FV8CWp07qqGyD",
                NotificationType.comment,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "okp3IrFw5iP52sdFg209uok3T4I2","26MOeG0FV8CWp07qqGyD",
                NotificationType.comment,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "qXOkkh3Fh2XuVPGPe0TPX3jOxc72","26MOeG0FV8CWp07qqGyD",
                NotificationType.leaf,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "rZeR7U1aiGeatawfR9zB7h4qEqL2","26MOeG0FV8CWp07qqGyD",
                NotificationType.comment,date,false));
        notificationsList.add(new Notification("not1","B725sSarQLQlGBop5JvNaS9b5cu1", "zS88jZq7QVbPwYPS3PU8vg1CSgw1","26MOeG0FV8CWp07qqGyD",
                NotificationType.comment,date,false));
    }*/
}