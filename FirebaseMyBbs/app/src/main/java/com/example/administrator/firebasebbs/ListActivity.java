package com.example.administrator.firebasebbs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.example.administrator.firebasebbs.domain.Bbs;
import com.example.administrator.firebasebbs.domain.Data;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

public class ListActivity extends AppCompatActivity {

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference("bbs");
    RecyclerView recyclerView;
    RecyclerAdapter adapter;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        adapter = new RecyclerAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        loadData();
    }

    private void loadData(){
        // 프로그래스 다이얼로그를 전역에서 초기화 하면 오류가 생김
        dialog = new ProgressDialog(this);
        dialog.show();
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // 데이터를 ReadActivity 도 써야 하기 때문에 따로 데이터 저장소를 만들어 준다.
                Data.list.clear();
                for(DataSnapshot item : dataSnapshot.getChildren()){
                    try{
                        Bbs bbs = item.getValue(Bbs.class);
                        Data.list.add(bbs);
                    } catch (Exception e){
                        Toast.makeText(ListActivity.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }
                }
                updateData(Data.list);
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void updateData(List<Bbs> data){
        adapter.setData(data);
        adapter.notifyDataSetChanged();
        dialog.dismiss();
    }

    public void add(View view){
        Intent intent = new Intent(this, CreateActivity.class);
        startActivity(intent);
    }
    int clickCount = 0;
    public void delete(View view){
        adapter.setClickCount(clickCount);
        adapter.notifyDataSetChanged(); // 이게 뭐하는가 봤더니 onBindViewHolder 를 갱신해 주는 역할을 하는 듯 하다. 즉, 데이터를 넣어주고 이것을 호출해 주면 데이터 값을 onBindViewHolder 에서 갱신해 준다.
        clickCount++;
    }

    public void confirm(View view){

        // 처음 설계를 잘못한 것이 키 값으로 접근해서 지울 수 있어야 하는데 키 값을 저장을 안 해 뒀다. 따라서 게시글 하나하나에 접근할 방법이 없음
        // 따라서 저장할 때 Bbs 객체에 키 값을 같이 저장해 두자
        for(Bbs bbs : Data.list){
            if(bbs.isChecked() == true){
                reference.child(bbs.getBbsKey()).setValue(null);
            }
        }

    }

}
