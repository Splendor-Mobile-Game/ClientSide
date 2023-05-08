package com.example.splendormobilegame.activities.GameActivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.transition.AutoTransition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.R;
import com.example.splendormobilegame.databinding.ActivityGameActivityBinding;
import com.example.splendormobilegame.model.Card;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Room;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.github.splendor_mobile_game.game.enums.CardTier;
import com.github.splendor_mobile_game.game.enums.TokenType;
import com.github.splendor_mobile_game.websocket.handlers.ServerMessageType;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class GameActivity extends CustomAppCompatActivity {
    private ActivityGameActivityBinding binding;
    private int blueTokens = 0;
    private int redTokens = 0;
    private int blackTokens = 0;
    private int whiteTokens = 0;
    private int greenTokens = 0;

    private DeckReservingController deckReservingController;
    private RevealedCardsReservingController revealedCardsReservingController;
    private TokensController tokensController;
    private TurnController turnController;
    private BuyingRevealedCardsController buyingRevealedCardsController;
    private BuyingReservedCardsController buyingReservedCardsController;
    private LeavingController leavingController;
    private GameEndingController gameEndingController;
    CardsFirstTierAdapter cardsFirstTierAdapter;
    CardsSecondTierAdapter cardsSecondTierAdapter;
    CardsThirdTierAdapter cardsThirdTierAdapter;
    ReservedCardsAdapter reservedCardsAdapter;
    RecyclerView cardsFirstTierRecyclerView;
    RecyclerView cardsSecondTierRecyclerView;
    RecyclerView cardsThirdTierRecyclerView;
    RecyclerView reservedCardsBuyingRecyclerView;
    List<Card> cardListFirstTier = new ArrayList<>();
    List<Card> cardListSecondTier = new ArrayList<>();
    List<Card> cardListThirdTier = new ArrayList<>();
    List<Card> cardListReservedCards = new ArrayList<>();

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
        setupCardsFirstTierRecyclerView();
        setupCardsSecondTierRecyclerView();
        setupCardsThirdTierRecyclerView();
        setupReservingFromDeckButtons();
        setupBuyingReservedCards();
        updateTokenNumber();

        // Create controllers
        this.turnController = new TurnController(this);

        this.deckReservingController = new DeckReservingController(this, this.turnController);
        this.revealedCardsReservingController = new RevealedCardsReservingController(this, this.turnController);
        this.tokensController = new TokensController(this, this.turnController);
        this.buyingRevealedCardsController = new BuyingRevealedCardsController(this, this.turnController);
        this.buyingReservedCardsController = new BuyingReservedCardsController(this, this.turnController);
        this.leavingController = new LeavingController(this);
        this.gameEndingController = new GameEndingController(this);

        // Set reactions
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.MAKE_RESERVATION_FROM_DECK_ANNOUNCEMENT,
                this.deckReservingController.getReservationFromDeckMessageHandler()
        );
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.MAKE_RESERVATION_FROM_DECK_RESPONSE,
                this.deckReservingController.getReservationFromDeckMessageHandler()
        );

        // Commented out reactions are still being developed on the client
//        CustomWebSocketClient.getInstance().assignReactionToMessageType(
//                ServerMessageType.MAKE_RESERVATION_FROM_REVEALED_ANNOUNCEMENT,
//                this.revealedCardsReservingController.getReservationFromRevealedMessageHandler()
//        );
//        CustomWebSocketClient.getInstance().assignReactionToMessageType(
//                ServerMessageType.MAKE_RESERVATION_FROM_REVEALED_RESPONSE,
//                this.revealedCardsReservingController.getReservationFromRevealedMessageHandler()
//        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.GET_TOKENS_RESPONSE,
                this.tokensController.getGetTokensMessageHandler()
        );

