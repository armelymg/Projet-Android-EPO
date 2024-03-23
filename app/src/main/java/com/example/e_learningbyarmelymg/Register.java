package com.example.e_learningbyarmelymg;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_learningbyarmelymg.domain.Course;
import com.example.e_learningbyarmelymg.domain.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

public class Register extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    private Button regBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // FirebaseApp.initializeApp(this);
        mAuth = FirebaseAuth.getInstance();


        regBtn = findViewById(R.id.letTheUserRegister);

        regBtn.setOnClickListener(new View.OnClickListener() {
              @Override
              public void onClick(View v) {
                  letTheUserRegister();
              }
          }
        );


    }

    // Implémentation d'une fonction pour appeler la page d'enregistrement
    public void callLoginScreen(View view){
        Intent intent = new Intent(getApplicationContext(), Login.class);
        startActivity(intent);
    }

    public void  letTheUserRegister(){

        TextInputEditText emailET = findViewById(R.id.email_edit_text);
        TextInputEditText passwordET = findViewById(R.id.login_password_editText);
        TextInputEditText usernameET = findViewById(R.id.username_edit_text);
        TextInputEditText telephoneET = findViewById(R.id.phone_number_edit_text);


        db = FirebaseFirestore.getInstance();

        User newUser = new User();

        mAuth.createUserWithEmailAndPassword(emailET.getText().toString(), passwordET.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();

                            /*PhoneAuthCredential phoneAuthCredential = new PhoneAuthCredential();
                            user.updatePhoneNumber(phoneAuthCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User email address updated.");
                                    }
                                }
                            });*/

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(usernameET.getText().toString())
                                    .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                                    .build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Log.d("TAG", "User profile updated.");
                                    }
                                }
                            });

                            newUser.setEmail(emailET.getText().toString());
                            newUser.setUsername(usernameET.getText().toString());
                            newUser.setTelephone(telephoneET.getText().toString());
                            newUser.setPhoto_url("RAS");
                            newUser.setPassword("RAS");
                            db.collection("Users").document().set(newUser);

                            Toast.makeText(Register.this, "Authentication success.",
                                    Toast.LENGTH_SHORT).show();
                            updateUI(user);
                            //callMainScreen();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "createUserWithEmail:failure", task.getException());
                            Toast.makeText(Register.this, "Authentication failed. " + task.getException(),
                                    Toast.LENGTH_SHORT).show();
                            //updateUI(null);
                        }
                    }
                });

    }

    public void updateUI(FirebaseUser user){
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("username", user.getDisplayName());
        intent.putExtra("email", user.getEmail());
        startActivity(intent);
    }
}