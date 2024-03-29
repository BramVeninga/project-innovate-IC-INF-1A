package com.example.miraclepack;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GeofenceAdapter extends RecyclerView.Adapter<GeofenceAdapter.MyViewHolder> {

    private final Context context;
    private final List<Geofence> geofences;
    private final MyDatabaseHelper myDB;

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

    // Check item count for recycler view
    @Override
    public int getItemCount() {
        return geofences.size();
    }

    // Confirmation dialog when deleting geofence
    private void showDeleteConfirmationDialog(final Geofence geofence) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Bevestig verwijdering")
                .setMessage("Weet je zeker dat je deze locatie wilt verwijderen?")
                .setPositiveButton("Ja", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteGeofence(geofence);
                    }
                })
                .setNegativeButton("Nee", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    // Method for deleting geofence from the database
    private void deleteGeofence(Geofence geofence) {
        myDB.deleteGeofence(geofence.getName());
        int position = geofences.indexOf(geofence);
        if (position != -1) {
            geofences.remove(position);
            notifyItemRemoved(position);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private final TextView nameTextView;
        private final ImageButton deleteLocationButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            deleteLocationButton = itemView.findViewById(R.id.deleteLocation);

            // Button that deletes a geofence from the database
            deleteLocationButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Geofence geofence = geofences.get(position);
                        showDeleteConfirmationDialog(geofence);
                    }
                }
            });
        }

        // Setting name for geofence to show in recycler view
        public void bind(Geofence geofence) {
            nameTextView.setText(geofence.getName());
        }
    }
}
