package com.example.examen3fernandocastillo_201910080192;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.examen3fernandocastillo_201910080192.Tablas.Medicamentos;

import java.io.ByteArrayInputStream;
import java.util.List;

public class Adaptador extends BaseAdapter {

    Context ctx;
    List<Medicamentos> lst;

    public Adaptador(Context ctx, List<Medicamentos> lst) {
        this.ctx = ctx;
        this.lst = lst;
    }

    @Override
    public int getCount() {
        return lst.size();
    }

    @Override
    public Object getItem(int i) {
        return lst.get(i);
    }

    @Override
    public long getItemId(int i) {
        return lst.get(i).getId_Medicamento();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vista = view;
        LayoutInflater inflater = LayoutInflater.from(ctx);

        vista = inflater.inflate(R.layout.item,null);

        ImageView img = (ImageView) vista.findViewById(R.id.imgviewItem);
        TextView descripcion = (TextView) vista.findViewById(R.id.txtItemDescripcion);
        TextView cantidad = (TextView) vista.findViewById(R.id.txtItemCantidad);
        TextView tiempo = (TextView) vista.findViewById(R.id.txtItemTiempo);
        TextView periodicidad = (TextView) vista.findViewById(R.id.txtItemPeriodicidad);

        byte[] blob =lst.get(i).getImagen();
        ByteArrayInputStream bais = new ByteArrayInputStream(blob);
        Bitmap bitmap = BitmapFactory.decodeStream(bais);

        img.setImageBitmap(bitmap);
        descripcion.setText(lst.get(i).getDescripcion().toString());
        cantidad.setText(lst.get(i).getCantidad().toString());
        tiempo.setText(lst.get(i).getTiempo().toString());
        periodicidad.setText(lst.get(i).getPeriodicidad().toString());

        return vista;
    }
}
