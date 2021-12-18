package com.example.examen3fernandocastillo_201910080192;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.examen3fernandocastillo_201910080192.Configuracion.SQLiteConexion;
import com.example.examen3fernandocastillo_201910080192.Configuracion.Transacciones;
import com.example.examen3fernandocastillo_201910080192.Tablas.Medicamentos;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityModificar extends AppCompatActivity {
    ImageView imgModificar;
    EditText txtModificarId,txtModificarDescripcion,txtModificarCantidad,txtModificarPeriodicidad;
    Spinner spModificarTiempo;
    String opciones[] = {"Horas", "Diaria"};
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PETICION_ACCESO_CAN = 101;
    String currentPhotoPath;
    Uri fotoUri;
    SQLiteConexion conexion;
    Bitmap bitmap, bitmap2;
    Button btnModificar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modificar);
        conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);

        imgModificar = (ImageView) findViewById(R.id.imgModificar);
        txtModificarId = (EditText) findViewById(R.id.txtModificarId);
        txtModificarDescripcion = (EditText) findViewById(R.id.txtModificarDescripcion);
        txtModificarCantidad = (EditText) findViewById(R.id.txtModificarCantidad);
        spModificarTiempo = (Spinner) findViewById(R.id.spModificarTiempo);
        txtModificarPeriodicidad = (EditText) findViewById(R.id.txtModificarPeriodicidad);

        txtModificarId.setEnabled(false);

        spModificarTiempo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if(spModificarTiempo.getSelectedItem().toString().equals("Horas")){
                    txtModificarPeriodicidad.setEnabled(true);
                }else{
                    txtModificarPeriodicidad.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                txtModificarPeriodicidad.setEnabled(false);
            }

        });

        ArrayAdapter<String> spinnerArrayAdapterTiempo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinnerArrayAdapterTiempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spModificarTiempo.setAdapter(spinnerArrayAdapterTiempo);


        Medicamentos medicamentos = (Medicamentos) getIntent().getExtras().getSerializable("object");

        byte[] blob =medicamentos.getImagen();
        ByteArrayInputStream bais = new ByteArrayInputStream(blob);
        bitmap = BitmapFactory.decodeStream(bais);

        imgModificar.setImageBitmap(bitmap);
        txtModificarId.setText(medicamentos.getId_Medicamento().toString());
        txtModificarDescripcion.setText(medicamentos.getDescripcion().toString());
        txtModificarCantidad.setText(medicamentos.getCantidad().toString());
        txtModificarPeriodicidad.setText(medicamentos.getPeriodicidad().toString());

        Button btnTomarFoto = (Button) findViewById(R.id.btnModificarFoto);
        btnModificar = (Button) findViewById(R.id.btnModificarUpdate);

        btnModificar.setEnabled(false);

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        btnModificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), fotoUri);
                    Actualizar();
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void permisos() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, PETICION_ACCESO_CAN);
        }else{
            tomarFoto();
        }
    }

    @Override
    public void onRequestPermissionsResult(int RequestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(RequestCode, permissions, grantResults);

        if (RequestCode == PETICION_ACCESO_CAN){
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                tomarFoto();
            }
        }else {
            Toast.makeText(getApplicationContext(), "Se necesita el permiso de camara", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE  && resultCode == RESULT_OK) {

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fotoUri);
                imgModificar.setImageBitmap(bitmap);
                btnModificar.setEnabled(true);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName, /* prefix */
                ".jpg", /* suffix */
                storageDir /* directory */);
        // Save a file: path for use with ACTION_VIEW
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private void tomarFoto(){
        Intent Intenttakephoto= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Intenttakephoto.resolveActivity(getPackageManager()) != null){
            File foto = null;
            try {
                foto = createImageFile();
            }
            catch (Exception ex){
                ex.toString();
            }
            if (foto!= null){
                fotoUri = FileProvider.getUriForFile(this, "com.example.examen3fernandocastillo_201910080192.fileprovider",foto);
                Intenttakephoto.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(Intenttakephoto, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void Actualizar() {
        ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
        bitmap2.compress(Bitmap.CompressFormat.PNG, 100 , baos);
        byte[] blob = baos.toByteArray();
        byte[] img = imageModificada(blob);


        SQLiteDatabase db = conexion.getWritableDatabase();
        String[] params = {txtModificarId.getText().toString()};

        ContentValues valores = new ContentValues();
        valores.put(Transacciones.Descripcion, txtModificarDescripcion.getText().toString());
        valores.put(Transacciones.Cantidad, txtModificarCantidad.getText().toString());
        valores.put(Transacciones.Tiempo, spModificarTiempo.getSelectedItem().toString());
        valores.put(Transacciones.Periodicidad, txtModificarPeriodicidad.getText().toString());
        valores.put(Transacciones.Imagen, img);


        if(txtModificarId.getText().toString().isEmpty()||
                txtModificarDescripcion.getText().toString().isEmpty() ||
                txtModificarCantidad.getText().toString().isEmpty() ||
                spModificarTiempo.getSelectedItem().toString().isEmpty() ||
                spModificarTiempo.getSelectedItem().toString().equals("Horas") && txtModificarPeriodicidad.getText().toString().isEmpty())
        {
            Toast.makeText(getApplicationContext(), "Debe completar todos los campos", Toast.LENGTH_LONG).show();
        }else{
            db.update(Transacciones.tablaMedicamentos, valores, Transacciones.Id_medicamento + "=?", params);
            Toast.makeText(getApplicationContext(), "Dato actualizado", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(intent);
        }
    }

    private byte[] imageModificada(byte[] imagem_img){

        while (imagem_img.length > 500000){
            Bitmap bitmap = BitmapFactory.decodeByteArray(imagem_img, 0, imagem_img.length);
            Bitmap resized = Bitmap.createScaledBitmap(bitmap, (int)(bitmap.getWidth()*0.8), (int)(bitmap.getHeight()*0.8), true);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            resized.compress(Bitmap.CompressFormat.PNG, 100, stream);
            imagem_img = stream.toByteArray();
        }
        return imagem_img;

    }
}