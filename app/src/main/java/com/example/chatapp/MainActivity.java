package com.example.chatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    public static String TAG = "MainActivity";

    private Button registerButton;
    private EditText userName;

    private FirebaseDatabase database = FirebaseDatabase.getInstance();

    DatabaseReference myRef = database.getReference("message");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("userName", getUserName);
                startActivity(intent);
            }
        });
    }
}