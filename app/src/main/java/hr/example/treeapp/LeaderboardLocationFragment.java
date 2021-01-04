package hr.example.treeapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.core.entities.User;

import java.util.ArrayList;
import java.util.List;


public class LeaderboardLocationFragment extends Fragment{

    private List<User> users;
    HorizontalScrollView mainLinearLayout;
    private Button button;
    private static final int MY_REQUEST_CODE = 0xe110;
    List<User> leaderboardKorisnici= new ArrayList<>();


    public LeaderboardLocationFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState
        );
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
        button=view.findViewById(R.id.button_location_leaderboard);
        button.setOnClickListener(new View.OnClickListener(){
           @Override
           public void onClick(View view) {
               Intent open = new Intent(getActivity(), LeaderboardLocationMapview.class);
               startActivity(open);


           }
       });
    }

}

