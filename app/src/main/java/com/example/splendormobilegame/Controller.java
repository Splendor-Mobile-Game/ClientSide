package com.example.splendormobilegame;

public abstract class Controller {
    protected CustomAppCompatActivity activity;

    protected Controller(CustomAppCompatActivity activity) {
        this.activity = activity;
    }
}
