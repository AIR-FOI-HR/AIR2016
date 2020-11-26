package auth;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import hr.example.treeapp.RegistrationStep2;
import hr.example.treeapp.RegistrationStep3;

public class RegistrationRepository {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    private StorageReference storageReference = firebaseStorage.getReference();

    public String dostupno, userID, slikaID;
    List<String> listaKorisnickihImena = new ArrayList<String>();
    private Context context;

    public RegistrationRepository(Context context){
        this.context=context;
    }

    public void checkUsernameAvailability(String korime, final UsernameAvailabilityCallback usernameAvailabilityCallback) {
        firebaseFirestore.collection("Korisnici")
                .whereEqualTo("Korisnicko_ime", korime)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                listaKorisnickihImena.add(document.get("Korisnicko_ime").toString());
                            }
                            if(listaKorisnickihImena.contains(korime)){
                                dostupno = "Zauzeto";
                                usernameAvailabilityCallback.onCallback(dostupno);
                            }
                            else{
                                dostupno = "Dostupno";
                                usernameAvailabilityCallback.onCallback(dostupno);
                            }
                        } else {
                            usernameAvailabilityCallback.onCallback(dostupno);
                        }
                    }
                });
    }

    public void firebaseCreateUser(String Email, String Password, String Ime, String Prezime, int Bodovi, String KorIme, String Slika , String datumRodenja, int UlogaID) {
        firebaseAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    userID = firebaseAuth.getCurrentUser().getUid();
                    DocumentReference documentReference = firebaseFirestore.collection("Korisnici").document(userID);
                    Map<String, Object> korisnik = new HashMap<>();
                    korisnik.put("Ime", Ime);
                    korisnik.put("Prezime", Prezime);
                    korisnik.put("E-mail", Email);
                    korisnik.put("Bodovi", Bodovi);
                    korisnik.put("Korisnicko_ime", KorIme);
                    if (!TextUtils.isEmpty(Slika)) {
                        UploadPicture(Slika);
                    }
                    korisnik.put("Profilna_slika_ID", slikaID);
                    korisnik.put("Datum_rodenja", datumRodenja);
                    korisnik.put("Uloga_ID", UlogaID);
                    documentReference.set(korisnik);
                } else {
                    //TODO: čemu služi ovaj else, maknuti ga ako ne treba (možemo složiti i da se kod korisnika koji ne postave sliku uploada neka default slika)
                }
            }
        });
    }

    private void UploadPicture(String Slika) {
        slikaID = UUID.randomUUID().toString();
        StorageReference riversRef = storageReference.child("Profilne_slike/" + slikaID);
        Uri myUri = Uri.parse(Slika);
        //smanjivanje slike profila
        Bitmap bmp = null;
        try {
            bmp = MediaStore.Images.Media.getBitmap(context.getContentResolver(), myUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 25, baos);
        byte[] data = baos.toByteArray();
        riversRef.putBytes(data);
    }

}
