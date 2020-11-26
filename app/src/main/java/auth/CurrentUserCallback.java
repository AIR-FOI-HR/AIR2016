package auth;

import com.google.firebase.auth.FirebaseUser;

public interface CurrentUserCallback {
    void OnCallback(FirebaseUser user);
}
