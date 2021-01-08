package hr.example.treeapp;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.core.entities.User;
import com.google.android.material.tabs.TabLayout;


public class LeaderboardFragment extends Fragment {

        private TabLayout tabLayout;
        private ViewPager viewPager;
        private LeaderboardViewAdapter leaderboardViewAdapter;
        private ImageView imageView;
        private TextView username;
        private TextView points;
        private Context context;


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

                return inflater.inflate(R.layout.fragment_leaderboard, container, false);

        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout=(TabLayout)view.findViewById(R.id.tablayoutID);
        viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        imageView =view.findViewById(R.id.user_image_leaderboard);
        username=view.findViewById(R.id.user_username_leaderboard);
        points=view.findViewById(R.id.user_points_leaderboard);
        leaderboardViewAdapter = new LeaderboardViewAdapter(getParentFragmentManager());
        leaderboardViewAdapter.AddFragment(new LeaderboardLocationFragment(), "Location");
        leaderboardViewAdapter.AddFragment(new LeaderboardScoreFragment(), "Score");
        viewPager.setAdapter(leaderboardViewAdapter);
        tabLayout.setupWithViewPager(viewPager);
        context=view.getContext();
        setUsersData();
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for(int i=0;i<leaderboardViewAdapter.getCount();i++) {
            getFragmentManager().beginTransaction().remove(leaderboardViewAdapter.getItem(i)).commitAllowingStateLoss();
        }
    }

    private void setUsersData() {
            UserRepository userRepository = new UserRepository();

            String currentUserID = userRepository.getCurrentUserID();

        userRepository.getUserImage(currentUserID, new ProfileImageCallback() {
            @Override
            public void onCallbackList(UserImage userImage) {
                if(userImage.image!=null && userImage.url==null) {
                    Glide.with(context).load(userImage.image).into(imageView);
                }
                if(userImage.url!=null && userImage.image==null){
                    RequestOptions options = new RequestOptions()
                            .centerCrop()
                            .placeholder(R.mipmap.ic_launcher_round)
                            .error(R.mipmap.ic_launcher_round);
                    Glide.with(context).load(userImage.url).apply(options).into(imageView);
                }

            }

        });

            userRepository.getUser(currentUserID, new UserCallback() {
                @Override
                public void onCallback(User user) {
                    username.setText(user.korisnickoIme);
                    points.setText(String.valueOf(user.bodovi)+" "+getString(R.string.points));

                }
            });
    }
}