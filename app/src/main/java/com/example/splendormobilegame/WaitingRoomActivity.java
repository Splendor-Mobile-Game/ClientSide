package com.example.splendormobilegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splendormobilegame.databinding.ActivityWaitingRoomActivityBinding;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.UserReaction;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.ServerMessageType;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.JoinRoom;
import com.github.splendor_mobile_game.websocket.handlers.reactions.LeaveRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.ArrayList;
import java.util.UUID;

public class WaitingRoomActivity extends AppCompatActivity {
    public ActivityWaitingRoomActivityBinding binding;
    private RecyclerView mRecyclerView;
    public WaitingRoomActivityAdapter mAdapter;
    private ArrayList<String> usersList = new ArrayList<>();

    private JoinRoomResponse joinRoomResponseReaction;
    private LeaveRoomResponse leaveRoomResponseReaction;

    public class JoinRoomResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered JoinRoomResponse react method");

            JoinRoom.ResponseData responseData = (JoinRoom.ResponseData) ReactionUtils.getResponseData(serverMessage, JoinRoom.ResponseData.class);

            JoinRoom.UserDataResponse newUser = responseData.users.get(responseData.users.size() - 1);
            Model.getRoom().addUser(new User(newUser.uuid, newUser.name));

            //Update recyclerView WaitingRoom
            WaitingRoomActivity.this.runOnUiThread(new Runnable() {
                public void run() {
                    ArrayList<String> usersList = new ArrayList<>();
                    for (User u : Model.getRoom().getUsers()) {
                        usersList.add(u.getName());
                    }
                    WaitingRoomActivityAdapter.usersList = usersList;
                    WaitingRoomActivity.this.mAdapter.notifyDataSetChanged();
                }
            });

            ReactionUtils.showToast(WaitingRoomActivity.this, "User: " + newUser.name + " has joined to the room.");
            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            ReactionUtils.showToast(WaitingRoomActivity.this, "Cannot join to the room: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            ReactionUtils.showToast(WaitingRoomActivity.this, "Cannot join to the room: " + errorResponse.data.error);
            return null;
        }

    }

    public class LeaveRoomResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered LeaveRoomResponse react method");

            // TODO: Probably not needed
            if (Model.getRoom() != null) {
                // Case when other player has left the room

                LeaveRoom.ResponseData responseData = (LeaveRoom.ResponseData) ReactionUtils.getResponseData(serverMessage, LeaveRoom.ResponseData.class);

                User userToRemove = Model.getRoom().getUserByUuid(responseData.user.id);
                Model.getRoom().removeUser(userToRemove);

                // DEBUG PURPOSES START
                WaitingRoomActivity.this.runOnUiThread(new Runnable() {
                    public void run() {
                        ArrayList<String> usersList = new ArrayList<>();
                        for (User u: Model.getRoom().getUsers()) {
                            usersList.add(u.getName());
                        }
                        WaitingRoomActivityAdapter.usersList = usersList;
                        WaitingRoomActivity.this.mAdapter.notifyDataSetChanged();
                    }
                });
                // DEBUG PURPOSES END

                ReactionUtils.showToast(WaitingRoomActivity.this, "User: " + responseData.user.name + " has left the room.");
            }

            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            ReactionUtils.showToast(WaitingRoomActivity.this, "Error while leaving the room: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            ReactionUtils.showToast(WaitingRoomActivity.this, "Error while leaving the room: " + errorResponse.data.error);
            return null;
        }

    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityWaitingRoomActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupButtons();

        // Set reaction
        this.joinRoomResponseReaction = new JoinRoomResponse();
        CustomWebSocketClient.getInstance().assignReactionToMessageType(ServerMessageType.JOIN_ROOM_RESPONSE, this.joinRoomResponseReaction);
        this.leaveRoomResponseReaction = new LeaveRoomResponse();
        CustomWebSocketClient.getInstance().assignReactionToMessageType(ServerMessageType.LEAVE_ROOM_RESPONSE, this.leaveRoomResponseReaction);

        binding.nameOfRoomTextView.setText(Model.getRoom().getName());
        binding.enterCode.setText(Model.getRoom().getEnterCode());

        //RecyclerView for user names
        for (User u : Model.getRoom().getUsers()) {
            usersList.add(u.getName());
        }
        mRecyclerView = binding.waitingPlayersRecyclerView;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WaitingRoomActivityAdapter(usersList);
        mRecyclerView.setAdapter(mAdapter);

    }

    private void setupButtons() {
        binding.startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WaitingRoomActivity.this, R.string.players_warning, Toast.LENGTH_SHORT).show();
            }
        });

        binding.leaveGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LeaveRoom.RoomDTO roomDTO = new LeaveRoom.RoomDTO(Model.getRoom().getUuid());
                LeaveRoom.UserDTO userDTO = new LeaveRoom.UserDTO(Model.getUserUuid());
                LeaveRoom.DataDTO dataDTO = new LeaveRoom.DataDTO(roomDTO, userDTO);

                UserMessage userMessage = new UserMessage(UUID.randomUUID(), UserRequestType.LEAVE_ROOM, dataDTO);
                CustomWebSocketClient.getInstance().send(userMessage);

                // Don't bother with what server thinks, we want to leave
                Model.setRoom(null);
                Intent myIntent = new Intent(WaitingRoomActivity.this, MainActivity.class);
                WaitingRoomActivity.this.startActivity(myIntent);
            }
        });
    }

    @Override
    public void onBackPressed() {

        LeaveRoom.RoomDTO roomDTO = new LeaveRoom.RoomDTO(Model.getRoom().getUuid());
        LeaveRoom.UserDTO userDTO = new LeaveRoom.UserDTO(Model.getUserUuid());
        LeaveRoom.DataDTO dataDTO = new LeaveRoom.DataDTO(roomDTO, userDTO);

        UserMessage userMessage = new UserMessage(UUID.randomUUID(), UserRequestType.LEAVE_ROOM, dataDTO);
        CustomWebSocketClient.getInstance().send(userMessage);

        // Don't bother with what server thinks, we want to leave
        Model.setRoom(null);
        Intent myIntent = new Intent(WaitingRoomActivity.this, MainActivity.class);
        WaitingRoomActivity.this.startActivity(myIntent);
    }

}
