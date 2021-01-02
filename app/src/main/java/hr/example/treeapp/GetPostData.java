package hr.example.treeapp;

import android.graphics.BitmapFactory;
import android.util.Log;

import com.example.core.entities.Comment;
import com.example.core.entities.Post;
import com.example.core.entities.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.type.DateTime;

import java.util.ArrayList;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import addTreeLogic.LatLng;


public class GetPostData {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    public FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
    QueryDocumentSnapshot lastDocument = null;

    List<Comment> listaKomentara = new ArrayList<Comment>();
    List<Post> listaObjava = new ArrayList<>();

    public void getPost(String postId, final PostCallback postCallback) {
        firebaseFirestore.collection("Objave")
                .document(postId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                Post post = new Post(document.getId(), document.get("Korisnik_ID").toString(), document.get("Datum_objave").toString(), (double)document.get("Latitude"), (double)document.get("Longitude"), document.get("Opis").toString(), document.get("URL_slike").toString(), (long)document.get("Broj_lajkova"));
                                postCallback.onCallback(post);
                            }
                        } else {
                            postCallback.onCallback(null);
                        }
                    }
                });
    }


    /**
     * Metoda se koristi za dohvaćanje samo dijela podataka o objavi kako bi se smanjilo opterećenje baze
     * @param postCallback
     * @return vraća se lista svih lokacija zajedno s iz objave
     */
    public void getPostsForMap (final PostLocationcallback postCallback) {
        List<PostLocation> listaLokacija = new ArrayList<>();
        firebaseFirestore.collection("Objave")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                LatLng postLatLag = new LatLng((double)document.get("Latitude"), (double)document.get("Longitude"));
                                String postId = document.get("ID_objava").toString();
                                PostLocation postLocation= new PostLocation(postLatLag, postId);
                                listaLokacija.add(postLocation);
                            }
                            postCallback.onCallbackList(listaLokacija);
                        } else {
                            postCallback.onCallbackList(null);
                        }
                    }
                });

    }
    public void getPostComments(String postId, final CommentCallback commentCallback) {
        firebaseFirestore.collection("Objave")
                .document(postId)
                .collection("Komentari")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Comment comment = new Comment(document.getId() ,document.getString("Korisnik_ID"), document.getString("Tekst"), "document.get().toString()");
                                listaKomentara.add(comment);
                            }
                            commentCallback.onCallback(listaKomentara);
                        } else {
                            commentCallback.onCallback(null);
                        }
                    }
                });
    }

//TODO: maknuti jedan od ovih
    /**public void getPostImage (String imageName, final ImageCallback imageCallback){
        StorageReference image= storageReference.child("Objave/"+imageName);
        image.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                imageCallback.onCallbackList(BitmapFactory.decodeByteArray(bytes,0, bytes.length));
            }
        });
    }**/

    public void getPostImage (String imageID, final PostImageCallback postImageCallback){
        StorageReference image= storageReference.child("Objave/"+imageID);
        image.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                postImageCallback.onCallback(BitmapFactory.decodeByteArray(bytes,0, bytes.length));
            }
        });
    }

    public void getAllPosts(final AllPostsCallback allPostsCallback) {
        firebaseFirestore.collection("Objave")
                .orderBy("Datum_objave", Query.Direction.DESCENDING)
                .limit(5)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listaObjava.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Post post = new Post(document.getId(), document.get("Korisnik_ID").toString(), document.get("Datum_objave").toString(), (double)document.get("Latitude"), (double)document.get("Longitude"), document.get("Opis").toString(), document.get("URL_slike").toString(), (long)document.get("Broj_lajkova"));
                                listaObjava.add(post);
                                lastDocument = document;
                            }
                            allPostsCallback.onCallback(listaObjava);
                        } else {
                            allPostsCallback.onCallback(null);
                        }
                    }
                });
    }

    public void getPostsFromLastID(final GetPostsFromLastID getPostsFromLastID) {
        firebaseFirestore.collection("Objave")
                .orderBy("Datum_objave", Query.Direction.DESCENDING)
                .startAfter(lastDocument)
                .limit(5)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Post post = new Post(document.getId(), document.get("Korisnik_ID").toString(), document.get("Datum_objave").toString(), (double)document.get("Latitude"), (double)document.get("Longitude"), document.get("Opis").toString(), document.get("URL_slike").toString(), (long)document.get("Broj_lajkova"));
                                listaObjava.add(post);
                                lastDocument = document;
                            }
                            getPostsFromLastID.onCallback(listaObjava);
                        } else {
                            getPostsFromLastID.onCallback(null);
                        }
                    }
                });
    }
    List<Post> objave = new ArrayList<>();
    List<Double> prikazaneObjaveId = new ArrayList<>();
    public void getPostsInLatLngBoundry (double minLatitude, double maxLatitude, double minLongitude, double maxLongitude, final GetPostsInLatLng getPostsInLatLng){
        //minLatitude=0; maxLatitude=0; maxLongitude=0; minLongitude=0;
        CollectionReference collectionObjave = firebaseFirestore.collection("Objave");
            collectionObjave.whereLessThanOrEqualTo("Latitude", maxLatitude)
                    .whereGreaterThanOrEqualTo("Latitude", minLatitude)
                    /**collectionObjave.whereLessThanOrEqualTo("Longitude", maxLongitude)
                     .whereGreaterThanOrEqualTo("Longitude", minLongitude);
                     if(prikazaneObjaveId.size()>0)*/
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Post post = new Post(document.getId(), document.get("Korisnik_ID").toString(), document.get("Datum_objave").toString(), (double) document.get("Latitude"), (double) document.get("Longitude"), document.get("Opis").toString(), document.get("URL_slike").toString(), (long) document.get("Broj_lajkova"));
                            if(!objave.contains(post)){
                                objave.add(post);
                            }
                        }
                        Log.d("Broj ucitanih:", String.valueOf(objave.size()));
                        getPostsInLatLng.onCallbackPostsInLatLng(objave);
                    }
                }
            });
    }

