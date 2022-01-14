package com.example.rotondeando.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabaseLockedException;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.rotondeando.BuildConfig;
import com.example.rotondeando.Constants;
import com.example.rotondeando.Util;
import com.example.rotondeando.model.Rotonda;
import com.example.rotondeando.model.Route;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.ResultSet;
import java.util.ArrayList;

import javax.xml.transform.Result;

public class DataBaseHelper extends SQLiteOpenHelper {

    private static final String TAG = DataBaseHelper.class.getSimpleName();
    private static final String DB_NAME = "rotondeando.db";
    private static final String C_ISFAV = "isFav";
    private static final String T_ROTONDA = "rotonda";
    private static final String T_RUTA = "ruta";
    private static String DB_PATH;
    private static final int DB_VERSION = 1;
    private static SQLiteDatabase m_database;
    private final Context m_context;
    private static DataBaseHelper mInstance;

    public static synchronized DataBaseHelper getInstance(final Context context) {
        if (mInstance == null) {
            mInstance = new DataBaseHelper(context);
        }
        return mInstance;
    }

    /**
     * Constructor Takes and keeps a reference of the passed context in order to access to the application assets and resources.
     *
     * @param context
     */
    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.m_context = context;
    }

    /**
     * Creates an empty database on the system and rewrites it with your own database.
     */
    public void createDataBase() throws IOException {
        DB_PATH = m_context.getDatabasePath(DB_NAME).getAbsolutePath();
        boolean dbExist = checkDataBase();
        if (dbExist) {
            Log.e("DB", "Database exists");
        }

        if (!dbExist) {
            try {
                copydatabase();
            } catch (IOException e) {
                Log.e("DB", e.getMessage());
                throw new Error("Error copying database");
            }
        }
       // m_database.execSQL(RUTAS_CREATE_TABLE);
        //addLugaresFav(new Lugar("27400", 42.52165, -7.51422, "Monforte"));
    }

    /**
     * Check if the database already exist to avoid re-copying the file each time you open the application.
     *
     * @return true if it exists, false if it doesn't
     */
    private boolean checkDataBase() {
        File dbFile = m_context.getDatabasePath(DB_NAME);
        return dbFile.exists();
    }

    /**
     * Copies your database from your local assets-folder to the just created empty database in the system folder, from where it can be accessed and handled. This is done by transferring byte stream.
     */
    private void copydatabase() throws IOException {
        //Open your local db as the input stream
        InputStream myinput = m_context.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outfilename = DB_PATH;

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer)) > 0) {
            myoutput.write(buffer, 0, length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
        checkDataBase();
    }

    @Override
    public synchronized void close() {
        if (m_database != null) m_database.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        m_database = db;
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }


    public  ArrayList<Rotonda> loadAllRotondas(){
        SQLiteDatabase db= getReadableDatabase();
        ArrayList<Rotonda> listRotondas = new ArrayList<>();
        try{
        Cursor datosConsulta = db.rawQuery("SELECT * FROM rotonda ORDER BY _id DESC", null);
        if (datosConsulta.moveToFirst()) {
            Rotonda rotonda;
            while (!datosConsulta.isAfterLast()) {
                rotonda = new Rotonda(datosConsulta.getInt(0),
                        datosConsulta.getDouble(1),
                        datosConsulta.getDouble(2),
                        datosConsulta.getString(3));
                listRotondas.add(rotonda);
                datosConsulta.moveToNext();
                System.out.println(rotonda);
            }
        }}catch (NullPointerException e){
            Log.e("ERROR BD", e.getMessage());
        }
        return listRotondas;
    }


    public ArrayList<Route> loadAllRoutes(){
        SQLiteDatabase db= getReadableDatabase();
        ArrayList<Route> listRoutes = new ArrayList<>();
        try{
        Cursor datosConsulta = db.rawQuery("SELECT * FROM ruta ORDER BY isFav DESC, _id ASC", null);
        if (datosConsulta.moveToFirst()) {
            Route route;

            while (!datosConsulta.isAfterLast()) {
                route = new Route();
                route.set_id(datosConsulta.getInt(0));
                route.setName(datosConsulta.getString(1));
                route.setDescription(datosConsulta.getString(2));
                route.setKm(datosConsulta.getDouble(3));
                route.setFromWalk(Util.parseBoolean(datosConsulta.getInt(4)));
                route.setFromBike(Util.parseBoolean(datosConsulta.getInt(5)));
                route.setFromCar(Util.parseBoolean(datosConsulta.getInt(6)));
                route.setLevelWalk(datosConsulta.getInt(7));
                route.setLevelBike(datosConsulta.getInt(8));
                route.setLevelCar(datosConsulta.getInt(9));
                route.setFav(Util.parseBoolean(datosConsulta.getInt(10)));
                route.setDeleted(Util.parseBoolean(datosConsulta.getInt(11)));
                route.set_idRotondas(datosConsulta.getString(12));
                listRoutes.add(route);
                datosConsulta.moveToNext();
            }
        }}catch (NullPointerException e){
            Log.e("ERRO NA BD RUTAS", e.getMessage());
        }
        return listRoutes;

    }


    public void editFav(int id, int isFav){
        SQLiteDatabase db= getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(Constants.R_ROUTE_ISFAV, isFav);
        int rs = db.update(T_RUTA, cv, "_id =?", new String[]{String.valueOf(id)});

    }


    public void addNewRoute(Route r){
        SQLiteDatabase db= getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(Constants.R_ROUTE_NAME, r.getName());
        cv.put(Constants.R_ROUTE_DESC, r.getDescription());
        cv.put(Constants.R_ROUTE_FWALK, r.isFromWalk());
        cv.put(Constants.R_ROUTE_FBIKE, r.isFromBike());
        cv.put(Constants.R_ROUTE_FCAR, r.isFromCar());
        cv.put(Constants.R_ROUTE_ISDELETED, r.isDeleted());
        cv.put(Constants.R_ROUTE_ISFAV, r.isFav());
        cv.put(Constants.R_ROUTE_LWALK, r.getLevelWalk());
        cv.put(Constants.R_ROUTE_LBIKE, r.getLevelBike());
        cv.put(Constants.R_ROUTE_LCAR, r.getLevelCar());
        cv.put(Constants.R_ROUTE_KM, r.getKm());
        cv.put(Constants.R_ROUTE_IDROTONDAS, r.get_idRotondas());

        db.insert(T_RUTA, null, cv);
    }

    public void deleteRoute(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(T_RUTA, "_id=?", new String[]{Integer.toString(id)});
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    private void closeTsDb(SQLiteDatabase db) {
        if (db.isOpen()) {
            try {
                db.endTransaction();
            } catch (Exception e) {
                //do nothing
            }
        }
    }

    public void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) cursor.close();
    }

    public static synchronized SQLiteDatabase getSafeDatabase() {
        return m_database;
    }

}

