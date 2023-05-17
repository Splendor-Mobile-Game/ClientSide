package com.example.splendormobilegame.activities.JoinRoom;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.databinding.ActivityJoinRoomActivityBinding;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.github.splendor_mobile_game.websocket.handlers.ServerMessageType;

public class JoinRoomActivity extends CustomAppCompatActivity {
    private ActivityJoinRoomActivityBinding binding;
    private JoinRoomController joinRoomController;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityJoinRoomActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupButtons();

        // Create controllers
        this.joinRoomController = new JoinRoomController(this, CustomWebSocketClient.getInstance(), Model.getInstance());

        // Set reactions
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.JOIN_ROOM_RESPONSE,
                this.joinRoomController.getJoinRoomResponse()
        );
    }


    private void setupButtons() {
        binding.joinRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = binding.enterNicknameEditText.getText().toString();
                String enterCode = binding.enterCodeEditText.getText().toString();
                String password = binding.enterPasswordEditText.getText().toString();

                joinRoomController.sendRequest(nickname, enterCode, password);
            }
        });
    }

}
