package com.example.administrator.firebasebbs;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.administrator.firebasebbs.domain.Bbs;
import com.example.administrator.firebasebbs.domain.Data;

public class ReadActivity extends AppCompatActivity {

    ImageView imageView, imageView2;
    TextView textTitle, textAuthor, textDate, textCount;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read);
        position = getIntent().getIntExtra("LIST_POSITION",0);
        imageView = (ImageView) findViewById(R.id.imageView2);
        imageView2 = (ImageView) findViewById(R.id.imageView3);
        textTitle = (TextView) findViewById(R.id.readTitle);
        textAuthor = (TextView) findViewById(R.id.readAuthor);
        textDate = (TextView) findViewById(R.id.readDate);
        textCount = (TextView) findViewById(R.id.readCount);
        setData();
    }

    private void setData(){
        if(position > -1){
            Bbs bbs = Data.list.get(position);
            textTitle.setText(bbs.getTitle());
            textAuthor.setText(bbs.getAuthor());
            textDate.setText(bbs.getDate()+"");
            if(bbs.getFileUriString() != null && !"".equals(bbs.getFileUriString())) {
                Glide.with(this).load(bbs.getFileUriString()).into(imageView);
                Glide.with(this).load(bbs.getFileUriString()).into(imageView2);
            }
            textCount.setText("0");
        }
    }
}
