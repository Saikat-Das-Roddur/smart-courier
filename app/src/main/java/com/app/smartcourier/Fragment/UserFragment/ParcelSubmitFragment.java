package com.app.smartcourier.Fragment.UserFragment;

import android.Manifest;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.app.smartcourier.Config;
import com.app.smartcourier.Model.Branch;
import com.app.smartcourier.Model.Parcel;
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
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ParcelSubmitFragment extends Fragment{

    EditText editTextTitle, editTextDesc;
    TextView textViewLocation, textViewPayment, textViewBranch,
            textViewCash, textViewBkash, textViewDate;
    Button buttonSubmit;
    RelativeLayout relativeLayoutBkash, relativeLayoutCash;

    Location currentLocation;
    LocationManager locationManager;
    FusedLocationProviderClient fusedLocationProviderClient;
    Marker marker;
    Task<Location> task;
    ArrayList<String> branches = new ArrayList<>();

    Context context;
    SharedPreferences sharedPreferences;
    String TAG = getClass().getSimpleName(), contact = "", myLocation = "",latitude="",longitude="";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_parcel_submit, container, false);
        context = getContext();
//        Bundle bundle = this.getArguments();
//        if (bundle!=null){
//            Log.d(TAG, "onCreateViewHaha: ");
//        }
        editTextTitle = view.findViewById(R.id.titleEt);
        editTextDesc = view.findViewById(R.id.descEt);
        textViewLocation = view.findViewById(R.id.locationTv);
        textViewPayment = view.findViewById(R.id.paymentTv);
        textViewDate = view.findViewById(R.id.dateTv);
        textViewBranch = view.findViewById(R.id.branchTv);
        buttonSubmit = view.findViewById(R.id.submitBtn);

        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        Date today = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        textViewDate.setText(formatter.format(today));

        Log.d(TAG, "onCreateView: " + UUID.randomUUID().toString().substring(27));
        sharedPreferences = context.getSharedPreferences(Config.SHARED_PREF_NAME, Context.MODE_PRIVATE);
        contact = sharedPreferences.getString(Config.CELL_SHARED_PREF, "contact");

        textViewLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationDialog();
            }
        });

        textViewPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();

            }
        });
        textViewBranch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showBranches();

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



    private void showBranches() {
        branches.clear();
        ApiInterface apiInterface = ApiClient.getApiClient().create(ApiInterface.class);
        Call<List<Branch>> call = apiInterface.getBranchData();

        call.enqueue(new Callback<List<Branch>>() {
            @Override
            public void onResponse(Call<List<Branch>> call, Response<List<Branch>> response) {
                Log.d(TAG, "onResponse: "+response.body());
               if (response.body()!=null){
                   for (int i = 0; i < response.body().size(); i++) {
                       branches.add(response.body().get(i).getBranchLocation());
                   }
               }
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle("Member List");
                builder.setIcon(R.drawable.branch);

                builder.setItems(branches.toArray(new String[0]), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        textViewBranch.setText(branches.get(i));

                    }
                }).show();

            }

            @Override
            public void onFailure(Call<List<Branch>> call, Throwable t) {

            }
        });

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
        if (editTextTitle.getText().toString().isEmpty()) {
            editTextTitle.setError("Title can't be empty");
        } else if (editTextDesc.getText().toString().isEmpty()) {
            editTextDesc.setError("Title can't be empty");
        } else if (textViewLocation.getText().toString().isEmpty()) {
            textViewLocation.setError("Location can't be empty");
        } else if (textViewPayment.getText().toString().isEmpty()) {
            textViewPayment.setError("Payment can't be empty");
        } else if (textViewBranch.getText().toString().isEmpty()) {
            textViewBranch.setError("Branch can't be empty");
        } else {
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
                latitude,longitude,
                textViewPayment.getText().toString(),
                textViewBranch.getText().toString(),
                "",
                textViewDate.getText().toString(),
                "Pending"
        );

        call.enqueue(new Callback<Parcel>() {
            @Override
            public void onResponse(Call<Parcel> call, Response<Parcel> response) {
                if (response.body().getValue().equals("success")) {
                    progressDialog.dismiss();
                    Log.d(TAG, "onResponse: " + response.body());
                }

            }

            @Override
            public void onFailure(Call<Parcel> call, Throwable t) {
                progressDialog.dismiss();
                Log.d(TAG, "onFailure: " + t.getMessage());
//                Toast.makeText(SignUpActivity.this, "Error! "+t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void locationDialog() {
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
                                currentLocation = location;
                                LatLng latLng = new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude());
                                latitude = String.valueOf(currentLocation.getLatitude());
                                longitude = String.valueOf(currentLocation.getLongitude());

                                Toast.makeText(context, currentLocation.getLatitude() + "" + currentLocation.getLongitude(), Toast.LENGTH_SHORT).show();

                                Geocoder geocoder = new Geocoder(context, Locale.getDefault());
                                try {
                                    List<Address> listAddresses = geocoder.getFromLocation(currentLocation.getLatitude(), currentLocation.getLongitude(), 1);
                                    if (null != listAddresses && listAddresses.size() > 0) {

                                        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(listAddresses.get(0).getAddressLine(0));
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 12));
                                        marker = googleMap.addMarker(markerOptions);
                                        marker.showInfoWindow();

                                        myLocation = listAddresses.get(0).getAddressLine(0);
                                        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                                            @Override
                                            public boolean onMarkerClick(Marker marker) {
                                                textViewLocation.setText(myLocation);
                                                dialog.dismiss();
                                                return false;
                                            }
                                        });
                                        Log.d(TAG, "onSuccessLocation: " + myLocation);
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
            startActivity(myIntent);
            Toast.makeText(context, "Please Turn On Location", Toast.LENGTH_SHORT).show();
        }

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(context,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
                Toast.makeText(context, "Permission Granted", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(context, "Permission Denied", Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermission()){
            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
           // task = fusedLocationProviderClient.getLastLocation();
        }
    }

    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) return true;
        else return false;
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
