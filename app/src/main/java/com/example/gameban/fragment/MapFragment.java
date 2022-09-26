package com.example.gameban.fragment;

import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.gameban.R;
import com.example.gameban.databinding.FragmentMapBinding;
import com.example.gameban.entity.AppUser;
import com.example.gameban.retrofit.GoogleMapDirectionRetrofitInterface;
import com.example.gameban.retrofit.GoogleMapLocation;
import com.example.gameban.retrofit.GoogleMapPlaceResponse;
import com.example.gameban.retrofit.GoogleMapRetrofitClient;
import com.example.gameban.retrofit.GoogleMapRetrofitInterface;
import com.example.gameban.retrofit.GoogleMapRouteResponse;
import com.example.gameban.viewmodel.AppUserViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener, GoogleMap.OnPolylineClickListener, OnMapReadyCallback {

    private FragmentMapBinding binding;
    private AppUserViewModel appUserViewModel;
    private GoogleMapRetrofitInterface googleMapRetrofitInterface;
    private GoogleMapDirectionRetrofitInterface googleMapDirectionRetrofitInterface;
    private LatLng currentUserAddress;
    private GoogleMap thisMap;
    private Polyline oneRoute;
    private String message;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = FragmentMapBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        googleMapRetrofitInterface = GoogleMapRetrofitClient.getRetrofitService();
        googleMapDirectionRetrofitInterface = GoogleMapRetrofitClient.getDirectionRetrofitService();

        // Get the SupportMapFragment and request notification when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }


    public LatLng getLocationFromAddress(Context context, String strAddress) {
        Geocoder geocoder = new Geocoder(context);
        List<Address> addressList;
        LatLng latLng = null;
        try {

            addressList = geocoder.getFromLocationName(strAddress, 1);
            if (strAddress != null) {
                Address location = addressList.get(0);
                latLng = new LatLng(location.getLatitude(), location.getLongitude());
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return latLng;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onMapReady(GoogleMap googleMap) {
        thisMap = googleMap;
        //Get current login user's email
        Bundle loginBundle = getActivity().getIntent().getExtras();
        String currentUserEmail = loginBundle.get("email").toString();

        //Interactive with the room
        appUserViewModel = ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()).create(AppUserViewModel.class);

        //Get all app users via the live data and observe
        appUserViewModel.getAllAppUsers().observe(this, new Observer<List<AppUser>>() {
            @Override
            public void onChanged(@NonNull List<AppUser> appUsers) {
                currentUserAddress = new LatLng(1, 1);
                boolean find = false;
                for (AppUser appUser : appUsers) {
                    //Find the current appUser via comparing the email address
                    if (appUser.email.equals(currentUserEmail)) {
                        currentUserAddress = getLocationFromAddress(getActivity().getApplicationContext(),
                                appUser.address);

                        //Add a marker to current user's registration address
                        googleMap.addMarker(new MarkerOptions()
                                .position(currentUserAddress)
                                .title(appUser.name + "'s registration address")
                        );
                        find = true;
                        /*
                        Zoom in to that specific address marker

                        jpm. (2013). Google Maps v2 zoom to specific marker. Retrieved 8 May 2022, from https://stackoverflow.com/questions/19905245/google-maps-v2-zoom-to-specific-marker
                         */
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentUserAddress, 13));
                        break;
                    }
                }
                if (find) {
                    StringBuffer target = new StringBuffer();
                    target.append(currentUserAddress.latitude);
                    target.append(",");
                    target.append(currentUserAddress.longitude);
                    String location = target.toString();
                    Call<GoogleMapPlaceResponse> googleMapCallAsync = googleMapRetrofitInterface.locationSearch("AIzaSyBaQ7NdDfcx2wfCz4uc92OMfpsceymELH0", location, 3000, "game");

                    googleMapCallAsync.enqueue(new Callback<GoogleMapPlaceResponse>() {
                        @Override
                        public void onResponse(Call<GoogleMapPlaceResponse> call, Response<GoogleMapPlaceResponse> response) {
                            GoogleMapPlaceResponse oneResponse = response.body();
                            for (GoogleMapLocation element : oneResponse.getResult()) {
                                LatLng oneLocation = new LatLng(element.getLat(), element.getLng());
                                googleMap.addMarker(new MarkerOptions()
                                        .position(oneLocation)
                                        .title(element.getName())
                                        .icon(BitmapDescriptorFactory.defaultMarker(30))
                                );
                            }
                        }

                        @Override
                        public void onFailure(Call<GoogleMapPlaceResponse> call, Throwable t) {
                            System.out.print(t.getMessage());
                        }
                    });
                }
            }
        });
        googleMap.setOnMarkerClickListener(this);
        googleMap.setOnPolylineClickListener(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        String origin = currentUserAddress.latitude + "," + currentUserAddress.longitude;
        String destination = marker.getPosition().latitude + "," + marker.getPosition().longitude;
        Call<GoogleMapRouteResponse> googleMapRouteCallAsync = googleMapDirectionRetrofitInterface.routeSearch("AIzaSyBaQ7NdDfcx2wfCz4uc92OMfpsceymELH0", destination, origin, "driving");
        googleMapRouteCallAsync.enqueue(new Callback<GoogleMapRouteResponse>() {
            @Override
            public void onResponse(Call<GoogleMapRouteResponse> call, Response<GoogleMapRouteResponse> response) {
                GoogleMapRouteResponse oneResponse = response.body();
                if (oneRoute != null) {
                    oneRoute.remove();
                }
                PolylineOptions polylineOptions = new PolylineOptions();
                for (LatLng oneLatLng : oneResponse.getRoutes().get(0).getAllSteps()) {
                    polylineOptions.add(oneLatLng);
                }
                polylineOptions.clickable(true);
                polylineOptions.width(20);
                polylineOptions.color(Color.BLUE);
                oneRoute = thisMap.addPolyline(polylineOptions);
                message = oneResponse.getRoutes().get(0).getMesage();
            }

            @Override
            public void onFailure(Call<GoogleMapRouteResponse> call, Throwable t) {
                System.out.print(t.getMessage());
            }
        });
        return false;
    }

    @Override
    public void onPolylineClick(@NonNull Polyline polyline) {
        Toast.makeText(getContext(), message, Toast.LENGTH_LONG).show();
    }
}