package com.example.rotondeando.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.rotondeando.R;
import com.example.rotondeando.activity.ShowRouteActivity;
import com.example.rotondeando.adapter.RecyclerItemTouchHelper;
import com.example.rotondeando.adapter.RoutesAdapter;
import com.example.rotondeando.database.DataBaseHelper;
import com.example.rotondeando.databinding.FragmentRotondeandoBinding;
import com.example.rotondeando.model.Route;
import com.example.rotondeando.ui.main.PageViewModel;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A placeholder fragment containing a simple view.
 */
public class RotondeandoFragment extends Fragment implements RecyclerItemTouchHelper.RecyclerItemTouchHelperListener {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private static final String KEY_RUTA = "ruta";

    private PageViewModel pageViewModel;
    FragmentRotondeandoBinding binding;
    ArrayList<Route> listRoutes;
    RoutesAdapter adapter;
    public RotondeandoFragment(){}

    public static RotondeandoFragment newInstance() {
        RotondeandoFragment fragment = new RotondeandoFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = new ViewModelProvider(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }


    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        binding = FragmentRotondeandoBinding.inflate(getLayoutInflater());
        return binding.recyclerRoutes;
    }



    private void loadListRoutes(){
        listRoutes = DataBaseHelper.getInstance(getContext()).loadAllRoutes();
        Log.e("listRoutes", listRoutes.toString());
        LinearLayoutManager lm = new LinearLayoutManager(getContext());
        binding.recyclerRoutes.setLayoutManager(lm);
        binding.recyclerRoutes.setHasFixedSize(true);

        adapter = new RoutesAdapter(getContext(), listRoutes, new RoutesAdapter.ListItemClickListener() {
            @Override
            public void onListItemClick(int clickedItemIndex) {
                goToRoute(clickedItemIndex);
            }
        });

        binding.recyclerRoutes.setAdapter(adapter);
        ItemTouchHelper.SimpleCallback simpleCallback =
                new RecyclerItemTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.recyclerRoutes);
    }


    private void goToRoute(int clickedItemIndex) {
        Intent i = new Intent(getContext(), ShowRouteActivity.class);
        Bundle b = new Bundle();
        b.putSerializable(KEY_RUTA, listRoutes.get(clickedItemIndex));
        i.putExtras(b);
        startActivity(i);
    }


    @Override
    public void onSwipe(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof RoutesAdapter.ListRoutesViewHolder){
            showUndo(viewHolder.getAdapterPosition(), listRoutes.get(viewHolder.getAdapterPosition()));
            DataBaseHelper.getInstance(getContext()).deleteRoute(listRoutes.get(viewHolder.getAdapterPosition()).get_id());
            listRoutes.remove(viewHolder.getAdapterPosition());
            adapter.notifyDataSetChanged();

        }
    }

    private void showUndo(int i, Route route) {
        Snackbar snackbar = Snackbar.make(binding.recyclerRoutes, "", BaseTransientBottomBar.LENGTH_LONG);
        snackbar.setAction(R.string.undo, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DataBaseHelper.getInstance(getContext()).addNewRoute(route);
                listRoutes.add(i, route);
                adapter.notifyDataSetChanged();
            }
        });
        snackbar.show();
    }

    @Override
    public void onStart() {
        super.onStart();
        loadListRoutes();
    }

    @Override
    public void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

}