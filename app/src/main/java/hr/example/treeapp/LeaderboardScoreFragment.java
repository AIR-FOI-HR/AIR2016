package hr.example.treeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.core.entities.User;

import java.util.List;


public class LeaderboardScoreFragment extends Fragment {

    private List<User> users;
    HorizontalScrollView mainLinearLayout;

    public LeaderboardScoreFragment() {
        // Required empty public constructor
    }



    public static LeaderboardScoreFragment newInstance(String param1, String param2) {
        LeaderboardScoreFragment fragment = new LeaderboardScoreFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_leaderboardscore, container, false);
    }


   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


}

