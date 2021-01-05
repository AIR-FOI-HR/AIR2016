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
    String timeline = "Timeline";
    String mapView = "Map View";

    public DataPresentersManager(Context context){
        this.context = context;
        loadPresenters();
    }

    private void loadPresenters() {
        presenters.add(new PostListFragment());
        presenters.add(new PostMapView());
        firstPresenter = presenters.get(0);
        DataManager dataManager = DataManager.getInstance();
        String modul = firstPresenter.getModuleName(context);
        if(modul.equals(timeline)){
            dataManager.loadDataTimeline(firstPresenter, context);
        }
        else if(modul.equals(mapView)){
            dataManager.loadDataMap(firstPresenter, context);
        }
        else{
            dataManager.loadAllData(firstPresenter, context);
        }
    }

    public void loadFragment(int i){
        DataManager dataManager = DataManager.getInstance();
        String modul = presenters.get(i).getModuleName(context);
        if(modul.equals(timeline)){
            dataManager.loadDataTimeline(presenters.get(i), context);
        }
        else if(modul.equals(mapView)){
            dataManager.loadDataMap(presenters.get(i), context);
        }
        else{
            dataManager.loadAllData(presenters.get(i), context);
        }
    }
}
