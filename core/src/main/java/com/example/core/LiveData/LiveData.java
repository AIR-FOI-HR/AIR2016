package com.example.core.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LiveData extends ViewModel {
    private static MutableLiveData<String> lastPostID;
    private static MutableLiveData<String> selectedPostId;

    public static MutableLiveData<String> lastPostID() {
        if (lastPostID == null) {
            lastPostID = new MutableLiveData<String>();
        }
        return lastPostID;
    }

    public void UpdateLastPostNumber(String ID){
        this.lastPostID().setValue(ID);
    }

    public static MutableLiveData<String> selectedPostId() {
        if (selectedPostId == null) {
            selectedPostId = new MutableLiveData<String>();
        }
        return selectedPostId;
    }

    public void UpdateSelectedPostId(String postId){ this.selectedPostId().setValue(postId);
    }
}
