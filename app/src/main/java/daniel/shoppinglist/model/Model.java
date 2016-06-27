package daniel.shoppinglist.model;

import android.content.Context;

import daniel.shoppinglist.ShoppingList;

/**
 * Created by Daniel on 24/06/2016.
 */
public class Model {

    private final static Model instance = new Model();

    public static Model getInstance(){
        return instance;
    }

    Context context;
    ModelFirebase modelFirebase;

    private Model(){
        this.context = ShoppingList.getAppContext();
        this.modelFirebase = new ModelFirebase(ShoppingList.getAppContext());
    }


}
