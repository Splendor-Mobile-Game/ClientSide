package com.example.splendormobilegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splendormobilegame.databinding.ActivityCreateRoomActivityBinding;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.CreateRoom;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import tech.gusavila92.websocketclient.WebSocketClient;

public class CreateRoomActivity extends AppCompatActivity {
    private ActivityCreateRoomActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityCreateRoomActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupButtons();



        //Example message
        //WebSocket.webSocketClient.send("{\"messageContextId\":\"80bdc250-5365-4caf-8dd9-a33e709a0116\",\"type\":\"CREATE_ROOM\",\"data\":{\"userDTO\":{\"uuid\":\"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454\",\"name\":\"James\"},\"roomDTO\":{\"name\":\"TajnyPokoj\",\"password\":\"kjashjkasd\"}}}");

    }

    private boolean validateResponse() {
        //TODO implement response logic
        return true;
    }

    private void setupButtons() {
        binding.createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.enterNameEditText.getText().toString();
                String password = binding.enterPasswordEditText.getText().toString();
                String nickname = binding.enterNicknameEditText.getText().toString();

                if (validateResponse()) {
                    CreateRoom.RoomDTO room = new CreateRoom.RoomDTO(name, password);
                    CreateRoom.UserDTO user = new CreateRoom.UserDTO(UUID.randomUUID(), nickname);
                    CreateRoom.DataDTO data = new CreateRoom.DataDTO(room, user);
                    UserMessage message = new UserMessage(UUID.randomUUID(), UserRequestType.CREATE_ROOM, data);

                    // You can also assign it to a variable first
                    CustomWebSocketClient.getInstance().send(message);

//                    Intent myIntent = new Intent(CreateRoomActivity.this, WaitingRoomActivity.class);
//                    CreateRoomActivity.this.startActivity(myIntent);
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    Toast.makeText(CreateRoomActivity.this,R.string.create_error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
