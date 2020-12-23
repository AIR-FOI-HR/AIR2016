package com.example.core.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LiveDataPostID extends ViewModel {
    private static MutableLiveData<Integer> lastPostNumber;

    public static MutableLiveData<Integer> lastPostNumber() {
        if (lastPostNumber == null) {
            lastPostNumber = new MutableLiveData<Integer>();
        }
        return lastPostNumber;
    }

    public void UpdateLastPostNumber(int number){
        this.lastPostNumber().setValue(number);
    }
}
