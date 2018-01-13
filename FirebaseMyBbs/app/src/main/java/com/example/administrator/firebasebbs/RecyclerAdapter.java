package com.example.administrator.firebasebbs;


import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.administrator.firebasebbs.domain.Bbs;

import java.util.ArrayList;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.Holder> {

    List<Bbs> data = new ArrayList<>();
    Context context;
    int clickCount = 0;

    public RecyclerAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Bbs> data){
        this.data = data;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Bbs bbs = data.get(position);
        holder.no.setText(position + 1 + "");
        holder.title.setText(bbs.getTitle());
        holder.count.setText(bbs.getCount() + "");
        holder.position = position;
        holder.checkBox.setChecked(false);

        if(clickCount%2 == 0){
            holder.checkBox.setVisibility(View.GONE);
        } else if(clickCount%2 == 1){
            holder.checkBox.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void goDetail(int position) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra("LIST_POSITION", position);
        context.startActivity(intent);
        Log.e("포지션값 확인", position + "");
    }

    public void setClickCount(int count){
        this.clickCount = count;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView no, title, count;
        CheckBox checkBox;
        int position;

        public Holder(View itemView) {
            super(itemView);
            no = (TextView) itemView.findViewById(R.id.listno);
            title = (TextView) itemView.findViewById(R.id.listtitle);
            count = (TextView) itemView.findViewById(R.id.listcount);
            checkBox = (CheckBox) itemView.findViewById(R.id.checkbox);
            itemView.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if(isChecked){
                        data.get(position).setChecked(true);
                    } else {
                        data.get(position).setChecked(false);
                    }
                }
            });
        }

        public void onClick(View view) {
            goDetail(position);
        }
    }
}
