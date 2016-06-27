package daniel.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private TextView userNameText;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Init variables
        userNameText = ((TextView)findViewById(R.id.userNameText));

        // Initialize Firebase library
        Firebase.setAndroidContext(this);
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // If no user has signed in - redirect him to the signupRedirect page
        FirebaseUser user = mAuth.getCurrentUser();
        if (user == null) {
            // User is signed in
            signupRedirect();
        } else {
            userNameText.setText(user.getEmail());
        }
    }

    private void signupRedirect(){
        Intent intent = new Intent(this, SignUpActivity.class);
        startActivity(intent);
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
