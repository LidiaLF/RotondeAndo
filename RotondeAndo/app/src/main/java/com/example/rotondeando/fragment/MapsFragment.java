package com.example.rotondeando.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rotondeando.Constants;
import com.example.rotondeando.R;
import com.example.rotondeando.Util;
import com.example.rotondeando.database.DataBaseHelper;
import com.example.rotondeando.databinding.FragmentMapsBinding;
import com.example.rotondeando.databinding.FragmentRotondeandoBinding;
import com.example.rotondeando.model.Rotonda;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsFragment extends Fragment {

    public static MapsFragment newInstance() {
        MapsFragment fragment = new MapsFragment();
        return fragment;
    }

    private OnMapReadyCallback callback = new OnMapReadyCallback() {
        @Override
        public void onMapReady(GoogleMap googleMap) {
            try {
                // Customise the styling of the base map using a JSON object defined
                // in a raw resource file.
                boolean success = googleMap.setMapStyle(
                        MapStyleOptions.loadRawResourceStyle(
                                getContext(), R.raw.style_json));

                if (!success) {
                    Log.e("show map", "Style parsing failed.");
                }
            } catch (Resources.NotFoundException e) {
                Log.e("show map", "Can't find style. Error: ", e);
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Constants.CARBALLO, 15));
            googleMap.getUiSettings().setZoomControlsEnabled(true);
            googleMap.setPadding(0,0,170, 0);

            for(Rotonda r : DataBaseHelper.getInstance(getContext()).loadAllRotondas()){
                googleMap.addMarker(new MarkerOptions()
                        .position(new LatLng(r.getLat(), r.getLng()))
                        .icon(Util.bitmapDescriptorFromVector(getActivity(), R.mipmap.ic_rotondeando_round))
                        .title(r.getName()));
            }

        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


}