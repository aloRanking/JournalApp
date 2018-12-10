package com.example.aloranking.journalapp.adapter;

import android.view.View;

public interface AdapterClicklistener {
    public void OnItemClick (View v, int position);

    void OnItemLongClick(View v,int pos);
}
