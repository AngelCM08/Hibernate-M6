package view;

import model.*;
import java.util.List;
import java.util.Scanner;

/**
 * Clase que comunica al usuario con la aplicación.
 */
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
            System.out.println("7. Desquipar objeto a personaje.");
            System.out.println("8. Modificar atributos de un registro.");
            System.out.println("9. Modificar registros según condición.");
            System.out.println("10. Eliminar registro de una tabla.");
            System.out.println("11. Eliminar registro por condicion.");
            System.out.println("12. Vaciar tablas.");
            System.out.println("13. Salir.");
            System.out.print("Escoger opción: ");
            try{
                option = Integer.parseInt(sc.nextLine());
                if(option < 1 || option > 13){
                    System.out.println("\n*** Indica un valor númerico válido. ***");
                }
            }catch(Exception e){
                System.out.println("\n*** Selecciona una opción válida. ***");
            }
        }while(option < 1 || option > 13);
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
     * Función que muestra un listado de todas las columnas de una tabla.
     * Permite al usuario escoger que columna quiere seleccionar.
     *
     * @param colsName Lista de columnas de la tabla las cuáles se quieren listar.
     */
    public int listHeader(List<String> colsName){
        do{
            for (int i = 1; i < colsName.size(); i++) {

                System.out.println(i + ". " + colsName.get(i));
            }
            System.out.print("Indica el valor de la columna que quieres seleccionar: ");
            try{
                option = Integer.parseInt(sc.nextLine());
                if(option < 0 || option >= colsName.size()){
                    System.out.println("\n*** Indica un valor númerico válido. ***");
                }
            }catch(Exception e){
                System.out.println("\n*** Indica un valor númerico válido. ***");
            }
        }while (option < 0 || option >= colsName.size());

        return option;
    }

    /**
     * Función que muestra un listado de todos los registros de una tabla.
     * Permite al usuario escoger que registro quiere seleccionar.
     *
     * @param list Lista que contiene los datos de la clase Objeto la cuál se quiere listar.
     */
    public int selectPersonajeId(List<Personaje> list, int lastID) {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.print("Indica el ID del registro que quieres seleccionar: ");
            try {
                option = Integer.parseInt(sc.nextLine());
                if (option < 0 || option > list.size()) {
                    System.out.println("\n*** Indica un valor númerico válido. ***");
                }
            } catch (Exception e) {
                System.out.println("\n*** Indica un valor númerico válido. ***");
            }
        } while (option < 0 || option > lastID);
        return option;
    }

    /**
     * Función que muestra un listado de todos los registros de una tabla.
     * Permite al usuario escoger que registro quiere seleccionar.
     *
     * @param list Lista que contiene los datos de la clase Objeto la cuál se quiere listar.
     */
    public int selectMonstruoId(List<Monstruo> list, int lastID) {
        Scanner sc = new Scanner(System.in);
        do {
            System.out.print("Indica el ID del registro que quieres seleccionar: ");
            try {
                option = Integer.parseInt(sc.nextLine());
                if (option < 0 || option > list.size()) {
                    System.out.println("\n*** Indica un valor númerico válido. ***");
                }
            } catch (Exception e) {
                System.out.println("\n*** Indica un valor númerico válido. ***");
            }
        } while (option < 0 || option > lastID);
        return option;
    }

    /**
     * Función que muestra un listado de todos los registros de una tabla.
     * Permite al usuario escoger que registro quiere seleccionar.
     *
     * @param list Lista que contiene los datos de la clase Objeto la cuál se quiere listar.
     */
    public int selectObjetoId(List<Objeto> list, int lastID) {
        Scanner sc = new Scanner(System.in);
        System.out.println(lastID);
        do {
            System.out.print("Indica el ID del registro que quieres seleccionar: ");
            try {
                option = Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("\n*** Indica un valor númerico válido. ***");
            }
        } while (option < 0 || option > lastID);
        return option;
    }
}