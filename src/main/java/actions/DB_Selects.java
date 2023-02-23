package actions;

import view.JDBC_Menu;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 *  Clase en la que hay funciones estáticas para acciones relacionadas
 *  con la selección de tablas y registros de la BBDD.
 */
public class DB_Selects {
    /**
     * Función que permite mostrar el contenido de los registros de la tabla seleccionada por el usuario.
     * Cabe mencionar que no se muestra el icono en éstos casos por temas de formateo de la consola.
     *
     * @param c Objeto de la conexión con la BBDD.
     * @param tabla Nombre de la tabla que se quiere mostrar.
     */
    public static void selectAllTable(Connection c, String tabla){
        List<List<String>> header = DB_Actions.GetHeader(c,tabla);
        try {
            ResultSet s = c.createStatement().executeQuery("SELECT * FROM "+tabla+" ORDER BY "+header.get(0).get(0));
            while(s.next()){
                for (int i = 1; i <= s.getMetaData().getColumnCount(); i++) {
                    if(!s.getMetaData().getColumnName(i).equals("icono")){
                        if(i != s.getMetaData().getColumnCount()) System.out.print(s.getMetaData().getColumnName(i) +": "+ s.getString(i) + "\t|\t");
                        else System.out.print(s.getMetaData().getColumnName(i) +": "+ s.getString(i));
                    }
                }
                System.out.println();
            }
        } catch (SQLException e) {
            System.out.println("\n**** NO SE HA PODIDO MOSTRAR LA TABLA '"+tabla+"' ****");
        }
    }

    /**
     * Función que muestra los registros de la tabla pasada por parámetro cuya información
     * en la columna indicada coincida con el valor introducido por el usuario.
     *
     * @param c Objeto de la conexión con la BBDD.
     * @param tabla Nombre de la tabla de la que se quieren mostrar registros.
     * @param columna Array que indica el nombre y tipo de dato de la columna seleccionada para comparar.
     */
    //Seleccionar todos los elementos que contengan un texto concreto.
    public static void selectSpecificText(Connection c, String tabla, String[] columna){
        Scanner sc = new Scanner(System.in);
        PreparedStatement pst;
        int col_text = 0;
        boolean out;
        try {
            if(columna[1].equals("integer")){
                pst = c.prepareStatement("SELECT * FROM "+tabla+" WHERE "+columna[0]+" = ?");
                do{
                    out = false;
                    System.out.print("Que texto quieres buscar en la columna "+columna[0]+": ");
                    try{
                        col_text = sc.nextInt();
                        out = true;
                    }catch(Exception e){
                        System.out.println("\n*** Selecciona una opción válida. ***");
                    }
                }while(out);

                pst.setInt(1, col_text);
                sc.nextLine();
            }else{
                System.out.print("Que texto quieres buscar en la columna "+columna[0]+": ");
                pst = c.prepareStatement("SELECT * FROM "+tabla+" WHERE "+columna[0]+" ILIKE ?");
                pst.setString(1, sc.nextLine());
            }

            ResultSet result = pst.executeQuery();
            System.out.println();
            while(result.next()){
                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                    System.out.println(result.getMetaData().getColumnName(i) +": "+ result.getString(i));
                }
                System.out.println("\n--------------------------------------\n");
            }
        } catch (SQLException e) {
            System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
        }
    }

    /**
     * Función que muestra los registros de la tabla pasada por parámetro cuya información
     * en la columna indicada coincida con el valor introducido por el usuario.
     * Muy similar a la función "selectSpecificText" pero contemplando otro tipo de condición.
     *
     * @param c Objeto de la conexión con la BBDD.
     * @param tabla Nombre de la tabla de la que se quieren mostrar registros.
     * @param columna Array que indica el nombre y tipo de dato de la columna seleccionada para comparar.
     */
    public static void selectByCondition(Connection c, String tabla, String[] columna) {
        Scanner sc = new Scanner(System.in);
        JDBC_Menu menu = new JDBC_Menu();
        int condition = menu.ConsultasPorCondicionesMenu();
        boolean out = false;
        int cantidad = 0;

        if(columna[1].equals("integer")){
            do{
                try{
                    System.out.print("Cuál es la cantidad que quieres filtrar: ");
                    cantidad = Integer.parseInt(sc.nextLine());
                    out = true;
                }catch(Exception e){
                    System.out.println("\n*** Selecciona una opción válida. ***");
                }
            }while(!out);
        }

        PreparedStatement pst = null;
        try{
            if(condition == 1){
                if(columna[1].equals("integer")){
                    pst = c.prepareStatement("SELECT * FROM "+tabla+" WHERE "+columna[0]+" > ?");
                }else{
                    pst = c.prepareStatement("SELECT * FROM "+tabla+" WHERE length("+columna[0]+") > ?");
                }
            }else if(condition == 2) {
                if(columna[1].equals("integer")){
                    pst = c.prepareStatement("SELECT * FROM "+tabla+" WHERE "+columna[0]+" < ?");
                }else{
                    pst = c.prepareStatement("SELECT * FROM "+tabla+" WHERE length("+columna[0]+") < ?");
                }
            }
            assert pst != null;
            pst.setInt(1, cantidad);
            System.out.println(pst.toString());
            ResultSet result = pst.executeQuery();
            System.out.println();
            while(result.next()){
                for (int i = 1; i <= result.getMetaData().getColumnCount(); i++) {
                    System.out.println(result.getMetaData().getColumnName(i) +": "+ result.getString(i));
                }
                System.out.println("\n--------------------------------------\n");
            }
        } catch (SQLException e) {
            System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
        }
    }

    /**
     * Función que muestra todos los campos de una tabla y columna específicamente introducida por el usuario.
     *
     * @param c Objeto de la conexión con la BBDD.
     * @param tabla Nombre de la tabla de la que se quieren mostrar campos.
     * @param columna Array que indica el nombre y tipo de dato de la columna seleccionada.
     */
    public static void selectColumn(Connection c, String tabla, String[] columna) {
        try {
            PreparedStatement pst = c.prepareStatement("SELECT "+columna[0]+" FROM "+tabla);
            //System.out.println(pst.toString());
            ResultSet result = pst.executeQuery();
            System.out.println("Mostrando columna "+columna[0]+" de la tabla "+tabla);
            int i = 1;
            while(result.next()){
                System.out.println(i+": "+ result.getString(1));
                i++;
            }
        } catch (SQLException e) {
            System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
        }
    }
}
