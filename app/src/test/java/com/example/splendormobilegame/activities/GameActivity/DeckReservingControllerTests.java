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
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.UUID;

public class DeckReservingControllerTests {

    @Test
    public void currentUserReservedCard() {

        ///////////////////////////
        // Arrange

        // Constructing test server message
        UUID userUuid = UUID.fromString("79f046d4-e813-400c-beb1-edc50257436e");
        UUID cardUuid = UUID.fromString("79e1ecd7-9b69-4a86-ae9b-e97a24362f8e");
        CardTier cardTier = CardTier.LEVEL_3;
        int cardPrestige = 3;
        TokenType cardBonusColor = TokenType.DIAMOND;
        MakeReservationFromDeck.TokensDataResponse cardTokensRequired = new MakeReservationFromDeck.TokensDataResponse(1, 2, 3, 0, 0);
        boolean gotGoldenToken = true;

        MakeReservationFromDeck.ResponseData responseData = new MakeReservationFromDeck.ResponseData(
                userUuid,
                new MakeReservationFromDeck.CardDataResponse(cardUuid, cardTier, cardPrestige, cardBonusColor, cardTokensRequired),
                gotGoldenToken
        );

        // Create mock objects in order to create subject `DeckReservingController`
        GameActivity gameActivityMock = Mockito.mock(GameActivity.class);
        CustomWebSocketClient customWebSocketClientMock = Mockito.mock(CustomWebSocketClient.class);
        Model modelMock = Mockito.mock(Model.class);
        EndTurnController<GameActivity> endTurnControllerMock = Mockito.mock(EndTurnController.class);

        // Setting up the behavior of the mock objects

        // Mock the behavior of the modelMock and roomMock
        Room roomMock = Mockito.mock(Room.class);
        Mockito.when(modelMock.getRoom()).thenReturn(roomMock);

        // Mock the behavior of the roomMock and userMock
        User userMock = Mockito.mock(User.class);
        Mockito.when(roomMock.getUserByUuid(userUuid)).thenReturn(userMock);

        // Mock the behavior of the roomMock and gameMock
        Game gameMock = Mockito.mock(Game.class);
        Mockito.when(roomMock.getGame()).thenReturn(gameMock);

        // Mock the behavior of the endTurnControllerMock
        Mockito.doNothing().when(endTurnControllerMock).endTurn();

        // Create an instance of DeckReservingController using the mock objects
        DeckReservingController<GameActivity> deckReservingController =
                new DeckReservingController<>(gameActivityMock, customWebSocketClientMock, modelMock, endTurnControllerMock);

        // Create the test server message
        ServerMessage serverMessage = new ServerMessage(UUID.randomUUID(), ServerMessageType.MAKE_RESERVATION_FROM_DECK_ANNOUNCEMENT, Result.OK, responseData);

        // Create argument captors to capture the arguments passed to mocked methods
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        ArgumentCaptor<ReservedCard> reservedCardCaptor = ArgumentCaptor.forClass(ReservedCard.class);

        ///////////////////////////
        // Act

        // Invoke the react() method of the DeckReservingController with the test server message
        deckReservingController.getReservationFromDeckMessageHandler().react(serverMessage);

        ///////////////////////////
        // Assert

        // Verify that the endTurn method of the endTurnControllerMock was called once
        Mockito.verify(endTurnControllerMock, Mockito.times(1)).endTurn();

        // Verify that the reserveCard method of the gameMock was called with the expected arguments
        Mockito.verify(gameMock, Mockito.times(1)).reserveCard(userCaptor.capture(), reservedCardCaptor.capture());

        // Retrieve the captured User and ReservedCard objects
        User capturedUser = userCaptor.getValue();
        ReservedCard capturedReservedCard = reservedCardCaptor.getValue();

        // Perform assertions to check the correctness of the captured objects
        Assert.assertEquals(userMock, capturedUser);
        Assert.assertEquals(cardUuid, capturedReservedCard.getCard().getUuid());
        Assert.assertEquals(cardTier, capturedReservedCard.getCard().getCardTier());
        Assert.assertEquals(cardPrestige, capturedReservedCard.getCard().getPoints());
        Assert.assertEquals(cardBonusColor, capturedReservedCard.getCard().getBonusToken());
        Assert.assertEquals(cardTokensRequired.diamond, capturedReservedCard.getCard().getDiamondCost());
        Assert.assertEquals(cardTokensRequired.emerald, capturedReservedCard.getCard().getEmeraldCost());
        Assert.assertEquals(cardTokensRequired.onyx, capturedReservedCard.getCard().getOnyxCost());
        Assert.assertEquals(cardTokensRequired.ruby, capturedReservedCard.getCard().getRubyCost());
        Assert.assertEquals(cardTokensRequired.sapphire, capturedReservedCard.getCard().getSapphireCost());
    }
}
