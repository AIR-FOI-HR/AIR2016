package hr.example.treeapp;

import com.google.firebase.storage.StorageReference;

import auth.User;

public interface UserCallback {
    void onCallback(User user);
}