//        CustomWebSocketClient.getInstance().assignReactionToMessageType(
//                ServerMessageType.NEW_TURN_ANNOUNCEMENT,
//                this.turnController.getNewTurnAnnouncementMessageHandler()
//        );
//        CustomWebSocketClient.getInstance().assignReactionToMessageType(
//                ServerMessageType.PASS_TURN_ANNOUNCEMENT,
//                this.turnController.getPassTurnAnnouncementMessageHandler()
//        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.BUY_REVEALED_MINE_ANNOUNCEMENT,
                this.buyingRevealedCardsController.getBuyRevealedCardMessageHandler()
        );
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.BUY_REVEALED_MINE_RESPONSE,
                this.buyingRevealedCardsController.getBuyRevealedCardMessageHandler()
        );

//        CustomWebSocketClient.getInstance().assignReactionToMessageType(
//                ServerMessageType.BUY_RESERVED_MINE_ANNOUNCEMENT,
//                this.buyingReservedCardsController.getBuyReservedCardMessageHandler()
//        );
//        CustomWebSocketClient.getInstance().assignReactionToMessageType(
//                ServerMessageType.BUY_RESERVED_MINE_RESPONSE,
//                this.buyingReservedCardsController.getBuyReservedCardMessageHandler()
//        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.LEAVE_ROOM_RESPONSE,
                this.leavingController.getLeaveRoomResponse()
        );

