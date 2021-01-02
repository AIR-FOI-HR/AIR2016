package hr.example.treeapp;

import com.example.core.entities.Post;

import java.util.List;

public interface GetLikesForPost {
    void onCallback(List<String> listOfLikesByUserID);
}
