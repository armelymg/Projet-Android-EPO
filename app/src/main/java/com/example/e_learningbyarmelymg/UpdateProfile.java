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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class UpdateProfile extends AppCompatActivity {

    TextInputEditText usernameET;
    TextInputEditText emailET;
    TextInputEditText telephoneET;

    Button updateBtn;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_update_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        mAuth = FirebaseAuth.getInstance();
        // Get Utilisateur connecté
        FirebaseUser user = mAuth.getCurrentUser();

        usernameET = findViewById(R.id.username_edit_text);
        emailET = findViewById(R.id.email_edit_text);
        telephoneET = findViewById(R.id.phone_number_edit_text);

        // Informations de l'utilisateur connecté
        usernameET.setText(user.getDisplayName());
        emailET.setText(user.getEmail());
        telephoneET.setText(user.getPhoneNumber());

        // update_profile_btn
        updateProfileOnClick();
    }

    private void updateProfileOnClick() {
        updateBtn = findViewById(R.id.update_profile_btn);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                FirebaseUser user = mAuth.getCurrentUser();

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setDisplayName(usernameET.getText().toString())
                        .setPhotoUri(Uri.parse("https://example.com/jane-q-user/profile.jpg"))
                        .build();

                user.updateEmail(emailET.getText().toString());

                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("TAG", "User profile updated.");
                            Toast.makeText(UpdateProfile.this, "User profile updated.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                Intent intent = new Intent(getApplicationContext(), Profile.class);
                intent.putExtra("username", usernameET.getText().toString());
                intent.putExtra("email", emailET.getText().toString());
                startActivity(intent);
                finish();
            }
        });

    }

    public void finishActivity(View view){
        finish();
    }
}