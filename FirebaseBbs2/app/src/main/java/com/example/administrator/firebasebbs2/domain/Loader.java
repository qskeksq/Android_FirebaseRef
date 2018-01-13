package com.example.administrator.firebasebbs2.domain;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-07-03.
 */

public class Loader {

    FirebaseDatabase database;
    DatabaseReference reference;
    List<Bbs> data;
    Bbs mBbs;

    public Loader() {

        getDatabase();
    }

    public void getDatabase(){

        database = FirebaseDatabase.getInstance();
    }

    public List<Bbs> getRefs(String key){

        reference = database.getReference(key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot items) {
                data = new ArrayList<>();   // todo 이거 여기 있으면 계속 생성됨
                for(DataSnapshot item : items.getChildren()){
                    Log.e("key", item.getKey()+"");
                    Bbs bbs = item.getValue(Bbs.class);
                    data.add(bbs);
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return data;
    }

    public Bbs getOneRef(String key){
        reference = database.getReference(key);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot item) {
                mBbs = item.getValue(Bbs.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        return mBbs;
    }







}
