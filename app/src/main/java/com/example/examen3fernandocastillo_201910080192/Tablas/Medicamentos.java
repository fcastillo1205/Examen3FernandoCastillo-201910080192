package com.example.examen3fernandocastillo_201910080192.Tablas;

import java.io.Serializable;

public class Medicamentos implements Serializable {
    private Integer Id_Medicamento;
    private String Descripcion;
    private Integer Cantidad;
    private String Tiempo;
    private Integer Periodicidad;
    private byte[] Imagen;

    public Medicamentos(Integer id_Medicamento, String descripcion, Integer cantidad, String tiempo, Integer periodicidad, byte[] imagen) {
        Id_Medicamento = id_Medicamento;
        Descripcion = descripcion;
        Cantidad = cantidad;
        Tiempo = tiempo;
        Periodicidad = periodicidad;
        Imagen = imagen;
    }

    public Medicamentos() {

    }

    public Integer getId_Medicamento() {
        return Id_Medicamento;
    }

    public void setId_Medicamento(Integer id_Medicamento) {
        Id_Medicamento = id_Medicamento;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public Integer getCantidad() {
        return Cantidad;
    }

    public void setCantidad(Integer cantidad) {
        Cantidad = cantidad;
    }

    public String getTiempo() {
        return Tiempo;
    }

    public void setTiempo(String tiempo) {
        Tiempo = tiempo;
    }

    public Integer getPeriodicidad() {
        return Periodicidad;
    }

    public void setPeriodicidad(Integer periodicidad) {
        Periodicidad = periodicidad;
    }

    public byte[] getImagen() {
        return Imagen;
    }

    public void setImagen(byte[] imagen) {
        Imagen = imagen;
    }
}
