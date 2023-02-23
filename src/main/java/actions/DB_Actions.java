package actions;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *  Clase en la que hay funciones estáticas para acciones relacionadas con la BBDD en general
 *  (Crear la BBDD, rellenarla o mostrar las cabeceras de una tabla)
 */
public class DB_Actions {
    /**
     * Función que ejecuta las sentencias SQL de creación de BBDD almacenadas en el archivo "src/data/schema.sql".
     *
     * @param c Objeto de la conexión con la BBDD.
     */
    public static void restart(Connection c){
        try {
            Statement st = c.createStatement();
            BufferedReader br = new BufferedReader(new FileReader("src/data/schema.sql"));
            st.execute(br.lines().collect(Collectors.joining(" \n")));
        } catch (FileNotFoundException | SQLException e) {
            System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
        }
    }

    /**
     * Función que permite rellenar las tablas principales de la BBDD.
     *
     * @param c Objeto de la conexión con la BBDD.
     * @param tabla Nombre de la tabla la cuál se quiere rellenar.
     */
    public static void FillTable(Connection c, String tabla){
        String values = "";
        List<String> header;
        List<String> data_types;
        List<String[]> data = GetDataFromCSV(tabla);

        try {
            List<List<String>> columnMetaData = GetHeader(c,tabla);
            header = columnMetaData.get(0);
            data_types = columnMetaData.get(1);

            for (int i = 0; i < header.size(); i++) {
                if(i == 0) values = values.concat("(?, ");
                else if(i+1 == header.size()) values = values.concat("?)");
                else values = values.concat("?, ");
            }

            String insert = "INSERT INTO "+tabla+"("+header.toString().substring(1,header.toString().length()-1)+") VALUES "+values+";";
            PreparedStatement ps_insert = c.prepareStatement(insert);

            for (int i = 0; i < data.size(); i++) {
                String[] fields = data.get(i);
                try {
                    for (int j = 0; j < fields.length; j++) {
                        if(data_types.get(j).equals("integer")){
                            ps_insert.setInt(j+1, Integer.parseInt(fields[j]));
                        }else{
                            ps_insert.setString(j+1,fields[j]);
                        }
                    }
                    System.out.println(ps_insert);
                    ps_insert.execute();
                } catch (SQLException e) {
                    System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
                }
            }
        } catch (SQLException e) {
            System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
        }
    }

    /**
     * Función que devuelve dos listas, una con los nombres de las columnas y otra
     * con los tipos de dato de cada una de ellas en la BBDD.
     *
     * @param c Objeto de la conexión con la BBDD.
     * @param tabla Nombre de la tabla la cuál se quieren listar sus columnas.
     * @return Retorna una lista con las dos listas de datos mencionadas en la descripción de la función.
     */
    public static List<List<String>> GetHeader(Connection c, String tabla) {
        List<String> header = new ArrayList<>();
        List<String> data_types = new ArrayList<>();
        try {
            String query = "SELECT column_name, data_type FROM information_schema.columns WHERE table_name = ? ORDER BY ordinal_position;";
            PreparedStatement ps_header = c.prepareStatement(query);
            ps_header.setString(1,tabla);
            ResultSet resultSet = ps_header.executeQuery();

            while (resultSet.next()) {
                header.add(resultSet.getString(1));
                data_types.add(resultSet.getString(2));
            }
        } catch (SQLException e) {
            System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
        }
        return List.of(header, data_types);
    }

    /**
     * Función que permite seleccionar el archivo CSV que contiene los datos de relleno de una tabla.
     *
     * @param tabla Nombre de la tabla la cuál se quieren listar sus columnas.
     * @return Lista de arrays de Strings, cada array es una fila de la BBDD.
     */
    public static List<String[]> GetDataFromCSV(String tabla){
        List<String[]> entities = null;
        try {
            CSVReader reader = new CSVReader(new FileReader("src/data/"+tabla+".csv"));
            entities = reader.readAll();
            entities.remove(0);
        } catch (IOException | CsvException e) {
            System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
        }
        return entities;
    }
}