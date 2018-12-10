package com.example.aloranking.journalapp.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHoler> {
    @NonNull
    @Override
    public MyViewHoler onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHoler holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHoler extends RecyclerView.ViewHolder {
        public MyViewHoler(View itemView) {
            super(itemView);
        }
    }
}
