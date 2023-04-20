package com.example.splendormobilegame.activities.GameActivity;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;

public class ReservingController<T extends GameActivity> extends Controller {

    private T gameActivity;

    protected ReservingController(T activity) {
        super(activity);
        this.gameActivity = activity;
    }


}
