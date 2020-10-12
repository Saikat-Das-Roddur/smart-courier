package com.app.smartcourier.Fragment;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.app.smartcourier.Activity.MainActivity;
import com.app.smartcourier.Activity.ParcelTrackActivity;
import com.app.smartcourier.Activity.SignInActivity;
import com.app.smartcourier.Activity.SignUpActivity;
import com.app.smartcourier.Config;
import com.app.smartcourier.Model.Parcel;
import com.app.smartcourier.Model.User;
import com.app.smartcourier.R;
import com.app.smartcourier.Server.ApiClient;
import com.app.smartcourier.Server.ApiInterface;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParcelSubmitFragment extends Fragment{

    EditText editTextTitle, editTextDesc;
    TextView textViewLocation, textViewPayment, textViewBranch,
            textViewCash, textViewBkash,textViewDate;
    Button buttonSubmit;
    RelativeLayout relativeLayoutBkash, relativeLayoutCash;
    Dialog dialog;

    Location currentLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST_CODE = 101;
    Marker marker;

    //ProgressDialog progressDialog;
    //Adapter

    ArrayList<Parcel> parcelArrayList = new ArrayList<>();
    Context context;
    SharedPreferences sharedPreferences;
    String TAG = getClass().getSimpleName(),trackingId="", contact="",myLocation ="";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_parcel_submit,container,false);
        context = getContext();
        editTextTitle = view.findViewById(R.id.titleEt);
        editTextDesc = view.findViewById(R.id.descEt);
        textViewLocation = view.findViewById(R.id.locationTv);
        textViewPayment = view.findViewById(R.id.paymentTv);
        textViewDate = view.findViewById(R.id.dateTv);
        textViewBranch = view.findViewById(R.id.branchTv);
        buttonSubmit = view.findViewById(R.id.submitBtn);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);

        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        textViewDate.setText(formatter.format(today));

        Log.d(TAG, "onCreateView: "+UUID.randomUUID().toString().substring(27));
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        contact = sharedPreferences.getString(Config.CELL_SHARED_PREF,"contact");

        textViewLocation.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                locationDialog();
                return false;
            }
        });

        textViewPayment.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showDialog();
                return true;
            }
        });
        textViewBranch.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return false;
            }
        });

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkValidation();
            }
        });

        return view;
    }


    private void showDialog() {
        Dialog dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.payment_method_dialog);
        relativeLayoutBkash = dialog.findViewById(R.id.bkashLayout);
        relativeLayoutCash = dialog.findViewById(R.id.cashLayout);
        textViewBkash = dialog.findViewById(R.id.bkashTv);
        textViewCash = dialog.findViewById(R.id.cashTv);

        relativeLayoutBkash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                textViewPayment.setText(textViewBkash.getText().toString());
            }
        });

        relativeLayoutCash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                textViewPayment.setText(textViewCash.getText().toString());
            }
        });

        dialog.show();
    }

    private void checkValidation() {
        if (editTextTitle.getText().toString().isEmpty()){
            editTextTitle.setError("Title can't be empty");
        }
        else if (editTextDesc.getText().toString().isEmpty()){
            editTextTitle.setError("Title can't be empty");
        }
        else if (textViewLocation.getText().toString().isEmpty()){
            editTextTitle.setError("Location can't be empty");
        }
        else if (textViewPayment.getText().toString().isEmpty()){
            editTextTitle.setError("Payment can't be empty");
        }
        else if (textViewBranch.getText().toString().isEmpty()){
            editTextTitle.setError("Branch can't be empty");
        }else{
            submitParcel();
        }


    }

    private void submitParcel() {
        final ProgressDialog progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("Please wait...");
        progressDialog.show();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<Parcel> call = apiInterface.submitParcel(
                UUID.randomUUID().toString().substring(27),
                contact,
                editTextTitle.getText().toString(),
                editTextDesc.getText().toString(),
                textViewLocation.getText().toString(),
                textViewPayment.getText().toString(),
                textViewBranch.getText().toString(),
                "",
                textViewDate.getText().toString(),
                "pending"
        );

        call.enqueue(new Callback<Parcel>() {
            @Override
            public void onResponse(Call<Parcel> call, Response<Parcel> response) {
                if (response.body().getValue().equals("success")){
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: "+response.body());
                }

            }

            @Override
            public void onFailure(Call<Parcel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: "+t.getMessage());
//                Toast.makeText(SignUpActivity.this, "Error! "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void locationDialog() {
        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.map_dialog);

        MapView mMapView;
        MapsInitializer.initialize(context);

        mMapView = dialog.findViewById(R.id.mapView);

        mMapView.onCreate(dialog.onSaveInstanceState());
        mMapView.onResume();

        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {

                if (ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                        context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
                    return;
                }
                Task<Location> task = fusedLocationProviderClient.getLastLocation();

                task.addOnSuccessListener(new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location != null) {
                            currentLocation = location;
                            LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                            MarkerOptions markerOptions = new MarkerOptions().position(latLng).title("I am here!");
                            googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5));
                            marker = googleMap.addMarker(markerOptions);
                            marker.showInfoWindow();

                            Toast.makeText(context, currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                            try {
                                List<Address> listAddresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                                if(null!=listAddresses&&listAddresses.size()>0){
                                    myLocation = listAddresses.get(0).getAddressLine(0);
                                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                        @Override
                                        public boolean onMarkerClick(Marker marker) {
                                            textViewLocation.setText(myLocation);
                                            dialog.dismiss();
                                            return false;
                                        }
                                    });
                                    Log.d(TAG, "onSuccessLocation: "+myLocation);
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
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                }
                break;
        }
    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle("");
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle("", mapViewBundle);
        }

       // mMapView.onSaveInstanceState(mapViewBundle);
    }


}
