package controller;

import actions.DB_Actions;
import model.Personaje;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class PersonajeController {
    private Connection connection;
    private EntityManagerFactory entityManagerFactory;
    Scanner sc = new Scanner(System.in);
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

    public static void printPersonajesFromFile(){
        readPersonajeFile().forEach(System.out::println);
    }

    /* Method to CREATE a Personaje in the database */
    public void addPersonaje(Personaje personaje) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Personaje PersonajeExists = em.find(Personaje.class, personaje.getId());
        if (PersonajeExists == null) {
            System.out.println("insert personaje");
            em.persist(personaje);
        }
        em.getTransaction().commit();
        em.close();
    }


    /* Method to READ all Personaje */
    public void listPersonajes() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Personaje> result = em.createQuery("from Personaje", Personaje.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Personaje::getId)).toList();
        for (Personaje personaje : result) {
            System.out.println(personaje.toString());
        }
        em.getTransaction().commit();
        em.close();
    }

    /* Method to UPDATE activity for Personaje */
    public void updatePersonaje(Integer personajeId, String column) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Personaje personaje = em.find(Personaje.class, personajeId);
        System.out.print("Indica el dato modificado: ");
        switch (column) {
            case "icono" -> personaje.setIcono(sc.nextLine());
            case "nombre" -> personaje.setNombre(sc.nextLine());
            case "vida" -> {
                personaje.setVida(sc.nextInt());
                sc.nextLine();
            }
            case "daño" -> personaje.setDaño(sc.nextLine());
            case "cadencia" -> personaje.setCadencia(sc.nextLine());
            case "vel_proyectil" -> personaje.setVel_proyectil(sc.nextLine());
            case "rango" -> personaje.setRango(sc.nextLine());
            case "velocidad" -> personaje.setVelocidad(sc.nextLine());
            case "suerte" -> {
                personaje.setSuerte(sc.nextInt());
                sc.nextLine();
            }
        }
        em.merge(personaje);
        em.getTransaction().commit();
        em.close();
        listPersonajes();
    }

    /* Method to DELETE Personaje from the records */
    public void deletePersonaje(Integer personajeId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Personaje personaje = em.find(Personaje.class, personajeId);
        em.remove(personaje);
        em.getTransaction().commit();
        em.close();
    }
}
