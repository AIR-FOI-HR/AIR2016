package hr.example.treeapp;

import android.graphics.Bitmap;

public class UserImage {
    public Bitmap image;
    public String url;

    public UserImage(Bitmap image, String url) {
        this.image = image;
        this.url = url;
    }
}
