package com.example.splendormobilegame;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splendormobilegame.databinding.ActivityMainActivityBinding;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.Model;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.handlers.ServerMessageType;

import java.io.Serializable;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import tech.gusavila92.websocketclient.WebSocketClient;

public class MainActivity extends AppCompatActivity implements Serializable {
    private ActivityMainActivityBinding binding;
    protected SharedPreferences sharedPreferences;
    private String userUUID;

    CustomWebSocketClient client;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Make app fullscreen + delete toolbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //Bind layout with class
        binding = ActivityMainActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //Setup buttons
        setupButtons();
        //Join Server
        //WebSocket.createWebSocketClient();
        createUserUUID();

        Map<ServerMessageType, Class<? extends UserReaction>> reactions = new HashMap<>();
        reactions.put(ServerMessageType.CREATE_ROOM_RESPONSE, com.example.splendormobilegame.websocket.reactions.CreateRoomResponse.class);
        Model model = new Model();

        try {
            CustomWebSocketClient.initialize(new URI("ws://10.0.2.2:8887"), model, reactions);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

        this.client = CustomWebSocketClient.getInstance();
    }

    private void createUserUUID(){
        sharedPreferences = getApplication().getApplicationContext().getSharedPreferences(
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        userUUID = sharedPreferences.getString("userUUID","");
        if(userUUID.isEmpty()){
            userUUID = UUID.randomUUID().toString();
            sharedPreferences.edit().putString("userUUID",userUUID).apply();
        }
    }
    private void setupButtons() {
        binding.createGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create new intent -> pass class -> start intent
                Intent myIntent = new Intent(MainActivity.this, CreateRoomActivity.class);
                MainActivity.this.startActivity(myIntent);
                //Animation when switching classes
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        binding.joinGameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(MainActivity.this, JoinRoomActivity.class);
                MainActivity.this.startActivity(myIntent);
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            }
        });
        binding.exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //close app
                finish();
                System.exit(0);

            }
        });


    }

}
