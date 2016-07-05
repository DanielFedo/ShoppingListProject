package daniel.shoppinglist;


import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import daniel.shoppinglist.model.Model;
import daniel.shoppinglist.model.Product;
import daniel.shoppinglist.model.ShoppingList;
import daniel.shoppinglist.utils.AuthManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShoppingListDetailsFragment extends Fragment {
    private String listKey;
    private List<Product> products;

    private ProductsAdapter adapter;
    private TextView listNameText;
    private TextView ownerEmail;
    private TextView newProductNameText;
    private TextView newProductQuantText;
    private ProgressBar progressBar;
    private Button addProduct;
    private Button shareListBtn;
    private ListView listView;

    public ShoppingListDetailsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list_details, container, false);
    }

    @Override
    public void onResume() {
        listNameText = (TextView) getView().findViewById(R.id.listName);
        ownerEmail = (TextView) getView().findViewById(R.id.ownerEmail);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        newProductNameText = (TextView) getView().findViewById(R.id.productName);
        newProductQuantText = (TextView) getView().findViewById(R.id.productQuantity);
        addProduct = (Button) getView().findViewById(R.id.btnAdd);
        listView = (ListView) getView().findViewById(R.id.listView);
        shareListBtn = (Button) getView().findViewById(R.id.btnShare);

        addProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = newProductNameText.getText().toString();

                int productQuant;
                if (!newProductQuantText.getText().toString().equals(""))
                    productQuant = Integer.parseInt(newProductQuantText.getText().toString());
                else
                    productQuant = 1;

                if (productName.equals("")) {
                    Toast.makeText(getActivity(), "Please enter product name", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                Model.getInstance().addProductToList(listKey, productName, productQuant, new Model.Listener() {
                    @Override
                    public void onResult(Object obj) {
                        progressBar.setVisibility(View.INVISIBLE);
                    }

                    @Override
                    public void onCancel() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });

                newProductNameText.setText("");
                newProductQuantText.setText("");
            }
        });

        shareListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                ShareListFragment shoppingListDetailsFragment = new ShareListFragment();
                shoppingListDetailsFragment.setListKey(listKey);
                fragmentTransaction.replace(R.id.fragContainer, shoppingListDetailsFragment);
                fragmentTransaction.addToBackStack("detail");
                fragmentTransaction.commit();
            }
        });

        ownerEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                UserDetailsFragment userDetails = new UserDetailsFragment();
                userDetails.setUserEmail(ownerEmail.getText().toString());
                fragmentTransaction.replace(R.id.fragContainer, userDetails);
                fragmentTransaction.addToBackStack("detail");
                fragmentTransaction.commit();
            }
        });


        if (listKey != null){
            progressBar.setVisibility(View.VISIBLE);
            Model.getInstance().getShoppingListByKey(listKey, new Model.Listener() {
                @Override
                public void onResult(Object obj) {
                    progressBar.setVisibility(View.INVISIBLE);
                    ShoppingList shoppingList = (ShoppingList)obj;
                    listNameText.setText(shoppingList.getName());
                    ownerEmail.setText(shoppingList.getOwnerEmail());

                    if (!AuthManager.getInstance().getSignedUser().getEmail().equals(shoppingList.getOwnerEmail())){
                        shareListBtn.setEnabled(false);
                    }

                    progressBar.setVisibility(View.VISIBLE);
                    Model.getInstance().getProductsByShoppingList(listKey, new Model.Listener() {
                        @Override
                        public void onResult(Object obj) {
                            progressBar.setVisibility(View.INVISIBLE);
                            products = (List<Product>) obj;
                            adapter = new ProductsAdapter();
                            listView.setAdapter(adapter);
                        }

                        @Override
                        public void onCancel() {

                        }
                    });
                }

                @Override
                public void onCancel() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }

        super.onResume();
    }

    public void setListKey(String newListKey) {
        this.listKey = newListKey;
    }

    private class ProductsAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (products == null)
                return 0;

            return products.size();
        }

        @Override
        public Object getItem(int position) {
            if (products == null)
                return null;
            else
                return products.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (products == null)
                return 0;
            else
                return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.fragment_product_row, null);
            }

            TextView productNameText = (TextView) convertView.findViewById(R.id.productName);
            TextView productQuantText = (TextView) convertView.findViewById(R.id.quantity);
            Button deleteButton = (Button)  convertView.findViewById(R.id.deleteBtn);

            Product pl = (Product) getItem(position);

            productNameText.setText(pl.getName());
            productQuantText.setText(String.valueOf(pl.getQuantity()));


            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Model.getInstance().removeProductFromList(listKey, ((Product) getItem(position)).getKey());
                    products.remove(position);
                    adapter.notifyDataSetChanged();
                }
            });

            return convertView;
        }
    }
}
