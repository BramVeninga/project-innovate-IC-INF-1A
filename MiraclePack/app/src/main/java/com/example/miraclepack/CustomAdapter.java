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

//This class is a blueprint that is used to create the adapter used for the recyclerView that shows all the items of a given configuration
public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<ConfigurationItem> configItems;
    private MyDatabaseHelper myDB;

    CustomAdapter(Context context, ArrayList<ConfigurationItem> configItems) {
        this.context = context;
        this.configItems = configItems;
        this.myDB = new MyDatabaseHelper(context.getApplicationContext());
        for (ConfigurationItem configItem : configItems) {
            this.myDB.filloutCompInConfigItem(this.myDB.getCompartment(configItem.getCompartment().getCompartmentId().toString()), configItem);
        }
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
        holder.item_name_txt.setText(configItems.get(position).getName());
        holder.compartment_id_txt.setText(configItems.get(position).getCompartment().getDescription());
    }

    @Override
    public int getItemCount() {
        return configItems.size();
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
