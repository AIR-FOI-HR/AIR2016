package hr.example.treeapp;

import android.graphics.BitmapFactory;

import com.example.core.entities.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import androidx.annotation.NonNull;
import com.example.core.entities.User;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    public FirebaseUser user;

    List<User> listaKorisnika = new ArrayList<>();

    public void getUser(String korisnikID, final UserCallback userCallback) {
        firebaseFirestore.collection("Korisnici")
                .document(korisnikID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                User user = new User(document.getId(), document.get("Ime").toString(), document.get("Prezime").toString(), document.get("E-mail").toString(), document.getString("Profilna_slika_ID"), (long)document.get("Uloga_ID"), document.get("Korisnicko_ime").toString(), document.get("Datum_rodenja").toString(), (long)document.get("Bodovi"));
                                userCallback.onCallback(user);
                            }
                        } else {
                            userCallback.onCallback(null);
                        }
                    }
                });
    }

    public void getAllUsers(final AllUsersCallback allUsersCallback) {
        firebaseFirestore.collection("Korisnici")
                .limit(6)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listaKorisnika.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User(document.getId(), document.get("Ime").toString(), document.get("Prezime").toString(), document.get("E-mail").toString(), document.getString("Profilna_slika_ID"), (long)document.get("Uloga_ID"), document.get("Korisnicko_ime").toString(), document.get("Datum_rodenja").toString(), (long)document.get("Bodovi"));
                                listaKorisnika.add(user);
                            }
                            allUsersCallback.onCallback(listaKorisnika);
                        } else {
                            allUsersCallback.onCallback(null);
                        }
                    }
                });
    }

    public void getUserImage (String userID, final UserImageCallback userImageCallback){
        StorageReference image= storageReference.child("Profilne_slike/"+userID);
        image.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                userImageCallback.onCallback(BitmapFactory.decodeByteArray(bytes,0, bytes.length));
            }
        });
    }

}
