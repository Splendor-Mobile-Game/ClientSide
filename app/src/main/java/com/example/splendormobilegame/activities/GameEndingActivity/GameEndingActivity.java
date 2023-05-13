package com.example.splendormobilegame.activities.GameEndingActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.annotation.Nullable;

import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.MainActivity;
import com.example.splendormobilegame.R;
import com.example.splendormobilegame.model.Model;
import com.github.splendor_mobile_game.websocket.handlers.reactions.EndTurn;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class GameEndingActivity extends CustomAppCompatActivity {

    public ArrayList<EndTurn.PlayerDataResponse> playerRanking;
    private ArrayAdapter<String> rankingAdapter;
    private ListView rankingListView;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.endingscreen);
        rankingListView = findViewById(R.id.rankingListView);
        playerRanking = Model.getRoom().getGame().getPlayerRanking();
        Collections.sort(playerRanking, new Comparator<EndTurn.PlayerDataResponse>() {
            @Override
            public int compare(EndTurn.PlayerDataResponse o1, EndTurn.PlayerDataResponse o2) {
                return Integer.compare(o1.points,o2.points);
            }
        });
        List<String> rankingList = new ArrayList<>();

        for (int i= 0; i<rankingList.size();i++)
        {
            EndTurn.PlayerDataResponse o1 = playerRanking.get(i);
            rankingList.add((i+1)+". "+ Model.getRoom().getUserByUuid(o1.playerUUID).getName()+ " "+o1.points);
        }
        rankingAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,rankingList);
        rankingListView.setAdapter(rankingAdapter);

        Button backToMenuButton = findViewById(R.id.BackToMenuButton);
        backToMenuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeActivity(MainActivity.class);
            }
        });
    }

}
