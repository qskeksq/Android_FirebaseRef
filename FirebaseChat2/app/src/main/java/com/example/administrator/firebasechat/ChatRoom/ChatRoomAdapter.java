package com.example.administrator.firebasechat.ChatRoom;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.firebasechat.R;
import com.example.administrator.firebasechat.RealChat.RealChatActivity;
import com.example.administrator.firebasechat.domain.Room;

import java.util.ArrayList;
import java.util.List;


/**
 * 채팅방 목록 어댑터
 */
public class ChatRoomAdapter extends RecyclerView.Adapter<ChatRoomAdapter.Holder> {

    List<Room> data = new ArrayList<>();

    public void setData(List<Room> data) {
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_room, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Room room = data.get(position);
        holder.roomName.setText(room.getName());
        holder.name = room.getName();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        private ImageView roomImage;
        private TextView roomName;
        String name;

        public Holder(View itemView) {
            super(itemView);
            initView(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), RealChatActivity.class);
                    intent.putExtra("room", name);
                    v.getContext().startActivity(intent);
                }
            });
        }

        private void initView(View view) {
            roomImage = (ImageView) view.findViewById(R.id.roomImage);
            roomName = (TextView) view.findViewById(R.id.roomName);
        }
    }
}
