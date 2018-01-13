package com.example.administrator.firebasechat.RealChat;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.administrator.firebasechat.R;
import com.example.administrator.firebasechat.domain.Chat;
import com.example.administrator.firebasechat.domain.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-07-20.
 */

public class RealChatAdapter extends RecyclerView.Adapter<RealChatAdapter.Holder> {

    List<Chat> chatList = new ArrayList<>();

    public void setData(List<Chat> chatList) {
        this.chatList = chatList;
    }

    /**
     * 채팅별 인플레이션 차별화
     *
     * 각 채팅 데이터에는 작성자 이메일 정보가 들어가 있어서 현재 유저 이메일 값을 가지고 있는 경우 왼쪽에, 다른 유저의 경우 오른쪽에 뷰를 배치한다.
     */
    @Override
    public int getItemViewType(int position) {
        if(chatList.get(position).getEmail().equals(Data.getUser().getEmail())){
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(viewType == 1){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_me, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_others, parent, false);
        }
        // 넘겨줄 때 viewType 값을 같이 넘겨준다.
        return new Holder(view, viewType);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        holder.chatTxt.setText(chatList.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        private TextView chatTxt;

        public Holder(View itemView, int viewType) {
            super(itemView);
            if(viewType == 1){
                chatTxt = (TextView) itemView.findViewById(R.id.myChatTxt);
            } else {
                chatTxt = (TextView) itemView.findViewById(R.id.friendChatTxt);
            }
        }

    }
}
