package com.example.rotondeando.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rotondeando.Constants;
import com.example.rotondeando.R;
import com.example.rotondeando.Util;
import com.example.rotondeando.model.Rotonda;
import com.example.rotondeando.model.Route;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class NewRouteFragment extends Fragment {
    private static final String KEY_ROTONDAS = "rotondas";
    ArrayList<Rotonda> rotondas;
    GoogleMap map;
    Marker marker;
    Snackbar snackbar;
    int rotondasAdded = 0;
    String id_RotondasAdded = "";
    OnVarChangedFromFragment mCallback;

    public static NewRouteFragment newInstance(Route route){
        NewRouteFragment fragment = new NewRouteFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_ROTONDAS, route);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); //conserva estado de instancia
        if(getArguments() != null){
            rotondas = (ArrayList<Rotonda>) getArguments().getSerializable(KEY_ROTONDAS);
        }

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

            if (rotondas != null) {
                for (Rotonda r : rotondas) {
                    marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(r.getLat(), r.getLng()))
                            .icon(Util.bitmapDescriptorFromVector(getActivity(), R.mipmap.ic_rotondeando_round))
                            .title(r.getName()));
                    marker.setTag(r.get_id());

                    googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
                        @Override
                        public boolean onMarkerClick(@NonNull Marker marker) {
                            addSnackbar(marker);
                            return true;
                        }
                    });
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(Constants.CARBALLO, 14));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                map = googleMap;
            }
        }
    };

    private void addSnackbar(Marker selectMarker) {
        snackbar = Snackbar.make(getView(), "¿Engadir esta rotonda á ruta?", Snackbar.LENGTH_LONG);
        snackbar.setAction("Engadir", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rotondasAdded == 0)
                     id_RotondasAdded += selectMarker.getTag() + "";
                if(rotondasAdded > 0)
                    id_RotondasAdded += "_" + selectMarker.getTag() + "";
                selectMarker.setVisible(false);
                rotondasAdded++;
                mCallback.onChangeVar(id_RotondasAdded, rotondasAdded);
            }
        });
        snackbar.show();
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_new_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.new_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }


    /*
    * INTERFAZ DE COMUNICACIÓN
    * */
    public interface OnVarChangedFromFragment {
        public void onChangeVar(String id_RotondasAdded, int rotondasAdded);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mCallback = (OnVarChangedFromFragment) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " debes implementar OnVarChangedFromFragment");
        }
    }
}