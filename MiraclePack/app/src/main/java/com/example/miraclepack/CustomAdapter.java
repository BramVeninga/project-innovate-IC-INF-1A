package com.example.miraclepack;

import android.content.Context;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList item_name, compartment_id;

    CustomAdapter(Context context,
                  ArrayList item_name,
                  ArrayList compartment_id) {
        this.context = context;
        this.item_name = item_name;
        this.compartment_id = compartment_id;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.item_name_txt.setText(String.valueOf(item_name.get(position)));
        holder.compartment_id_txt.setText(String.valueOf(compartment_id.get(position)));

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView item_name_txt, compartment_id_txt;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            item_name_txt = itemView.findViewById(R.id.item_name_txt);
            compartment_id_txt = itemView.findViewById(R.id.compartment_number_txt);
        }
    }
}
