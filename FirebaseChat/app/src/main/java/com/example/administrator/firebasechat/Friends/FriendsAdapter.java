package com.example.administrator.firebasechat.Friends;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.firebasechat.R;
import com.example.administrator.firebasechat.domain.Data;
import com.example.administrator.firebasechat.domain.User;

import java.util.ArrayList;
import java.util.List;

/**
 * 가입된 유저 목록 어댑터
 */
public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.Holder> {

    List<User> chatList = new ArrayList<>();

    public FriendsAdapter(List<User> chatList) {
        this.chatList = chatList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User user = Data.users.get(position);
        holder.friendName.setText(user.getEmail());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView friendName;

        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            friendName = (TextView) itemView.findViewById(R.id.friendName);

        }

    }
}
