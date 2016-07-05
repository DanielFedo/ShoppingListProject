package daniel.shoppinglist;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import daniel.shoppinglist.utils.Utils;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private EditText emailText;
    private EditText passwordText;
    private Button signUp;
    private Button login;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();

        emailText = ((EditText)findViewById(R.id.email));
        passwordText = ((EditText)findViewById(R.id.listName));

        signUp = (Button) findViewById(R.id.regBtn);
        login = (Button) findViewById(R.id.logBtn);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
    }

    public void signup(View view) {
        String email;
        String password;

        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        if (!Utils.isValidEmail(email)){
            Toast.makeText(SignUpActivity.this, "Please enter a valid email",
                    Toast.LENGTH_SHORT).show();
            return;
        } else if (!Utils.isValidPassword(password)) {
            Toast.makeText(SignUpActivity.this, "Please enter a valid password",
                    Toast.LENGTH_SHORT).show();

            return;
        }

        disableButtons();
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Info", "createUserWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Sign-up completed!",
                            Toast.LENGTH_SHORT).show();
                    returnToMainActivity();
                }

                enableButtons();
            }
        });
    }

    public void login(View view){
        String email;
        String password;

        email = emailText.getText().toString();
        password = passwordText.getText().toString();

        disableButtons();
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>(){

            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                Log.d("Info", "loginUserWithEmail:onComplete:" + task.isSuccessful());

                // If sign in fails, display a message to the user. If sign in succeeds
                // the auth state listener will be notified and logic to handle the
                // signed in user can be handled in the listener.
                if (!task.isSuccessful()) {
                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(SignUpActivity.this, "Sign-in completed!",
                            Toast.LENGTH_SHORT).show();

                    returnToMainActivity();
                }

                enableButtons();
            }
        });
    }

    private void disableButtons(){
        this.signUp.setEnabled(false);
        this.login.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void enableButtons(){
        this.signUp.setEnabled(true);
        this.login.setEnabled(true);
        progressBar.setVisibility(View.INVISIBLE);
    }

    private void returnToMainActivity(){
        super.onBackPressed();
    }

}