/**
        if(prikazaneObjaveId.size()>0)
            firebaseFirestore.collection("Objave")
                    .whereLessThanOrEqualTo("Latitude",maxLatitude)
                    .whereGreaterThanOrEqualTo("Latitude", minLatitude)
                    .whereLessThanOrEqualTo("Longitude", maxLongitude)
                    .whereGreaterThanOrEqualTo("Longitude", minLongitude)
                    //.whereNotIn("ID_objava",prikazaneObjaveId)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {

                        }
                    });
        else
            firebaseFirestore.collection("Objave")
                    .whereLessThanOrEqualTo("Latitude",maxLatitude)
                    .whereGreaterThanOrEqualTo("Latitude", minLatitude)
                    .whereLessThanOrEqualTo("Longitude", maxLongitude)
                    .whereGreaterThanOrEqualTo("Longitude", minLongitude)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                for (QueryDocumentSnapshot document : task.getResult()){
                                    Post post = new Post(document.getId(), document.get("Korisnik_ID").toString(), document.get("Datum_objave").toString(), (double)document.get("Latitude"), (double)document.get("Longitude"), document.get("Opis").toString(), document.get("URL_slike").toString(), (long)document.get("Broj_lajkova"));
                                    objave.add(post);
                                    prikazaneObjaveId.add(post.getID_objava());
                                }
                                getPostsInLatLng.onCallbackPostsInLatLng(objave);
                            }
                        }
                    });*/

    public void likePost(String postID){
        DocumentReference documentReference = firebaseFirestore.collection("Objave").document(postID).collection("Lajkovi").document(currentUser.getUid());
        Map<String, Object> like= new HashMap<>();
        like.put("Korisnik_ID", currentUser.getUid());
        documentReference.set(like);

    }

    public void removeLikeOnPost (String postID){
        DocumentReference documentReference = firebaseFirestore.collection("Objave").document(postID).collection("Lajkovi").document(currentUser.getUid());
        documentReference.delete();
    }

    public void hasUserLikedPost(String postID, final CheckIfUserLikedPhotoCallback checkCallback){

        firebaseFirestore.collection("Objave")
                .document(postID).collection("Lajkovi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        boolean check=false;
                        if(task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                if(document.get("Korisnik_ID").toString().equals(currentUser.getUid()))
                                    check=true;
                         checkCallback.onCallback(check);
                    }
                });
    }

    public void getPostLikes(String postID, final GetLikesForPostCallback postLikesCallback){
        List<String> likesOnThisPost = new ArrayList<>();
        firebaseFirestore.collection("Objave")
                .document(postID).collection("Lajkovi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult())
                                if(document!=null)
                                    likesOnThisPost.add(document.get("Korisnik_ID").toString());
                        postLikesCallback.onCallback(likesOnThisPost);
            }
        });
    }
    public void getUsersLiked (String postID, final GetLikesForPostCallback postLikesCallback){
        UserRepository userRepository= new UserRepository();
        List<String> likesOnThisPost = new ArrayList<>();
        firebaseFirestore.collection("Objave")
                .document(postID).collection("Lajkovi")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful())
                            for (QueryDocumentSnapshot document : task.getResult()){
                                userRepository.getUser(document.get("Korisnik_ID").toString(), new UserCallback() {
                                    @Override
                                    public void onCallback(User user) {
                                        if(user!=null)
                                            likesOnThisPost.add(user.korisnickoIme);
                                        if(user==null)
                                            likesOnThisPost.add("Anonymous");
                                        if(likesOnThisPost.size()==task.getResult().size() && likesOnThisPost.size()>0)
                                            postLikesCallback.onCallback(likesOnThisPost);
                                    }
                                });
                            }
                    }
                });
    }

    public void postComent(String postID, String commentText){
        String userID=currentUser.getUid();
        Date dateTime = Calendar.getInstance().getTime();
        DocumentReference documentReference=firebaseFirestore.collection("Objave").document(postID).collection("Komentari").document();
        Map<String, Object> comment= new HashMap<>();
        comment.put("Korisnik_ID", userID);
        comment.put("Datum", dateTime);
        comment.put("Tekst", commentText);
        documentReference.set(comment);
    }
}


