package com.example.administrator.firebasechat.ChatRoom;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.firebasechat.Friends.FriendsDialogAdapter;
import com.example.administrator.firebasechat.R;
import com.example.administrator.firebasechat.domain.Data;
import com.example.administrator.firebasechat.domain.Room;
import com.example.administrator.firebasechat.domain.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * 채팅방 생성, 목록 프래그먼트
 */
public class ChatRoomFragment extends Fragment {

    private View view;
    private ChatRoomAdapter adapter;
    private Button add;
    private RecyclerView chatRoomRecycler;

    /**
     * 파이어베이스 자원
     */
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataReference = database.getReference();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(view == null) {
            view = inflater.inflate(R.layout.fragment_chat_room, container, false);
            initView(view);
            adapter = new ChatRoomAdapter();
            setListeners();
            chatRoomRecycler.setAdapter(adapter);
            chatRoomRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        }
        return view;
    }

    /**
     * 초기화
     */
    private void initView(View view) {
        chatRoomRecycler = (RecyclerView) view.findViewById(R.id.chatRoomRecycler);
        add = (Button) view.findViewById(R.id.add);
    }

    public void setListeners(){
        /**
         * 채팅방 추가
         */
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = LayoutInflater.from(getActivity()).inflate(R.layout.custom_dialog, null);   // 커스텀 다이얼로그 인플레이트
                final EditText editText = (EditText) view.findViewById(R.id.addRoomName);
                final RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.chooseFriends);
                FriendsDialogAdapter adapter = new FriendsDialogAdapter(Data.users);                        // 리사이클러뷰 어댑터
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
                AlertDialog.Builder dialog = new AlertDialog.Builder(getActivity());                        // AlertDialog
                dialog.setView(view);
                dialog.setNegativeButton("취소", null);
                dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 방 이름
                        String roomName = editText.getText().toString();
                        // 방 정보
                        Room room = new Room(roomName);
                        // 채팅 공용 데이터
                        dataReference.child("rooms").child(room.getName()).setValue(room);
                        // 유저 정보에 내 채팅방 저장
                        dataReference.child("users").child(Data.getUser().getKey()).child("rooms").child(room.getName()).setValue(room);
                        // 체크된 유저(초대된 유저) 유저 정보에도 채팅방 추가
                        for(User user : Data.users){
                            if(user.isChecked() == true){
                                dataReference.child("users").child(user.getKey()).child("rooms").child(room.getName()).setValue(room);
                            }
                        }
                    }
                });
                dialog.show();
            }
        });

        /**
         * 저장된 채팅방 목록에 자동 세팅
         *
         * 채팅목록 프래그먼트에서는 채팅전체 정보가 아닌 채팅방 이름과 같은 가벼운 정보만 가지고 있어도 된다
         * 이런 이유에서 유저 정보의 채팅방에는 이름만 들어 있고, 이 이름을 통해서 전체 채팅 정보에서 유저가 사용하고 있는 채팅 정보만 가져올 수 있다.
         */
        dataReference.child("users").child(Data.getUser().getKey()).child("rooms").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Data.rooms.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    Room room = item.getValue(Room.class);
                    Data.rooms.add(room);
                }
                adapter.setData(Data.rooms);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
