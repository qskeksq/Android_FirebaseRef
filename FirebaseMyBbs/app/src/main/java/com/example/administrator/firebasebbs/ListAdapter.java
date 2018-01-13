package com.example.administrator.firebasebbs;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.example.administrator.firebasebbs.domain.Bbs;

import java.util.ArrayList;
import java.util.List;

public class ListAdapter extends BaseAdapter {

    List<Bbs> data = new ArrayList<>();
    List<Holder> holders = new ArrayList<>();
    Holder holder;
    Context context;
    int clickCount = 0;

    public ListAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Bbs> data) {
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

        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
            holder = new Holder(convertView);
            convertView.setTag(holder);
        }

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

        return convertView;
    }

    public void setClickCount(int count){
        this.clickCount = count;
    }

    public void goDetail(int position) {
        Intent intent = new Intent(context, ReadActivity.class);
        intent.putExtra("LIST_POSITION", position);
        context.startActivity(intent);
        Log.e("포지션값 확인", position + "");
    }

    class Holder implements View.OnClickListener {

        View view;
        TextView no, title, count;
        CheckBox checkBox;
        int position;

        public Holder(View view) {
            this.view = view;
            no = (TextView) view.findViewById(R.id.listno);
            title = (TextView) view.findViewById(R.id.listtitle);
            count = (TextView) view.findViewById(R.id.listcount);
            checkBox = (CheckBox) view.findViewById(R.id.checkbox);
            setListener();
        }

        public void setListener() {
            view.setOnClickListener(this);
            checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                }
            });
        }

//        이렇게 하나하나 체크박스 선택 해 줄 수 는 없는 듯듯
//       public void setCheckboxVisibility() {
//            if (checkBox.getVisibility() == View.GONE) {
//                checkBox.setVisibility(View.VISIBLE);
//            } else {
//                checkBox.setVisibility(View.GONE);
//            }
//        }

        public void onClick(View view) {
            goDetail(position);
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
