package com.example.splendormobilegame;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.splendormobilegame.databinding.ActivityGameActivityBinding;

public class GameActivity extends AppCompatActivity {
    private ActivityGameActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityGameActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        // binding.gameActivityConstraintLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        setupSideBar();
        setupButtons();
    }

    public void setupSideBar() {
        binding.sideBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pointCardVisible = (binding.pointsCardView.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
                int otherCardsVisible = (binding.pointsCardView.getVisibility() == View.GONE) ? View.GONE : View.VISIBLE;
                TransitionManager.beginDelayedTransition(binding.gameActivityConstraintLayout, new AutoTransition());
                binding.pointsCardView.setVisibility(pointCardVisible);
                binding.otherPlayerCardView.setVisibility(otherCardsVisible);
            }
        });


    }
    public void setupButtons(){
        //Button used for taking points
        binding.takePointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //button used for reserving cards
        binding.reserveCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        //button used for buying card
        binding.buyCardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }
}
