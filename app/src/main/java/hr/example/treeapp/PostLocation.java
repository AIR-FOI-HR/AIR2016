package hr.example.treeapp;

import addTreeLogic.LatLng;

public class PostLocation {
    public LatLng latLng;
    public String postId;

    public PostLocation(LatLng latLng, String postId){
        this.latLng=latLng;
        this.postId=postId;
    }
}
