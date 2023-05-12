package com.example.splendormobilegame.activities.GameActivity;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splendormobilegame.R;
import com.example.splendormobilegame.model.Card;
import com.example.splendormobilegame.model.Noble;
import com.github.splendor_mobile_game.game.enums.TokenType;

import java.util.List;

public class nobleCardsAdapter extends RecyclerView.Adapter<nobleCardsAdapter.ViewHolder> {
    private List<Noble> cardList;
    private Context context;


    public nobleCardsAdapter(List<Noble> assetDataList){
        this.cardList = assetDataList;
    }
    @NonNull
    @Override
    public nobleCardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.noble_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull nobleCardsAdapter.ViewHolder holder, int position) {
        GameActivity activity = (GameActivity) context;
        Noble cardData = cardList.get(position);
        CardView cardView = holder.cardView;

        TextView[] neededPoints = new TextView[3];
        neededPoints[0] = cardView.findViewById(R.id.neededPoints1);
        neededPoints[1] = cardView.findViewById(R.id.neededPoints2);
        neededPoints[2] = cardView.findViewById(R.id.neededPoints3);

        int whichCard = 0;

        if (cardData.getEmeraldCost() != 0) {
            neededPoints[whichCard].setText(String.valueOf(cardData.getEmeraldCost()));
            neededPoints[whichCard].setBackgroundColor(context.getColor(R.color.green));
            whichCard++;
        }

        if (cardData.getSapphireCost() != 0) {
            neededPoints[whichCard].setText(String.valueOf(cardData.getSapphireCost()));
            neededPoints[whichCard].setBackgroundColor(context.getColor(R.color.blue));
            whichCard++;
        }

        if (cardData.getRubyCost() != 0) {
            neededPoints[whichCard].setText(String.valueOf(cardData.getRubyCost()));
            neededPoints[whichCard].setBackgroundColor(context.getColor(R.color.poppy));
            whichCard++;
        }

        if (cardData.getDiamondCost() != 0) {
            neededPoints[whichCard].setText(String.valueOf(cardData.getDiamondCost()));
            neededPoints[whichCard].setBackgroundColor(context.getColor(R.color.white));
            neededPoints[whichCard].setTextColor(context.getColor(R.color.black));
            whichCard++;
        }

        if (cardData.getOnyxCost() != 0) {
            neededPoints[whichCard].setText(String.valueOf(cardData.getOnyxCost()));
            neededPoints[whichCard].setBackgroundColor(context.getColor(R.color.black));

            whichCard++;
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        CardView cardView;

        public ViewHolder(View v){
            super(v);
            // Each ViewHolder only has a CardView (even though that CardView has child TextViews
            cardView = (CardView) v;
        }
    }


}
