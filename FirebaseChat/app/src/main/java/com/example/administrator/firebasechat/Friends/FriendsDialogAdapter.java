package com.example.administrator.firebasechat.Friends;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.firebasechat.R;
import com.example.administrator.firebasechat.domain.Data;
import com.example.administrator.firebasechat.domain.User;

import java.util.ArrayList;
import java.util.List;


/**
 * 커스텀 다이얼로그 리사이클러뷰 어댑터
 */
public class FriendsDialogAdapter extends RecyclerView.Adapter<FriendsDialogAdapter.Holder> {

    List<User> chatList = new ArrayList<>();
    User user;

    public FriendsDialogAdapter(List<User> chatList) {
        this.chatList = chatList;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_friend, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        user = Data.users.get(position);
        holder.friendName.setText(user.getEmail());
        holder.position = position;
    }

    @Override
    public int getItemCount() {
        return chatList.size();
    }


    public class Holder extends RecyclerView.ViewHolder {

        private ImageView imageView;
        private TextView friendName;
        private CheckBox checkBox;
        int position;

        public Holder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.imageView);
            friendName = (TextView) itemView.findViewById(R.id.friendName);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);

            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        Data.users.get(position).setChecked(true);
                        Log.e("체크", Data.users.get(position).getEmail()+"체크됨");
                    } else {
                        Data.users.get(position).setChecked(false);
                        Log.e("체크", Data.users.get(position).getEmail()+"풀림");
                    }
                }
            });
        }

    }
}
