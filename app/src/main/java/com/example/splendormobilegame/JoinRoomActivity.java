package com.example.splendormobilegame;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splendormobilegame.databinding.ActivityJoinRoomActivityBinding;

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
    }

    private boolean validateResponse() {
        //TODO implement response logic
        return false;
    }

    private void setupButtons() {
        binding.joinRoomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.enterNameEditText.getText().toString();
                String password = binding.enterPasswordEditText.getText().toString();
                String nickname = binding.enterNicknameEditText.getText().toString();
                if (validateResponse()) {
                    Intent myIntent = new Intent(JoinRoomActivity.this, WaitingRoomActivity.class);
                    JoinRoomActivity.this.startActivity(myIntent);
                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                } else {
                    Toast.makeText(JoinRoomActivity.this,R.string.join_error,Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

}
