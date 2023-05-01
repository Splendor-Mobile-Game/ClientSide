package com.example.splendormobilegame.activities.WaitingRoom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.MainActivity;
import com.example.splendormobilegame.R;
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

public class WaitingRoomActivity extends CustomAppCompatActivity {
    public ActivityWaitingRoomActivityBinding binding;
    private RecyclerView mRecyclerView;
    public WaitingRoomActivityAdapter mAdapter;
    private ArrayList<String> usersList = new ArrayList<>();

    private LeavingController leavingController;
    private JoiningController joiningController;
    private StartGameController startGameController;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityWaitingRoomActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupButtons();

        // Create adapter for list of users in the room
        // RecyclerView for user names
        for (User u : Model.getRoom().getUsers()) {
            usersList.add(u.getName());
        }

        mRecyclerView = binding.waitingPlayersRecyclerView;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WaitingRoomActivityAdapter(usersList);
        mRecyclerView.setAdapter(mAdapter);

        // Create controllers
        this.leavingController = new LeavingController(this, mAdapter);
        this.joiningController = new JoiningController(this, mAdapter);
        this.startGameController = new StartGameController(this);

        // Set reaction
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.LEAVE_ROOM_RESPONSE,
                this.leavingController.getLeaveRoomResponse()
        );
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.JOIN_ROOM_RESPONSE,
                this.joiningController.getJoinRoomResponse()
        );
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.START_GAME_RESPONSE,
                this.startGameController.getStartGameResponse()
        );

        binding.nameOfRoomTextView.setText(Model.getRoom().getName());
        binding.enterCode.setText(Model.getRoom().getEnterCode());
    }

    private void setupButtons() {
        binding.startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WaitingRoomActivity.this.startGameController.startGame();
            }
        });

        binding.leaveGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                leaveRoom();
            }
        });
    }

    @Override
    public void onBackPressed() {leaveRoom();}


    private void leaveRoom(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AlertDialogCustom);
        builder.setMessage(R.string.leave_confirmation_message);
        builder.setTitle(R.string.leave_confirmation_title);
        builder.setPositiveButton(R.string.leave_room, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                WaitingRoomActivity.this.leavingController.sendRequest();
            }
        });
        builder.setNegativeButton(R.string.cancel, null);
        builder.show();
    }

}
