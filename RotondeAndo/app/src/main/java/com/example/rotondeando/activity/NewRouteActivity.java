package com.example.rotondeando.activity;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.rotondeando.R;
import com.example.rotondeando.database.DataBaseHelper;
import com.example.rotondeando.databinding.ActivityNewRouteBinding;
import com.example.rotondeando.fragment.NewRouteFragment;
import com.example.rotondeando.model.Rotonda;
import com.example.rotondeando.model.Route;
import com.google.android.gms.maps.GoogleMap;

import java.util.ArrayList;

public class NewRouteActivity extends AppCompatActivity implements NewRouteFragment.OnVarChangedFromFragment {
    private static final int CODE_SELECT = 1;
    private static final String KEY_ROTONDAS = "rotondas";
    ActivityNewRouteBinding binding;
    GoogleMap mMap;
    String id_rotondas = "";
    int numRotondas = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNewRouteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        init();
        isChecked();
    }


    private void init() {
        binding.spWalk.setEnabled(false);
        binding.spBike.setEnabled(false);
        binding.spCar.setEnabled(false);

        ArrayList<Rotonda> rotondas = DataBaseHelper.getInstance(getApplicationContext()).loadAllRotondas();
        Bundle dataRoute = new Bundle();
        dataRoute.putSerializable(KEY_ROTONDAS, rotondas);
        NewRouteFragment fragment = new NewRouteFragment();
        fragment.setArguments(dataRoute);
        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.new_mapView, fragment)
                .commit();
    }


    private void isChecked() {
        binding.cbxWalk.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(binding.cbxWalk.isChecked())
                    binding.spWalk.setEnabled(true);
                if(!binding.cbxWalk.isChecked())
                    binding.spWalk.setEnabled(false);
            }
        });

        binding.cbxBike.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(binding.cbxBike.isChecked())
                    binding.spBike.setEnabled(true);
                if(!binding.cbxBike.isChecked())
                    binding.spBike.setEnabled(false);
            }
        });

        binding.cbxCar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(binding.cbxCar.isChecked())
                    binding.spCar.setEnabled(true);
                if(!binding.cbxCar.isChecked())
                    binding.spCar.setEnabled(false);
            }
        });

      /*  binding.btAddPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,getString(R.string.selectImg)), CODE_SELECT);
            }
        });*/
    }



    public void onClick(View view) {
        switch (view.getId()){
            case R.id.iv_back:
                dialogSave();
                break;
            case R.id.iv_save:
                checkInputs();
        }
    }

    private void checkInputs() {
        boolean isEmpty = true;
        if(binding.newNameRoute.getText().toString() != null && !binding.newNameRoute.getText().toString().isEmpty()){
            isEmpty = false;
        }

        if(isEmpty){
            Toast.makeText(getApplicationContext(), R.string.isEmptyTitle, Toast.LENGTH_LONG).show();
        }else {
            if(numRotondas <= 1){
                Toast.makeText(getApplicationContext(), R.string.selectMoreRotondas, Toast.LENGTH_LONG).show();
            }else{
                dialogPictures();
            }
        }
    }

    private void dialogPictures() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.drawable.addpicture);
        builder.setTitle("¿Queres engadir algunha imaxe da ruta?");
        builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                saveRoute();
                finish();
            }
        });
        builder.create().show();
    }


    private void dialogSave() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("¿Quere sair sen gardar a nova ruta?");//Cambiar estilo
        builder.setPositiveButton(R.string.acept, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                onBackPressed();
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).create().show();
    }

    public void saveRoute(){
        Route newRoute = new Route();
        newRoute.setName(binding.newNameRoute.getText().toString());
        newRoute.setDescription(binding.etDescription.getText().toString());
        newRoute.setFromWalk(binding.cbxWalk.isChecked());
        newRoute.setFromBike(binding.cbxBike.isChecked());
        newRoute.setFromCar(binding.cbxCar.isChecked());
        newRoute.setLevelWalk(binding.spWalk.getSelectedItemPosition()+1);
        newRoute.setLevelBike(binding.spBike.getSelectedItemPosition()+1);
        newRoute.setLevelCar(binding.spCar.getSelectedItemPosition()+1);
        newRoute.set_idRotondas(id_rotondas);

        DataBaseHelper.getInstance(getApplicationContext()).addNewRoute(newRoute);
    }



   /* @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String path;
        switch (requestCode){
            case CODE_SELECT:
                if(data.getClipData() != null) {
                    int count = data.getClipData().getItemCount(); //evaluate the count before the for loop --- otherwise, the count is evaluated every loop.
                    for(int i = 0; i < count; i++){
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        //Environment.get
                    }
                    //do something with the image (save it to some directory or whatever you need to do with it here)
                } else if(data.getData() != null) {
                     String imagePath = data.getData().getPath();
            //do something with the image (save it to some directory or whatever you need to do with it here)
        }
                break;
        }
    }*/
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            dialogSave();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onChangeVar(String id_RotondasAdded, int rotondasAdded) {
        id_rotondas = id_RotondasAdded;
        numRotondas = rotondasAdded;
    }
}