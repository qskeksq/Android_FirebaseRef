package com.example.administrator.firebasebbs2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.example.administrator.firebasebbs2.domain.Bbs;
import com.example.administrator.firebasebbs2.domain.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    ListView listView;
    ListAdapter adapter;

    FirebaseDatabase database;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        database = FirebaseDatabase.getInstance();
        reference = database.getReference("bbs");

        setContentView(R.layout.activity_list);

        listView = (ListView) findViewById(R.id.list);

        adapter = new ListAdapter(this);

        listView.setAdapter(adapter);

        loadData();
    }

    private void updateList(List<Bbs> data){
        adapter.setData(data);
        adapter.notifyDataSetChanged();
    }

    public void loadData(){
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot items) {
                Data.list.clear();
//                List<Bbs> data = new ArrayList<>();     // 처리하려면 제대로 해라 호출되는 콜백 메소드는 onDataChange 이다. 위에다 적으면 당연히 계속 복사되지
                for(DataSnapshot item : items.getChildren()){
                    // 만약 서버에서 데이터를 추가했는데, json 데이터를 Bbs 인스턴스로 변환할 때 서로 안 맞아서 오류가 생길 수 있다.
                    try {
                        Bbs bbs = item.getValue(Bbs.class);
                        Data.list.add(bbs);
                    } catch (Exception e){
                        e.printStackTrace();
                    }
                }
                updateList(Data.list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void add(View view){
        Intent intent = new Intent(this, WriteActivity.class);
        startActivity(intent);
    }

}
