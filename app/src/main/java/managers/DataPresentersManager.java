package managers;

import android.content.Context;

import com.example.core.DataPresenter;
import com.example.timeline.PostListFragment;

import java.util.ArrayList;
import java.util.List;

import hr.example.mapview.PostMapView;


public class DataPresentersManager {
    public List<DataPresenter> presenters = new ArrayList<>();
    public DataPresenter currentPresenter;
    private Context context;
    public DataPresenter firstPresenter;

    public DataPresentersManager(Context context){
        this.context = context;
        loadPresenters();
    }

    private void loadPresenters() {
        presenters.add(new PostListFragment());
        presenters.add(new PostMapView());
        firstPresenter = presenters.get(0);
        DataManager dataManager = DataManager.getInstance();
        dataManager.loadData(firstPresenter, context);
    }

    public void loadFragment(int i){
        DataManager dataManager = DataManager.getInstance();
        dataManager.loadData(presenters.get(i), context);
    }
}
