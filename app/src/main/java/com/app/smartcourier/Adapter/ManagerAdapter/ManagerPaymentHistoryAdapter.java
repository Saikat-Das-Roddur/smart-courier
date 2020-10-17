package com.app.smartcourier.Adapter.ManagerAdapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartcourier.Activity.SignInActivity;
import com.app.smartcourier.Activity.SignUpActivity;
import com.app.smartcourier.Model.BranchManager;
import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.Model.Payment;
import com.app.smartcourier.Model.User;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerPaymentHistoryAdapter extends RecyclerView.Adapter<ManagerPaymentHistoryAdapter.ViewHolder> {

    Context context;
    List<Payment> paymentList;
    ProgressDialog progressDialog;

    public ManagerPaymentHistoryAdapter(Context context, List<Payment> paymentList) {
        this.context = context;
        this.paymentList = paymentList;
        progressDialog = new ProgressDialog(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.manager_payment_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textViewTrackId.setText("Track Id: "+paymentList.get(position).getTracking_id());
        if (paymentList.get(position).getBkash_trx_id().contains("cash")){
            holder.textViewTrxId.setText("Payment Method: Cash");
            holder.textViewBkashNo.setVisibility(View.GONE);
        }
        else {
            holder.textViewTrxId.setText("Bkash Trx Id: "+paymentList.get(position).getBkash_trx_id());
            holder.textViewBkashNo.setText("Bkash No: "+paymentList.get(position).getBkash_number());
        }
        holder.textViewContactNo.setText("User Contact: "+paymentList.get(position).getContact_no());
        holder.textViewAmount.setText("Amount: "+paymentList.get(position).getAmount());
        holder.textViewBranchName.setText("Branch Name: "+paymentList.get(position).getBranch());
        holder.textViewStatus.setText("Status: "+paymentList.get(position).getPaymentStatus());
        if (paymentList.get(position).getPaymentStatus().equalsIgnoreCase("pending")){
            holder.buttonConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    progressDialog.show();
                    ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
                    Call<Payment> call;
                    call = apiInterface.updateManagerPayment(paymentList.get(position).getTracking_id(),"Paid");
                    //Toast.makeText(context, "Paid", Toast.LENGTH_SHORT).show();
                    call.enqueue(new Callback<Payment>() {
                        @Override
                        public void onResponse(Call<Payment> call, Response<Payment> response) {
                            if (response.body().getValue().equals("success")){
                                progressDialog.dismiss();
                                Toast.makeText(context, "Paid", Toast.LENGTH_SHORT).show();
//                                startActivity(new Intent(SignUpActivity.this, SignInActivity.class));
//                                finish();
                            }
                            else{
                                progressDialog.dismiss();
                                Toast.makeText(context,"Already Paid ", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<Payment> call, Throwable t) {
                           progressDialog.dismiss();
//                            Log.d(TAG, "onFailure: "+t.getMessage());
                            Toast.makeText(context, "Error! "+t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });

        }else {
            holder.buttonConfirm.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        return paymentList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTrackId, textViewTrxId, textViewBkashNo,
                textViewAmount,textViewStatus, textViewBranchName,textViewContactNo;
        Button buttonCancel, buttonConfirm;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTrackId = itemView.findViewById(R.id.trackIdTv);
            textViewTrxId = itemView.findViewById(R.id.trxIdTv);
            textViewBkashNo = itemView.findViewById(R.id.bkashNoTv);
            textViewAmount = itemView.findViewById(R.id.bkashAmountTv);
            textViewStatus = itemView.findViewById(R.id.statusTv);
            textViewBranchName = itemView.findViewById(R.id.branchTv);
            textViewContactNo = itemView.findViewById(R.id.contactTv);
            buttonCancel = itemView.findViewById(R.id.cancelBtn);
            buttonConfirm = itemView.findViewById(R.id.confirmBtn);
        }
    }
}
