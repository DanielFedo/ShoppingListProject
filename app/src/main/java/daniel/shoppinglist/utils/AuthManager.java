package daniel.shoppinglist.utils;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

/**
 * Created by Daniel on 01/07/2016.
 */
public class AuthManager {

    private static AuthManager instance;
    private FirebaseAuth mAuth;

    public static AuthManager getInstance(){
        if (instance == null){
            instance = new AuthManager();
        }

        return instance;
    }

    private AuthManager(){
        mAuth = FirebaseAuth.getInstance();
    }

    public FirebaseUser getSignedUser(){
        return mAuth.getCurrentUser();

    }

}
