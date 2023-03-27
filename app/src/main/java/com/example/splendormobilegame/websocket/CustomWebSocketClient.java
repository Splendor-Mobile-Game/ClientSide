package com.example.splendormobilegame.websocket;

import android.util.Log;

import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.ServerMessageType;
import com.github.splendor_mobile_game.websocket.utils.reflection.Reflection;
import com.google.gson.Gson;

import java.net.URI;
import java.util.Map;

import tech.gusavila92.websocketclient.WebSocketClient;

public class CustomWebSocketClient extends WebSocketClient {

    private static CustomWebSocketClient instance;
    Map<ServerMessageType, Class<? extends UserReaction>> clientReactionsClasses;
    Model model;

    private CustomWebSocketClient(URI uri, Model model, Map<ServerMessageType, Class<? extends UserReaction>> clientReactionsClasses) {
        super(uri);
        this.model = model;
        this.clientReactionsClasses = clientReactionsClasses;
    }

    public static void initialize(URI uri, Model model, Map<ServerMessageType, Class<? extends UserReaction>> clientReactionsClasses) {
        CustomWebSocketClient.instance = new CustomWebSocketClient(uri, model, clientReactionsClasses);
        CustomWebSocketClient.instance.setConnectTimeout(10000);
        CustomWebSocketClient.instance.setReadTimeout(60000);
        CustomWebSocketClient.instance.enableAutomaticReconnection(5000);
        CustomWebSocketClient.instance.connect();
    }

    public static CustomWebSocketClient getInstance() {
        if (CustomWebSocketClient.instance == null) {
            throw new NotInitializedException("Please, run first `initialize` method before using this class!");
        }
        return CustomWebSocketClient.instance;
    }

    @Override
    public void onOpen() {
        Log.i("WebSocket", "Session is starting");
    }

    @Override
    public void onTextReceived(String s) {
        Log.i("WebSocket", "Message received: " + s);

        ServerMessage serverMessage = new ServerMessage(s);

        Class<? extends UserReaction> reactionClass = clientReactionsClasses.get(serverMessage.getType());
        UserReaction reactionInstance = (UserReaction) Reflection.createInstanceOfClass(reactionClass, serverMessage, model);

        UserMessage userMessage = reactionInstance.react();

        if (userMessage != null) {
            this.send(userMessage);
        }
    }

    @Override
    public void onBinaryReceived(byte[] data) {
    }

    @Override
    public void onPingReceived(byte[] data) {
    }

    @Override
    public void onPongReceived(byte[] data) {
    }

    @Override
    public void onException(Exception e) {
        Log.e("Error on message received", e.getMessage());
    }

    @Override
    public void onCloseReceived() {
        Log.i("WebSocket", "Closed ");
        System.out.println("onCloseReceived");
    }
    
    public void send(UserMessage userMessage) {
        String json = (new Gson()).toJson(userMessage);
        this.send(json);
    }
}
