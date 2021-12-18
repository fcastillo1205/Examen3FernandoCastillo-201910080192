package com.example.examen3fernandocastillo_201910080192;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.examen3fernandocastillo_201910080192.Configuracion.SQLiteConexion;
import com.example.examen3fernandocastillo_201910080192.Configuracion.Transacciones;
import com.example.examen3fernandocastillo_201910080192.Tablas.Medicamentos;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.Serializable;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ListView lvMainMedicamentos;
    ArrayList<Medicamentos> lst;
    SQLiteConexion conexion;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
        lvMainMedicamentos = (ListView) findViewById(R.id.lvMainMedicamentos);
        lst = new ArrayList<Medicamentos>();

        obtenerMedicamentos();

        Adaptador adaptador = new Adaptador(getApplicationContext(),lst);
        lvMainMedicamentos.setAdapter(adaptador);

        lvMainMedicamentos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Medicamentos object = (Medicamentos) adapterView.getItemAtPosition(i);

                final CharSequence[] options = { "Modificar", "Eliminar","Cancelar" };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Seleccione una opción");
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int item) {
                        if (options[item].equals("Modificar")) {
                            Intent intent = new Intent(getApplicationContext(), ActivityModificar.class);
                            intent.putExtra("object", (Serializable) object);
                            startActivity(intent);
                        } else if (options[item].equals("Eliminar")) {
                            eliminarMedicamento(object.getId_Medicamento().toString());
                        } else if (options[item].equals("Cancelar")) {
                            dialog.dismiss();
                        }
                    }
                });
                builder.show();
            }
        });

        FloatingActionButton btnAgregar = (FloatingActionButton) findViewById(R.id.btnMainAgregar);

        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityAgregar.class);
                startActivity(intent);
            }
        });

    }

    private void eliminarMedicamento(String Id) {
        SQLiteDatabase db = conexion.getWritableDatabase();
        String [] params = {Id};
        String wherecond = Transacciones.Id_medicamento + "=?";
        if(!Id.isEmpty()){
            db.delete(Transacciones.tablaMedicamentos, wherecond, params);
            Toast.makeText(getApplicationContext(), "Dato eliminado", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }else{
            Toast.makeText(getApplicationContext(), "Campo de Id Vacio", Toast.LENGTH_LONG).show();
        }
    }

    private void obtenerMedicamentos() {
        SQLiteDatabase db = conexion.getReadableDatabase();
        Medicamentos medicamentos = null;
        //lista = new ArrayList<Medicamentos>();

        //cursor de  bd: nos apoya a recorrer la informacion de la tabla a la cual consltamos
        Cursor cursor = db.rawQuery("SELECT * FROM " + Transacciones.tablaMedicamentos , null);

        //recorrer la informacion del cursor
        while (cursor.moveToNext()){
            medicamentos = new Medicamentos();
            medicamentos.setId_Medicamento(cursor.getInt( 0));
            medicamentos.setDescripcion(cursor.getString( 1));
            medicamentos.setCantidad(cursor.getInt(2));
            medicamentos.setTiempo(cursor.getString( 3));
            medicamentos.setPeriodicidad(cursor.getInt( 4));
            medicamentos.setImagen(cursor.getBlob( 5));

            lst.add(medicamentos);
        }

        cursor.close();

    }
}