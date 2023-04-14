package com.example.splendormobilegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splendormobilegame.databinding.ActivityJoinRoomActivityBinding;
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
import com.github.splendor_mobile_game.websocket.handlers.reactions.JoinRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.UUID;

public class JoinRoomActivity extends AppCompatActivity {
    private ActivityJoinRoomActivityBinding binding;
    private JoinRoomResponse joinRoomResponseReaction;

    public class JoinRoomResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered JoinRoomResponse react method");

            JoinRoom.ResponseData responseData = (JoinRoom.ResponseData) ReactionUtils.getResponseData(serverMessage, JoinRoom.ResponseData.class);

            JoinRoom.UserDataResponse owner = responseData.users.get(0);
            Room room = new Room(responseData.room.uuid, responseData.room.name, Model.getEnteredRoomCode(), new User(owner.uuid, owner.name));

            // DEBUG PURPOSES START
            String message = "Users:\n" + owner.name + "\n";
            for (int i = 1; i < responseData.users.size(); i++) {
                room.addUser(new User(responseData.users.get(i).uuid, responseData.users.get(i).name));
                message += responseData.users.get(i).name + "\n";
            }
            // DEBUG PURPOSES END

            Model.setRoom(room);

            Intent myIntent = new Intent(JoinRoomActivity.this, WaitingRoomActivity.class);
            JoinRoomActivity.this.startActivity(myIntent);

            // DEBUG PURPOSES START
            ReactionUtils.showToast(JoinRoomActivity.this, message);
            // DEBUG PURPOSES END

            // Return null if you don't want to send anything to the server
            return null;

        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            ReactionUtils.showToast(JoinRoomActivity.this, "Cannot join to the room: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            ReactionUtils.showToast(JoinRoomActivity.this, "Cannot join to the room: " + errorResponse.data.error);
            return null;
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityJoinRoomActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        setupButtons();

        // Set reaction
        this.joinRoomResponseReaction = new JoinRoomActivity.JoinRoomResponse();
        CustomWebSocketClient.getInstance().assignReactionToMessageType(ServerMessageType.JOIN_ROOM_RESPONSE, this.joinRoomResponseReaction);

    }


    private void setupButtons() {
        binding.joinRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nickname = binding.enterNicknameEditText.getText().toString();
                String enterCode = binding.enterCodeEditText.getText().toString();
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
