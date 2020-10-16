package com.app.smartcourier.Adapter.ManagerAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartcourier.Model.Payment;
import com.app.smartcourier.R;

import java.util.List;

public class ManagerPaymentHistoryAdapter extends RecyclerView.Adapter<ManagerPaymentHistoryAdapter.ViewHolder> {

    Context context;
    List<Payment> paymentList;

    public ManagerPaymentHistoryAdapter(Context context, List<Payment> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.manager_payment_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewTrackId.setText("Track Id: "+paymentList.get(position).getTracking_id());
        holder.textViewTrxId.setText("Bkash Trx Id: "+paymentList.get(position).getBkash_trx_id());
        holder.textViewBkashNo.setText("Bkash No: "+paymentList.get(position).getBkash_number());
        holder.textViewAmount.setText("Amount: "+paymentList.get(position).getAmount());
        holder.textViewBranchName.setText("Branch Name: "+paymentList.get(position).getBranch());
        holder.textViewStatus.setText("Status: "+paymentList.get(position).getPaymentStatus());

    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTrackId, textViewTrxId, textViewBkashNo,
                textViewAmount,textViewStatus, textViewBranchName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTrackId = itemView.findViewById(R.id.trackIdTv);
            textViewTrxId = itemView.findViewById(R.id.trxIdTv);
            textViewBkashNo = itemView.findViewById(R.id.bkashNoTv);
            textViewAmount = itemView.findViewById(R.id.bkashAmountTv);
            textViewStatus = itemView.findViewById(R.id.statusTv);
            textViewBranchName = itemView.findViewById(R.id.branchTv);

        }
    }
}
