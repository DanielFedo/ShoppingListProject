package daniel.shoppinglist.model;

import com.firebase.client.Firebase;
import android.content.Context;

/**
 * Created by Daniel on 24/06/2016.
 */
public class ModelFirebase {

    Firebase firebase;

    ModelFirebase(Context contex){
        Firebase.setAndroidContext(contex);
        this.firebase = new Firebase("https://shoppinglist-f463a.firebaseio.com/");
    }
}
