package com.example.rotondeando.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.rotondeando.adapter.RecyclerItemTouchHelper;
import com.example.rotondeando.adapter.RoutesAdapter;
import com.example.rotondeando.database.DataBaseHelper;
import com.example.rotondeando.databinding.ActivityRotondeandoBinding;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.View;

import com.example.rotondeando.ui.main.SectionsPagerAdapter;

public class RotondeandoActivity extends AppCompatActivity{
    ActivityRotondeandoBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRotondeandoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        binding.viewPager.setAdapter(sectionsPagerAdapter);
        binding.tabs.setupWithViewPager(binding.viewPager);
        DataBaseHelper dbHelper = DataBaseHelper.getInstance(RotondeandoActivity.this);
        try {
            dbHelper.createDataBase();
        } catch (Exception e) {
            Log.e(DataBaseHelper.class.getSimpleName(), "Cant create or get database ");
        }

        binding.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(RotondeandoActivity.this, NewRouteActivity.class);
                startActivity(i);
            }
        });
    }


}