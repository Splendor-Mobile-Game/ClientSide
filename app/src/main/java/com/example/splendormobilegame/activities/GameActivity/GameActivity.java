package com.example.splendormobilegame.activities.GameActivity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.example.splendormobilegame.activities.CreateRoom.CreateRoomActivity;
import com.example.splendormobilegame.activities.WaitingRoom.NewRoomOwnerController;
import com.example.splendormobilegame.databinding.ActivityGameActivityBinding;
import com.example.splendormobilegame.model.Card;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Noble;
import com.example.splendormobilegame.model.ReservedCard;
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
    private EndTurnController endTurnController;
    private BuyingRevealedCardsController buyingRevealedCardsController;
    private BuyingReservedCardsController buyingReservedCardsController;
    private LeavingController leavingController;
    private GameEndingController gameEndingController;
    private NewRoomOwnerController newRoomOwnerController;
    private NobleController nobleController;
    CardsFirstTierAdapter cardsFirstTierAdapter;
    CardsSecondTierAdapter cardsSecondTierAdapter;
    CardsThirdTierAdapter cardsThirdTierAdapter;
    ReservedCardsAdapter reservedCardsAdapter;
    nobleCardsAdapter nobleCardsAdapter;
    RecyclerView cardsFirstTierRecyclerView;
    RecyclerView cardsSecondTierRecyclerView;
    RecyclerView cardsThirdTierRecyclerView;
    RecyclerView reservedCardsBuyingRecyclerView;
    RecyclerView cardsNobleCardsRecyclerView;
    List<Card> cardListFirstTier = new ArrayList<>();
    List<Card> cardListSecondTier = new ArrayList<>();
    List<Card> cardListThirdTier = new ArrayList<>();
    List<ReservedCard> cardListReservedCards = new ArrayList<>();
    List<Noble> cardListNobleCards = new ArrayList<>();
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        binding = ActivityGameActivityBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        //update lists
        cardListFirstTier = Model.getRoom().getGame().getFirstTierCards();
        cardListSecondTier = Model.getRoom().getGame().getSecondTierCards();
        cardListThirdTier = Model.getRoom().getGame().getThirdTierCards();
        cardListReservedCards = Model.getRoom().getGame().getReservedCards();
        cardListNobleCards = Model.getRoom().getGame().getNoble();

        // binding.gameActivityConstraintLayout.getLayoutTransition().enableTransitionType(LayoutTransition.CHANGING);
        setupSideBar();
        setupButtons();
        setupSkipButton();
        setupPointsButtons();
        setupCardsFirstTierRecyclerView();
        setupCardsSecondTierRecyclerView();
        setupCardsThirdTierRecyclerView();
        setupReservingFromDeckButtons();
        setupBuyingReservedCards();
        setupNobleCardsRecyclerView();

        this.updateTokenNumber();
        this.updateReservedCards();
        this.updateScoreBoard();

        // Create controllers
        this.endTurnController = new EndTurnController(this);

        this.deckReservingController = new DeckReservingController(this, this.endTurnController);
        this.revealedCardsReservingController = new RevealedCardsReservingController(this, this.endTurnController);
        this.tokensController = new TokensController(this, this.endTurnController);
        this.buyingRevealedCardsController = new BuyingRevealedCardsController(this, this.endTurnController);
        this.buyingReservedCardsController = new BuyingReservedCardsController(this, this.endTurnController);
        this.leavingController = new LeavingController(this);
        this.gameEndingController = new GameEndingController(this);
        this.newRoomOwnerController = new NewRoomOwnerController(this);
        this.nobleController = new NobleController(this);

        // Set reactions
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.MAKE_RESERVATION_FROM_DECK_ANNOUNCEMENT,
                this.deckReservingController.getReservationFromDeckMessageHandler()
        );
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.MAKE_RESERVATION_FROM_DECK_RESPONSE,
                this.deckReservingController.getReservationFromDeckMessageHandler()
        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.MAKE_RESERVATION_FROM_TABLE_ANNOUNCEMENT,
                this.revealedCardsReservingController.getReservationFromRevealedMessageHandler()
        );
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.MAKE_RESERVATION_FROM_TABLE_RESPONSE,
                this.revealedCardsReservingController.getReservationFromRevealedMessageHandler()
        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.GET_TOKENS_RESPONSE,
                this.tokensController.getGetTokensMessageHandler()
        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.NEW_TURN_ANNOUNCEMENT,
                this.endTurnController.getNewTurnAnnouncementMessageHandler()
        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.END_TURN_RESPONSE,
                this.endTurnController.getNewTurnAnnouncementMessageHandler()
        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.BUY_REVEALED_MINE_ANNOUNCEMENT,
                this.buyingRevealedCardsController.getBuyRevealedCardMessageHandler()
        );
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.BUY_REVEALED_MINE_RESPONSE,
                this.buyingRevealedCardsController.getBuyRevealedCardMessageHandler()
        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.BUY_RESERVED_MINE_ANNOUNCEMENT,
                this.buyingReservedCardsController.getBuyReservedCardMessageHandler()
        );
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.BUY_RESERVED_MINE_RESPONSE,
                this.buyingReservedCardsController.getBuyReservedCardMessageHandler()
        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.LEAVE_ROOM_RESPONSE,
                this.leavingController.getLeaveRoomResponse()
        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.END_GAME_ANNOUNCEMENT,
                this.gameEndingController.getGameEndedMessageHandler()
        );
        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.NEW_ROOM_OWNER,
                this.newRoomOwnerController.getNewRoomOwnerResponse()
        );

        CustomWebSocketClient.getInstance().assignReactionToMessageType(
                ServerMessageType.NOBLE_RECEIVED_ANNOUNCEMENT,
                this.nobleController.getNobleReceivedResponse()
        );
    }

    private void setupSideBar() {
        binding.sideBarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int pointCardVisible = (binding.pointsCardView.getVisibility() == GONE) ? VISIBLE : GONE;
                int otherCardsVisible = (binding.pointsCardView.getVisibility() == GONE) ? GONE : VISIBLE;
                TransitionManager.beginDelayedTransition(binding.gameActivityConstraintLayout, new AutoTransition());
                binding.pointsCardView.setVisibility(pointCardVisible);
                binding.otherPlayerCardView.setVisibility(otherCardsVisible);
                binding.reservedCardsTextView.setVisibility(otherCardsVisible);
            }
        });
    }


    private void setupSkipButton() {
        binding.skipTurnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                endTurnController.endTurn();

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
        int cardsCard = (binding.CardsAristocratsCardView.getVisibility() == GONE) ? VISIBLE : GONE;
        int takeTokensCard = (binding.CardsAristocratsCardView.getVisibility() == GONE) ? GONE : VISIBLE;
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

        binding.takeTokensButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GameActivity.this.tokensController.getTokens(redTokens, blueTokens, greenTokens, blackTokens, whiteTokens);
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
        cardsFirstTierAdapter = new CardsFirstTierAdapter(cardListFirstTier);
        cardsFirstTierRecyclerView.setAdapter(cardsFirstTierAdapter);
        // The list we passed to the mAdapter was changed so we have to notify it in order to update
        cardsFirstTierAdapter.notifyDataSetChanged();
    }

    public void setupCardsSecondTierRecyclerView() {
        cardsSecondTierRecyclerView = (RecyclerView) binding.cards2RecyclerView;
        cardsSecondTierRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardsSecondTierAdapter = new CardsSecondTierAdapter(cardListSecondTier);
        cardsSecondTierRecyclerView.setAdapter(cardsSecondTierAdapter);
        // The list we passed to the mAdapter was changed so we have to notify it in order to update
        cardsFirstTierAdapter.notifyDataSetChanged();
    }

    public void setupCardsThirdTierRecyclerView() {
        cardsThirdTierRecyclerView = (RecyclerView) binding.cards3RecyclerView;
        cardsThirdTierRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        cardsThirdTierAdapter = new CardsThirdTierAdapter(cardListThirdTier);
        cardsThirdTierRecyclerView.setAdapter(cardsThirdTierAdapter);
        // The list we passed to the mAdapter was changed so we have to notify it in order to update
        cardsFirstTierAdapter.notifyDataSetChanged();
    }
    public void updateCards(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cardListFirstTier = Model.getRoom().getGame().getFirstTierCards();
                cardListSecondTier = Model.getRoom().getGame().getSecondTierCards();
                cardListThirdTier = Model.getRoom().getGame().getThirdTierCards();
                cardsFirstTierAdapter.setCardList(cardListFirstTier);
                cardsSecondTierAdapter.setCardList(cardListSecondTier);
                cardsThirdTierAdapter.setCardList(cardListThirdTier);
                cardsFirstTierAdapter.notifyDataSetChanged();
                cardsSecondTierAdapter.notifyDataSetChanged();
                cardsThirdTierAdapter.notifyDataSetChanged();
            }});
    }
    public void setupNobleCardsRecyclerView() {

        cardsNobleCardsRecyclerView = (RecyclerView) binding.nobleCardsRecyclerView;
        cardsNobleCardsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        cardListNobleCards = Model.getRoom().getGame().getNoble();
        nobleCardsAdapter = new nobleCardsAdapter(cardListNobleCards);
        cardsNobleCardsRecyclerView.setAdapter(nobleCardsAdapter);
        // The list we passed to the mAdapter was changed so we have to notify it in order to update
        nobleCardsAdapter.notifyDataSetChanged();

    }
    public void updateNobleCardsRecyclerView(){
        runOnUiThread(new Runnable() {
        @Override
        public void run() {
            cardListNobleCards = Model.getRoom().getGame().getNoble();
            nobleCardsAdapter.setCardList(cardListNobleCards);
            nobleCardsAdapter.notifyDataSetChanged();
        }});
    }
    //Numbers don't need to be localized
    @SuppressLint("SetTextI18n")
    public void updateScoreBoard(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayList<User> users = Model.getRoom().getUsers();
                int userCount = Model.getRoom().getPlayerCount();
                hideScoreBoard();

                if (userCount > 0) {
                    HashMap<TokenType, Integer> tokens = users.get(0).getTokens();
                    mainPlayerScoreBoard();
                    binding.mainPlayerNameTV.setText(users.get(0).getName());
                    binding.mainPlayerBlackPointsTV.setText(tokens.get(TokenType.ONYX).toString());
                    binding.mainPlayerBluePointsTV.setText(tokens.get(TokenType.SAPPHIRE).toString());
                    binding.mainPlayerRedPointsTV.setText(tokens.get(TokenType.RUBY).toString());
                    binding.mainPlayerWhitePointsTV.setText(tokens.get(TokenType.DIAMOND).toString());
                    binding.mainPlayerGreenPointsTV.setText(tokens.get(TokenType.EMERALD).toString());
                    binding.mainPlayerYellowPointsTV.setText(tokens.get(TokenType.GOLD_JOKER).toString());
                    binding.mainPlayerPointsTV.setText(Integer.toString(users.get(0).getPoints()));

                }
                if (userCount > 1) {
                    HashMap<TokenType, Integer> tokens = users.get(1).getTokens();
                    show3PlayerScoreBoard();
                    binding.Player3NameTV.setText(users.get(1).getName());
                    binding.Player3BlackPointsTV.setText(tokens.get(TokenType.ONYX).toString());
                    binding.Player3BluePointsTV.setText(tokens.get(TokenType.SAPPHIRE).toString());
                    binding.Player3RedPointsTV.setText(tokens.get(TokenType.RUBY).toString());
                    binding.Player3WhitePointsTV.setText(tokens.get(TokenType.DIAMOND).toString());
                    binding.Player3GreenPointsTV.setText(tokens.get(TokenType.EMERALD).toString());
                    binding.Player3YellowPointsTV.setText(tokens.get(TokenType.GOLD_JOKER).toString());
                    binding.Player3PointsTV.setText(Integer.toString(users.get(1).getPoints()));
                }
                if (userCount > 2) {
                    HashMap<TokenType, Integer> tokens = users.get(2).getTokens();
                    show2PlayerScoreBoard();
                    binding.Player2NameTV.setText(users.get(2).getName());
                    binding.Player2BlackPointsTV.setText(tokens.get(TokenType.ONYX).toString());
                    binding.Player2BluePointsTV.setText(tokens.get(TokenType.SAPPHIRE).toString());
                    binding.Player2RedPointsTV.setText(tokens.get(TokenType.RUBY).toString());
                    binding.Player2WhitePointsTV.setText(tokens.get(TokenType.DIAMOND).toString());
                    binding.Player2GreenPointsTV.setText(tokens.get(TokenType.EMERALD).toString());
                    binding.Player2YellowPointsTV.setText(tokens.get(TokenType.GOLD_JOKER).toString());
                    binding.Player2PointsTV.setText(Integer.toString(users.get(2).getPoints()));
                }
                if (userCount > 3) {
                    HashMap<TokenType, Integer> tokens = users.get(3).getTokens();
                    show1PlayerScoreBoard();
                    binding.Player1NameTV.setText(users.get(3).getName());
                    binding.Player1BlackPointsTV.setText(tokens.get(TokenType.ONYX).toString());
                    binding.Player1BluePointsTV.setText(tokens.get(TokenType.SAPPHIRE).toString());
                    binding.Player1RedPointsTV.setText(tokens.get(TokenType.RUBY).toString());
                    binding.Player1WhitePointsTV.setText(tokens.get(TokenType.DIAMOND).toString());
                    binding.Player1GreenPointsTV.setText(tokens.get(TokenType.EMERALD).toString());
                    binding.Player1YellowPointsTV.setText(tokens.get(TokenType.GOLD_JOKER).toString());
                    binding.Player1PointsTV.setText(Integer.toString(users.get(3).getPoints()));


                }
            }});
    }
    private void hideScoreBoard(){
        binding.Player1NameTV.setVisibility(GONE);
        binding.Player1BlackPointsTV.setVisibility(GONE);
        binding.Player1BluePointsTV.setVisibility(GONE);
        binding.Player1RedPointsTV.setVisibility(GONE);
        binding.Player1WhitePointsTV.setVisibility(GONE);
        binding.Player1GreenPointsTV.setVisibility(GONE);
        binding.Player1YellowPointsTV.setVisibility(GONE);
        binding.Player2NameTV.setVisibility(GONE);
        binding.Player2BlackPointsTV.setVisibility(GONE);
        binding.Player2BluePointsTV.setVisibility(GONE);
        binding.Player2RedPointsTV.setVisibility(GONE);
        binding.Player2WhitePointsTV.setVisibility(GONE);
        binding.Player2GreenPointsTV.setVisibility(GONE);
        binding.Player2YellowPointsTV.setVisibility(GONE);
        binding.Player3NameTV.setVisibility(GONE);
        binding.Player3BlackPointsTV.setVisibility(GONE);
        binding.Player3BluePointsTV.setVisibility(GONE);
        binding.Player3RedPointsTV.setVisibility(GONE);
        binding.Player3WhitePointsTV.setVisibility(GONE);
        binding.Player3GreenPointsTV.setVisibility(GONE);
        binding.Player3YellowPointsTV.setVisibility(GONE);
        binding.mainPlayerNameTV.setVisibility(GONE);
        binding.mainPlayerBlackPointsTV.setVisibility(GONE);
        binding.mainPlayerBluePointsTV.setVisibility(GONE);
        binding.mainPlayerRedPointsTV.setVisibility(GONE);
        binding.mainPlayerWhitePointsTV.setVisibility(GONE);
        binding.mainPlayerGreenPointsTV.setVisibility(GONE);
        binding.mainPlayerYellowPointsTV.setVisibility(GONE);
    }

    private void show1PlayerScoreBoard(){
        binding.Player1NameTV.setVisibility(VISIBLE);
        binding.Player1BlackPointsTV.setVisibility(VISIBLE);
        binding.Player1BluePointsTV.setVisibility(VISIBLE);
        binding.Player1RedPointsTV.setVisibility(VISIBLE);
        binding.Player1WhitePointsTV.setVisibility(VISIBLE);
        binding.Player1GreenPointsTV.setVisibility(VISIBLE);
        binding.Player1YellowPointsTV.setVisibility(VISIBLE);
    }
    private void show2PlayerScoreBoard(){
        binding.Player2NameTV.setVisibility(VISIBLE);
        binding.Player2BlackPointsTV.setVisibility(VISIBLE);
        binding.Player2BluePointsTV.setVisibility(VISIBLE);
        binding.Player2RedPointsTV.setVisibility(VISIBLE);
        binding.Player2WhitePointsTV.setVisibility(VISIBLE);
        binding.Player2GreenPointsTV.setVisibility(VISIBLE);
        binding.Player2YellowPointsTV.setVisibility(VISIBLE);
    }
    private void show3PlayerScoreBoard(){
        binding.Player3NameTV.setVisibility(VISIBLE);
        binding.Player3BlackPointsTV.setVisibility(VISIBLE);
        binding.Player3BluePointsTV.setVisibility(VISIBLE);
        binding.Player3RedPointsTV.setVisibility(VISIBLE);
        binding.Player3WhitePointsTV.setVisibility(VISIBLE);
        binding.Player3GreenPointsTV.setVisibility(VISIBLE);
        binding.Player3YellowPointsTV.setVisibility(VISIBLE);
    }
    private void mainPlayerScoreBoard(){
        binding.mainPlayerNameTV.setVisibility(VISIBLE);
        binding.mainPlayerBlackPointsTV.setVisibility(VISIBLE);
        binding.mainPlayerBluePointsTV.setVisibility(VISIBLE);
        binding.mainPlayerRedPointsTV.setVisibility(VISIBLE);
        binding.mainPlayerWhitePointsTV.setVisibility(VISIBLE);
        binding.mainPlayerGreenPointsTV.setVisibility(VISIBLE);
        binding.mainPlayerYellowPointsTV.setVisibility(VISIBLE);
    }
    private void setupBuyingReservedCards(){
        reservedCardsBuyingRecyclerView = (RecyclerView) binding.reservedCardsRecyclerView;
        LinearLayoutManager HorizontalLayout = new LinearLayoutManager(GameActivity.this, LinearLayoutManager.HORIZONTAL, false);
        reservedCardsBuyingRecyclerView.setLayoutManager(HorizontalLayout);
        cardListReservedCards = Model.getRoom().getGame().getReservedCards();
        reservedCardsAdapter = new ReservedCardsAdapter(cardListReservedCards);
        reservedCardsBuyingRecyclerView.setAdapter(reservedCardsAdapter);
        // The list we passed to the mAdapter was changed so we have to notify it in order to update
        reservedCardsAdapter.notifyDataSetChanged();
    }
    public void updateReservedCards(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                cardListReservedCards = Model.getRoom().getGame().getReservedCards();
                nobleCardsAdapter.setCardList(cardListNobleCards);
                nobleCardsAdapter.notifyDataSetChanged();
            }});
    }

    public void updateTokenNumber(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                binding.blackTokenButton.setText(Model.getRoom().getGame().getTokenValue(TokenType.ONYX).toString());
                binding.blueTokenButton.setText(Model.getRoom().getGame().getTokenValue(TokenType.SAPPHIRE).toString());
                binding.redTokenButton.setText(Model.getRoom().getGame().getTokenValue(TokenType.RUBY).toString());
                binding.greenTokenButton.setText(Model.getRoom().getGame().getTokenValue(TokenType.EMERALD).toString());
                binding.whiteTokenButton.setText(Model.getRoom().getGame().getTokenValue(TokenType.DIAMOND).toString());
                binding.yellowTokenButton.setText(Model.getRoom().getGame().getTokenValue(TokenType.GOLD_JOKER).toString());
            }});

    }

    public void endGame(){
        //Change to end game activity screen
        Intent myIntent = new Intent(GameActivity.this, CreateRoomActivity.class);
        GameActivity.this.startActivity(myIntent);
        //Animation when switching classes
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
