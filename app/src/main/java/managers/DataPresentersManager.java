package managers;

import android.content.Context;

import com.example.core.DataPresenter;
import com.example.timeline.PostListFragment;

import java.util.ArrayList;
import java.util.List;

public class DataPresentersManager {
    List<DataPresenter> presenters = new ArrayList<>();
    DataPresenter currentPresenter = null;
    private Context context;

    public DataPresentersManager(Context context){
        this.context = context;
        loadPresenters();
    }

    private void loadPresenters(){
        presenters.add(new PostListFragment());

        currentPresenter = presenters.get(0);
        DataManager dataManager = DataManager.getInstance();
        dataManager.loadData(currentPresenter, context);
    }
}
