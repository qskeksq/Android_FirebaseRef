package com.example.administrator.firebasechat.RealChat;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.administrator.firebasechat.R;
import com.example.administrator.firebasechat.domain.Chat;
import com.example.administrator.firebasechat.domain.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RealChatActivity extends AppCompatActivity {

    /**
     * 파이어베이스 데이터페이스, 레퍼런스
     */
    private FirebaseDatabase database = FirebaseDatabase.getInstance();
    private DatabaseReference dataReference = database.getReference();

    /**
     * 위젯
     */
    private RecyclerView realChatRecycler;
    private Button send;
    private EditText inputContent;
    private String roomName;
    private RealChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_chat);
        roomName = getIntent().getStringExtra("room");
        initView();
        adapter = new RealChatAdapter();
        realChatRecycler.setAdapter(adapter);
        realChatRecycler.setLayoutManager(new LinearLayoutManager(this));
        setListener();
    }

    private void initView() {
        realChatRecycler = (RecyclerView) findViewById(R.id.realChatRecycler);
        send = (Button) findViewById(R.id.send);
        inputContent = (EditText) findViewById(R.id.inputContent);
    }

    private void setListener(){
        /**
         * 채팅 보내기
         */
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 입력한 내용
                String content = inputContent.getText().toString();
                // 전체 공유 채팅 데이터
                dataReference.child("rooms").child(roomName).child("chat").child(getSDF()).setValue(getChat(content));
                // 현 사용자 채팅 데이터
                dataReference.child("users").child(Data.getUser().getKey()).child("rooms").child(roomName).child("chat").child(getSDF()).setValue(getChat(content));
                inputContent.setText("");
            }
        });

        /**
         * 채팅 데이터 쿼리
         */
        dataReference.child("rooms").child(roomName).child("chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<Chat> chats = new ArrayList<>();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    Chat chat = item.getValue(Chat.class);
                    chats.add(chat);
                }
                adapter.setData(chats);
                adapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    /**
     * 하나의 채팅 데이터 객체 만들기
     */
    private Chat getChat(String content){
        Chat chat = new Chat(Data.getUser().getEmail(), content);
        return chat;
    }

    /**
     * 채팅 데이터 키 값
     */
    private String getSDF(){
        // 채팅이 역순으로 지정되게 하기 위해서 시간순으로 키 값을 저장한다.
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy:MM:dd HH:mm:ss");
        String chatName = sdf.format(date);
        return chatName;
    }


}
