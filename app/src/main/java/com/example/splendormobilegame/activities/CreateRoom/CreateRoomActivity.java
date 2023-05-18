package com.example.splendormobilegame.activities.CreateRoom;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.databinding.ActivityCreateRoomActivityBinding;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;

import com.github.splendor_mobile_game.websocket.handlers.ServerMessageType;


public class CreateRoomActivity extends CustomAppCompatActivity {

    private CreateRoomController createRoomController;
    private ActivityCreateRoomActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityCreateRoomActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupButtons();

        // Create controllers
        this.createRoomController = new CreateRoomController(this, CustomWebSocketClient.getInstance(), Model.getInstance());

        // Set reactions
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.CREATE_ROOM_RESPONSE,
                this.createRoomController.getCreateRoomResponse()
        );
    }

    private void setupButtons() {
        binding.createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Get the data from the view
                String name = binding.enterNameEditText.getText().toString();
                String password = binding.enterPasswordEditText.getText().toString();
                String nickname = binding.enterNicknameEditText.getText().toString();
                // TODO implement emojis in nicknames

                // Send this "event" with data to controller
                CreateRoomActivity.this.createRoomController.sendRequest(nickname, name, password);
            }
        });
    }
}
