package hr.example.treeapp;

import com.example.core.entities.Post;

import java.util.List;

public interface GetLikesForPostCallback {
    void onCallback(List<String> listOfLikesByUserID);
}
