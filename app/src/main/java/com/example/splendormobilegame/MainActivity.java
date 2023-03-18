package com.example.splendormobilegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splendormobilegame.databinding.ActivityMainActivityBinding;

public class MainActivity extends AppCompatActivity {
    private ActivityMainActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Make app fullscreen + delete toolbar
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        //Bind layout with class
        binding = ActivityMainActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        //Setup buttons
        setupButtons();
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
