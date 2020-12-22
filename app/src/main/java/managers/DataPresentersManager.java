package managers;

import android.content.Context;
import android.view.Menu;

import androidx.navigation.NavController;

import com.example.core.DataPresenter;
import com.example.timeline.PostListFragment;
import com.google.android.material.bottomnavigation.BottomNavigationMenu;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.internal.NavigationMenu;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

import hr.example.treeapp.R;

public class DataPresentersManager {
    public List<DataPresenter> presenters = new ArrayList<>();
    public DataPresenter currentPresenter;
    public String FirstModuleFragmentId;
    private Context context;
    private NavigationView navigationView;
    private NavController navController;

    public DataPresentersManager() {
        loadPresenters();
    }

    public DataPresentersManager(Context context){
        this.context = context;
        loadPresenters();
    }

    public DataPresentersManager(Context context, NavigationView navigationView, NavController navController) {
        this.context = context;
        this.navigationView = navigationView;
        this.navController = navController;
        loadPresenters();
    }

    private void loadPresenters() {
        presenters.add(new PostListFragment());
        currentPresenter = presenters.get(0);
        DataManager dataManager = DataManager.getInstance();
        dataManager.loadData(currentPresenter, context);
    }
}
