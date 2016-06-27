package daniel.shoppinglist;

import android.app.Application;

import android.content.Context;

/**
 * Created by Daniel on 24/06/2016.
 */
public class ShoppingList extends Application {

    private static Context context;

    public static Context getAppContext(){
        return ShoppingList.context;
    }

    public void onCreate(){
        super.onCreate();
        ShoppingList.context = getApplicationContext();
    }


}
