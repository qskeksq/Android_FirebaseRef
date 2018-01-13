package com.example.administrator.firebasechat.Main;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.example.administrator.firebasechat.ChatRoom.ChatRoomFragment;
import com.example.administrator.firebasechat.Friends.FriendsFragment;
import com.example.administrator.firebasechat.Profile.ProfileFragment;
import com.example.administrator.firebasechat.R;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView navigation;
    private ChatRoomFragment roomFragment;
    private FriendsFragment friendsFragment;
    private ProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setListener();
        initFragment();
        startFragment(roomFragment);
    }

    /**
     * 초기화
     * initView ~ startFragment
     */
    private void initView() {
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
    }

    private void setListener(){
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    private void initFragment(){
        roomFragment = new ChatRoomFragment();
        friendsFragment = new FriendsFragment();
        profileFragment = new ProfileFragment();
    }

    private void startFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).commit();
    }

    /**
     * 하단 탭바 리스너
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.room:
                    startFragment(roomFragment);
                    return true;
                case R.id.friends:
                    startFragment(friendsFragment);
                    return true;
                case R.id.profile:
                    startFragment(profileFragment);
                    return true;
            }
            return false;
        }

    };
}
