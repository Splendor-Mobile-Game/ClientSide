package com.example.splendormobilegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splendormobilegame.databinding.ActivityWaitingRoomActivityBinding;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.LeaveRoom;

import java.util.ArrayList;
import java.util.UUID;

public class WaitingRoomActivity extends AppCompatActivity {
    public ActivityWaitingRoomActivityBinding binding;
    private RecyclerView mRecyclerView;
    public static WaitingRoomActivityAdapter mAdapter;
    private ArrayList<String> usersList = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityWaitingRoomActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        setupButtons();
        Model.setActivity(this);

        binding.nameOfRoomTextView.setText(Model.getRoom().getName());
        binding.enterCode.setText(Model.getRoom().getEnterCode());

        // DEBUG PURPOSES START
        String users = "";
        for (User u: Model.getRoom().getUsers()) {
            users += u.getName() + "\n";
            usersList.add(u.getName());
        }
        mRecyclerView = binding.waitingPlayersRecyclerView;
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new WaitingRoomActivityAdapter(usersList);
        binding.debugUsers.setText(users);
        mRecyclerView.setAdapter(mAdapter);
        // DEBUG PURPOSES END

    }

    private void setupButtons() {
        binding.startGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(WaitingRoomActivity.this,R.string.players_warning,Toast.LENGTH_SHORT).show();
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
}
