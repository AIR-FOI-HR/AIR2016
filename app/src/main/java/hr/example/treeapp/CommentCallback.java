package hr.example.treeapp;

import com.example.core.entities.Comment;

import java.util.List;

public interface CommentCallback {
    void onCallback(List<Comment> comment);
}
