package controller;

import actions.DB_Actions;
import model.Monstruo;

import javax.persistence.*;
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

    public void updateRegistersByCondition(String nombreColumna) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        System.out.print("Cuál es el texto que quieres actualizar: ");
        String valorNuevo = sc.nextLine();
        System.out.print("Cuál es el texto actualizado: ");
        String valorAntiguo = sc.nextLine();

        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            // Seleccionar el monstruo que coincide con el nombre proporcionado
            TypedQuery<Monstruo> query = em.createQuery("UPDATE monstruo SET "+ nombreColumna +
                                                            " = :valorNuevo WHERE " + nombreColumna +
                                                            " = :valorAntiguo", Monstruo.class);
            query.setParameter("valorNuevo", valorNuevo);
            query.setParameter("valorAntiguo", valorAntiguo);
            int numFilasActualizadas = query.executeUpdate();
            transaction.commit();
            System.out.println(numFilasActualizadas + " registros actualizados.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
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

    /* DELETE by text */
    public void eliminarMonstruoPorCondicionDeTexto(String columna) {
        EntityManager em = null;
        EntityTransaction transaction = null;

        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            System.out.print("Cuál es el texto condición para eliminar los registros: ");
            // Seleccionar el monstruo que coincide con el nombre proporcionado
            TypedQuery<Monstruo> query = em.createQuery(
                    "SELECT m FROM Monstruo m WHERE "+columna+" LIKE :texto", Monstruo.class);
            query.setParameter("texto", sc.nextLine());
            List<Monstruo> monstruos = query.getResultList();
            // Eliminar el monstruo
            for (Monstruo m : monstruos) {
                em.remove(m);
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            if (em != null) {
                em.close();
            }
        }
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
