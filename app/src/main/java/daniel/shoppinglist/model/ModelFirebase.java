package daniel.shoppinglist.model;

import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Context;
import android.util.Log;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by Daniel on 24/06/2016.
 */
public class ModelFirebase {

    private FirebaseDatabase database;
    private DatabaseReference mDatabase;


    ModelFirebase(Context context){
        Firebase.getDefaultConfig().setPersistenceEnabled(true);
        Firebase.setAndroidContext(context);
        database = FirebaseDatabase.getInstance();
        mDatabase = database.getReference();

        mDatabase.keepSynced(true);
    }

    public void createNewList(final ShoppingList list, final Model.Listener listener){
        final DatabaseReference push = mDatabase.child("shoppingLists").push();

        push.setValue(list, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                if (databaseError != null) {
                    Log.e("APP", "Failed to write shopping List", databaseError.toException());
                    listener.onCancel();
                } else {
                    Log.e("APP", "Write shopping list succeeded");
                    listener.onResult(push.getKey());
                }
            }
        });
    }

    public void addProductToList(String listKey, Product product, final Model.Listener listener){
        final DatabaseReference push = mDatabase.child("shoppingLists").child(listKey).child("products").push();

        push.setValue(product, new DatabaseReference.CompletionListener(){
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference reference) {
                if (databaseError != null) {
                    Log.e("APP", "Failed to write product", databaseError.toException());
                    listener.onCancel();
                } else {
                    listener.onResult(push.getKey());
                }
            }
        });
    }

    public void getShoppingListByKey(final String key, final Model.Listener listener){
        DatabaseReference ref = mDatabase.child("shoppingLists").child(key);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ShoppingList sl = dataSnapshot.getValue(ShoppingList.class);
                Log.d("TAG", "Got shopping list with key " + key);
                listener.onResult(sl);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("TAG", "The read failed  " + databaseError.getMessage());
                listener.onCancel();
            }
        });
    }

    public void getShoppingListsByUser(final String userEmail, final Model.Listener listener){
        mDatabase.child("shoppingLists").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<ShoppingList> shoppingLists = new LinkedList<ShoppingList>();

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    ShoppingList shoppingList = snapshot.getValue(ShoppingList.class);

                    if (shoppingList.getSharedUsers() != null && shoppingList.getSharedUsers().contains(userEmail)) {
                        shoppingList.setKey(snapshot.getKey());
                        shoppingLists.add(shoppingList);
                    }
                }

                listener.onResult(shoppingLists);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onCancel();
            }
        });
    }

    public void getSharedUsersByListKey(String listKey, final Model.Listener listener) {
        mDatabase.child("shoppingLists").child(listKey).child("sharedUsers").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onResult(dataSnapshot.getValue());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void getProductsByShoppingList(final String shoppingListKey, final Model.Listener listener){
        mDatabase.child("shoppingLists").child(shoppingListKey).child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final List<Product> products = new LinkedList<Product>();

                for (DataSnapshot snapshot: dataSnapshot.getChildren()){
                    Product product = snapshot.getValue(Product.class);
                    product.setKey(snapshot.getKey());
                    products.add(product);
                }

                listener.onResult(products);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void updateSharedUsers(final String shoppingListKey, final List<String> users, final Model.Listener listener){
        mDatabase.child("shoppingLists").child(shoppingListKey).child("sharedUsers").setValue(users, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                if (databaseError == null) {
                    listener.onResult(null);
                } else {
                    listener.onCancel();
                }
            }
        });
    }

    public void removeProductFromList(String listKey, String productKey){
        mDatabase.child("shoppingLists").child(listKey).child("products").child(productKey).removeValue();
    }
}
