package com.example.e_learningbyarmelymg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_learningbyarmelymg.utils.Constants;
import com.example.e_learningbyarmelymg.utils.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    private TextView emailTV;
    private TextView usernameTV;
    private ConstraintLayout updateProfileLayout;
    private ConstraintLayout logoutLayout;
    private FirebaseAuth mAuth;
    private PreferenceManager preferenceManager;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        emailTV = findViewById(R.id.email);
        usernameTV = findViewById(R.id.username);

        preferenceManager = new PreferenceManager(getApplicationContext());

        updateProfileLayout = findViewById(R.id.updateProfileLayout);
        updateProfileLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(getApplicationContext(), UpdateProfile.class);
                startActivity(intent1);
                finish();
            }
        });

        logout();
    }

    private void logout() {
        logoutLayout = findViewById(R.id.logoutLayout);

        Intent intent = new Intent(getApplicationContext(), Login.class);

        logoutLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent);
                preferenceManager.putBoolean(Constants.KEY_IS_SIGNED_IN, false);
                finish();
                Toast.makeText(Profile.this, "Vous êtes déconnecté. Au revoir!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateUI();
    }

    public void updateUI() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        usernameTV = findViewById(R.id.username);
        usernameTV.setText(user.getDisplayName());
    }

    public void finishActivity(View view){
        finish();
    }
}