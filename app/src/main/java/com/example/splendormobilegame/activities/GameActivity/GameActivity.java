package com.example.splendormobilegame.activities.GameActivity;

import android.animation.LayoutTransition;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.databinding.ActivityGameActivityBinding;

import org.xmlpull.v1.XmlPullParser;

public class GameActivity extends CustomAppCompatActivity {
    private ActivityGameActivityBinding binding;
    private int blueTokens = 0;
    private int redTokens = 0;
    private int blackTokens = 0;
    private int whiteTokens = 0;
    private int greenTokens = 0;

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
        setupPointsButtons();
    }

    private void setupSideBar() {
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
    private void setupButtons(){
        //Button used for taking points
        binding.takePointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeRightSide();
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
    //Function swapping right side between take points and cards.
    private void ChangeRightSide(){
        int cardsCard = (binding.CardsAristocratsCardView.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int takeTokensCard = (binding.CardsAristocratsCardView.getVisibility() == View.GONE) ? View.GONE : View.VISIBLE;
        TransitionManager.beginDelayedTransition(binding.gameActivityConstraintLayout, new AutoTransition());
        binding.CardsAristocratsCardView.setVisibility(cardsCard);
        binding.takeTokensCardView.setVisibility(takeTokensCard);
    }
    private void setupPointsButtons(){
        binding.addBlackTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(blackTokens<2){
                    blackTokens++;
                    binding.blackTokenNumberTextView.setText(String.valueOf(blackTokens));
                }
            }
        });
        binding.addRedTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(redTokens<2){
                    redTokens++;
                    binding.redTokenNumberTextView.setText(String.valueOf(redTokens));
                }
            }
        });
        binding.addWhiteTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(whiteTokens<2){
                    whiteTokens++;
                    binding.whiteTokenNumberTextView.setText(String.valueOf(whiteTokens));
                }
            }
        });
        binding.addGreenTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(greenTokens<2){
                    greenTokens++;
                    binding.greenTokenNumberTextView.setText(String.valueOf(greenTokens));
                }
            }
        });
        binding.addBlueTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(blueTokens<2){
                    blueTokens++;
                    binding.blueTokenNumberTextView.setText(String.valueOf(blueTokens));
                }
            }
        });
        binding.removeBlackTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(blackTokens>-3){
                    blackTokens--;
                    binding.blackTokenNumberTextView.setText(String.valueOf(blackTokens));
                }
            }
        });
        binding.removeRedTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(redTokens>-3){
                    redTokens--;
                    binding.redTokenNumberTextView.setText(String.valueOf(redTokens));
                }
            }
        });
        binding.removeWhiteTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(whiteTokens>-3){
                    whiteTokens--;
                    binding.whiteTokenNumberTextView.setText(String.valueOf(whiteTokens));
                }
            }
        });
        binding.removeGreenTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(greenTokens>-3){
                    greenTokens--;
                    binding.greenTokenNumberTextView.setText(String.valueOf(greenTokens));
                }
            }
        });
        binding.removeBlueTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(blueTokens>-3){
                    blueTokens--;
                    binding.blueTokenNumberTextView.setText(String.valueOf(blueTokens));
                }
            }
        });

    }
}
