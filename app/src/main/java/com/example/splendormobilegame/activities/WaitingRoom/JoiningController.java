package com.example.splendormobilegame.activities.WaitingRoom;

import android.util.Log;

import com.example.splendormobilegame.Controller;
import com.example.splendormobilegame.CustomAppCompatActivity;
import com.example.splendormobilegame.model.Model;
import com.example.splendormobilegame.model.User;
import com.example.splendormobilegame.websocket.ReactionUtils;
import com.example.splendormobilegame.websocket.UserReaction;
import com.github.splendor_mobile_game.websocket.communication.ServerMessage;
import com.github.splendor_mobile_game.websocket.communication.UserMessage;
import com.github.splendor_mobile_game.websocket.handlers.reactions.JoinRoom;
import com.github.splendor_mobile_game.websocket.response.ErrorResponse;

import java.util.ArrayList;

public class JoiningController extends Controller {

    private JoinRoomResponse joinRoomResponse;
    private WaitingRoomActivityAdapter usersAdapter;

    protected JoiningController(CustomAppCompatActivity activity, WaitingRoomActivityAdapter usersAdapter) {
        super(activity);
        this.joinRoomResponse = new JoinRoomResponse();
        this.usersAdapter = usersAdapter;
    }

    public JoinRoomResponse getJoinRoomResponse() {
        return joinRoomResponse;
    }

    public class JoinRoomResponse extends UserReaction {

        @Override
        public UserMessage react(ServerMessage serverMessage) {
            Log.i("UserReaction", "Entered JoinRoomResponse react method");

            JoinRoom.ResponseData responseData = (JoinRoom.ResponseData) ReactionUtils.getResponseData(serverMessage, JoinRoom.ResponseData.class);

            JoinRoom.UserDataResponse newUser = responseData.users.get(responseData.users.size() - 1);
            Model.getRoom().addUser(new User(newUser.uuid, newUser.name));

            //Update recyclerView WaitingRoom
            ArrayList<String> usersList = new ArrayList<>();
            for (User u : Model.getRoom().getUsers()) {
                usersList.add(u.getName());
            }
            usersAdapter.setUsersList(usersList);
            usersAdapter.notifyDataSetChanged();

            activity.showToast("User: " + newUser.name + " has joined to the room.");
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
