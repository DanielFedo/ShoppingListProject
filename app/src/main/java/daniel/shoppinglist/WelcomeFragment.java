package daniel.shoppinglist;

import android.app.Dialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;

import java.util.List;

import daniel.shoppinglist.model.Model;
import daniel.shoppinglist.model.ShoppingList;
import daniel.shoppinglist.utils.AuthManager;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class WelcomeFragment extends Fragment {

    private ShoppingListAdapter adapter;

    private List<ShoppingList> shoppingListList;
    private TextView userNameText;
    private Button newListButton;
    private ProgressBar progressBar;
    private ListView list;

    public WelcomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();

        // Init variables
        userNameText = ((TextView)getView().findViewById(R.id.userNameText));
        newListButton = ((Button)getView().findViewById(R.id.createListBtn));
        progressBar = (ProgressBar)getView().findViewById(R.id.progressBar);
        list = (ListView) getView().findViewById(R.id.list);

        userNameText.setText(AuthManager.getInstance().getSignedUser().getEmail());
        newListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewListAction();
            }
        });

        progressBar.setVisibility(View.VISIBLE);
        Model.getInstance().getShoppingListsByUser(AuthManager.getInstance().getSignedUser().getEmail(),
                new Model.Listener() {
            @Override
            public void onResult(Object obj) {
                progressBar.setVisibility(View.INVISIBLE);
                shoppingListList = (List<ShoppingList>) obj;
                adapter = new ShoppingListAdapter();
                list.setAdapter(adapter);
                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        // Start shopping list details fragment
                        FragmentManager fragmentManager = getFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        ShoppingListDetailsFragment shoppingListDetailsFragment = new ShoppingListDetailsFragment();
                        shoppingListDetailsFragment.setListKey(shoppingListList.get(position).getKey());
                        fragmentTransaction.replace(R.id.fragContainer, shoppingListDetailsFragment);
                        fragmentTransaction.addToBackStack("welcome");
                        fragmentTransaction.commit();
                    }
                });
            }

            @Override
            public void onCancel() {

            }
        });
    }

    public void createNewListAction(){
        final Dialog dialog = new Dialog(getActivity());

        dialog.setContentView(R.layout.dialog_new_shopping_list);
        dialog.setTitle("New List");

        dialog.show();

        final TextView listNameText = (TextView) dialog.findViewById(R.id.listName);
        final Button dialogButton = (Button) dialog.findViewById(R.id.createListBtn);

        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                String userEmail = AuthManager.getInstance().getSignedUser().getEmail();

                Model.getInstance().
                        createNewList(listNameText.getText().toString(), userEmail, new Model.Listener() {
                            @Override
                            public void onResult(Object obj) {
                                progressBar.setVisibility(View.INVISIBLE);

                                // Start shopping list details fragment
                                ShoppingListDetailsFragment shoppingListDetailsFragment = new ShoppingListDetailsFragment();
                                shoppingListDetailsFragment.setListKey((String) obj);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(((ViewGroup)getView().getParent()).getId(), shoppingListDetailsFragment);
                                fragmentTransaction.addToBackStack("Welcome");
                                fragmentTransaction.commit();
                            }

                            @Override
                            public void onCancel() {

                            }
                        });

                dialog.dismiss();
            }
        });
    }

    private class ShoppingListAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            if (shoppingListList == null){
                return 0;
            }

            return shoppingListList.size();
        }

        @Override
        public Object getItem(int position) {
            return shoppingListList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.fragment_shopping_list_row, null);
            }

            TextView listNameText = (TextView) convertView.findViewById(R.id.listName);
            TextView ownerEmailText = (TextView) convertView.findViewById(R.id.ownerEmail);

            ShoppingList sl = (ShoppingList) getItem(position);
            listNameText.setText(sl.getName());
            ownerEmailText.setText(sl.getOwnerEmail().substring(0, sl.getOwnerEmail().indexOf("@")));
            ownerEmailText.setVisibility(View.INVISIBLE);

            return convertView;
        }
    }

}
