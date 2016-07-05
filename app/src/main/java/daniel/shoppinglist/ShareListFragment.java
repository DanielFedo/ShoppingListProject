package daniel.shoppinglist;


import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import daniel.shoppinglist.model.Model;
import daniel.shoppinglist.utils.AuthManager;
import daniel.shoppinglist.utils.Utils;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShareListFragment extends Fragment {

    private String listKey;
    private ArrayList<String> users;
    private SharedUserAdapter sharedUserAdapter;

    private Button shareBtn;
    private TextView emailText;
    private ProgressBar progressBar;
    private ListView listView;

    public ShareListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onResume() {
        super.onResume();

        users = new ArrayList<String >();

        shareBtn = (Button) getView().findViewById(R.id.shareBtn);
        emailText = (TextView) getView().findViewById(R.id.emailText);
        progressBar = (ProgressBar) getView().findViewById(R.id.progressBar);
        listView = (ListView) getView().findViewById(R.id.sharedWithList);

        shareBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = emailText.getText().toString();

                if (!Utils.isValidEmail(email)){
                    Toast.makeText(getActivity(), "Invalid Email",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                users.add(email);
                Model.getInstance().updateSharedUsers(listKey, users, new Model.Listener() {
                    @Override
                    public void onResult(Object obj) {
                        progressBar.setVisibility(View.INVISIBLE);
                        sharedUserAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancel() {
                        progressBar.setVisibility(View.INVISIBLE);
                    }
                });
            }
        });

        if (listKey != null){
            progressBar.setVisibility(View.VISIBLE);
            Model.getInstance().getSharedUsersByListKey(listKey, new Model.Listener() {

                @Override
                public void onResult(Object obj) {
                    progressBar.setVisibility(View.INVISIBLE);

                    if (obj == null)
                        users = new ArrayList<String>();
                    else
                        users = (ArrayList<String>)obj;


                    sharedUserAdapter = new SharedUserAdapter();
                    listView.setAdapter(sharedUserAdapter);
                }

                @Override
                public void onCancel() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_share_list, container, false);
    }

    public void setListKey(String listKey) {
        this.listKey = listKey;
    }


    private class SharedUserAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            if (users == null)
                return 0;
            else
                return users.size();
        }

        @Override
        public Object getItem(int position) {
            if (users == null)
                return 0;
            else
                return users.get(position);
        }

        @Override
        public long getItemId(int position) {
            if (users == null)
                return 0;
            else return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            if (convertView == null){
                LayoutInflater inflater = getActivity().getLayoutInflater();
                convertView = inflater.inflate(R.layout.fragment_shared_user_row, null);
            }

            TextView nameText = (TextView) convertView.findViewById(R.id.userName);
            Button removeBtm = (Button) convertView.findViewById(R.id.btnRemov);

            String userName = getItem(position).toString();

            removeBtm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    users.remove(position);
                    sharedUserAdapter.notifyDataSetChanged();

                    Model.getInstance().updateSharedUsers(listKey, users, new Model.Listener() {
                        @Override
                        public void onResult(Object obj) {
                            progressBar.setVisibility(View.INVISIBLE);
                            sharedUserAdapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancel() {
                            progressBar.setVisibility(View.INVISIBLE);
                        }
                    });
                }
            });

            if (userName.equals(AuthManager.getInstance().getSignedUser().getEmail())) {
                removeBtm.setVisibility(View.INVISIBLE);
            }

            nameText.setText(userName);

            return convertView;
        }
    }
}
