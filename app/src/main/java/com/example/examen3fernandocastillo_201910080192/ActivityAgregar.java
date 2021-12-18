package com.example.examen3fernandocastillo_201910080192;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ActivityAgregar extends AppCompatActivity {
    EditText txtAgregarDescripcion, txtAgregarCantidad, txtAgregarPeriodicidad;
    Spinner spAgregarTiempo;
    ImageView imgAgregarMedicamento;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int PETICION_ACCESO_CAN = 101;
    String currentPhotoPath;
    Uri fotoUri;
    String opciones[] = {"Horas", "Diaria"};
    Button btnGuardar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar);

        txtAgregarDescripcion = (EditText) findViewById(R.id.txtAgregarDescripcion);
        txtAgregarCantidad = (EditText) findViewById(R.id.txtAgregarCantidad);
        spAgregarTiempo = (Spinner) findViewById(R.id.spAgregarTiempo);
        txtAgregarPeriodicidad = (EditText) findViewById(R.id.txtAgregarPeriodicidad);
        imgAgregarMedicamento = (ImageView) findViewById(R.id.imgAgregarMedicamento);

        spAgregarTiempo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (spAgregarTiempo.getSelectedItem().toString().equals("Horas")) {
                    txtAgregarPeriodicidad.setEnabled(true);
                } else {
                    txtAgregarPeriodicidad.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                txtAgregarPeriodicidad.setEnabled(false);
            }
        });

        ArrayAdapter<String> spinnerArrayAdapterTiempo = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, opciones);
        spinnerArrayAdapterTiempo.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAgregarTiempo.setAdapter(spinnerArrayAdapterTiempo);

        Button btnFoto = (Button) findViewById(R.id.btnAgregarFoto);
        btnGuardar = (Button) findViewById(R.id.btnAgregarGuardar);
        btnGuardar.setEnabled(false);

        btnFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        btnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bitmap bitmap;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fotoUri);
                    guardarImagen(txtAgregarDescripcion.getText().toString(),
                            txtAgregarCantidad.getText().toString(),
                            spAgregarTiempo.getSelectedItem().toString(),
                            txtAgregarPeriodicidad.getText().toString(),
                            bitmap);
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
            Toast.makeText(getApplicationContext(), "Es necesario el acceso a la camera", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_IMAGE_CAPTURE  && resultCode == RESULT_OK) {

            Bitmap bitmap;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), fotoUri);
                imgAgregarMedicamento.setImageBitmap(bitmap);
                btnGuardar.setEnabled(true);
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
        Intent takeFoto= new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takeFoto.resolveActivity(getPackageManager()) != null){
            File foto = null;
            try {
                foto = createImageFile();
            }
            catch (Exception ex){
                ex.toString();
            }
            if (foto!= null){
                fotoUri = FileProvider.getUriForFile(this, "com.example.examen3fernandocastillo_201910080192.fileprovider",foto);
                takeFoto.putExtra(MediaStore.EXTRA_OUTPUT, fotoUri);
                startActivityForResult(takeFoto, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private void guardarImagen(String Descripcion, String Cantidad, String Tiempo, String Periodicidad,Bitmap bitmap) {
        if(Descripcion.equals("")){
            Toast.makeText(getApplicationContext(), "Complete el campo de descripcion", Toast.LENGTH_LONG).show();
        }else if(Cantidad.equals("")){
            Toast.makeText(getApplicationContext(), "Complete el campo de Cantidad", Toast.LENGTH_LONG).show();
        }else if(Tiempo.equals("")){
            Toast.makeText(getApplicationContext(), "Complete el campo de Tiempo", Toast.LENGTH_LONG).show();
        }else if(Tiempo.equals("Horas") && Periodicidad.equals("")){
            Toast.makeText(getApplicationContext(), "Complete el campo de Periodicidad", Toast.LENGTH_LONG).show();
        } else {
            SQLiteConexion conexion = new SQLiteConexion(this, Transacciones.NameDatabase, null, 1);
            SQLiteDatabase db = conexion.getWritableDatabase();

            ByteArrayOutputStream baos = new ByteArrayOutputStream(20480);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] blob = baos.toByteArray();
            byte[] imgInsert = imageModificada(blob);

            String sql = "INSERT INTO Medicamentos (Descripcion,Cantidad, Tiempo, Periodicidad, Imagen) VALUES(?,?,?,?,?)";
            SQLiteStatement insert = db.compileStatement(sql);
            insert.clearBindings();
            insert.bindString(1, Descripcion);
            insert.bindString(2, Cantidad);
            insert.bindString(3, Tiempo);
            insert.bindString(4, Periodicidad);
            insert.bindBlob(5, imgInsert);
            Long resultado = insert.executeInsert();
            Toast.makeText(getApplicationContext(), "Registro guardado", Toast.LENGTH_LONG).show();
            db.close();
            limpiar();
            irInicio();
        }

    }

    private void irInicio() {
        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
        startActivity(intent);
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

    private void limpiar(){
        imgAgregarMedicamento.setImageBitmap(null);
        txtAgregarDescripcion.setText(null);
        txtAgregarCantidad.setText(null);
        txtAgregarPeriodicidad.setText(null);
        btnGuardar.setEnabled(false);
    }
}