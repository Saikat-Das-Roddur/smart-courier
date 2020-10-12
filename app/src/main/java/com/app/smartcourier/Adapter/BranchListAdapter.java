package com.app.smartcourier.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartcourier.Model.Branch;
import com.app.smartcourier.R;

import org.w3c.dom.Text;

import java.util.List;

public class BranchListAdapter extends RecyclerView.Adapter<BranchListAdapter.ViewHolder> {
    List<Branch> branches;
    Context context;

    public BranchListAdapter(List<Branch> branches, Context context) {
        this.branches = branches;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.branch_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull BranchListAdapter.ViewHolder holder, int position) {
        holder.textViewLocation.setText(branches.get(position).getBranchLocation());
        holder.textViewContact.setText(branches.get(position).getBranchContact());
        holder.textViewName.setText(branches.get(position).getBranchName());
    }

    @Override
    public int getItemCount() {
        return branches.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewLocation, textViewContact;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewContact = itemView.findViewById(R.id.contactTv);
            textViewName = itemView.findViewById(R.id.nameTv);
            textViewLocation = itemView.findViewById(R.id.locationTv);

        }
    }
}
