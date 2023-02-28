package controller;

import actions.DB_Actions;
import model.Objeto;
import model.Objeto;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class ObjetoController {
    private Connection connection;
    private EntityManagerFactory entityManagerFactory;
    Scanner sc = new Scanner(System.in);
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

    public static void printObjetosFromFile(){
        readObjetoFile().forEach(System.out::println);
    }

    /* Method to CREATE a Objeto in the database */
    public void addObjeto(Objeto objeto) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Objeto ObjetoExists = em.find(Objeto.class, objeto.getId());
        if (ObjetoExists == null) {
            System.out.println("insert objeto");
            em.persist(objeto);
        }
        em.getTransaction().commit();
        em.close();
    }


    /* Method to READ all Objeto */
    public void listObjetos() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Objeto> result = em.createQuery("from Objeto", Objeto.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Objeto::getId)).toList();
        for (Objeto objeto : result) {
            System.out.println(objeto.toString());
        }
        em.getTransaction().commit();
        em.close();
    }

    /* Method to UPDATE activity for Objeto */
    public void updateObjeto(Integer objetoId, String column) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Objeto objeto = em.find(Objeto.class, objetoId);
        System.out.print("Indica el dato modificado: ");
        switch (column) {
            case "icono" -> objeto.setIcono(sc.nextLine());
            case "nombre" -> objeto.setNombre(sc.nextLine());
            case "descripcion" -> objeto.setDescripcion(sc.nextLine());
        }
        em.merge(objeto);
        em.getTransaction().commit();
        em.close();
        listObjetos();
    }

    /* Method to DELETE Objeto from the records */
    public void deleteObjeto(Integer objetoId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Objeto objeto = em.find(Objeto.class, objetoId);
        em.remove(objeto);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteTableObjeto(){
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        em.createQuery("DELETE FROM ").executeUpdate();
        em.getTransaction().commit();
    }
}