package com.example.splendormobilegame;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.Nullable;

import java.net.URI;
import java.net.URISyntaxException;

import tech.gusavila92.websocketclient.WebSocketClient;

public class WebSocket extends Activity {
    public static WebSocketClient webSocketClient;
    public static String message = "Null";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        createWebSocketClient();
    }

    public static void createWebSocketClient() {
        URI uri;
        try {
            // Connect to server
            uri = new URI("ws://20.199.10.79:8887");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen() {
                Log.i("WebSocket", "Session is starting");
                //Example message
                webSocketClient.send("{\"messageContextId\":\"80bdc250-5365-4caf-8dd9-a33e709a0116\",\"type\":\"CreateRoom\",\"data\":{\"userDTO\":{\"id\":\"f8c3de3d-1fea-4d7c-a8b0-29f63c4c3454\",\"name\":\"James\"},\"roomDTO\":{\"name\":\"TajnyPokoj\",\"password\":\"kjashjkasd\"}}}");
            }

            @Override
            public void onTextReceived(String s) {
                Log.i("WebSocket", "Message received");
                message = s;
                Log.i("WebSocket1", s);

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
                System.out.println(e.getMessage());
                Log.e("WebSocket2", "Error");
            }

            @Override
            public void onCloseReceived() {
                Log.i("WebSocket", "Closed ");
                System.out.println("onCloseReceived");
            }
        };

        webSocketClient.setConnectTimeout(10000);
        webSocketClient.setReadTimeout(60000);
        webSocketClient.enableAutomaticReconnection(5000);
        webSocketClient.connect();
    }


}
