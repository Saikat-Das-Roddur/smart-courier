package com.app.smartcourier.Adapter.ManagerAdapter;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class ParcelRequestAdapter extends RecyclerView.Adapter<ParcelRequestAdapter.ViewHolder> {
    List<Parcel> parcels;
    Context context;
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    Marker marker;
    Task<Location> task;


    public ParcelRequestAdapter(List<Parcel> parcels, Context context) {
        this.parcels = parcels;
        this.context = context;
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

    }

    @NonNull
    @Override
    public ParcelRequestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.parcel_request_rv,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ParcelRequestAdapter.ViewHolder holder, int position) {
        holder.textViewTrackID.setText("Track-ID: "+parcels.get(position).getTracking_id());
        holder.textViewTitle.setText(parcels.get(position).getTitle());
        holder.textViewDate.setText("Date: "+parcels.get(position).getDate());
        holder.textViewDesc.setText(parcels.get(position).getDescription());
        holder.textViewLocation.setText("Branch: "+parcels.get(position).getLocation());
        holder.textViewPayment.setText("Payment: "+parcels.get(position).getPayment_method());
        holder.textViewStatus.setText("Status: "+parcels.get(position).getParcelStatus());
        holder.imageViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(parcels.get(position).getLatitude(),parcels.get(position).getLongitude());
            }
        });
    }

    private void showDialog(String latitude, String longitude) {

            Dialog dialog = new Dialog(context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
            dialog.setCancelable(true);
            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            dialog.setContentView(R.layout.map_dialog);

            MapView mMapView;
            MapsInitializer.initialize(context);

            mMapView = dialog.findViewById(R.id.mapView);

            mMapView.onCreate(dialog.onSaveInstanceState());
            mMapView.onResume();


            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                mMapView.getMapAsync(new OnMapReadyCallback() {
                    @Override
                    public void onMapReady(GoogleMap googleMap) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            if (context.checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && context.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                return;
                            }
                        }
                        task = fusedLocationProviderClient.getLastLocation();
                        task.addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location != null) {
                                    LatLng latLng = new LatLng(Double.parseDouble(latitude), Double.parseDouble(longitude));
                                    Toast.makeText(context, Double.parseDouble(longitude) + "" + Double.parseDouble(longitude), Toast.LENGTH_SHORT).show();

                                    Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                    try {
                                        List<Address> listAddresses = geocoder.getFromLocation(Double.parseDouble(latitude), Double.parseDouble(longitude), 1);
                                        if (null != listAddresses && listAddresses.size() > 0) {

                                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(listAddresses.get(0).getAddressLine(0));
                                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                                            marker = googleMap.addMarker(markerOptions);
                                            marker.showInfoWindow();

                                            String myLocation = listAddresses.get(0).getAddressLine(0);
                                            Log.d("", "onSuccessLocation: " + myLocation);
                                        }
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        });


                    }
                });
                mMapView.onSaveInstanceState(dialog.onSaveInstanceState());
                dialog.show();
            } else {
                Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                context.startActivity(myIntent);
                Toast.makeText(context, "Please Turn On Location", Toast.LENGTH_SHORT).show();
            }


    }

    @Override
    public int getItemCount() {
        return parcels.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textViewTrackID, textViewTitle, textViewDate, textViewDesc,
                textViewLocation, textViewPayment,textViewStatus;
        ImageView imageViewLocation;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewTrackID = itemView.findViewById(R.id.trackIdTv);
            textViewTitle = itemView.findViewById(R.id.titleTv);
            textViewDate = itemView.findViewById(R.id.dateTv);
            textViewDesc = itemView.findViewById(R.id.descTv);
            textViewLocation = itemView.findViewById(R.id.locationTv);
            textViewPayment = itemView.findViewById(R.id.paymentTv);
            textViewStatus = itemView.findViewById(R.id.statusTv);
            imageViewLocation = itemView.findViewById(R.id.locationIv);

        }
    }
}
