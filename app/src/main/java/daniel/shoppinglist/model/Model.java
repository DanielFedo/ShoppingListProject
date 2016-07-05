package daniel.shoppinglist.model;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Display;

import java.util.List;

import daniel.shoppinglist.ShoppingListApp;

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
    ModelCloudinary modelCloudinary;

    private Model(){
        this.context = ShoppingListApp.getAppContext();
        this.modelFirebase = new ModelFirebase(ShoppingListApp.getAppContext());
        this.modelCloudinary = new ModelCloudinary();
    }

    public void getSharedUsersByListKey(String listKey, Listener listener) {
        modelFirebase.getSharedUsersByListKey(listKey, listener);
    }

    public interface Listener {
        public void onResult(Object obj);
        public void onCancel();
    }

    public void createNewList(String listName, String userEmail, Listener listener) {
        ShoppingList list = new ShoppingList(listName, userEmail);

        modelFirebase.createNewList(list, listener);
    }

    public void getShoppingListByKey(String key, Listener listener){
        modelFirebase.getShoppingListByKey(key, listener);
    }

    public void getShoppingListsByUser(String userEmail, Listener listener){
        modelFirebase.getShoppingListsByUser(userEmail, listener);
    }

    public void addProductToList(String listKey, String productName, int productQuantity, Listener listener){
        Product product = new Product(productName, productQuantity);

        modelFirebase.addProductToList(listKey, product, listener);
    }

    public void getProductsByShoppingList(final String shoppingListKey, final Model.Listener listener) {
        modelFirebase.getProductsByShoppingList(shoppingListKey, listener);
    }

    public void updateSharedUsers(final String shoppingListKey, final List<String> users, final Model.Listener listener){
        modelFirebase.updateSharedUsers(shoppingListKey, users, listener);
    }

    public void saveImage(final Bitmap imageBitmap, final String imageName, final Listener listener){
        modelCloudinary.saveImage(imageBitmap, imageName, listener);
    }

    public void getImage(final String imageName, final Listener listener){
        modelCloudinary.getImage(imageName, listener);
    }

    public void removeProductFromList(String listKey, String productKey){
        modelFirebase.removeProductFromList(listKey, productKey);
    }
}
