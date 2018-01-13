package com.example.administrator.firebasebbs2;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.administrator.firebasebbs2.domain.Bbs;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017-07-03.
 */

public class ListAdapter extends BaseAdapter {

    List<Bbs> data = new ArrayList<>();
    Holder holder;
    Context context;

    int position;

    public ListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Bbs> data){
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        this.position = position;

        if(convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }

        Bbs bbs = data.get(position);
        holder.no.setText(position+1+"");
        holder.title.setText(bbs.getTitle());
        holder.count.setText(bbs.getCount()+"");

        return convertView;
    }

    public void goDetail(){
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra("LIST_POSITION", position);
        context.startActivity(intent);
    }



    class Holder implements View.OnClickListener{

        View view;
        TextView no, title, count;

        public Holder(View view) {
            this.view = view;
            no = (TextView) view.findViewById(R.id.listno);
            title = (TextView) view.findViewById(R.id.listtitle);
            count = (TextView) view.findViewById(R.id.listcount);
            setListener();
        }

        public void setListener(){
            view.setOnClickListener(this);
        }

        public void onClick(View view){
            goDetail();
        }

        public void setNo(String no) {
            this.no.setText(no);
        }

        public void setTitle(String title) {
            this.title.setText(title);
        }

        public void setCount(String count) {
            this.count.setText(count);
        }
    }
}
