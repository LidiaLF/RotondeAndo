package com.example.rotondeando.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.rotondeando.Constants;
import com.example.rotondeando.R;
import com.example.rotondeando.Util;
import com.example.rotondeando.activity.ShowRouteActivity;
import com.example.rotondeando.database.DataBaseHelper;
import com.example.rotondeando.databinding.CardRoutesBinding;
import com.example.rotondeando.model.Route;

import java.util.ArrayList;
    /*
    *  Adapter
    * */
public class RoutesAdapter extends RecyclerView.Adapter<RoutesAdapter.ListRoutesViewHolder> {
    ArrayList<Route> listRoutes;
    final private RoutesAdapter.ListItemClickListener mOnClickListener;
    Context context;

    public RoutesAdapter(Context context, ArrayList<Route> listRoutes, RoutesAdapter.ListItemClickListener listener){
        this.listRoutes = listRoutes;
        this.context = context;
        mOnClickListener = listener;
    }

    public interface ListItemClickListener{
        void onListItemClick(int clickedItemIndex);
    }

    @NonNull
    @Override
    public ListRoutesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ListRoutesViewHolder(CardRoutesBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ListRoutesViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return listRoutes.size();
    }


    /*
    *  RecyclerView
    * */
    public class ListRoutesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        CardRoutesBinding binding;
        public RelativeLayout layoutDelete;

        ListRoutesViewHolder(CardRoutesBinding b) {
            super(b.getRoot());
            binding = b;
            b.getRoot().setOnClickListener(this);
            layoutDelete = binding.layoutDelete;
        }

        //Enlaza os datos coa vista
        public void bind(int listIndex){
            loadData(listIndex);
        }

        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(clickedPosition);
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }



        private void loadData(int listIndex) {
            try {
                Route route = listRoutes.get(listIndex);
                if (route.isFav()) {
                    binding.tvFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star_up, 0);
                } else {
                    binding.tvFav.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_star, 0);
                }

                binding.tvNameRoute.setText(route.getName());
                binding.tvKm.setText(route.getKm() + " km");
                binding.ivLevelWalk.setCompoundDrawablesWithIntrinsicBounds(0, 0, Util.getDrawableInfo(route, 1), 0);
                binding.ivLevelBike.setCompoundDrawablesWithIntrinsicBounds(0, 0, Util.getDrawableInfo(route, 2), 0);
                binding.ivLevelCar.setCompoundDrawablesWithIntrinsicBounds(0, 0, Util.getDrawableInfo(route, 3), 0);


                binding.tvFav.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        route.setFav(Util.checkFav(binding.tvFav, context, route.get_id(), route.isFav()));
                    }
                });

            }catch (NullPointerException e){
                binding.tvKm.setText("- km");
                binding.tvNameRoute.setText("-");
                Log.e("ERRO RoutesADAPTER", e.getMessage());
            }
        }

    }
    }