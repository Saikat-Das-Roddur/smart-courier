package com.app.smartcourier.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.R;

import java.util.List;

public class ParcelHistoryAdapter extends RecyclerView.Adapter<ParcelHistoryAdapter.ViewHolder> {

    List<Parcel> parcelList;
    Context context;

    public ParcelHistoryAdapter(List<Parcel> parcelList, Context context) {
        this.parcelList = parcelList;
        this.context = context;
    }

    @NonNull
    @Override
    public ParcelHistoryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.parcel_history_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelHistoryAdapter.ViewHolder holder, int position) {
        holder.textViewTrackID.setText("Track-ID: "+parcelList.get(position).getTracking_id());
        holder.textViewTitle.setText(parcelList.get(position).getTitle());
        holder.textViewDate.setText("Date: "+parcelList.get(position).getDate());
        holder.textViewDesc.setText(parcelList.get(position).getDescription());
        holder.textViewLocation.setText("Branch: "+parcelList.get(position).getLocation());
        holder.textViewPayment.setText("Payment: "+parcelList.get(position).getPayment_method());
        holder.textViewStatus.setText("Status: "+parcelList.get(position).getParcelStatus());
    }

    @Override
    public int getItemCount() {
        return parcelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTrackID, textViewTitle, textViewDate, textViewDesc,
                textViewLocation, textViewPayment,textViewStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTrackID = itemView.findViewById(R.id.trackIdTv);
            textViewTitle = itemView.findViewById(R.id.titleTv);
            textViewDate = itemView.findViewById(R.id.dateTv);
            textViewDesc = itemView.findViewById(R.id.descTv);
            textViewLocation = itemView.findViewById(R.id.locationTv);
            textViewPayment = itemView.findViewById(R.id.paymentTv);
            textViewStatus = itemView.findViewById(R.id.statusTv);

        }
    }
}
