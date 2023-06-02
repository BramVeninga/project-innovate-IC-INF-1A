package com.example.miraclepack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GeofenceAdapter extends RecyclerView.Adapter<GeofenceAdapter.MyViewHolder> {

    private Context context;
    private List<Geofence> geofences;
    private MyDatabaseHelper myDB;

    GeofenceAdapter(Context context, List<Geofence> geofences) {
        this.context = context;
        this.geofences = geofences;
        myDB = new MyDatabaseHelper(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_geofence, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GeofenceAdapter.MyViewHolder holder, int position) {
        Geofence geofence = geofences.get(position);
        holder.bind(geofence);
    }

    @Override
    public int getItemCount() {
        return geofences.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView nameTextView;
        Button deleteLocationButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            deleteLocationButton = itemView.findViewById(R.id.deleteLocation);

            deleteLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Geofence geofence = geofences.get(position);
                        myDB.deleteGeofence(geofence.getName());
                        geofences.remove(position);
                        notifyItemRemoved(position);
                    }
                }
            });
        }

        public void bind(Geofence geofence) {
            nameTextView.setText(geofence.getName());
        }
    }
}
