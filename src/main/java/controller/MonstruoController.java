package controller;

import actions.DB_Actions;
import model.Author;
import model.Monstruo;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

public class MonstruoController {
    private Connection connection;
    private EntityManagerFactory entityManagerFactory;

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

    public static void printMonstruos(){
        readMonstruoFile().forEach(System.out::println);
    }

    /* Method to CREATE a Monstruo in the database */
    public void addMonstruo(Monstruo monstruo) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Monstruo MonstruoExists = (Monstruo) em.find(Monstruo.class, monstruo.getId());
        if (MonstruoExists == null) {
            System.out.println("insert autor");
            em.persist(monstruo);
        }
        em.getTransaction().commit();
        em.close();
    }


    /* Method to READ all Monstruo */
    public void listMonstruos() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Author> result = em.createQuery("from Author", Author.class)
                .getResultList();
        for (Author author : result) {
            System.out.println(author.toString());
        }
        em.getTransaction().commit();
        em.close();
    }

    /* Method to UPDATE activity for an Monstruo */
    public void updateMonstruo(Integer authorId, boolean active) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Author author = (Author) em.find(Author.class, authorId);
        author.setActive(active);
        em.merge(author);
        em.getTransaction().commit();
        em.close();
    }

    /* Method to DELETE an Monstruo from the records */
    public void deleteMonstruo(Integer authorId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Author author = (Author) em.find(Author.class, authorId);
        em.remove(author);
        em.getTransaction().commit();
        em.close();
    }

    public static void main(String[] args) {

    }
}
