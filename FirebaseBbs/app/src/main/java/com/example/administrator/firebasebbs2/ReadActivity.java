package com.example.administrator.firebasebbs2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.firebasebbs2.domain.Bbs;
import com.example.administrator.firebasebbs2.domain.Data;

public class ReadActivity extends AppCompatActivity {

    ImageView imageView;
    TextView textTitle, textAuthor, textDate, textCount, textContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);

        imageView = (ImageView) findViewById(R.id.imageView2);
        textTitle = (TextView) findViewById(R.id.readTitle);
        textAuthor = (TextView) findViewById(R.id.readAuthor);
        textDate = (TextView) findViewById(R.id.readDate);
        textCount = (TextView) findViewById(R.id.readCount);

        setData();


    }

    public void setData(){
        Intent intent = getIntent();
        int position = intent.getIntExtra("LIST_POSITION", -1);

        if(position > -1){
            Bbs bbs = Data.list.get(position);
            if(bbs.getFileUriString() != null && !"".equals(bbs.getFileUriString())){
                Glide.with(this).load(bbs.getFileUriString()).into(imageView);
            }
            textTitle.setText(bbs.getTitle());
            textAuthor.setText(bbs.getAuthor());
            textDate.setText(bbs.getDate()+"");
        }
    }

    // storage 에 저장하는 방식이 두 가지이다
    // download url 일반 url 을 사용하여 어디에서나 사용할 수 있다.
    // storage location 은 파이어베이스 로케이션을 통해서만 다운 가능


}
