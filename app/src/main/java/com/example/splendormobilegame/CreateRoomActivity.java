package com.example.splendormobilegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splendormobilegame.databinding.ActivityCreateRoomActivityBinding;

import tech.gusavila92.websocketclient.WebSocketClient;

public class CreateRoomActivity extends AppCompatActivity {
    private ActivityCreateRoomActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        binding = ActivityCreateRoomActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupButtons();
        //Example message
        //WebSocket.webSocketClient.send("{\"messageContextId\":\"80bdc250-5365-4caf-8dd9-a33e709a0116\",\"type\":\"CREATE_ROOM\",\"data\":{\"userDTO\":{\"uuid\":\"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454\",\"name\":\"James\"},\"roomDTO\":{\"name\":\"TajnyPokoj\",\"password\":\"kjashjkasd\"}}}");

    }
    private boolean checkResponse(boolean response){
      if(response){
          Intent myIntent = new Intent(CreateRoomActivity.this, WaitingRoomActivity.class);
          CreateRoomActivity.this.startActivity(myIntent);
          overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      }
      return false;
    }

    private void setupButtons() {
        binding.createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.enterNameEditText.getText().toString();
                String password = binding.enterPasswordEditText.getText().toString();
                //TODO create message to server that starts the room
                Intent myIntent = new Intent(CreateRoomActivity.this, WaitingRoomActivity.class);
                CreateRoomActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
    }
}
