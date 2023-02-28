package controller;

import actions.DB_Actions;
import model.Monstruo;
import model.Monstruo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class MonstruoController {
    private Connection connection;
    private EntityManagerFactory entityManagerFactory;
    Scanner sc = new Scanner(System.in);
    public MonstruoController(Connection connection) {
        this.connection = connection;
    }

    public MonstruoController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
    }

    public static List<Monstruo> readMonstruoFile(){
        List<Monstruo> listMonstruo = new ArrayList<>();
        List<String[]> listCSV = DB_Actions.GetDataFromCSV("monstruo");

        listCSV.forEach(row -> {
            listMonstruo.add(new Monstruo(Integer.parseInt(row[0]),row[1],row[2],Integer.parseInt(row[3]),row[4]));
        });
        return listMonstruo;
    }

    public static void printMonstruosFromFile(){
        readMonstruoFile().forEach(System.out::println);
    }

    /* Method to CREATE a Monstruo in the database */
    public void addMonstruo(Monstruo monstruo) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Monstruo MonstruoExists = em.find(Monstruo.class, monstruo.getId());
        if (MonstruoExists == null) {
            System.out.println("insert monstruo");
            em.persist(monstruo);
        }
        em.getTransaction().commit();
        em.close();
    }


    /* Method to READ all Monstruo */
    public void listMonstruos() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Monstruo> result = em.createQuery("from Monstruo", Monstruo.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Monstruo::getId)).toList();
        for (Monstruo monstruo : result) {
            System.out.println(monstruo.toString());
        }
        em.getTransaction().commit();
        em.close();
    }

    /* Method to UPDATE activity for Monstruo */
    public void updateMonstruo(Integer monstruoId, String column) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Monstruo monstruo = em.find(Monstruo.class, monstruoId);
        System.out.print("Indica el dato modificado: ");
        switch (column) {
            case "icono" -> monstruo.setIcono(sc.nextLine());
            case "nombre" -> monstruo.setNombre(sc.nextLine());
            case "vida" -> {
                monstruo.setVida(sc.nextInt());
                sc.nextLine();
            }
            case "descripcion" -> monstruo.setDescripcion(sc.nextLine());
        }
        em.merge(monstruo);
        em.getTransaction().commit();
        em.close();
        listMonstruos();
    }

    /* Method to DELETE Monstruo from the records */
    public void deleteMonstruo(Integer monstruoId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Monstruo monstruo = em.find(Monstruo.class, monstruoId);
        em.remove(monstruo);
        em.getTransaction().commit();
        em.close();
    }
}
