package com.example.administrator.firebasechat.Friends;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.administrator.firebasechat.R;
import com.example.administrator.firebasechat.domain.Data;


/**
 * 가입된 유저 목록
 */
public class FriendsFragment extends Fragment {


    private RecyclerView friendsRecycler;
    FriendsAdapter adapter;
    private Button invite;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        initView(view);
        adapter = new FriendsAdapter(Data.users);
        friendsRecycler.setAdapter(adapter);
        friendsRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        return view;
    }

    private void initView(View view) {
        friendsRecycler = (RecyclerView) view.findViewById(R.id.friendsRecycler);
        invite = (Button) view.findViewById(R.id.invite);
    }

}
