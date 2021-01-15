package hr.example.treeapp;


import android.graphics.BitmapFactory;

import com.example.core.entities.Notification;
import com.example.core.entities.NotificationType;
import com.example.core.entities.Post;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import androidx.annotation.NonNull;
import com.example.core.entities.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import auth.AuthRepository;
import hr.example.treeapp.notifications.NotificationsCallback;


public class UserRepository {
    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    public FirebaseUser user;
    public User currentUser;

    public List<User> listaKorisnika = new ArrayList<>();

    private boolean usersReady = false;
    private boolean userpostoji = false;
    private int numberOfNewUsers = 0;
    List<User> leaderboardKorisnici = new ArrayList<>();
    private List<Notification> notificationList = new ArrayList<Notification>();


    public void getUser(String korisnikID, final UserCallback userCallback) {
        firebaseFirestore.collection("Korisnici")
                .document(korisnikID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = new User(document.getId(), document.get("Ime").toString(), document.get("Prezime").toString(), document.get("E-mail").toString(), document.getString("Profilna_slika_ID"), (long) document.get("Uloga_ID"), document.get("Korisnicko_ime").toString(), document.get("Datum_rodenja").toString(), (long) document.get("Bodovi"));
                                userCallback.onCallback(user);
                            } else
                                userCallback.onCallback(null);
                        }
                    }
                });
    }

    public void getUserImage(String userId, final ProfileImageCallback imageCallback) {
        firebaseFirestore.collection("Korisnici")
                .document(userId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                String imageId = document.getString("Profilna_slika_ID");
                                if (imageId.contains("https://")) {
                                    imageCallback.onCallbackList(new UserImage(null, imageId));
                                }
                                StorageReference image = storageReference.child("Profilne_slike/" + imageId);
                                image.getBytes(1024 * 1024).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        imageCallback.onCallbackList(new UserImage(BitmapFactory.decodeByteArray(bytes, 0, bytes.length), null));
                                    }
                                });
                            }
                        }
                    }
                });
    }

    public void getAllUsers(final AllUsersCallback allUsersCallback) {
        firebaseFirestore.collection("Korisnici")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            listaKorisnika.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User(document.getId(), document.get("Ime").toString(), document.get("Prezime").toString(), document.get("E-mail").toString(), document.getString("Profilna_slika_ID"), (long) document.get("Uloga_ID"), document.get("Korisnicko_ime").toString(), document.get("Datum_rodenja").toString(), (long) document.get("Bodovi"));
                                listaKorisnika.add(user);
                            }
                            allUsersCallback.onCallback(listaKorisnika);
                        } else {
                            allUsersCallback.onCallback(null);
                        }
                    }
                });
    }

    public void getUser(Post post, final UserCallback UserCallback) {
        firebaseFirestore.collection("Korisnici")
                .document(post.getKorisnik_ID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                User user = new User(document.getId(), document.get("Ime").toString(), document.get("Prezime").toString(), document.get("E-mail").toString(), document.getString("Profilna_slika_ID"), (long) document.get("Uloga_ID"), document.get("Korisnicko_ime").toString(), document.get("Datum_rodenja").toString(), (long) document.get("Bodovi"));
                                UserCallback.onCallback(user);
                            }
                        } else {
                            UserCallback.onCallback(null);


                        }
                    }
                });
    }

    public void getTopUsers(final AllUsersCallback allUsersCallback) {
        firebaseFirestore.collection("Korisnici")
                .orderBy("Bodovi",Query.Direction.DESCENDING)
                .limit(10)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            leaderboardKorisnici.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User(document.getId(), document.get("Ime").toString(), document.get("Prezime").toString(), document.get("E-mail").toString(), document.getString("Profilna_slika_ID"), (long)document.get("Uloga_ID"), document.get("Korisnicko_ime").toString(), document.get("Datum_rodenja").toString(), (long)document.get("Bodovi"));
                                leaderboardKorisnici.add(user);
                            }
                            allUsersCallback.onCallback(leaderboardKorisnici);
                        } else {
                            allUsersCallback.onCallback(null);
                        }
                    }
                });
    }

    public void getUsersSearch(String search, final AllUsersCallback allUsersCallback) {
        firebaseFirestore.collection("Korisnici")
                .orderBy("Korisnicko_ime")
                .startAt(search)
                .endAt(search+'\uf8ff')
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            leaderboardKorisnici.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                User user = new User(document.getId(), document.get("Ime").toString(), document.get("Prezime").toString(), document.get("E-mail").toString(), document.getString("Profilna_slika_ID"), (long) document.get("Uloga_ID"), document.get("Korisnicko_ime").toString(), document.get("Datum_rodenja").toString(), (long) document.get("Bodovi"));
                                leaderboardKorisnici.add(user);
                            }
                            allUsersCallback.onCallback(leaderboardKorisnici);
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
                userImageCallback.onCallback(BitmapFactory.decodeByteArray(bytes, 0, bytes.length));
            }
        });
    }


    public String getCurrentUserID() {
        return firebaseAuth.getCurrentUser().getUid();
    }

    public void isCurrentUserAnonymous(final UserAnonymousCallback userAnonymousCallback) {
        firebaseFirestore.collection("Korisnici")
                .document(getCurrentUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                userAnonymousCallback.onCallback(false);
                            } else
                                userAnonymousCallback.onCallback(true);
                        }
                    }
                });

    }

    public void getCurrentUserNotifications (final NotificationsCallback notificationsCallback){
        String currentUserId = getCurrentUserID();
        notificationList.clear();
        firebaseFirestore.collection("Korisnici")
                .document(currentUserId)
                .collection("Notifikacije")
                .orderBy("Timestamp", Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if(task.isSuccessful()){
                            for (QueryDocumentSnapshot document : task.getResult()){
                                NotificationType notificationType = null;
                                if(document.get("Type").toString().equals("comment"))
                                    notificationType=NotificationType.comment;
                                if(document.get("Type").toString().equals("leaf"))
                                    notificationType=NotificationType.leaf;

                                Timestamp googleTimestamp = (Timestamp) document.get("Timestamp");
                                Date timestamp =googleTimestamp.toDate();
                                Notification notification = new Notification(document.getId(),document.get("ReciverId").toString(),document.get("SenderId").toString(),document.get("PostId").toString(),notificationType,timestamp);
                                notificationList.add(notification);
                            }
                            if (notificationList.isEmpty())
                                 notificationsCallback.onCallback(null);
                            else
                                notificationsCallback.onCallback(notificationList);
                        }
                    }
                });
    }
    public void clearCurrentUserNotifications (){
        String currentUserId = getCurrentUserID();
        for(int i=0; i<=notificationList.size();){
            if(firebaseFirestore.collection("Korisnici")
                    .document(currentUserId)
                    .collection("Notifikacije")
                    .document(notificationList.get(i).notificationId)
                    .delete()
                    .isComplete())
                i++;
        }
    }

    public User getCurrentUser(final UserCallback UserCallback) {
        firebaseFirestore.collection("Korisnici")
                .document(getCurrentUserID())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists()) {
                                currentUser = new User(document.getId(), document.get("Ime").toString(), document.get("Prezime").toString(), document.get("E-mail").toString(), document.getString("Profilna_slika_ID"), (long) document.get("Uloga_ID"), document.get("Korisnicko_ime").toString(), document.get("Datum_rodenja").toString(), (long) document.get("Bodovi"));
                                UserCallback.onCallback(currentUser);
                            }
                        } else {
                            UserCallback.onCallback(null);
                        }
                    }
                });
        return currentUser;
    }

    public void changeUserDataFirebase(User user){
        firebaseFirestore.collection("Korisnici")
                .document(getCurrentUserID())
                .update("Ime", user.ime, "Prezime", user.prezime, "Korisnicko_ime", user.korisnickoIme);
    }


    public void changeUserPasswordFirebase(String password){
        firebaseAuth.getCurrentUser().updatePassword(password);
    }

}
