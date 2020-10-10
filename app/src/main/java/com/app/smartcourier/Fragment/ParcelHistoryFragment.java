package com.app.smartcourier.Fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.R;

import java.util.ArrayList;

public class ParcelHistoryFragment extends Fragment {

    RecyclerView recyclerView;

    //ProgressDialog progressDialog;
    //Adapter

    ArrayList<Parcel> videoArrayList = new ArrayList<>();
    String channelId;
    Context context;
    String TAG = getClass().getSimpleName();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_parcel_history,container,false);
        context = getContext();
        recyclerView = view.findViewById(R.id.historyParcelRv);

        return view;
    }

}
