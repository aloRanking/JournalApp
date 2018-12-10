package com.example.aloranking.journalapp.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aloranking.journalapp.DairyHome;
import com.example.aloranking.journalapp.R;
import com.example.aloranking.journalapp.model.Diaries;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.MyViewHolder> {

    private List<Diaries> mDiaries;
   // private Context mcontext;
   private AdapterClicklistener clicklistener;
   private SparseBooleanArray selectedItems;

    public DiaryAdapter(List<Diaries> mDiaries) {
        this.mDiaries = mDiaries;
        selectedItems = new SparseBooleanArray();

    }



   public void setOnItemClickListener(AdapterClicklistener clicklistener){
       this.clicklistener = clicklistener;
   }



    @NonNull
    @Override
    public DiaryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.custom_row, parent, false);

        return new MyViewHolder(itemView,clicklistener);
    }

    @Override
    public void onBindViewHolder(@NonNull DiaryAdapter.MyViewHolder holder, int position) {

        Diaries diaries = mDiaries.get(position);

        holder.feelingText.setText(diaries.getDiaryFeeling());
        holder.date_timeText.setText(diaries.getDairyDateAndTime());
        holder.thoughts_text.setText(diaries.getDairyEntries());
        holder.myBackground.setSelected(selectedItems.get(position, false));

    }

    @Override
    public int getItemCount() {
        return mDiaries.size();
    }

    public void removeIteml(int position) {

      mDiaries.remove(position);
      notifyItemRemoved(position);
    }

    public void restoreItem(Diaries item, int position) {
        mDiaries.add(position, item);
        notifyItemInserted(position);
    }




    public List<Diaries> getData() {
        return mDiaries;
    }

    public void removeItem(ArrayList<String> list){



        for (String diaries : list){
            //DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference();
            mDiaries.remove(diaries);
        }
        notifyDataSetChanged();

    }



    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,View.OnLongClickListener{
        public TextView feelingText, date_timeText, thoughts_text;
        private AdapterClicklistener clicklistener;
       public LinearLayout myBackground;


        public MyViewHolder(final View itemView, AdapterClicklistener clicklistener) {
            super(itemView);

            feelingText = itemView.findViewById(R.id.feeling_text);
            date_timeText = itemView.findViewById(R.id.date_time_text);
            thoughts_text = itemView.findViewById(R.id.thought_text);
            myBackground = itemView.findViewById(R.id.cust_row);
            this.clicklistener =clicklistener;

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

           /* itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(itemView, position);
                        }
                    }
                }
            });*/


        }



        @Override
        public void onClick(View view) {
            DairyHome dairyHome = new DairyHome();
            boolean action = dairyHome.in_action_mode;

            if (clicklistener != null){
                clicklistener.OnItemClick(view,getAdapterPosition());
            }

            if (selectedItems.get(getAdapterPosition(), false)){
                selectedItems.delete(getAdapterPosition());
                myBackground.setSelected(false);
            }/*if (!action){
                selectedItems.delete(getAdapterPosition());
                myBackground.setSelected(false);
            }*/
            else {
                selectedItems.put(getAdapterPosition(), true);
                myBackground.setSelected(true);
            }



          /*  int position = getAdapterPosition(); // gets item position
            if (position != RecyclerView.NO_POSITION) { // Check if an item was deleted, but the user clicked it before the UI removed it
                Diaries diaries = mDiaries.get(position);
                // We can access the data within the views
                Toast.makeText(context, diaries.getDiaryFeeling(), Toast.LENGTH_SHORT).show();
            }*/

        }



        @Override
        public boolean onLongClick(View view) {

            if (clicklistener != null){
               clicklistener.OnItemLongClick(view,getAdapterPosition());


        }
        return true;
    }
}}
