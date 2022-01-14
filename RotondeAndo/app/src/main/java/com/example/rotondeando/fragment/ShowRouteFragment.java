package com.example.rotondeando.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.rotondeando.Constants;
import com.example.rotondeando.R;
import com.example.rotondeando.Util;
import com.example.rotondeando.database.DataBaseHelper;
import com.example.rotondeando.databinding.DesignRotondaInfoBinding;
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

public class ShowRouteFragment extends Fragment implements LocationListener {
    protected LocationManager locationManager;
    protected LocationListener locationListener;
    private static final String KEY_RUTA = "ruta";
    private static final int REQUEST_CAMERA = 0;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    Route route;
    Rotonda rInit;
    Snackbar snackbar;
    int i = 0;
    ArrayList<Rotonda> rotondas;
    String[] id_rotondas;
    GoogleMap map;
    Marker marker;
    DesignRotondaInfoBinding bindingSnack;

    public static ShowRouteFragment newInstance(Route route) {
        ShowRouteFragment fragment = new ShowRouteFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_RUTA, route);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true); //conserva estado de instancia
        if (getArguments() != null) {
            route = (Route) getArguments().getSerializable(KEY_RUTA);
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
                            showSnakBarInfo(marker);
                            return true;
                        }
                    });
                }
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                        new LatLng(rotondas.get(0).getLat(), rotondas.get(0).getLng()), 16));
                googleMap.getUiSettings().setZoomControlsEnabled(true);
                map = googleMap;
            }
        }
    };


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_show_route, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.show_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
            loadPoints();
        }
    }

    public void loadPoints() {
        try {
            id_rotondas = route.get_idRotondas().split("_");
            rotondas = new ArrayList<>();
            if (route.get_idRotondas() != null) {
                for (Rotonda r : DataBaseHelper.getInstance(getContext()).loadAllRotondas()) {
                    for (int i = 0; i < id_rotondas.length; i++) {
                        if (id_rotondas[i].equals(String.valueOf(r.get_id()))) {
                            rotondas.add(r);
                        }
                    }
                }
            }
        } catch (NullPointerException e) {
            Log.e("error show", e.getMessage());
        }
    }


    private void showSnakBarInfo(Marker marker) {
        snackbar = Snackbar.make(getView(), "", Snackbar.LENGTH_LONG);
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        View snackView = View.inflate(getContext(), R.layout.design_rotonda_info, null);
        bindingSnack = DesignRotondaInfoBinding.bind(snackView);
        bindingSnack.tvInfoRoute.setText(route.getName());
        bindingSnack.tvInfoRotonda.setText(marker.getTitle());
        bindingSnack.btValideRotonda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkPermissions();
            }
        });
        layout.setPadding(10, 10, 10, 20);
        layout.setBackgroundColor(Color.WHITE);
        layout.addView(snackView, 0);
        snackbar.show();
    }


/*
 COMPROBAR QUE O USUARIO SE SITUA CERCA DA UBICACION DE CADA ROTONDA NO MOMENTO DE VALIDAR


    private boolean checkUbication() {
        if (!isLocationEnabled())
            showAlert();
        return isLocationEnabled();
    }

    private boolean isLocationEnabled() {
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void showAlert() {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setTitle("Enable Location")
                .setMessage("Su ubicación esta desactivada.\npor favor active su ubicación " +
                        "usa esta app")
                .setPositiveButton("Configuración de ubicación", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                });
        dialog.show();
    }
*/
    public void openCamera(){
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (i.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(i, REQUEST_CAMERA);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == REQUEST_CAMERA ) {
            if(bindingSnack.getRoot().getParent() != null) {
                ((ViewGroup)bindingSnack.getRoot().getParent()).removeView(bindingSnack.getRoot());
            }            Log.e("foto", "sacada");
        }
    }

    public void checkPermissions() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.M) {
            int permiso0 = getActivity().checkSelfPermission(Manifest.permission.INTERNET);
            int permiso1 = getActivity().checkSelfPermission(Manifest.permission.CAMERA);
            int permiso2 = getActivity().checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE);
            int permiso3 = getActivity().checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE);
            int permiso4 = getActivity().checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (permiso0 == PackageManager.PERMISSION_GRANTED
                    & permiso1 == PackageManager.PERMISSION_GRANTED
                    & permiso2 == PackageManager.PERMISSION_GRANTED
                    & permiso3 == PackageManager.PERMISSION_GRANTED
                    & permiso4 == PackageManager.PERMISSION_GRANTED) {
                //checkUbication();
                openCamera();
            } else {
                getActivity().requestPermissions(new String[]{Manifest.permission.INTERNET, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE
                        , Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST);
            }
        } else { // Pre-Marshmallow
            //checkUbication();
            openCamera();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST :
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //checkUbication();
                    openCamera();
                } else {
                    Toast.makeText(getContext(), "NECESITANSE OS PERMISOS", Toast.LENGTH_LONG).show();
                }
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }

}