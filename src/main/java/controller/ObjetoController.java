package controller;

import actions.DB_Actions;
import model.Monstruo;
import model.Objeto;

import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class ObjetoController {
    private Connection connection;
    private EntityManagerFactory entityManagerFactory;

    public ObjetoController(Connection connection) {
        this.connection = connection;
    }

    public ObjetoController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
    }

    public static List<Objeto> readObjetoFile(){
        List<Objeto> listObjeto = new ArrayList<>();
        List<String[]> listCSV = DB_Actions.GetDataFromCSV("objeto");

        listCSV.forEach(row -> {
            listObjeto.add(new Objeto(Integer.parseInt(row[0]),row[1],row[2],row[3]));
        });
        return listObjeto;
    }

    public static void printObjetos(){
        readObjetoFile().forEach(System.out::println);
    }
    public static void main(String[] args) {

    }
}
