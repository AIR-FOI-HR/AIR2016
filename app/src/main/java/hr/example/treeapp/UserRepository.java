package hr.example.treeapp;


import android.graphics.BitmapFactory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
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

import auth.AuthRepository;


public class UserRepository {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
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
                            else
                                userCallback.onCallback(null);
                        }
                    }
                });
    }
    public void getUserImage (String userId, final ProfileImageCallback imageCallback){
        firebaseFirestore.collection("Korisnici")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                String imageId = document.getString("Profilna_slika_ID");
                                if(imageId.contains("https://")){
                                    imageCallback.onCallbackList(new UserImage(null, imageId));
                                }
                                StorageReference image= storageReference.child("Profilne_slike/"+imageId);
                                image.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        imageCallback.onCallbackList(new UserImage(BitmapFactory.decodeByteArray(bytes,0, bytes.length), null));
                                    }
                                });
                            }
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

    public void getUserImage (String imageID, final UserImageCallback userImageCallback){
        StorageReference image= storageReference.child("Profilne_slike/"+imageID);
        image.getBytes(1024*1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
            @Override
            public void onSuccess(byte[] bytes) {
                userImageCallback.onCallback(BitmapFactory.decodeByteArray(bytes,0, bytes.length));
            }
        });
    }

    public String getCurrentUserID(){
        return firebaseAuth.getCurrentUser().getUid();
    }

    public void isCurrentUserAnonymous(final UserAnonymousCallback userAnonymousCallback){
        firebaseFirestore.collection("Korisnici")
                .document(getCurrentUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if(document.exists()){
                                userAnonymousCallback.onCallback(false);
                            }
                            else
                                userAnonymousCallback.onCallback(true);
                        }
                    }
                });
    }



}
