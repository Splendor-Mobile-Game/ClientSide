package com.example.splendormobilegame.activities.JoinRoom;

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
import com.github.splendor_mobile_game.websocket.handlers.reactions.JoinRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.UUID;

public class JoinRoomController extends Controller {

    private JoinRoomResponse joinRoomResponse;
    private String enterCode;

    public JoinRoomController(CustomAppCompatActivity activity) {
        super(activity);
        this.joinRoomResponse = new JoinRoomResponse();
    }

    public JoinRoomResponse getJoinRoomResponse() {
        return this.joinRoomResponse;
    }

    public void sendRequest(String nickname, String enterCode, String password) {
        this.enterCode = enterCode;

        JoinRoom.RoomDTO roomDTO = new JoinRoom.RoomDTO(enterCode, password);
        JoinRoom.UserDTO userDTO = new JoinRoom.UserDTO(Model.getUserUuid(), nickname);
        JoinRoom.DataDTO dataDTO = new JoinRoom.DataDTO(roomDTO, userDTO);

        UserMessage userMessage = new UserMessage(UUID.randomUUID(), UserRequestType.JOIN_ROOM, dataDTO);
        CustomWebSocketClient.getInstance().send(userMessage);
    }

    public class JoinRoomResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered JoinRoomResponse react method");

            JoinRoom.ResponseData responseData = (JoinRoom.ResponseData) ReactionUtils.getResponseData(serverMessage, JoinRoom.ResponseData.class);

            JoinRoom.UserDataResponse owner = responseData.users.get(0);
            Room room = new Room(
                    responseData.room.uuid,
                    responseData.room.name,
                    JoinRoomController.this.enterCode,
                    new User(owner.uuid, owner.name)
            );

            for(JoinRoom.UserDataResponse user : responseData.users)
            {
                if(user==owner){
                    continue;
                }
                room.addUser(new User(user.uuid, user.name));
            }

            Model.setRoom(room);

            activity.changeActivity(WaitingRoomActivity.class);
            activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            // Return null if you don't want to send anything to the server
            return null;

        }

        @Override
        public UserMessage onFailure(ErrorResponse errorResponse) {
            activity.showToast("Cannot join to the room: " + errorResponse.data.error);
            return null;
        }

        @Override
        public UserMessage onError(ErrorResponse errorResponse) {
            activity.showToast("Cannot join to the room: " + errorResponse.data.error);
            return null;
        }

    }
}
