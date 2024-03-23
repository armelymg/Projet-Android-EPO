package com.example.e_learningbyarmelymg;

import android.os.Bundle;
import android.util.Log;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learningbyarmelymg.adapter.CourseAdapter;
import com.example.e_learningbyarmelymg.adapter.UserAdapter;
import com.example.e_learningbyarmelymg.domain.Course;
import com.example.e_learningbyarmelymg.domain.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ListOfUsers extends AppCompatActivity {

    private RecyclerView.Adapter adapterUserList;
    private RecyclerView recyclerViewUser;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_list_of_users);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initRecyclerView();
    }

    private void initRecyclerView() {
        ArrayList<User> items = new ArrayList<>();

        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser connectedUser = mAuth.getCurrentUser();

        recyclerViewUser = findViewById(R.id.rv_view);
        recyclerViewUser.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));


        db.collection("Users")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("TAG", document.getId() + " => " + document.getData().toString());
                                User userToAdd = document.toObject(User.class);
                                if (!connectedUser.getEmail().equals(userToAdd.getEmail())){
                                    Log.d("TAG", " ****** Connnected user email ****** => " + connectedUser.getEmail());
                                    items.add(userToAdd);
                                }

                            }

                            adapterUserList = new UserAdapter(items);
                            recyclerViewUser.setAdapter(adapterUserList);
                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });

    }

}