package com.example.e_learningbyarmelymg;

import android.app.ActivityOptions;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_learningbyarmelymg.utils.Constants;
import com.example.e_learningbyarmelymg.utils.PreferenceManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class Login extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Button loginBtn;

    private CheckBox rememberMeCB;
    private PreferenceManager preferenceManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        // Vérifier si l'utilisateur est déjà connecté
        preferenceManager = new PreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_SIGNED_IN)) {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
        // FIN verification

        mAuth = FirebaseAuth.getInstance();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        loginBtn = findViewById(R.id.letTheUserLogIn);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                letTheUserLoggedIn();
            }
        });
    }

    // Implémentation d'une fonction pour appeler la page d'enregistrement
    public void callSignUpScreen(View view){
        Intent intent = new Intent(getApplicationContext(), Register.class);
        startActivity(intent);
    }

    public void letTheUserLoggedIn() {

        TextInputEditText emailET = findViewById(R.id.email_edit_text);
        TextInputEditText passwordET = findViewById(R.id.login_password_editText);

        rememberMeCB = findViewById(R.id.remember_me);

        mAuth.signInWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            if (rememberMeCB.isChecked()){
                                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                            }
                            Toast.makeText(Login.this, "Connexion réussie.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithEmail:failure", task.getException());
                            Toast.makeText(Login.this, "Connexion échouée.",
                                    Toast.LENGTH_SHORT).show();
                            // updateUI(null);
                        }
                    }
                });

    }


    public void  updateUI(FirebaseUser user){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", user.getDisplayName());
        intent.putExtra("email", user.getEmail());
        startActivity(intent);
    }


}