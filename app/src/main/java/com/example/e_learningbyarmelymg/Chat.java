package com.example.e_learningbyarmelymg;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.e_learningbyarmelymg.adapter.ChatAdapter;
import com.example.e_learningbyarmelymg.adapter.UserAdapter;
import com.example.e_learningbyarmelymg.domain.ChatMessage;
import com.example.e_learningbyarmelymg.domain.User;
import com.example.e_learningbyarmelymg.utils.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;

public class Chat extends AppCompatActivity {

    private RecyclerView.Adapter adapterChatList;
    private RecyclerView recyclerViewChat;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    FirebaseUser connectedUser;

    ArrayList<ChatMessage> items = new ArrayList<>();

    EditText inputMessage;
    FrameLayout sendMessageLayout;

    ProgressBar progressBar;
    private String emailReceiver;
    private String usernameReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        progressBar = findViewById(R.id.progressBar);
        initExtra();
        initRecyclerView();
        listenMessages();
        sendMessage();
    }


    private void initRecyclerView() {

        db = FirebaseFirestore.getInstance();

        mAuth = FirebaseAuth.getInstance();
        connectedUser = mAuth.getCurrentUser();

        recyclerViewChat = findViewById(R.id.chat_rv);
        recyclerViewChat.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        db.collection("Chats")
                .whereEqualTo("senderId", emailReceiver)
                .whereEqualTo("receiverId", connectedUser.getEmail())
                //.whereArrayContains("senderId", connectedUser.getEmail())
                //.whereArrayContains("receiverId", emailReceiver)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("TAG", document.getId() + " => " + document.getData().toString());
                                ChatMessage chatToAdd = document.toObject(ChatMessage.class);

                                items.add(chatToAdd);


                            }

                            //adapterChatList = new ChatAdapter(items);
                            //recyclerViewChat.setAdapter(adapterChatList);

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });


        db.collection("Chats")
                //.whereEqualTo("senderId", emailReceiver)
                //.whereEqualTo("receiverId", connectedUser.getEmail())
                .whereEqualTo("senderId", connectedUser.getEmail())
                .whereEqualTo("receiverId", emailReceiver)
                //.orderBy("dateTime", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                Log.d("TAG", document.getId() + " => " + document.getData().toString());
                                ChatMessage chatToAdd = document.toObject(ChatMessage.class);

                                items.add(chatToAdd);


                            }

                            adapterChatList = new ChatAdapter(items);
                            recyclerViewChat.setAdapter(adapterChatList);

                        } else {
                            Log.d("TAG", "Error getting documents: ", task.getException());
                        }

                    }
                });




    }

    private void listenMessages() {
        db.collection("Chats").addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error !=null) {
            return;
        }
        if (value != null) {
            int count = items.size();
            for (DocumentChange documentChange : value.getDocumentChanges()){
                if(documentChange.getType() == DocumentChange.Type.ADDED & count!=0) {

                    ChatMessage chatMessage = new ChatMessage();
                    chatMessage.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    chatMessage.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVER_ID);
                    chatMessage.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    chatMessage.dateTime = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    items.add(chatMessage);

                }
            }

            if (count != 0) {
                adapterChatList.notifyItemRangeInserted(items.size(), items.size());
                recyclerViewChat.smoothScrollToPosition(items.size() - 1);
            }

        }

        // Effacer la barre de progression
        progressBar.setVisibility(View.GONE);
    };

    public  void initExtra(){
        Intent intent = getIntent();
        emailReceiver = intent.getStringExtra("email_receiver");
        usernameReceiver = intent.getStringExtra("username_receiver");

        TextView username = findViewById(R.id.username_receiver_Txt);
        username.setText(usernameReceiver);
    }

    public void sendMessage(){
        inputMessage = findViewById(R.id.inputMessage);
        sendMessageLayout = findViewById(R.id.send_message_layout);

        sendMessageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChatMessage chatMessage = new ChatMessage();
                chatMessage.setMessage(inputMessage.getText().toString());
                chatMessage.setSenderId(connectedUser.getEmail());
                chatMessage.setReceiverId(emailReceiver);
                Date date = new Date();
                chatMessage.setDateTime(date);
                db.collection("Chats").document().set(chatMessage);
            }
        });
    }

    public void finishActivity(View view){
        finish();
    }

}