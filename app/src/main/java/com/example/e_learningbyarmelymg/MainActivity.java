package com.example.e_learningbyarmelymg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learningbyarmelymg.adapter.CourseAdapter;
import com.example.e_learningbyarmelymg.domain.Course;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView usernameTV;
    private String email;
    private String username;
    private RecyclerView.Adapter adapterCourseList;
    private RecyclerView recyclerViewCourse;

    private FirebaseAuth mAuth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        updateUI();

        initRecyclerView();

    }

    private void initRecyclerView() {
        ArrayList<Course> items = new ArrayList<>();

        db = FirebaseFirestore.getInstance();
        CollectionReference cities = db.collection("Course");


        //items.add(new Course("Programmation en C", "Prog", "trends"));
        //items.add(new Course("Developpement natif sous Android Studio", "Dev", "trends"));

        recyclerViewCourse = findViewById(R.id.rv_view);
        recyclerViewCourse.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));


        db.collection("Course")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("TAG", document.getId() + " => " + document.getData().toString());
                                items.add(document.toObject(Course.class));

                            }

                            adapterCourseList = new CourseAdapter(items);
                            recyclerViewCourse.setAdapter(adapterCourseList);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });

    }

    public void callProfileScreen(View view){
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        intent.putExtra("username", username);
        intent.putExtra("email", email);
        startActivityForResult(intent, 123);
    }

    public void callPublishCourseScreen(View view){
        Intent intent = new Intent(getApplicationContext(), PublishCourse.class);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        updateUI();
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateUI();
    }

    public void updateUI() {
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        usernameTV = findViewById(R.id.username);
        usernameTV.setText(user.getDisplayName());
        initRecyclerView();
    }

    public void callListOfUsersScreen(View view) {
        Intent intent = new Intent(getApplicationContext(), ListOfUsers.class);
        startActivity(intent);
    }
}