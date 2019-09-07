package com.example.callerthing;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    private ArrayList<Item> mList;

    public  static class ViewHolder extends RecyclerView.ViewHolder{
        public ImageView mProfilePic;
        public TextView mDescription;
        public  TextView mNumber;

        public  ViewHolder(View itemView) {
            super(itemView);
            mProfilePic=itemView.findViewById(R.id.profilePic);
            mDescription=itemView.findViewById(R.id.description);
            mNumber=itemView.findViewById(R.id.number);
        }
    }

    public Adapter(ArrayList<Item> list){
        mList=list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.item, parent, false);
        ViewHolder evh = new ViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Item currentItem=mList.get(position);

        holder.mProfilePic.setImageResource(currentItem.getProfilePic());
        holder.mDescription.setText(currentItem.getDescription());
        holder.mNumber.setText(currentItem.getNumber());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
}
