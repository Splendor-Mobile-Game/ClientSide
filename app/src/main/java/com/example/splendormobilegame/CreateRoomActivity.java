package com.example.splendormobilegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splendormobilegame.databinding.ActivityCreateRoomActivityBinding;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Room;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;

import com.example.splendormobilegame.websocket.UserReaction;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.ServerMessageType;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.CreateRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;


import java.util.UUID;


public class CreateRoomActivity extends AppCompatActivity {

    private CreateRoomResponse createRoomResponseReaction;

    public class CreateRoomResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered CreateRoomResponse");

            CreateRoom.ResponseData responseData = (CreateRoom.ResponseData) ReactionUtils.getResponseData(serverMessage, CreateRoom.ResponseData.class);

            Model.setRoom(new Room(
                    responseData.room.uuid, responseData.room.name, responseData.room.enterCode, new User(responseData.user.id, responseData.user.name)
            ));

            Intent myIntent = new Intent(CreateRoomActivity.this, WaitingRoomActivity.class);
            CreateRoomActivity.this.startActivity(myIntent);

            // Return null if you don't want to send anything to the server
            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            ReactionUtils.showToast(CreateRoomActivity.this, "Cannot create room: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            ReactionUtils.showToast(CreateRoomActivity.this,"Cannot create room: " + errorResponse.data.error);
            return null;
        }

    }

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

        // Set reaction
        this.createRoomResponseReaction = new CreateRoomResponse();
        CustomWebSocketClient.getInstance().assignReactionToMessageType(ServerMessageType.CREATE_ROOM_RESPONSE, this.createRoomResponseReaction);

        //Example message
        //WebSocket.webSocketClient.send("{\"messageContextId\":\"80bdc250-5365-4caf-8dd9-a33e709a0116\",\"type\":\"CREATE_ROOM\",\"data\":{\"userDTO\":{\"uuid\":\"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454\",\"name\":\"James\"},\"roomDTO\":{\"name\":\"TajnyPokoj\",\"password\":\"kjashjkasd\"}}}");

    }

    private boolean validateInput() {
        // TODO
        return true;
    }


    private void setupButtons() {
        binding.createRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.enterNameEditText.getText().toString();
                String password = binding.enterPasswordEditText.getText().toString();
                String nickname = binding.enterNicknameEditText.getText().toString();
                // TODO implement emojis in nicknames

                if (validateInput()) {
                    CreateRoom.RoomDTO room = new CreateRoom.RoomDTO(name, password);
                    CreateRoom.UserDTO user = new CreateRoom.UserDTO(Model.getUserUuid(), nickname);
                    CreateRoom.DataDTO data = new CreateRoom.DataDTO(room, user);
                    UserMessage message = new UserMessage(UUID.randomUUID(), UserRequestType.CREATE_ROOM, data);

                    // You can also assign it to a variable first
                    CustomWebSocketClient.getInstance().send(message);

//                    Intent myIntent = new Intent(CreateRoomActivity.this, WaitingRoomActivity.class);
//                    CreateRoomActivity.this.startActivity(myIntent);
//                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    Toast.makeText(CreateRoomActivity.this, R.string.create_error, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
