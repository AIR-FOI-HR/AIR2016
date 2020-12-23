package com.example.core.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LiveData extends ViewModel {
    private static MutableLiveData<Integer> lastPostNumber;
    private static MutableLiveData<String> selectedPostId;

    public static MutableLiveData<Integer> lastPostNumber() {
        if (lastPostNumber == null) {
            lastPostNumber = new MutableLiveData<Integer>();
        }
        return lastPostNumber;
    }

    public void UpdateLastPostNumber(int number){
        this.lastPostNumber().setValue(number);
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
