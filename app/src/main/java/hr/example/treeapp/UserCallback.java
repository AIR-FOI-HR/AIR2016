package hr.example.treeapp;

import com.google.firebase.storage.StorageReference;

import com.example.core.entities.User;

public interface UserCallback {
    void onCallback(User user);
}
