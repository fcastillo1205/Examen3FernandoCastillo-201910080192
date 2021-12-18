package com.example.examen3fernandocastillo_201910080192.Configuracion;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteConexion extends SQLiteOpenHelper {
    public SQLiteConexion(Context context, String dbname, SQLiteDatabase.CursorFactory factory, int version){
        //constructor de clase  cracion en la base de datos de SQLite
        super(context, dbname, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //creacion de la primera tabla de bd
        db.execSQL(Transacciones.CreateTableMedicamentos);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        //eliminacion de las tablas
        db.execSQL(Transacciones.DROPTableMedicamentos);
        onCreate(db);
    }
}
