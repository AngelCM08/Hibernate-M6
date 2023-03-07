package view;

import actions.DB_Actions;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private int option;
    Scanner sc = new Scanner(System.in);
    public Menu() {
        super();
    }

    /**
     * Menú con las opciones principales de la aplicación.
     *
     * @return valor entero introducido por el usuario para seleccionar la opción deseada.
     */
    public int mainMenu() {
        do{
            option = 0;
            System.out.println(" \nMENU PRINCIPAL \n");

            System.out.println("1. Poblar o restaurar tablas.");
            System.out.println("2. Mostrar tabla completa.");
            System.out.println("3. Seleccionar una columna.");
            System.out.println("4. Seleccionar elementos que contengan un texto.");
            System.out.println("5. Insertar registro.");
            System.out.println("6. Equipar objeto a personaje.");
            System.out.println("7. Modificar elementos de un registro.");
            System.out.println("8. Modificar registros según condición.");
            System.out.println("9. Eliminar registro de una tabla.");
            System.out.println("10. Eliminar tabla.");
            System.out.println("11. Vaciar tablas.");
            System.out.println("12. Salir.");
            System.out.print("Escoger opción: ");
            try{
                option = Integer.parseInt(sc.nextLine());
                if(option < 1 || option > 12){
                    System.out.println("\n*** Indica un valor númerico válido. ***");
                }
            }catch(Exception e){
                System.out.println("\n*** Selecciona una opción válida. ***");
            }
        }while(option < 1 || option > 12);
        return option;
    }

    /**
     * Menú para seleccionar una de las tablas de la BBDD.
     *
     * @return nombre de la tabla escogida por el usuario a través de un valor entero.
     */
    public String TablesMenu() {
        do {
            option = 0;
            System.out.println(" \nSOBRE QUE TABLA QUIERES REALIZAR LA ACCIÓN\n");

            System.out.println("1. Personaje.");
            System.out.println("2. Monstruo.");
            System.out.println("3. Objeto.");
            System.out.println("4. Atrás.");
            System.out.print("Escoger opción: ");

            try{
                option = Integer.parseInt(sc.nextLine());
                if(option < 1 || option > 4){
                    System.out.println("\n*** Indica un valor númerico válido. ***");
                }
            }catch(Exception e){
                System.out.println("\n*** Selecciona una opción válida. ***");
            }
        } while (option < 1 || option > 4);
        switch (option) {
            case 1 -> { return "personaje"; }
            case 2 -> { return "monstruo"; }
            case 3 -> { return "objeto"; }
        }
        return "";
    }

    /**
     * Función que muestra un listado de todas las columnas de la tabla pasada por parámetro.
     * Permite al usuario escoger que columna quiere seleccionar.
     *
     * @param c Objeto de la conexión con la BBDD.
     * @param tabla Nombre de la tabla la cuál se quieren listar sus columnas.
     * @return Array que contiene 2 strings, en el primer valor se encuentra el nombre de la columna seleccionada,
     * en el segundo indica el tipo de dato que es ésa columna en la BBDD.
     */
    public String[] ColumnsMenu(Connection c, String tabla) {
        List<List<String>> header = DB_Actions.GetHeader(c, tabla);

        for (int i = 0; i < header.get(0).size(); i++) {
            System.out.println(i+". "+header.get(0).get(i));
        }
        do{
            option = -1;
            System.out.println("Indica el valor de la columna que quieres seleccionar:");

            try{
                option = Integer.parseInt(sc.nextLine());
                if(option < 0 || option >= header.get(0).size()){
                    System.out.println("\n*** Indica un valor númerico válido. ***");
                }
            }catch(Exception e){
                System.out.println("\n*** Indica un valor númerico válido. ***");
            }
        }while(option < 0 || option >= header.get(0).size());
        return new String[]{header.get(0).get(option), header.get(1).get(option)};
    }
}