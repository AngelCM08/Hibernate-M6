package controller;

import model.Objeto;
import model.Personaje;
import org.hibernate.engine.jdbc.spi.SqlExceptionHelper;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;

/**
 *  Clase que permite realizar una serie de acciones sobre la entidad Objeto.
 */
public class ObjetoController {
    private Connection connection;
    private EntityManagerFactory entityManagerFactory;
    Scanner sc = new Scanner(System.in);
    public List<String> colsName = new ArrayList<>();
    public List<String> colsDataType = new ArrayList<>();

    /**
     * Constructor de la clase.
     */
    public ObjetoController(Connection connection) {
        this.connection = connection;
    }

    /**
     * Constructor de la clase utilizado en la clase main,
     * genera los datos para rellenar los atributos globales colsName y colsDataType.
     */
    public ObjetoController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
        setHeaders();
    }
    /**
     * Función que rellena de información las entidades a partir del CSV específico para la clase.
     */
    public void getDataFromObjetoFile(){
        List<Objeto> listObjeto = new ArrayList<>();
        List<String[]> listCSV = MainController.GetDataFromCSV("objeto");
        listCSV.forEach(row -> {
            listObjeto.add(new Objeto(Integer.parseInt(row[0]),row[1],row[2],row[3]));
        });
        listObjeto.forEach(this::addObjeto);
    }

    /**
     * Método para listar las entidades que existen en la tabla de la BBDD equivalente a la clase.
     *
     * @return Lista de objetos de la entidad que controla la clase.
     */
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

    /**
     * Método que permite introducir un objeto en la BBDD.
     *
     * @param objeto Objeto de la clase que se controla.
     */
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

    /**
     * Método para relacionar un personaje y un objeto.
     */
    public void addRelation(int idPersonaje, int idObjeto){
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        Objeto objeto = em.find(Objeto.class, idObjeto);
        Personaje personaje = em.find(Personaje.class, idPersonaje);

        objeto.getPersonajesQueEquipan().add(personaje);
        objeto.setPersonajesQueEquipan(objeto.getPersonajesQueEquipan());

        em.merge(objeto);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Método para insertar un nuevo elemento a la BBDD.
     */
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

    /**
     * Método para seleccionar todos los registros que cumplan una condición
     * cuya columna seleccionada coincida con el valor indicado.
     *
     * @param numColumna Columna seleccionada para hacer la selección.
     */
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

    /**
     * Método para seleccionar todos los campos de la columna seleccionada.
     *
     * @param numColumna Columna seleccionada para hacer la selección.
     */
    public void selectObjetoTableColumn(int numColumna) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        List<Objeto> result = em.createQuery("from Objeto", Objeto.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Objeto::getId)).toList();
        for (Objeto objeto : result) {
            switch (numColumna) {
                case 0 -> System.out.println(objeto.getId());
                case 1 -> System.out.println(objeto.getIcono());
                case 2 -> System.out.println(objeto.getNombre());
                case 3 -> System.out.println(objeto.getDescripcion());
            }
        }

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Método para actualizar los campos del registro seleccionado.
     *
     * @param objetoId Id del registro seleccionado.
     * @param numColumna Columna seleccionada para hacer la selección.
     */
    public void updateObjeto(Integer objetoId, int numColumna) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Objeto objeto = em.find(Objeto.class, objetoId);
        System.out.print("Indica el dato modificado: ");
        switch (numColumna) {
            case 1 -> objeto.setIcono(sc.nextLine());
            case 2 -> objeto.setNombre(sc.nextLine());
            case 3 -> objeto.setDescripcion(sc.nextLine());
        }
        em.merge(objeto);
        em.getTransaction().commit();
        em.close();
        listObjetos();
    }

    /**
     * Método para actualizar todos los registros que cumplan una condición
     * cuya columna seleccionada coincida con el valor indicado.
     *
     * @param numColumna Columna seleccionada para hacer la selección.
     */
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

    /**
     * Método para eliminar el registro seleccionado.
     *
     * @param objetoId Id del registro seleccionado.
     */
    public void deleteObjeto(int objetoId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        Objeto objeto = em.find(Objeto.class, objetoId);
        deleteRelation(objeto);

        em.remove(objeto);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteRelation(Objeto objeto){
        objeto.getPersonajesQueEquipan().forEach(personaje -> {
            personaje.getObjetosEquipados().remove(objeto);
        });
    }

    public void deleteRelation(int objetoId){
        EntityManager em = entityManagerFactory.createEntityManager();

        em.getTransaction().begin();
        Objeto objeto = em.find(Objeto.class, objetoId);

        objeto.getPersonajesQueEquipan().forEach(personaje -> {
            personaje.getObjetosEquipados().remove(objeto);
        });

        em.merge(objeto);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Método para eliminar todos los registros que cumplan una condición
     * cuya columna seleccionada coincida con el valor indicado.
     *
     * @param numColumna Columna seleccionada para hacer la selección.
     */
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

    /**
     * Método para eliminar todos los registros de la tabla controlada.
     */
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

    /**
     * Método que devuelve el id del último registro introducido en la BBDD.
     */
    public int getObjetosLastId() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Objeto> result = em.createQuery("from Objeto", Objeto.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Objeto::getId)).toList();
        em.getTransaction().commit();
        em.close();
        return result.get(result.size()-1).getId();
    }

    /**
     * Genera los datos para rellenar los atributos globales colsName y colsDataType.
     */
    public void setHeaders() {
        Objeto objeto = new Objeto();
        List<Field> fields = Arrays.asList(objeto.getClass().getDeclaredFields());
        for (Field field : fields){
            if(!field.getName().equals("personajesQueEquipan")) colsName.add(field.getName());
            if(field.getType().getName().equals("java.lang.String")) colsDataType.add("String");
            else colsDataType.add("int");
        }
    }
}