package com.example.splendormobilegame.activities.GameActivity;

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
import com.github.splendor_mobile_game.game.enums.TokenType;

import java.util.List;

public class CardsSecondTierAdapter extends RecyclerView.Adapter<CardsSecondTierAdapter.ViewHolder> {
    private List<Card> cardList;
    private TextView whitePointsTextView;
    private TextView greenPointsTextView;
    private TextView redPointsTextView;
    private TextView bluePointsTextView;
    private TextView blackPointsTextView;
    private Context context;
    private TextView pointsTextView;
    private ImageView cardType;

    public void setCardList(List<Card> cardList) {
        this.cardList = cardList;
    }

    public CardsSecondTierAdapter(List<Card> assetDataList){
        this.cardList = assetDataList;
    }
    @NonNull
    @Override
    public CardsSecondTierAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.game_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CardsSecondTierAdapter.ViewHolder holder, int position) {
        GameActivity activity = (GameActivity) context;
        Card cardData = cardList.get(position);
        CardView cardView = holder.cardView;

        View image = cardView.findViewById(R.id.cardConstraintLayout);
        String resourceName = "cards_bg" + (cardData.getGraphicsID());
        int drawableResourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
        image.setBackgroundResource(drawableResourceId);

        whitePointsTextView = cardView.findViewById(R.id.whitePointsTextView);
        whitePointsTextView.setText(String.valueOf(cardData.getDiamondCost()));
        greenPointsTextView = cardView.findViewById(R.id.greenPointsTextView);
        greenPointsTextView.setText(String.valueOf(cardData.getEmeraldCost()));
        redPointsTextView = cardView.findViewById(R.id.redPointsTextView);
        redPointsTextView.setText(String.valueOf(cardData.getRubyCost()));
        bluePointsTextView = cardView.findViewById(R.id.bluePointsTextView);
        bluePointsTextView.setText(String.valueOf(cardData.getSapphireCost()));
        blackPointsTextView = cardView.findViewById(R.id.blackPointsTextView);
        blackPointsTextView.setText(String.valueOf(cardData.getOnyxCost()));
        pointsTextView = cardView.findViewById(R.id.pointsTextView);
        pointsTextView.setText(String.valueOf(cardData.getPoints()));
        cardType = cardView.findViewById(R.id.cardTypeImageView);
        if(cardData.getBonusToken() == TokenType.EMERALD){
            cardType.setImageResource(R.drawable.diamond_shape_green);
        }
        if(cardData.getBonusToken() == TokenType.SAPPHIRE){
            cardType.setImageResource(R.drawable.diamond_shape_blue);
        }
        if(cardData.getBonusToken() == TokenType.RUBY){
            cardType.setImageResource(R.drawable.diamond_shape_red);
        }
        if(cardData.getBonusToken() == TokenType.ONYX){
            cardType.setImageResource(R.drawable.diamond_shape_black);
        }
        if(cardData.getBonusToken() == TokenType.DIAMOND){
            cardType.setImageResource(R.drawable.diamond_shape_white);
        }
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showCardAlertDialog(cardData.getUuid());
            }
        });

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
