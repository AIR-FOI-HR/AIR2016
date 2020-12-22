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

    public DataPresentersManager(Context context){
        this.context = context;
        loadPresenters();
    }

    private void loadPresenters() {
        presenters.add(new PostListFragment());
        presenters.add(new PostMapView());
        currentPresenter = presenters.get(0);
        DataManager dataManager = DataManager.getInstance();
        dataManager.loadData(currentPresenter, context);
    }
}