//        CustomWebSocketClient.getInstance().assignReactionToMessageType(
//                ServerMessageType.GAME_ENDED,
//                this.gameEndingController.getGameEndedMessageHandler()
//        );
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
                binding.reservedCardsTextView.setVisibility(otherCardsVisible);
            }
        });
    }

    private void setupButtons() {
        //Button used for taking points
        binding.takePointsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeRightSide();
            }
        });
    }

    //Function swapping right side between take points and cards.
    private void ChangeRightSide() {
        int cardsCard = (binding.CardsAristocratsCardView.getVisibility() == View.GONE) ? View.VISIBLE : View.GONE;
        int takeTokensCard = (binding.CardsAristocratsCardView.getVisibility() == View.GONE) ? View.GONE : View.VISIBLE;
        TransitionManager.beginDelayedTransition(binding.gameActivityConstraintLayout, new AutoTransition());
        binding.CardsAristocratsCardView.setVisibility(cardsCard);
        binding.takeTokensCardView.setVisibility(takeTokensCard);
    }

    private void setupPointsButtons() {
        binding.addBlackTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blackTokens < 2) {
                    blackTokens++;
                    binding.blackTokenNumberTextView.setText(String.valueOf(blackTokens));
                }
            }
        });
        binding.addRedTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (redTokens < 2) {
                    redTokens++;
                    binding.redTokenNumberTextView.setText(String.valueOf(redTokens));
                }
            }
        });
        binding.addWhiteTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (whiteTokens < 2) {
                    whiteTokens++;
                    binding.whiteTokenNumberTextView.setText(String.valueOf(whiteTokens));
                }
            }
        });
        binding.addGreenTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (greenTokens < 2) {
                    greenTokens++;
                    binding.greenTokenNumberTextView.setText(String.valueOf(greenTokens));
                }
            }
        });
        binding.addBlueTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blueTokens < 2) {
                    blueTokens++;
                    binding.blueTokenNumberTextView.setText(String.valueOf(blueTokens));
                }
            }
        });
        binding.removeBlackTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blackTokens > -3) {
                    blackTokens--;
                    binding.blackTokenNumberTextView.setText(String.valueOf(blackTokens));
                }
            }
        });
        binding.removeRedTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (redTokens > -3) {
                    redTokens--;
                    binding.redTokenNumberTextView.setText(String.valueOf(redTokens));
                }
            }
        });
        binding.removeWhiteTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (whiteTokens > -3) {
                    whiteTokens--;
                    binding.whiteTokenNumberTextView.setText(String.valueOf(whiteTokens));
                }
            }
        });
        binding.removeGreenTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (greenTokens > -3) {
                    greenTokens--;
                    binding.greenTokenNumberTextView.setText(String.valueOf(greenTokens));
                }
            }
        });
        binding.removeBlueTokenImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (blueTokens > -3) {
                    blueTokens--;
                    binding.blueTokenNumberTextView.setText(String.valueOf(blueTokens));
                }
            }
        });

        binding.tokenButtonsCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }

    public void showCardAlertDialog(UUID cardUuid) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.card_title))
                .setMessage(getResources().getString(R.string.card_message))
                .setNeutralButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Respond to neutral button press (hide dialog)
                    }
                })
                .setNegativeButton(getResources().getString(R.string.reserve_card), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Respond to negative button press (left button)
                        revealedCardsReservingController.reserveCard(cardUuid);
                    }
                })
                .setPositiveButton(getResources().getString(R.string.buy_card), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Respond to positive button press (right button)
                        buyingRevealedCardsController.buyRevealedCard(cardUuid);
                    }
                })
                .show();
    }

    public void showDeckReserveDialog(int cardTier) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.card_title))
                .setMessage(getResources().getString(R.string.card_message))
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //hide dialog
                    }
                })
                .setPositiveButton(getResources().getString(R.string.reserve_card), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Respond to positive button press (right button)
                        deckReservingController.reserveCard(cardTier);
                    }
                })
                .show();
    }
    public void showBuyingReservedCardDialog(UUID cardUuid) {
        new MaterialAlertDialogBuilder(this)
                .setTitle(getResources().getString(R.string.card_title))
                .setMessage(getResources().getString(R.string.card_message))
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //hide dialog
                    }
                })
                .setPositiveButton(getResources().getString(R.string.buy_card), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Respond to positive button press (right button)
                        buyingRevealedCardsController.buyRevealedCard(cardUuid);
                    }
                })
                .show();
    }

    private void setupReservingFromDeckButtons() {
        binding.CardPile1CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reserve 1 tier card
                showDeckReserveDialog(1);
            }
        });
        binding.CardPile2CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reserve 2 tier card
                showDeckReserveDialog(2);
            }
        });
        binding.CardPile3CardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //reserve 3 tier card
                showDeckReserveDialog(3);
            }
        });

    }

    public void setupCardsFirstTierRecyclerView() {
        cardsFirstTierRecyclerView = (RecyclerView) binding.cards1RecyclerView;
        cardsFirstTierRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Card myCard1 = new Card(UUID.randomUUID(), CardTier.LEVEL_1, 5, 10, 7, 3, 2, 1, TokenType.RUBY);
        Card myCard2 = new Card(UUID.randomUUID(), CardTier.LEVEL_1, 3, 40, 9, 3, 2, 1, TokenType.SAPPHIRE);
        Card myCard3 = new Card(UUID.randomUUID(), CardTier.LEVEL_1, 4, 50, 5, 3, 2, 1, TokenType.DIAMOND);
        Card myCard4 = new Card(UUID.randomUUID(), CardTier.LEVEL_1, 3, 16, 58, 3, 2, 1, TokenType.DIAMOND);
        cardListFirstTier.add(myCard1);
        cardListFirstTier.add(myCard2);
        cardListFirstTier.add(myCard3);
        cardListFirstTier.add(myCard4);
        cardsFirstTierAdapter = new CardsFirstTierAdapter(cardListFirstTier);
        cardsFirstTierRecyclerView.setAdapter(cardsFirstTierAdapter);
        // The list we passed to the mAdapter was changed so we have to notify it in order to update
        cardsFirstTierAdapter.notifyDataSetChanged();
    }

    public void setupCardsSecondTierRecyclerView() {
        cardsSecondTierRecyclerView = (RecyclerView) binding.cards2RecyclerView;
        cardsSecondTierRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Card myCard1 = new Card(UUID.randomUUID(), CardTier.LEVEL_2, 5, 10, 7, 3, 2, 1, TokenType.EMERALD);
        Card myCard2 = new Card(UUID.randomUUID(), CardTier.LEVEL_2, 3, 40, 9, 3, 2, 1, TokenType.SAPPHIRE);
        Card myCard3 = new Card(UUID.randomUUID(), CardTier.LEVEL_2, 4, 50, 5, 3, 2, 1, TokenType.RUBY);
        Card myCard4 = new Card(UUID.randomUUID(), CardTier.LEVEL_2, 3, 16, 58, 3, 2, 1, TokenType.ONYX);
        cardListSecondTier.add(myCard1);
        cardListSecondTier.add(myCard2);
        cardListSecondTier.add(myCard3);
        cardListSecondTier.add(myCard4);
        cardsSecondTierAdapter = new CardsSecondTierAdapter(cardListSecondTier);
        cardsSecondTierRecyclerView.setAdapter(cardsSecondTierAdapter);
        // The list we passed to the mAdapter was changed so we have to notify it in order to update
        cardsFirstTierAdapter.notifyDataSetChanged();
    }

    public void setupCardsThirdTierRecyclerView() {
        cardsThirdTierRecyclerView = (RecyclerView) binding.cards3RecyclerView;
        cardsThirdTierRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        Card myCard1 = new Card(UUID.randomUUID(), CardTier.LEVEL_3, 5, 10, 7, 3, 2, 1, TokenType.ONYX);
        Card myCard2 = new Card(UUID.randomUUID(), CardTier.LEVEL_3, 3, 40, 9, 3, 2, 1, TokenType.EMERALD);
        Card myCard3 = new Card(UUID.randomUUID(), CardTier.LEVEL_3, 4, 50, 5, 3, 2, 1, TokenType.DIAMOND);
        Card myCard4 = new Card(UUID.randomUUID(), CardTier.LEVEL_3, 3, 16, 58, 3, 2, 1, TokenType.EMERALD);
        cardListThirdTier.add(myCard1);
        cardListThirdTier.add(myCard2);
        cardListThirdTier.add(myCard3);
        cardListThirdTier.add(myCard4);
        cardsThirdTierAdapter = new CardsThirdTierAdapter(cardListThirdTier);
        cardsThirdTierRecyclerView.setAdapter(cardsThirdTierAdapter);
        // The list we passed to the mAdapter was changed so we have to notify it in order to update
        cardsFirstTierAdapter.notifyDataSetChanged();
    }
    //Numbers don't need to be localized
    @SuppressLint("SetTextI18n")
    public void updateScoreBoard(){
        ArrayList<User> users = Model.getRoom().getUsers();
        int userCount = Model.getRoom().getPlayerCount();
        if(userCount>0){
            HashMap<TokenType, Integer> tokens =  users.get(0).getTokens();
            binding.Player1NameTV.setText(users.get(0).getName());
            binding.Player1BlackPointsTV.setText(tokens.get(TokenType.ONYX).toString());
            binding.Player1BluePointsTV.setText(tokens.get(TokenType.SAPPHIRE).toString());
            binding.Player1RedPointsTV.setText(tokens.get(TokenType.RUBY).toString());
            binding.Player1WhitePointsTV.setText(tokens.get(TokenType.DIAMOND).toString());
            binding.Player1GreenPointsTV.setText(tokens.get(TokenType.EMERALD).toString());
            binding.Player1YellowPointsTV.setText(tokens.get(TokenType.GOLD_JOKER).toString());
            binding.Player1PointsTV.setText(users.get(0).getPoints());
        }
        if(userCount>1){
            HashMap<TokenType, Integer> tokens =  users.get(1).getTokens();
            binding.Player2NameTV.setText(users.get(1).getName());
            binding.Player2BlackPointsTV.setText(tokens.get(TokenType.ONYX).toString());
            binding.Player2BluePointsTV.setText(tokens.get(TokenType.SAPPHIRE).toString());
            binding.Player2RedPointsTV.setText(tokens.get(TokenType.RUBY).toString());
            binding.Player2WhitePointsTV.setText(tokens.get(TokenType.DIAMOND).toString());
            binding.Player2GreenPointsTV.setText(tokens.get(TokenType.EMERALD).toString());
            binding.Player2YellowPointsTV.setText(tokens.get(TokenType.GOLD_JOKER).toString());
            binding.Player2PointsTV.setText(users.get(1).getPoints());
        }
        if(userCount>2){
            HashMap<TokenType, Integer> tokens =  users.get(2).getTokens();
            binding.Player3NameTV.setText(users.get(2).getName());
            binding.Player3BlackPointsTV.setText(tokens.get(TokenType.ONYX).toString());
            binding.Player3BluePointsTV.setText(tokens.get(TokenType.SAPPHIRE).toString());
            binding.Player3RedPointsTV.setText(tokens.get(TokenType.RUBY).toString());
            binding.Player3WhitePointsTV.setText(tokens.get(TokenType.DIAMOND).toString());
            binding.Player3GreenPointsTV.setText(tokens.get(TokenType.EMERALD).toString());
            binding.Player3YellowPointsTV.setText(tokens.get(TokenType.GOLD_JOKER).toString());
            binding.Player3PointsTV.setText(users.get(2).getPoints());
        }
        if(userCount>3){
            HashMap<TokenType, Integer> tokens =  users.get(3).getTokens();
            binding.Player3NameTV.setText(users.get(3).getName());
            binding.Player3BlackPointsTV.setText(tokens.get(TokenType.ONYX).toString());
            binding.Player3BluePointsTV.setText(tokens.get(TokenType.SAPPHIRE).toString());
            binding.Player3RedPointsTV.setText(tokens.get(TokenType.RUBY).toString());
            binding.Player3WhitePointsTV.setText(tokens.get(TokenType.DIAMOND).toString());
            binding.Player3GreenPointsTV.setText(tokens.get(TokenType.EMERALD).toString());
            binding.Player3YellowPointsTV.setText(tokens.get(TokenType.GOLD_JOKER).toString());
            binding.Player3PointsTV.setText(users.get(3).getPoints());
        }
    }

    private void setupBuyingReservedCards(){
        reservedCardsBuyingRecyclerView = (RecyclerView) binding.reservedCardsRecyclerView;
        LinearLayoutManager HorizontalLayout = new LinearLayoutManager(GameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        reservedCardsBuyingRecyclerView.setLayoutManager(HorizontalLayout);
        Card myCard1 = new Card(UUID.randomUUID(), CardTier.LEVEL_2, 5, 10, 7, 3, 2, 1, TokenType.EMERALD);
        Card myCard2 = new Card(UUID.randomUUID(), CardTier.LEVEL_2, 3, 40, 9, 3, 2, 1, TokenType.SAPPHIRE);
        Card myCard3 = new Card(UUID.randomUUID(), CardTier.LEVEL_2, 4, 50, 5, 3, 2, 1, TokenType.RUBY);
        Card myCard4 = new Card(UUID.randomUUID(), CardTier.LEVEL_2, 3, 16, 58, 3, 2, 1, TokenType.ONYX);
        //testing
        cardListReservedCards.add(myCard1);
        cardListReservedCards.add(myCard2);
        cardListReservedCards.add(myCard3);
        cardListReservedCards.add(myCard4);
        reservedCardsAdapter = new ReservedCardsAdapter(cardListReservedCards);
        reservedCardsBuyingRecyclerView.setAdapter(reservedCardsAdapter);
        // The list we passed to the mAdapter was changed so we have to notify it in order to update
        reservedCardsAdapter.notifyDataSetChanged();
    }

    public void updateTokenNumber(){
        //Another Getter is missing, it will be implemented in next commit.
        binding.blackTokenButton.setText("");
        binding.blueTokenButton.setText("");
        binding.redTokenButton.setText("");
        binding.greenTokenButton.setText("");
        binding.whiteTokenButton.setText("");
        binding.yellowTokenButton.setText("");

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
