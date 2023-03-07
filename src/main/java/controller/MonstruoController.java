package controller;

import actions.DB_Actions;
import model.Monstruo;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;

public class MonstruoController {
    public Connection connection;
    public EntityManagerFactory entityManagerFactory;
    Scanner sc = new Scanner(System.in);
    public List<String> colsName = new ArrayList<>();
    public List<String> colsDataType = new ArrayList<>();

    public MonstruoController(Connection connection) {
        this.connection = connection;
    }

    public MonstruoController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
        setHeaders();
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
    public void updateOneElementMonstruo(Integer monstruoId, String column) {
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

    public void updateManyElementsMonstruo(Integer monstruoId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Monstruo monstruo = em.find(Monstruo.class, monstruoId);
        List<Field> elements = Arrays.asList(Monstruo.class.getDeclaredFields());
        elements.forEach(field -> System.out.println(field.toString()));
        System.out.print("Indica el dato modificado: ");
        /*switch (column) {
            case "icono" -> monstruo.setIcono(sc.nextLine());
            case "nombre" -> monstruo.setNombre(sc.nextLine());
            case "vida" -> {
                monstruo.setVida(sc.nextInt());
                sc.nextLine();
            }
            case "descripcion" -> monstruo.setDescripcion(sc.nextLine());
        }*/
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

    /* Method to delete Monstruo's table data */
    public void deleteMonstruoTableData() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Monstruo> result = em.createQuery("from Monstruo", Monstruo.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Monstruo::getId)).toList();
        for (Monstruo monstruo : result) {
            deleteMonstruo(monstruo.getId());
        }
        em.getTransaction().commit();
        em.close();
    }

    /* Method to select Monstruo's table column */
    public void selectMonstruoTableColumn(int column) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        List<Monstruo> result = em.createQuery("from Monstruo", Monstruo.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Monstruo::getId)).toList();
        for (Monstruo monstruo : result) {
            switch (column) {
                case 0 -> System.out.println(monstruo.getId());
                case 1 -> System.out.println(monstruo.getIcono());
                case 2 -> System.out.println(monstruo.getNombre());
                case 3 -> System.out.println(monstruo.getVida());
                case 4 -> System.out.println(monstruo.getDescripcion());
            }
        }

        em.getTransaction().commit();
        em.close();
    }

    // ************** UTILS ***************
    /* Method to get the last Monstruo ID */
    public int getMonstruosLastId() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Monstruo> result = em.createQuery("from Monstruo", Monstruo.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return result.size();
    }

    public void setHeaders() {
        Monstruo monstruo = new Monstruo();
        List<Field> fields = Arrays.asList(monstruo.getClass().getDeclaredFields());
        for (Field field : fields){
            colsName.add(field.getName());
            if(field.getType().getName().equals("java.lang.String")) colsDataType.add("String");
            else colsDataType.add("int");
        }
    }
}
