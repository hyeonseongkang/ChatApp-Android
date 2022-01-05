package com.example.chatapp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.MyViewHolder>{

    private List<ChatData> dataList;
    private String user;

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView date, message;
        public MyViewHolder(View v) {
            super(v);

            message = (TextView) v.findViewById(R.id.message);
            date = (TextView) v.findViewById(R.id.time);

        }
    }

    public ChatAdapter(List<ChatData> dataList, String user) {
        this.dataList = dataList;
        this.user = user;
    }


    @Override
    public ChatAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;

        if (viewType == 1) {
            v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_my_chat_layout, parent, false);
        } else {
            v = (View) LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_chat_adapter, parent, false);
        }


        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public int getItemViewType(int position) {
        if(dataList.get(position).getUserName().equals(user)) {
            return 1;
        } else {
            return 2;
        }
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.date.setText(dataList.get(position).getTime());
        holder.message.setText(dataList.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return dataList == null ? 0 : dataList.size();
    }

}
