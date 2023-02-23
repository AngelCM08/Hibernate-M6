package actions;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *  Clase en la que hay funciones estáticas para acciones relacionadas
 *  con la inserción de registros en la BBDD.
 */
public class DB_Inserts {
    /**
     * Función que permite insertar registros en la tabla seleccionada por el usuario.
     *
     * @param c Objeto de la conexión con la BBDD.
     * @param tabla Nombre de la tabla en la que se quiere insertar un registro.
     */
    public static void InsertRegister(Connection c, String tabla) {
        Scanner sc = new Scanner(System.in);
        List<List<String>> header = DB_Actions.GetHeader(c, tabla);
        String values_question = "";
        List<String> values = new ArrayList<>();

        try {
            Statement st = c.createStatement();
            ResultSet rs = st.executeQuery("SELECT MAX("+header.get(0).get(0)+") FROM "+tabla);
            rs.next();
            String insert = rs.getString(1);
            values.add(String.valueOf(Integer.parseInt(insert)+1));
        } catch (SQLException e) {
            System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
        }

        System.out.println("**** INSERTAR REGISTRO EN '"+tabla+"' ****");
        for (int i = 0; i < header.get(0).size(); i++) {
            if(i == 0) values_question = values_question.concat("(?, ");
            else if(i+1 == header.get(0).size()) values_question = values_question.concat("?)");
            else values_question = values_question.concat("?, ");
            if(i != 0){
                System.out.print("Campo "+header.get(0).get(i)+": ");
                values.add(sc.nextLine());
            }
        }

        try {
            String insert = "INSERT INTO "+tabla+"("+header.get(0).toString().substring(1,header.get(0).toString().length()-1)+") VALUES "+values_question+";";
            PreparedStatement pst = c.prepareStatement(insert);
            try{
                for (int i = 1; i <= values.size(); i++) {
                    if(header.get(1).get(i-1).equals("integer")) pst.setInt(i, Integer.parseInt(values.get(i-1)));
                    else pst.setString(i, values.get(i-1));
                }
                pst.executeUpdate();
                System.out.println("**** REGISTRO INSERTADO CORRECTAMENTE ****");
            }catch(Exception e){
                System.out.println("**** ERROR AL INSERTAR LOS DATOS (NO SE HAN INSERTADO) ****");
            }
        } catch (SQLException e) {
            System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
        }
    }
}
