package com.example.splendormobilegame.activities.CreateRoom;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.activities.WaitingRoom.WaitingRoomActivity;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.Room;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.CustomWebSocketClient;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.UserRequestType;
import com.github.splendor_mobile_game.websocket.handlers.reactions.CreateRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.UUID;

public class CreateRoomController extends Controller {
    private CreateRoomResponse createRoomResponse;

    public CreateRoomController(CustomAppCompatActivity activity, CustomWebSocketClient customWebSocketClient, Model model) {
        super(activity, customWebSocketClient, model);
        this.createRoomResponse = new CreateRoomResponse();
    }

    public void sendRequest(String nickname, String name, String password) {

        // Prepare data
        CreateRoom.RoomDTO room = new CreateRoom.RoomDTO(name, password);
        CreateRoom.UserDTO user = new CreateRoom.UserDTO(model.getUserUuid(), nickname);
        CreateRoom.DataDTO data = new CreateRoom.DataDTO(room, user);
        UserMessage message = new UserMessage(UUID.randomUUID(), UserRequestType.CREATE_ROOM, data);

        // Send request
        customWebSocketClient.send(message);
    }

    public CreateRoomResponse getCreateRoomResponse() {
        return createRoomResponse;
    }

    public class CreateRoomResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered CreateRoomResponse");

            // Get the data from the request
            CreateRoom.ResponseData responseData = (CreateRoom.ResponseData) ReactionUtils.getResponseData(
                    serverMessage, CreateRoom.ResponseData.class
            );

            // Update the model
            model.setRoom(new Room(
                    responseData.room.uuid,
                    responseData.room.name,
                    responseData.room.enterCode,
                    new User(responseData.user.id, responseData.user.name)
            ));

            activity.changeActivity(WaitingRoomActivity.class);

            // Nice looking stuff
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            // Return null if you don't want to send anything to the server
            return null;
        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Cannot create room: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Cannot create room: " + errorResponse.data.error);
            return null;
        }

    }

}
