package com.example.splendormobilegame;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splendormobilegame.databinding.ActivityJoinRoomActivityBinding;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.JoinRoom;

import java.util.UUID;

public class JoinRoomActivity extends AppCompatActivity {
    private ActivityJoinRoomActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityJoinRoomActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupButtons();
        Model.setActivity(this);
    }


    private void setupButtons() {
        binding.joinRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = binding.enterNicknameEditText.getText().toString();
                String enterCode = binding.enterNameEditText.getText().toString();
                String password = binding.enterPasswordEditText.getText().toString();

                Model.setEnteredRoomCode(enterCode);

                JoinRoom.RoomDTO roomDTO = new JoinRoom.RoomDTO(enterCode, password);
                JoinRoom.UserDTO userDTO = new JoinRoom.UserDTO(Model.getUserUuid(), nickname);
                JoinRoom.DataDTO dataDTO = new JoinRoom.DataDTO(roomDTO, userDTO);

                UserMessage userMessage = new UserMessage(UUID.randomUUID(), UserRequestType.JOIN_ROOM, dataDTO);
                CustomWebSocketClient.getInstance().send(userMessage);
            }
        });
    }

}
