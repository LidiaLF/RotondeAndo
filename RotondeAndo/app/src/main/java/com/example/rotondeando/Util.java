package com.example.rotondeando;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.rotondeando.database.DataBaseHelper;
import com.example.rotondeando.model.Punto;
import com.example.rotondeando.model.Rotonda;
import com.example.rotondeando.model.Route;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Util {
    private static Object jsonObjectRequest;

    public static boolean parseBoolean(int num){
        if(num == 0)
            return  false;
        if (num == 1)
            return true;
        else
            return false;
    }

    public static int getDrawableInfo(Route route, int i){
        if (i == 1) {
            if (route.isFromWalk()) {
                switch (route.getLevelWalk()) {
                    case 1:
                        return R.drawable.outline_hiking_24_green;
                    case 2:
                        return R.drawable.outline_hiking_24_yellow;
                    case 3:
                        return R.drawable.outline_hiking_24_red;
                    default:
                        return R.drawable.outline_hiking_black_24;
                }
            } else {
                return 0;
            }
        }

           if(i == 2) {
               if (route.isFromBike()) {
                   switch (route.getLevelBike()) {
                       case 1:
                           return R.drawable.outline_pedal_bike_24_green;
                       case 2:
                           return R.drawable.outline_pedal_bike_24_yellow;
                       case 3:
                           return R.drawable.outline_pedal_bike_24_red;
                       default:
                           return R.drawable.outline_pedal_bike_black_24;
                   }
               } else {
                   return 0;
               }
           }

            if(i == 3) {
                if (route.isFromCar()) {
                    switch (route.getLevelCar()) {
                        case 1:
                            return R.drawable.outline_directions_car_24_green;
                        case 2:
                            return R.drawable.outline_directions_car_24_yellow;
                        case 3:
                            return R.drawable.outline_directions_car_24_red;
                        default:
                            return R.drawable.outline_directions_car_black_24;
                    }
                } else {
                    return 0;
                }
            }

        return i;
    }


    public static BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static boolean checkFav(TextView view, Context context, int id, boolean isFav) {
        if (isFav) {
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star, 0);
            DataBaseHelper.getInstance(context).editFav(id, 0);
            return false;
        } else {
            view.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_up, 0);
            DataBaseHelper.getInstance(context).editFav(id, 1);
            return true;
        }
    }

}
