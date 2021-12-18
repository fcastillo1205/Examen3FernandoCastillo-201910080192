package com.example.examen3fernandocastillo_201910080192.Configuracion;

public class Transacciones {
    public static final String NameDatabase = "PMO1DB";
    public static final String tablaMedicamentos = "Medicamentos";


    public static final String Id_medicamento = "Id_medicamento";
    public static final String Descripcion = "Descripcion";
    public static final String Cantidad = "Cantidad";
    public static final String Tiempo = "Tiempo";
    public static final String Periodicidad = "Periodicidad";
    public static final String Imagen = "Imagen";

    public static final String CreateTableMedicamentos = "CREATE TABLE Medicamentos (Id_medicamento INTEGER PRIMARY KEY AUTOINCREMENT "+
            ",Descripcion TEXT, Cantidad INTEGER, Tiempo TEXT, Periodicidad INTEGER, Imagen BLOB )";

    public static final String DROPTableMedicamentos = "DROP TABLE IF EXISTS Medicamentos";
}
