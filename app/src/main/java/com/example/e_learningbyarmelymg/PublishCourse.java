package com.example.e_learningbyarmelymg;

import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.e_learningbyarmelymg.domain.Course;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.firestore.FirebaseFirestore;

public class PublishCourse extends AppCompatActivity {

    TextInputEditText titleET;
    TextInputEditText subtitleET;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_publish_course);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    public void publishCourse(View view) {
        db = FirebaseFirestore.getInstance();

        titleET = findViewById(R.id.title_edit_text);
        subtitleET = findViewById(R.id.subtitle_edit_text);

        Course newCourse = new Course(titleET.getText().toString(), titleET.getText().toString(), "trends");
        db.collection("Course").document().set(newCourse);

        finish();
    }

    public void finishActivity(View view){
        finish();
    }
}