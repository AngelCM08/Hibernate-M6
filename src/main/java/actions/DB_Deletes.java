package actions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

/**
 *  Clase en la que hay funciones estáticas para acciones relacionadas
 *  con la eliminación de tablas o registros de la BBDD.
 */
public class DB_Deletes {
    /**
     * Función que elimina la tabla pasada por parámetro.
     *
     * @param c Objeto de la conexión con la BBDD.
     * @param tabla Nombre de la tabla que se quiere eliminar.
     */
    public static void deleteTable(Connection c, String tabla){
        try {
            c.createStatement().executeUpdate("DROP TABLE "+tabla+" CASCADE");
            System.out.println("\n**** LA TABLA '"+tabla+"' HA SIDO ELIMINADA ****");
        } catch (SQLException e) {
            System.out.println("\n**** NO SE HA PODIDO ELIMINAR LA TABLA '"+tabla+"' ****");
        }
    }

    /**
     * Función que elimina el registro de la tabla pasados por parámetro.
     *
     * @param c Objeto de la conexión con la BBDD.
     * @param tabla Nombre de la tabla de la que se quiere eliminar un registro.
     * @param register Entero que indica el valor de la Primary Key del registro que se quiere eliminar.
     */
    public static void deleteTableRegister(Connection c, String tabla, int register) {
        String id = DB_Actions.GetHeader(c, tabla).get(0).get(0);
        try {
            c.createStatement().executeUpdate("DELETE FROM "+tabla+" WHERE "+id+" = "+register);
            System.out.println("\n**** EL REGISTRO HA SIDO ELIMINADO ****");
        } catch (SQLException e) {
            System.out.println("\n**** NO SE HA PODIDO ELIMINAR EL REGISTRO ****");
        }
    }

    /**
     * Función que elimina el registro de la tabla pasada por parámetro cuya información
     * en la columna indicada coincida con el valor introducido por el usuario.
     *
     * @param c Objeto de la conexión con la BBDD.
     * @param tabla Nombre de la tabla de la que se quieren eliminar registros.
     * @param columna Array que indica el nombre y tipo de dato de la columna seleccionada para comparar.
     */
    public static void deleteTableRegisterByCondition(Connection c, String tabla, String[] columna) {
        Scanner sc = new Scanner(System.in);
        String text;
        int value;
        try {
            System.out.print("Cuál es el texto que contienen los registros a eliminar en la columna "+columna[0]+": ");
            PreparedStatement insert_pst;
            PreparedStatement select_pst;
            if(columna[1].equals("integer")){
                value = sc.nextInt();
                sc.nextLine();

                insert_pst = c.prepareStatement("DELETE FROM "+tabla+" WHERE "+columna[0]+" = ?");
                insert_pst.setInt(1, value);

                select_pst = c.prepareStatement("SELECT * FROM "+tabla+" WHERE "+columna[0]+" = ?");
                select_pst.setInt(1, value);
            }else{
                text = sc.nextLine();

                select_pst = c.prepareStatement("SELECT * FROM "+tabla+" WHERE "+columna[0]+" ILIKE ?");
                select_pst.setString(1, text);

                insert_pst = c.prepareStatement("DELETE FROM "+tabla+" WHERE "+columna[0]+" ILIKE ?");
                insert_pst.setString(1, text);
            }
            ResultSet result = select_pst.executeQuery();
            insert_pst.executeUpdate();

            System.out.println("\n**** SE HAN ELIMINADO LOS SIGUIENTES REGISTROS ****");
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
}