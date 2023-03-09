import java.sql.Connection;
import java.sql.SQLException;

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

        PersonajeController personajeController = new PersonajeController(c, entityManagerFactory);
        MonstruoController monstruoController = new MonstruoController(c, entityManagerFactory);
        ObjetoController objetoController = new ObjetoController(c, entityManagerFactory);

        Menu menu = new Menu();

        int id;
        int option = menu.mainMenu();

        while (option != 12) {
            switch (option) {
                case 1: // Poblar o restaurar tablas. // Con Statement porque es lo que hay, Hibernate va raro pa esto.
                    MainController.restartDB(c);
                    personajeController.getDataFromPersonajeFile();
                    monstruoController.getDataFromMonstruoFile();
                    objetoController.getDataFromObjetoFile();
                    break;
                case 2: // Mostrar tabla completa.
                    switch (menu.TablesMenu()) {
                        case "personaje" -> personajeController.listPersonajes();
                        case "monstruo" -> monstruoController.listMonstruos();
                        case "objeto" -> objetoController.listObjetos();
                    }
                    break;
                case 3: // Seleccionar una columna
                    switch (menu.TablesMenu()) {
                        case "personaje" -> personajeController.selectPersonajeTableColumn(menu.listHeader(personajeController.colsName));
                        case "monstruo" -> monstruoController.selectMonstruoTableColumn(menu.listHeader(monstruoController.colsName));
                        case "objeto" ->  objetoController.selectObjetoTableColumn(menu.listHeader(objetoController.colsName));
                    }
                    break;
                case 4: // Seleccionar elementos que contengan un texto.
                    switch (menu.TablesMenu()) {
                        case "personaje" -> {
                            personajeController.listPersonajes();
                            personajeController.selectRegisterByCondition(menu.listHeader(personajeController.colsName));
                        }
                        case "monstruo" -> {
                            monstruoController.listMonstruos();
                            monstruoController.selectRegisterByCondition(menu.listHeader(monstruoController.colsName));
                        }
                        case "objeto" -> {
                            objetoController.listObjetos();
                            objetoController.selectRegisterByCondition(menu.listHeader(objetoController.colsName));
                        }
                    }
                    break;
                case 5: // Insertar registro.
                    switch (menu.TablesMenu()) {
                        case "personaje" -> personajeController.addNewPersonaje();
                        case "monstruo" -> monstruoController.addNewMonstruo();
                        case "objeto" -> objetoController.addNewObjeto();
                    }
                    break;
                case 6: // Equipar objeto a personaje.
                    int idPersonaje, idObjeto;
                    idPersonaje = menu.selectPersonajeId(personajeController.listPersonajes());
                    idObjeto = menu.selectObjetoId(objetoController.listObjetos());

                    personajeController.addRelation(idPersonaje, idObjeto);
                    objetoController.addRelation(idPersonaje, idObjeto);

                    break;
                case 7: // Modificar elementos de un registro.
                    switch (menu.TablesMenu()) {
                        case "personaje" -> personajeController.updatePersonaje(menu.selectPersonajeId(personajeController.listPersonajes()), menu.listHeader(personajeController.colsName));
                        case "monstruo" -> monstruoController.updateMonstruo(menu.selectMonstruoId(monstruoController.listMonstruos()), menu.listHeader(monstruoController.colsName));
                        case "objeto" -> objetoController.updateObjeto(menu.selectObjetoId(objetoController.listObjetos()), menu.listHeader(objetoController.colsName));
                    }
                    break;
                case 8: // Modificar registros según condición.
                    switch (menu.TablesMenu()) {
                        case "personaje" -> personajeController.updateRegistersByCondition(menu.listHeader(personajeController.colsName));
                        case "monstruo" -> monstruoController.updateRegistersByCondition(menu.listHeader(monstruoController.colsName));
                        case "objeto" -> objetoController.updateRegistersByCondition(menu.listHeader(objetoController.colsName));
                    }
                    break;
                case 9: // Eliminar registro de una tabla.
                    switch (menu.TablesMenu()) {
                        case "personaje" -> personajeController.deletePersonaje(menu.selectPersonajeId(personajeController.listPersonajes()));
                        case "monstruo" -> monstruoController.deleteMonstruo(menu.selectMonstruoId(monstruoController.listMonstruos()));
                        case "objeto" -> objetoController.deleteObjeto(menu.selectObjetoId(objetoController.listObjetos()));
                    }
                case 10: // Eliminar registro de una tabla.
                    switch (menu.TablesMenu()) {
                        case "personaje" -> {
                            personajeController.listPersonajes();
                            personajeController.eliminarPersonajePorCondicionDeTexto(menu.listHeader(personajeController.colsName));
                        }
                        case "monstruo" -> {
                            monstruoController.listMonstruos();
                            monstruoController.eliminarMonstruoPorCondicionDeTexto(menu.listHeader(monstruoController.colsName));
                        }
                        case "objeto" -> {
                            objetoController.listObjetos();
                            objetoController.eliminarObjetoPorCondicionDeTexto(menu.listHeader(objetoController.colsName));
                        }
                    }
                    break;
                case 11: // Vaciar tablas.
                    MainController.restartDB(c);
                    break;
            }
            option = menu.mainMenu();
        }
        System.out.println("\n**** ADIÓS! ****");
        try {
            c.close();
        } catch (SQLException e) {
            System.out.println("Error al cerrar la BBDD");
        }
    }
}
