package com.example.chatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainActivity";

    private Button registerButton;
    private EditText userName;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference myRef = database.getReference("users");

    private List<String> userList;

    SharedPreferences sharedPreferences;

    private String sharedUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("users", MODE_PRIVATE);
        sharedUserName = sharedPreferences.getString("name", null);

        if (sharedUserName != null) {
            Intent intent = new Intent(MainActivity.this, ChatActivity.class);
            intent.putExtra("userName", sharedUserName);
            startActivity(intent);
        }
        userList = new ArrayList<>();

        getUsersName();

        registerButton = (Button) findViewById(R.id.registerButton);
        userName = (EditText) findViewById(R.id.userName);
        
        registerButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String getUserName = userName.getText().toString();

                if (getUserName.length() <= 0 ) {
                    Toast.makeText(MainActivity.this, "입력 사항을 확인해 주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!userList.contains(getUserName)) {
                    myRef.push().setValue(new User(getUserName));
                }

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", getUserName);
                editor.commit();

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("userName", getUserName);
                startActivity(intent);
                userName.setText("");
            }
        });
    }

    void getUsersName() {
        userList.clear();
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    User data = childSnapshot.getValue(User.class);
                    Log.d(TAG, String.valueOf(snapshot.getValue()));
                    userList.add(data.getName());
                    Log.d(TAG, data.getName());
                }

            }

            @Override
            public void onCancelled(@NonNull  DatabaseError error) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d(TAG, "onResume");
        getUsersName();
    }
}