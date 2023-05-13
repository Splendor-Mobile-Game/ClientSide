package com.example.splendormobilegame.activities.GameActivity;

import com.example.splendormobilegame.model.Game;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.ReservedCard;
import com.example.splendormobilegame.model.Room;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.github.splendor_mobile_game.game.enums.CardTier;
import com.github.splendor_mobile_game.game.enums.TokenType;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.handlers.ServerMessageType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.MakeReservationFromDeck;
import com.github.splendor_mobile_game.websocket.response.Result;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.UUID;

public class DeckReservingControllerTests {

    @Mock
    private GameActivity gameActivityMock;

    @Mock
    private CustomWebSocketClient customWebSocketClientMock;

    @Mock
    private Model modelMock;

    @Mock
    private EndTurnController<GameActivity> endTurnControllerMock;

    @Mock
    private Room roomMock;

    @Mock
    private User userMock;

    @Mock
    private Game gameMock;

    @InjectMocks
    private DeckReservingController<GameActivity> deckReservingController;

    private final UUID userUuid = UUID.fromString("79f046d4-e813-400c-beb1-edc50257436e");
    private final UUID cardUuid = UUID.fromString("79e1ecd7-9b69-4a86-ae9b-e97a24362f8e");
    private final CardTier cardTier = CardTier.LEVEL_3;
    private final int cardPrestige = 3;
    private final TokenType cardBonusColor = TokenType.DIAMOND;
    private final MakeReservationFromDeck.TokensDataResponse cardTokensRequired = new MakeReservationFromDeck.TokensDataResponse(1, 2, 3, 0, 0);
    private final boolean gotGoldenToken = true;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.when(modelMock.getRoom()).thenReturn(roomMock);
        Mockito.when(roomMock.getUserByUuid(userUuid)).thenReturn(userMock);
        Mockito.when(roomMock.getGame()).thenReturn(gameMock);
        Mockito.doNothing().when(endTurnControllerMock).endTurn();
    }

    @Test
    public void currentUserReservedCard() {
        // Arrange
        MakeReservationFromDeck.ResponseData responseData = new MakeReservationFromDeck.ResponseData(
                userUuid,
                new MakeReservationFromDeck.CardDataResponse(cardUuid, cardTier, cardPrestige, cardBonusColor, cardTokensRequired),
                gotGoldenToken
        );

        ServerMessage serverMessage = new ServerMessage(UUID.randomUUID(), ServerMessageType.MAKE_RESERVATION_FROM_DECK_ANNOUNCEMENT, Result.OK, responseData);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<ReservedCard> reservedCardCaptor = ArgumentCaptor.forClass(ReservedCard.class);

        // Act
        deckReservingController.getReservationFromDeckMessageHandler().react(serverMessage);

        // Assert
        Mockito.verify(endTurnControllerMock, Mockito.times(1)).endTurn();
        Mockito.verify(gameMock, Mockito.times(1)).reserveCard(userCaptor.capture(), reservedCardCaptor.capture());

        User capturedUser = userCaptor.getValue();
        ReservedCard capturedCard = reservedCardCaptor.getValue();

        Assert.assertEquals(userMock, capturedUser);
        Assert.assertEquals(cardUuid, capturedCard.getCard().getUuid());
        Assert.assertEquals(cardTier, capturedCard.getCard().getCardTier());
        Assert.assertEquals(cardPrestige, capturedCard.getCard().getPoints());
        Assert.assertEquals(cardBonusColor, capturedCard.getCard().getBonusToken());
        Assert.assertEquals(cardTokensRequired.diamond, capturedCard.getCard().getDiamondCost());
        Assert.assertEquals(cardTokensRequired.emerald, capturedCard.getCard().getEmeraldCost());
        Assert.assertEquals(cardTokensRequired.onyx, capturedCard.getCard().getOnyxCost());
        Assert.assertEquals(cardTokensRequired.ruby, capturedCard.getCard().getRubyCost());
        Assert.assertEquals(cardTokensRequired.sapphire, capturedCard.getCard().getSapphireCost());
    }
}