package com.example.rotondeando.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentManager;

import android.Manifest;
import android.app.Activity;
import android.app.Fragment;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rotondeando.Constants;
import com.example.rotondeando.R;
import com.example.rotondeando.Util;
import com.example.rotondeando.database.DataBaseHelper;
import com.example.rotondeando.databinding.ActivityShowRouteBinding;
import com.example.rotondeando.fragment.MapsFragment;
import com.example.rotondeando.fragment.ShowRouteFragment;
import com.example.rotondeando.model.Rotonda;
import com.example.rotondeando.model.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;

public class ShowRouteActivity extends AppCompatActivity {
    ActivityShowRouteBinding binding;
    private static final String KEY_RUTA = "ruta";
    Route route;
    SupportMapFragment mapFragment;
    MapsFragment map;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShowRouteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initViews();
    }

    private void initViews() {
        route =  (Route) getIntent().getExtras().getSerializable(KEY_RUTA);
        if(route.isFav()){
            binding.rFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_up , 0);
        }

        binding.rFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Util.checkFav(binding.rFav, getApplicationContext(), route.get_id(), route.isFav());
            }
        });
        binding.tvRouteTool.setText(route.getName().toUpperCase() + "");
        binding.rWalk.setCompoundDrawablesWithIntrinsicBounds(0, 0, Util.getDrawableInfo(route, 1), 0);
        binding.rBike.setCompoundDrawablesWithIntrinsicBounds(0, 0, Util.getDrawableInfo(route, 2), 0);
        binding.rCar.setCompoundDrawablesWithIntrinsicBounds(0, 0, Util.getDrawableInfo(route, 3), 0);

        try{
            binding.rKm.setText(route.getKm() + " km");
        }catch (NullPointerException e){
            binding.rKm.setText("- km");
            Log.e("show init km", e.getMessage());
        }
        try {
            binding.rDescription.setText(route.getDescription());
        }catch (NullPointerException e){
            binding.rDescription.setText("");
            Log.e("show init description", e.getMessage());
        }

        Bundle dataRoute = new Bundle();
        dataRoute.putSerializable(KEY_RUTA, route);
        ShowRouteFragment fragment = new ShowRouteFragment();
        fragment.setArguments(dataRoute);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.fragment, fragment)
                .commit();
    }

}