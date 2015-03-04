package com.models;

import android.content.ContentValues;
import android.database.Cursor;

/**
 * Esta clase es una clase abstracta encargada de la estructura de todos los objetos del modelo que hacen referencia a una tabla de la base de datos.
 *
 * Una clase entitie solo puede contener las variables privadas en alucion a las columnas de su tabla, variables de control, y metodos que administren el objeto como entidad.
 * No puede tener metodos que administren varias entidades (ejemplo: orderByDate(ArrayList entities) ) o que gestionen conexiones a bases de datos (ejemplo: getByDate(date) )
 * Estos metodos tendran que ser manejados o bien por un controlador, o bien por un Manager de la entidad (EntitieManager) en el modelo en caso de necesitar varias conexiones.
 *
 * Los entitie managers controlan y gestionan conexiones a bases de datos de una entidad especifica en caso de necesitar metodos especificos para la entidad, por ejemplo:
 *          obtener todas las notas por fecha.
 *          obtener todas las notas favoritas.
 *          obtener todas las notas padres.
 * En ese caso se crea un EntitieManager  (NotasManager) para gestionar estos metodos personalizados.
 * Estas deben contener una variable DBManager privada e instanciarla en el constructor de la misma.
 *
 * @author Carlos Herrera
 *
 * @version 1.50, 17/02/2015
 *
 */
public abstract class Entitie{
    private String tableName; // se define el nombre de la tabla a la que la entidad hace referencia
    private String[] columnNames; //Se escribe un arreglo de los nombres de las columnas en la base de datos incluyendo la columna id {"id", "name", "desc"},
                                  // ademas cada clase extendida debe tener variables privadas correspondientes a las columnas, exceptuando la variable id que ya esta declarada en esta clase (Entitie)
    private int id; // el id de la entidad, si la entidad es nueva y no ha sido insertada a la base de datos, su valor debe ser -1

    public Entitie(){

    }

    protected Entitie(int id, String tableName, String[] columnNames) {
        this.id = id;
        this.tableName = tableName;
        this.columnNames = columnNames;
    }

    protected Entitie(String tableName, String[] columnNames) {
        this.columnNames = columnNames;
        this.tableName = tableName;
        this.id = -1;
    }


    /*
    *   getContentValues retorna un ContentValues
    *   El contentValues es un coontenedor de relacion nombre de la columna con el valor que tiene
    *   values = new ContentValue();
    *   values.put(String ColumnName, Obj Value);
    *
    *   Debe crearse con el String[] columnNames y las diferentes variables privadas de la entidad
    *
    *   EL CONTENT VALUES NO DEBE CONTENER EL ID, NI EL NOMBRE DE LA COLUMNA NI SU VALOR
    *   ya que Android se encarga de darle el valor.
     */
    public abstract ContentValues getContentValues();




    /*
    * setContentValues se encarga de poner valores a las variables privadas dadas por un resultado
    * de base de datos.
    *
    * recibe un Cursor un objeto que contiene los resultados de una query en un arreglo
    *
    * title = cursor.getString(0);
    * number = cursor.getInt(1);
    *
    * El arreglo viene en el mismo orden con el cual se defina el arreglo String[] columnNames
     */
    public abstract void setContentValues(Cursor cursor);




    /*
    * Retorna un nuevo objeto de la misma instancia;
    * si la clase es:
    * public class Nota extends Entitie
    *
    * el metodo retornara:
    * return new Nota();
     */
    public abstract Entitie getNewInstance();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String[] getColumnNames() {
        return columnNames;
    }

    public void setColumnNames(String[] columnNames) {
        this.columnNames = columnNames;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
