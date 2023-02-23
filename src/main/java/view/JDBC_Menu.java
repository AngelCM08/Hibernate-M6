package view;

import actions.DB_Actions;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

/**
 *	Clase en la que se ubican las funciones principales de comunicación entre el usuario y el programa.
 *
 * 	@author Ángel Castro Merino
 */
public class JDBC_Menu {
	private int option;
	Scanner sc = new Scanner(System.in);
	/**
	 * Constructor de la clase para acceder a los menús desde cualquier otra clase.
	 */
	public JDBC_Menu() {
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
			System.out.println("3. Consultas específicas.");
			System.out.println("4. Insertar registro.");
			System.out.println("5. Actualizar registro.");
			System.out.println("6. Eliminar datos.");
			System.out.println("7. Eliminar registro que contengan un texto específico.");
			System.out.println("8. Vaciar tablas.");
			System.out.println("9. Salir.");
			System.out.print("Escoger opción: ");
			try{
				option = Integer.parseInt(sc.nextLine());
				if(option < 1 || option > 9){
					System.out.println("\n*** Indica un valor númerico válido. ***");
				}
			}catch(Exception e){
				System.out.println("\n*** Selecciona una opción válida. ***");
			}
		}while(option < 1 || option > 9);
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

	/**
	 * Menú con las opciones para consultas específicas de la BBDD.
	 *
	 * @return valor entero introducido por el usuario para seleccionar la opción deseada.
	 */
	public int ConsultasEspecificasMenu() {
		do {
			option = 0;
			System.out.println(" \nQUE TIPO DE ACCIÓN QUIERES REALIZAR\n");

			System.out.println("1. Seleccionar todos los elementos que contengan un texto concreto.");
			System.out.println("2. Seleccionar todos los elementos que cumplan una condición.");
			System.out.println("3. Seleccionar una columna.");
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
		return option;
	}

	/**
	 * Menú con las opciones para consultas por condiciones de la BBDD.
	 *
	 * @return valor entero introducido por el usuario para seleccionar la opción deseada.
	 */
	public int ConsultasPorCondicionesMenu() {
		do {
			option = 0;
			System.out.println(" \nQUE TIPO DE ACCIÓN QUIERES REALIZAR\n");

			System.out.println("1. Seleccionar todos los elementos con un valor superior al indicado. ");
			System.out.println("2. Seleccionar todos los elementos con un valor inferior al indicado. ");
			System.out.println("3. Atrás.");
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
		return option;
	}

	/**
	 * Menú con las opciones de eliminado de la BBDD.
	 *
	 * @return valor entero introducido por el usuario para seleccionar la opción deseada.
	 */
	public int DeleteMenu() {
		do {
			option = 0;
			System.out.println(" \nQUE TIPO DE ACCIÓN QUIERES REALIZAR\n");

			System.out.println("1. Eliminar tabla.");
			System.out.println("2. Eliminar registro de una tabla.");
			System.out.println("3. Atrás.");
			System.out.print("Escoger opción: ");

			try{
				option = Integer.parseInt(sc.nextLine());
				if(option < 1 || option > 3){
					System.out.println("\n*** Indica un valor númerico válido. ***");
				}
			}catch(Exception e){
				System.out.println("\n*** Selecciona una opción válida. ***");
			}
		} while (option < 1 || option > 3);
		return option;
	}

	/**
	 * Menú con las opciones de actualización de la BBDD.
	 *
	 * @return valor entero introducido por el usuario para seleccionar la opción deseada.
	 */
	public int UpdateMenu() {
		do {
			option = 0;
			System.out.println(" \nQUE TIPO DE ACCIÓN QUIERES REALIZAR\n");

			System.out.println("1. Seleccionar registro y modificar sus elementos.");
			System.out.println("2. Modificar varios registros según condición.");
			System.out.println("3. Atrás.");
			System.out.print("Escoger opción: ");

			try{
				option = Integer.parseInt(sc.nextLine());
				if(option < 1 || option > 3){
					System.out.println("\n*** Indica un valor númerico válido. ***");
				}
			}catch(Exception e){
				System.out.println("\n*** Selecciona una opción válida. ***");
			}
		} while (option < 1 || option > 3);
		return option;
	}

	/**
	 * Función que muestra un listado de todos los valores de cada una de las columnas de una tabla consultada. De la
	 * cuál se recibe el objeto ResultSet, que es la respuesta a la consulta. Permite al usuario escoger que atributo
	 * quiere seleccionar.
	 *
	 * @param result respuesta a una consulta.
	 * @return valor entero introducido por el usuario para seleccionar la opción deseada.
	 */
	public int SelectFieldMenu(ResultSet result){
		try {
			int columnCount = result.getMetaData().getColumnCount();
			do {
				option = -1;
				while(result.next()) {
					for (int i = 1; i < columnCount; i++) {
						System.out.println(i + ". " + result.getMetaData().getColumnName(i+1) + ": " + result.getString(i+1));
					}
				}
				System.out.println("0. Cancelar.");
				System.out.print("Selecciona el atributo que quieres actualizar: ");
				try{
					option = Integer.parseInt(sc.nextLine());
					if(option < 0 || option > columnCount){
						System.out.println("\n*** Indica un valor númerico válido. ***");
					}
				}catch(Exception e){
					System.out.println("\n*** Selecciona una opción válida. ***");
					result.beforeFirst();
				}
			} while (option < 0 || option > columnCount);
		} catch (SQLException e) {
			System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
		}
		return option;
	}
}