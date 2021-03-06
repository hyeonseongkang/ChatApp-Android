package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    public static String TAG = "ChatActivity";

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference chatRef = database.getReference("chat");
    DatabaseReference usersRef = database.getReference("users");

    private List<ChatData> chatDataList;
    private List<String> userList;

    private TextView users;
    private EditText message;
    private Button sendButton;
    private TextView logoutButton;

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private ChatAdapter chatAdapter;

    private String userName;
    private String userListString = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Intent intent = getIntent();
        userName = intent.getStringExtra("userName");

        userList = new ArrayList<>();
        chatDataList = new ArrayList<>();

        users = (TextView) findViewById(R.id.users);
        message = (EditText) findViewById(R.id.message);
        sendButton = (Button) findViewById(R.id.sendButton);
        logoutButton = (TextView) findViewById(R.id.logoutButton);
        logoutButton.setEnabled(true);
        logoutButton.setClickable(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        layoutManager = new LinearLayoutManager(ChatActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        chatAdapter = new ChatAdapter(chatDataList, userName);
        recyclerView.setAdapter(chatAdapter);

        sendButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String userMessage = message.getText().toString();

                if (userMessage.length() <= 0) {
                    Toast.makeText(ChatActivity.this, "?????? ????????? ????????? ?????????.", Toast.LENGTH_SHORT).show();
                    return;
                }

                SimpleDateFormat format = new SimpleDateFormat ( "HH:mm");
                Calendar time = Calendar.getInstance();
                String formatTime = format.format(time.getTime());


                // ?????? ????????? ?????? ?????? ????????????
                SimpleDateFormat format2 = new SimpleDateFormat("yyyy??? MM??? dd??? E??????");
                Calendar time2 = Calendar.getInstance();
                String formatTime2 = format2.format(time2.getTime());
                Log.d(TAG, formatTime2);


                chatRef.push().setValue(new ChatData(userName, formatTime, userMessage));
                message.setText("");
            }
        });

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getSharedPreferences("users", MODE_PRIVATE);
                SharedPreferences.Editor edit = sharedPreferences.edit();
                edit.clear();
                edit.commit();
                finish();
            }
        });

        getData();
        Log.d(TAG, userName);

    }


    private void getData() {

        // usersData
        usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                User userData = snapshot.getValue(User.class);
                userListString += userData.getName() + "??? ";
                users.setText(userListString);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // chatData
        chatRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

                ChatData chatData = snapshot.getValue(ChatData.class);
                chatDataList.add(chatData);
                recyclerView.scrollToPosition(chatDataList.size() - 1);
                chatAdapter.notifyDataSetChanged();
                Log.d(TAG, chatData.getMessage());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                    for (String userNames: userList) {
                        Log.d(TAG, " Contains UserNames.....");
                    }
                    Log.d(TAG,"Not Contains UserNames....");
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }
}