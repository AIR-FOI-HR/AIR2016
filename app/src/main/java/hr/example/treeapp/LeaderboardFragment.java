package hr.example.treeapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;


public class LeaderboardFragment extends Fragment {

        private TabLayout tabLayout;
        private ViewPager viewPager;
        private LeaderboardViewAdapter leaderboardViewAdapter;


        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

                return inflater.inflate(R.layout.fragment_leaderboard, container, false);

        }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        tabLayout=(TabLayout)view.findViewById(R.id.tablayoutID);
        viewPager=(ViewPager)view.findViewById(R.id.viewpager);
        leaderboardViewAdapter = new LeaderboardViewAdapter(getParentFragmentManager());
        leaderboardViewAdapter.AddFragment(new LeaderboardLocationFragment(), "Location");
        leaderboardViewAdapter.AddFragment(new LeaderboardScoreFragment(), "Score");
        viewPager.setAdapter(leaderboardViewAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        for(int i=0;i<leaderboardViewAdapter.getCount();i++) {
            getFragmentManager().beginTransaction().remove(leaderboardViewAdapter.getItem(i)).commitAllowingStateLoss();
        }
    }
}