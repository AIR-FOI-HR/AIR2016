package hr.example.treeapp;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

import java.util.List;


import androidx.annotation.NonNull;


import addTreeLogic.LatLng;


public class GetPostData {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    public FirebaseUser user;

    List<Comment> listaKomentara = new ArrayList<Comment>();

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
                                Comment comment = new Comment(document.getId() ,document.getString("Korisnik_ID"), document.getString("Tekst"), document.getString("Datum"));
                                listaKomentara.add(comment);
                            }
                            commentCallback.onCallback(listaKomentara);
                        } else {
                            commentCallback.onCallback(null);
                        }
                    }
                });
    }

    public void getPostImage (String imageName, final ImageCallback imageCallback){
        StorageReference image= storageReference.child("Objave/"+imageName);
        image.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                imageCallback.onCallbackList(BitmapFactory.decodeByteArray(bytes,0, bytes.length));
            }
        });
    }
}
