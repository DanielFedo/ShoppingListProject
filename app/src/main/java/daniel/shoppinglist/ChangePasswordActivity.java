package daniel.shoppinglist;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import daniel.shoppinglist.utils.Utils;

public class ChangePasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
    }

    public void changePassword(View view){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        String newPassword =  ((EditText)findViewById(R.id.passwordText)).getText().toString();
        String newPassword2 =  ((EditText)findViewById(R.id.password2Text)).getText().toString();

        if (!newPassword.equals(newPassword2)){
            Toast.makeText(this, "Password Dont Match!", Toast.LENGTH_SHORT).show();
            return;
        } else if (!Utils.isValidPassword(newPassword)){
            Toast.makeText(this, "Please enter a valid password!", Toast.LENGTH_SHORT).show();
            return;
        }


        user.updatePassword(newPassword)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("ChangePassword", "User password updated.");
                            Toast.makeText(ChangePasswordActivity.this,
                                    "User password updated", Toast.LENGTH_SHORT).show();
                            returnToMainActivity();
                        } else {
                            Toast.makeText(ChangePasswordActivity.this,
                                    "Change password failed", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void returnToMainActivity(){
        super.onBackPressed();
    }
}
