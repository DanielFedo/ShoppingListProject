package daniel.shoppinglist;

import android.app.FragmentContainer;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import daniel.shoppinglist.utils.AuthManager;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase library
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();

        FirebaseUser user = mAuth.getCurrentUser();
//        if (user != null) {
//            openWelcomeFragment();
//        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (getFragmentManager().getBackStackEntryCount() == 0) {
            // If no user has signed in - redirect him to the signupRedirect page
            FirebaseUser user = mAuth.getCurrentUser();
            if (user == null) {
                // User is signed in
                signupRedirect();
            } else {
                openWelcomeFragment();
            }
        }

    }

    private void signupRedirect(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
    }

    private void openWelcomeFragment(){
        FragmentManager fragmentManager = getFragmentManager();

        if (fragmentManager.getBackStackEntryCount() == 0) {

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            WelcomeFragment welcomeFragment = new WelcomeFragment();
            fragmentTransaction.replace(R.id.fragContainer, welcomeFragment);
            fragmentTransaction.commit();
        }
    }

    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menu_signout:
                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "You have signed out", Toast.LENGTH_SHORT).show();
                signupRedirect();
                break;
            case R.id.menu_change_password:
                Intent intent = new Intent(this, ChangePasswordActivity.class);
                startActivity(intent);
                break;
            case R.id.menu_profile:
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                UserDetailsFragment userDetailsFragment = new UserDetailsFragment();
                userDetailsFragment.setUserEmail(AuthManager.getInstance().getSignedUser().getEmail());
                fragmentTransaction.replace(R.id.fragContainer, userDetailsFragment);
                fragmentTransaction.addToBackStack("Welcome");
                fragmentTransaction.commit();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onBackPressed() {
        if(getFragmentManager().getBackStackEntryCount() == 0) {
            super.onBackPressed();
        }
        else {
            getFragmentManager().popBackStack();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
