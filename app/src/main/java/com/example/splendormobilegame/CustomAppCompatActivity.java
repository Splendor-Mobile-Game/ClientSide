package com.example.splendormobilegame;

import android.content.Intent;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CustomAppCompatActivity extends AppCompatActivity {

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    public void changeActivity(Class<? extends AppCompatActivity> newActivity) {
        Intent myIntent = new Intent(this, newActivity);
        this.startActivity(myIntent);
    }
}
