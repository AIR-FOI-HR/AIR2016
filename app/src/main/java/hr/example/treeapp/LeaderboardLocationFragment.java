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


public class LeaderboardLocationFragment extends Fragment {

    private List<User> users;
    HorizontalScrollView mainLinearLayout;

    public LeaderboardLocationFragment() {
        // Required empty public constructor
    }



    public static LeaderboardLocationFragment newInstance(String param1, String param2) {
        LeaderboardLocationFragment fragment = new LeaderboardLocationFragment();
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
        return inflater.inflate(R.layout.fragment_leaderboardlocation, container, false);
    }


   @Override
   public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }


}

