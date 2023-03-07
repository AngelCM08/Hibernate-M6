import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.stream.Collectors;

import controller.*;
import database.ConnectionFactory;
import view.Menu;

import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
    public static EntityManagerFactory createEntityManagerFactory() {
        EntityManagerFactory emf;
        try {
            emf = Persistence.createEntityManagerFactory("tboia");
        } catch (Throwable ex) {
            System.err.println("Failed to create EntityManagerFactory object." + ex);
            throw new ExceptionInInitializerError(ex);
        }
        return emf;
    }

    public static void main(String[] args) {
        ConnectionFactory connectionFactory = ConnectionFactory.getInstance();
        Connection c = connectionFactory.connect();
        EntityManagerFactory entityManagerFactory = createEntityManagerFactory();

        MonstruoController monstruoController = new MonstruoController(c, entityManagerFactory);
        ObjetoController objetoController = new ObjetoController(c, entityManagerFactory);
        PersonajeController personajeController = new PersonajeController(c, entityManagerFactory);

        Menu menu = new Menu();
        monstruoController.selectMonstruoTableColumn(menu.listHeader(monstruoController.colsName));
        /*monstruoController.addMonstruo(new Monstruo(194, "iconoTest", "nombreMonstruo2", 8, "Descripcion2"));
        monstruoController.listMonstruos();
        monstruoController.updateOneElementMonstruo(196, "nombre");
        monstruoController.listMonstruos();
        monstruoController.deleteMonstruo(196);
        monstruoController.listMonstruos();
        monstruoController.updateManyElementsMonstruo(195);

        objetoController.addObjeto(new Objeto(718, "icono", "nombreObjeto", "DescripcionObjeto"));
        objetoController.listObjetos();
        objetoController.updateObjeto(718, "nombre");
        objetoController.listObjetos();
        objetoController.deleteObjeto(718);
        objetoController.listObjetos();

        personajeController.addPersonaje(new Personaje(35, "icono","nombre", 2, "2.3", "2.3", "2.3", "2.3", "2.3", 2));
        personajeController.listPersonajes();
        personajeController.updatePersonaje(35, "nombre");
        personajeController.listPersonajes();
        personajeController.deletePersonaje(35);
        personajeController.listPersonajes();*/

        /*Menu menu = new Menu();
        int option = menu.mainMenu();

        while (option != 11) {
            switch (option) {
                case 1: // Poblar o restaurar tablas. // Con Statement porque es lo que hay, Hibernate va raro pa esto.
                    try {
                        Statement st = c.createStatement();
                        BufferedReader br = new BufferedReader(new FileReader("src/data/schema.sql"));
                        st.execute(br.lines().collect(Collectors.joining(" \n")));
                    } catch (FileNotFoundException | SQLException e) {
                        System.out.println("\n**** ERROR! LA TAREA NO HA PODIDO REALIZARSE CORRECTAMENTE ****");
                    }
                    break;
                case 2: // Mostrar tabla completa.

                    break;
                case 3: // Seleccionar una columna

                    break;
                case 4: // Seleccionar elementos que contengan un texto.

                    break;
                case 5: // Insertar registro.

                    break;
                case 6: // Equipar objeto a personaje.

                    break;
                case 7: // Modificar elementos de un registro.

                    break;
                case 8: // Modificar registros según condición.

                    break;
                case 9: // Eliminar registro de una tabla.

                    break;
                case 10: // Vaciar tablas.
                    monstruoController.deleteMonstruoTableData();
                    break;
            }
            option = menu.mainMenu();
        }
        System.out.println("\n**** ADIÓS! ****");
        try {
            c.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la BBDD");
        }*/
    }
}
