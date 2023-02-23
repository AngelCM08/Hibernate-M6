package controller;

import actions.DB_Actions;
import model.Personaje;

import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class PersonajeController {
    private Connection connection;
    private EntityManagerFactory entityManagerFactory;

    public PersonajeController(Connection connection) {
        this.connection = connection;
    }

    public PersonajeController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
    }

    public static List<Personaje> readPersonajeFile(){
        List<Personaje> listPersonaje = new ArrayList<>();
        List<String[]> listCSV = DB_Actions.GetDataFromCSV("personaje");

        listCSV.forEach(row -> {
            listPersonaje.add(new Personaje(Integer.parseInt(row[0]),row[1],row[2],Integer.parseInt(row[3]),row[4],row[5],row[6],row[7],row[8],Integer.parseInt(row[9])));
        });
        return listPersonaje;
    }

    public static void printPersonaje(){
        readPersonajeFile().forEach(System.out::println);
    }
    public static void main(String[] args) {

    }
}
