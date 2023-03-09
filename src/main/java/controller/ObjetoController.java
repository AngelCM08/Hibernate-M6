package controller;

import model.Objeto;
import model.Personaje;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;

public class ObjetoController {
    private Connection connection;
    private EntityManagerFactory entityManagerFactory;
    Scanner sc = new Scanner(System.in);
    public List<String> colsName = new ArrayList<>();
    public List<String> colsDataType = new ArrayList<>();
    
    public ObjetoController(Connection connection) {
        this.connection = connection;
    }

    public ObjetoController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
        setHeaders();
    }

    public void getDataFromObjetoFile(){
        List<Objeto> listObjeto = new ArrayList<>();
        List<String[]> listCSV = MainController.GetDataFromCSV("objeto");
        listCSV.forEach(row -> {
            listObjeto.add(new Objeto(Integer.parseInt(row[0]),row[1],row[2],row[3]));
        });
        listObjeto.forEach(this::addObjeto);
    }

    /* Method to READ all Objeto */
    public List<Objeto> listObjetos() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Objeto> result = em.createQuery("from Objeto", Objeto.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Objeto::getId)).toList();
        for (Objeto objeto : result) {
            System.out.println(objeto.toString());
        }
        em.getTransaction().commit();
        em.close();
        return result;
    }

    /* Method to CREATE a Objeto in the database */
    public void addObjeto(Objeto objeto) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Objeto ObjetoExists = em.find(Objeto.class, objeto.getId());
        if (ObjetoExists == null) {
            System.out.print("insert objeto -> ");
            System.out.println(objeto);
            em.persist(objeto);
        }
        em.getTransaction().commit();
        em.close();
    }

    public void addRelation(int idPersonaje, int idObjeto){
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        Objeto objeto = em.find(Objeto.class, idObjeto);
        Personaje personaje = em.find(Personaje.class, idPersonaje);

        objeto.getPersonajeQueEquipa().add(personaje);
        objeto.setPersonajeQueEquipa(objeto.getPersonajeQueEquipa());

        em.merge(objeto);
        em.getTransaction().commit();
        em.close();
    }

    public void addNewObjeto(){
        Scanner sc = new Scanner(System.in);
        String icono, nombre, descripcion;
        int id, vida=0;
        boolean valorValido;

        id = getObjetosLastId()+1;

        System.out.print("Escribe el valor para la columna icono: ");
        icono = sc.nextLine();

        System.out.print("Escribe el valor para la columna nombre: ");
        nombre = sc.nextLine();

        System.out.print("Escribe el valor para la columna descripcion: ");
        descripcion = sc.nextLine();

        addObjeto(new Objeto(id, icono, nombre, descripcion));
    }

    /* Method to SELECT Objeto's registers */
    public void selectRegisterByCondition(int numColumna){
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        String nombreColumna = colsName.get(numColumna);
        boolean esInteger = colsDataType.get(numColumna).equals("int");

        System.out.print("Cuál es el valor que quieres seleccionar: ");
        String valor = sc.nextLine();

        TypedQuery<Objeto> query = em.createQuery("FROM Objeto WHERE "+nombreColumna+" = :texto", Objeto.class);
        if (esInteger){
            query.setParameter("texto", Integer.parseInt(valor));
        }else{
            query.setParameter("texto", valor);
        }
        query.getResultList().forEach(System.out::println);

        em.getTransaction().commit();
        em.close();


    }

    /* Method to select Objeto's table column */
    public void selectObjetoTableColumn(int column) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        List<Objeto> result = em.createQuery("from Objeto", Objeto.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Objeto::getId)).toList();
        for (Objeto objeto : result) {
            switch (column) {
                case 0 -> System.out.println(objeto.getId());
                case 1 -> System.out.println(objeto.getIcono());
                case 2 -> System.out.println(objeto.getNombre());
                case 3 -> System.out.println(objeto.getDescripcion());
            }
        }

        em.getTransaction().commit();
        em.close();
    }

    /* Method to UPDATE activity for Objeto */
    public void updateObjeto(Integer objetoId, int column) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Objeto objeto = em.find(Objeto.class, objetoId);
        System.out.print("Indica el dato modificado: ");
        switch (column) {
            case 1 -> objeto.setIcono(sc.nextLine());
            case 2 -> objeto.setNombre(sc.nextLine());
            case 3 -> objeto.setDescripcion(sc.nextLine());
        }
        em.merge(objeto);
        em.getTransaction().commit();
        em.close();
        listObjetos();
    }

    public void updateRegistersByCondition(int numColumna) {
        EntityManager em = entityManagerFactory.createEntityManager();
        EntityTransaction transaction = em.getTransaction();

        String nombreColumna = colsName.get(numColumna);
        boolean esInteger = colsDataType.get(numColumna).equals("int");

        System.out.print("Cuál es el valor que quieres actualizar: ");
        String valorAntiguo = sc.nextLine();
        System.out.print("Cómo será el valor actualizado: ");
        String valorNuevo = sc.nextLine();

        try {
            transaction.begin();

            // Seleccionar el objeto que coincide con el nombre proporcionado
            Query query = em.createQuery("UPDATE Objeto SET " + nombreColumna + " = :valorNuevo WHERE " + nombreColumna + " = :valorAntiguo");
            if (esInteger){
                query.setParameter("valorNuevo", Integer.parseInt(valorNuevo));
                query.setParameter("valorAntiguo", Integer.parseInt(valorAntiguo));
            }else{
                query.setParameter("valorNuevo", valorNuevo);
                query.setParameter("valorAntiguo", valorAntiguo);
            }

            int numFilasActualizadas = query.executeUpdate();
            transaction.commit();
            System.out.println(numFilasActualizadas + " registros actualizados.");
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        } finally {
            em.close();
        }
    }

    /* Method to DELETE Objeto from the records */
    public void deleteObjeto(int objetoId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Objeto objeto = em.find(Objeto.class, objetoId);
        em.remove(objeto);
        em.getTransaction().commit();
        em.close();
    }

    /* DELETE by text */
    public void eliminarObjetoPorCondicionDeTexto(int numColumna) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        String nombreColumna = colsName.get(numColumna);
        boolean esInteger = colsDataType.get(numColumna).equals("int");

        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            System.out.print("Cuál es el texto condición para eliminar los registros: ");
            // Seleccionar el objeto que coincide con el nombre proporcionado
            TypedQuery<Objeto> query = em.createQuery(
                    "SELECT m FROM Objeto m WHERE "+nombreColumna+" = :texto", Objeto.class);

            if (esInteger){
                query.setParameter("texto", Integer.parseInt(sc.nextLine()));
            }else{
                query.setParameter("texto", sc.nextLine());
            }

            List<Objeto> objetos = query.getResultList();
            // Eliminar el objeto
            for (Objeto m : objetos) {
                em.remove(m);
            }
            transaction.commit();
            System.out.println(objetos.size()+" Registros eliminados.");
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



    // ************** UTILS ***************
    /* Method to delete Objeto's table data */
    public void deleteObjetoTableData() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Objeto> result = em.createQuery("from Objeto", Objeto.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Objeto::getId)).toList();
        for (Objeto objeto : result) {
            deleteObjeto(objeto.getId());
        }
        em.getTransaction().commit();
        em.close();
    }

    /* Method to get the last Objeto ID */
    public int getObjetosLastId() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Objeto> result = em.createQuery("from Objeto", Objeto.class).getResultList();
        em.getTransaction().commit();
        em.close();
        return result.size();
    }

    public void setHeaders() {
        Objeto objeto = new Objeto();
        List<Field> fields = Arrays.asList(objeto.getClass().getDeclaredFields());
        for (Field field : fields){
            colsName.add(field.getName());
            if(field.getType().getName().equals("java.lang.String")) colsDataType.add("String");
            else colsDataType.add("int");
        }
    }
}