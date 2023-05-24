package com.example.splendormobilegame.activities.WaitingRoom;

import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.splendormobilegame.R;
import com.example.splendormobilegame.model.Model;

import java.util.ArrayList;

public class WaitingRoomActivityAdapter extends RecyclerView.Adapter<WaitingRoomActivityAdapter.ViewHolder> {
    public ArrayList<String> usersList;

    public void setUsersList(ArrayList<String> usersList) {
        this.usersList = usersList;
    }

    public ArrayList<String> getUsersList() {
        return this.usersList;
    }

    public WaitingRoomActivityAdapter(ArrayList<String> usersList) {
        this.usersList = usersList;
    }

    @NonNull
    @Override
    public WaitingRoomActivityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.waiting_room_recycler_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WaitingRoomActivityAdapter.ViewHolder holder, int position) {
        holder.userName.setText(usersList.get(position));
        String userTest = Model.getInstance().getRoom().getUserByUuid(Model.getInstance().getUserUuid()).getName();
        if(usersList.get(position)==userTest){
            holder.userName.setTypeface(null, Typeface.BOLD);
        }
    }

    @Override
    public int getItemCount() {
        return usersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView userName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.userNameTextView);

        }
    }
}
