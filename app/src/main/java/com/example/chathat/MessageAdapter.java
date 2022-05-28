package com.example.chathat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder>{
    List<ModelClass> list;
    String userName;
    boolean status;
    int send;
    int receive;

    public MessageAdapter(List<ModelClass> list, String userName) {
        this.list = list;
        this.userName = userName;
        status = false;
        send=1;
        receive = 2;

    }

    public MessageAdapter() {
    }

    @NonNull
    @Override
    public MessageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(viewType==send){
            view= LayoutInflater.from(parent.getContext()).inflate(R.layout.card_send,parent,false);
        }else{
            view=LayoutInflater.from(parent.getContext()).inflate(R.layout.card_receive,parent, false);

        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.ViewHolder holder, int position) {
        holder.textView.setText(list.get(position).getMessage());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            if(status){
                textView=itemView.findViewById(R.id.textViewSend);
            }
            else{
                textView=itemView.findViewById(R.id.textViewReceive);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(list.get(position).getFrom().equals(userName)){
            status=true;
            return send;
        }else{
            status=false;
            return receive;
        }
    }
}
