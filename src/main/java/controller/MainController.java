package controller;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Clase que permite realizar una serie de acciones generales sobre la BBDD.
 */
public class MainController {
    /**
     * Función que permite seleccionar el archivo CSV que contiene los datos de una entidad.
     *
     * @param name Nombre del documento del cuál se quiere listar su contenido.
     * @return Lista de arrays de Strings, cada array es una entidad.
     */
    public static List<String[]> GetDataFromCSV(String name){
        List<String[]> entities = null;
        try {
            CSVReader reader = new CSVReader(new FileReader("src/main/resources/"+name+".csv"));
            entities = reader.readAll();
            entities.remove(0);
        } catch (IOException | CsvException e) {
            System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
        }
        return entities;
    }

    /**
     * Función que permite crea las tablas y relaciones entre ellas en la BBDD.
     *
     * @param c Elemento de conexión con la BBDD.
     */
    public static void restartDB(Connection c){
        try {
            Statement st = c.createStatement();
            BufferedReader br = new BufferedReader(new FileReader("src/main/resources/schema.sql"));
            st.execute(br.lines().collect(Collectors.joining(" \n")));
        } catch (FileNotFoundException | SQLException e) {
            System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
        }
    }
}
