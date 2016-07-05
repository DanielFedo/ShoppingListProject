package daniel.shoppinglist.model;

import com.google.firebase.database.Exclude;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Daniel on 29/06/2016.
 */
public class ShoppingList {

    @Exclude
    private String key;

    private String name;
    private String ownerEmail;
    private List<String> sharedUsers;

    @Exclude
    private HashMap<String, Product> products;


    public ShoppingList(String key, String name, String ownerEmail) {
        this.key = key;
        this.name = name;
        this.ownerEmail = ownerEmail;
    }

    public ShoppingList(String listName, String ownerEmail) {
        this.name = listName;
        this.ownerEmail = ownerEmail;
        this.sharedUsers = new LinkedList<String>();
        this.sharedUsers.add(ownerEmail);
    }

    public ShoppingList() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerEmail() {
        return ownerEmail;
    }

    public void setOwnerEmail(String ownerEmail) {
        this.ownerEmail = ownerEmail;
    }

    public HashMap<String, Product> getProducts() {
        return products;
    }

    public Collection<String> getSharedUsers() {
        return sharedUsers;
    }
}
