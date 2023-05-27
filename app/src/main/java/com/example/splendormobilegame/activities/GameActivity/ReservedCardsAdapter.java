package com.example.splendormobilegame.activities.GameActivity;

import static android.view.View.GONE;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splendormobilegame.R;
import com.example.splendormobilegame.model.Card;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Noble;
import com.example.splendormobilegame.model.ReservedCard;
import com.example.splendormobilegame.model.User;
import com.github.splendor_mobile_game.game.enums.TokenType;

import java.util.List;

public class ReservedCardsAdapter extends RecyclerView.Adapter<ReservedCardsAdapter.ViewHolder> {
    private List<ReservedCard> cardList;
    private TextView whitePointsTextView;
    private TextView greenPointsTextView;
    private TextView redPointsTextView;
    private TextView bluePointsTextView;
    private TextView blackPointsTextView;
    private TextView pointsTextView;
    private TextView playerName;
    private ImageView cardType;
    private Context context;
    private android.app.Activity activity;

    public void setCardList(List<ReservedCard> cardList) {
        this.cardList = cardList;
    }


    public ReservedCardsAdapter(List<ReservedCard> assetDataList){
        this.cardList = assetDataList;
    }
    @NonNull
    @Override
    public ReservedCardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.reserved_card_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReservedCardsAdapter.ViewHolder holder, int position) {
        GameActivity activity = (GameActivity) context;
        ReservedCard cardData = cardList.get(position);
        LinearLayout linearLayout = holder.linearLayout;
        CardView cardView = linearLayout.findViewById(R.id.cardLayout);
        playerName = linearLayout.findViewById(R.id.playerName);
        View image = cardView.findViewById(R.id.cardConstraintLayout);
        playerName.setText(cardData.getUser().getName());

        cardType = cardView.findViewById(R.id.cardTypeImageView);
        pointsTextView = cardView.findViewById(R.id.pointsTextView);
        blackPointsTextView = cardView.findViewById(R.id.blackPointsTextView);
        bluePointsTextView = cardView.findViewById(R.id.bluePointsTextView);
        redPointsTextView = cardView.findViewById(R.id.redPointsTextView);
        greenPointsTextView = cardView.findViewById(R.id.greenPointsTextView);
        whitePointsTextView = cardView.findViewById(R.id.whitePointsTextView);

        String userTest = Model.getInstance().getRoom().getUserByUuid(Model.getInstance().getUserUuid()).getName();
        if(!cardData.isVisible()&&(userTest!=cardData.getUser().getName())){
            String resourceName = "cards_back1";
            if(cardData.getCard().getCardTier().toString()=="LEVEL_1")
            {
                resourceName = "cards_back1";
            }
            else if(cardData.getCard().getCardTier().toString()=="LEVEL_2")
            {
                resourceName = "cards_back2";
            }
            if(cardData.getCard().getCardTier().toString()=="LEVEL_3")
            {
                resourceName = "cards_back3";
            }

            int drawableResourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
            image.setBackgroundResource(drawableResourceId);
            whitePointsTextView.setVisibility(GONE);
            greenPointsTextView.setVisibility(GONE);
            redPointsTextView.setVisibility(GONE);
            bluePointsTextView.setVisibility(GONE);
            blackPointsTextView.setVisibility(GONE);
            pointsTextView.setVisibility(GONE);
            cardType.setVisibility(GONE);
        }else{
        String resourceName = "cards_bg" + (cardData.getCard().getGraphicsID());
        int drawableResourceId = context.getResources().getIdentifier(resourceName, "drawable", context.getPackageName());
        image.setBackgroundResource(drawableResourceId);

        whitePointsTextView.setText(String.valueOf(cardData.getCard().getDiamondCost()));
        greenPointsTextView.setText(String.valueOf(cardData.getCard().getEmeraldCost()));
        redPointsTextView.setText(String.valueOf(cardData.getCard().getRubyCost()));
        bluePointsTextView.setText(String.valueOf(cardData.getCard().getSapphireCost()));
        blackPointsTextView.setText(String.valueOf(cardData.getCard().getOnyxCost()));
        pointsTextView.setText(String.valueOf(cardData.getCard().getPoints()));

        if(cardData.getCard().getBonusToken() == TokenType.EMERALD){
            cardType.setImageResource(R.drawable.diamond_shape_green);
        }
        if(cardData.getCard().getBonusToken() == TokenType.SAPPHIRE){
            cardType.setImageResource(R.drawable.diamond_shape_blue);
        }
        if(cardData.getCard().getBonusToken() == TokenType.RUBY){
            cardType.setImageResource(R.drawable.diamond_shape_red);
        }
        if(cardData.getCard().getBonusToken() == TokenType.ONYX){
            cardType.setImageResource(R.drawable.diamond_shape_black);
        }
        if(cardData.getCard().getBonusToken() == TokenType.DIAMOND){
            cardType.setImageResource(R.drawable.diamond_shape_white);
        }
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.showBuyingReservedCardDialog(cardData.getCard().getUuid());
            }
        });
        }
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    protected static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;
        public ViewHolder(View v){
            super(v);
            // Each ViewHolder only has a CardView (even though that CardView has child TextViews
            linearLayout = (LinearLayout) v;
        }
    }


}
