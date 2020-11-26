package auth;

import android.content.Intent;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

public class Registration {

    private FirebaseFirestore firebaseFirestore = FirebaseFirestore.getInstance();

    public String dostupno;
    List<String> listaKorisnickihImena = new ArrayList<String>();
    int i = 0;

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
                                i++;
                                if(i==3){
                                    break;
                                }
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

}
