package com.example.administrator.firebasefcm;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Administrator on 2017-07-05.
 */

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.Holder> {

    List<Uid> uids;
    IAdapter iAdapter;

    public CustomAdapter(List<Uid> uids, IAdapter iAdapter) {

        this.uids = uids;
        this.iAdapter = iAdapter;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        Uid uid = uids.get(position);
        holder.textView2.setText(uid.deviceUid);
        holder.textView3.setText(uid.name);
        holder.token = uid.token;
    }

    @Override
    public int getItemCount() {
        return uids.size();
    }

    public interface IAdapter {
        void setToken(String token);
    }

    public class Holder extends RecyclerView.ViewHolder {

        TextView textView2, textView3;
        String token;

        public Holder(final View itemView) {
            super(itemView);

            textView2 = (TextView) itemView.findViewById(R.id.textView2);
            textView3 = (TextView) itemView.findViewById(R.id.textView3);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    iAdapter.setToken(token);
                }
            });
        }
    }
}
