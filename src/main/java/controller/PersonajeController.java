package controller;

import model.Objeto;
import model.Personaje;
import javax.persistence.*;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.util.*;

/**
 *  Clase que permite realizar una serie de acciones sobre la entidad Personaje.
 */
public class PersonajeController {
    private Connection connection;
    private EntityManagerFactory entityManagerFactory;
    Scanner sc = new Scanner(System.in);
    public List<String> colsName = new ArrayList<>();
    public List<String> colsDataType = new ArrayList<>();

    /**
     * Constructor de la clase.
     */
    public PersonajeController(Connection connection) {
        this.connection = connection;
    }

    /**
     * Constructor de la clase utilizado en la clase main,
     * genera los datos para rellenar los atributos globales colsName y colsDataType.
     */
    public PersonajeController(Connection connection, EntityManagerFactory entityManagerFactory) {
        this.connection = connection;
        this.entityManagerFactory = entityManagerFactory;
        setHeaders();
    }

    /**
     * Función que rellena de información las entidades a partir del CSV específico para la clase.
     */
    public void getDataFromPersonajeFile(){
        List<Personaje> listPersonaje = new ArrayList<>();
        List<String[]> listCSV = MainController.GetDataFromCSV("personaje");
        listCSV.forEach(row -> {
            listPersonaje.add(new Personaje(Integer.parseInt(row[0]),row[1],row[2],
                                            Integer.parseInt(row[3]),row[4],row[5],
                                            row[6],row[7],row[8],Integer.parseInt(row[9])));
        });
        listPersonaje.forEach(this::addPersonaje);
    }

    /**
     * Método para listar las entidades que existen en la tabla de la BBDD equivalente a la clase.
     *
     * @return Lista de objetos de la entidad que controla la clase.
     */
    public List<Personaje> listPersonajes() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Personaje> result = em.createQuery("from Personaje", Personaje.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Personaje::getId)).toList();
        for (Personaje personaje : result) {
            System.out.println(personaje.toString());
        }
        em.getTransaction().commit();
        em.close();
        return result;
    }

    /**
     * Método que permite introducir un objeto en la BBDD.
     *
     * @param personaje Objeto de la clase que se controla.
     */
    public void addPersonaje(Personaje personaje) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Personaje PersonajeExists = em.find(Personaje.class, personaje.getId());
        if (PersonajeExists == null) {
            System.out.print("insert personaje -> ");
            System.out.println(personaje);
            em.persist(personaje);
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

        Personaje personaje = em.find(Personaje.class, idPersonaje);
        Objeto objeto = em.find(Objeto.class, idObjeto);

        personaje.getObjetosEquipados().add(objeto);
        personaje.setObjetosEquipados(personaje.getObjetosEquipados());

        em.merge(personaje);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Método para insertar un nuevo elemento a la BBDD.
     */
    public void addNewPersonaje(){
        Scanner sc = new Scanner(System.in);
        String icono, nombre, daño, cadencia, vel_proyectil, rango, velocidad;
        int id, vida=0, suerte=0;
        boolean valorValido;

        id = getPersonajesLastId()+1;

        System.out.print("Escribe el valor para la columna icono: ");
        icono = sc.nextLine();

        System.out.print("Escribe el valor para la columna nombre: ");
        nombre = sc.nextLine();

        do {
            System.out.print("Escribe el valor para la columna vida: ");
            try {
                vida = Integer.parseInt(sc.nextLine());
                valorValido = true;
            } catch (Exception e) {
                System.out.println("\n*** Indica un valor númerico válido. ***");
                valorValido = false;
            }
        }while(!valorValido);

        System.out.print("Escribe el valor para la columna daño: ");
        daño = sc.nextLine();

        System.out.print("Escribe el valor para la columna cadencia: ");
        cadencia = sc.nextLine();

        System.out.print("Escribe el valor para la columna vel_proyectil: ");
        vel_proyectil = sc.nextLine();

        System.out.print("Escribe el valor para la columna rango: ");
        rango = sc.nextLine();

        System.out.print("Escribe el valor para la columna velocidad: ");
        velocidad = sc.nextLine();

        do {
            System.out.print("Escribe el valor para la columna vida: ");
            try {
                suerte = Integer.parseInt(sc.nextLine());
                valorValido = true;
            } catch (Exception e) {
                System.out.println("\n*** Indica un valor númerico válido. ***");
                valorValido = false;
            }
        }while(!valorValido);

        addPersonaje(new Personaje(id, icono, nombre, vida, daño, cadencia, vel_proyectil, rango, velocidad, suerte));
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

        TypedQuery<Personaje> query = em.createQuery("FROM Personaje WHERE "+nombreColumna+" = :texto", Personaje.class);
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
    public void selectPersonajeTableColumn(int numColumna) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        List<Personaje> result = em.createQuery("from Personaje", Personaje.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Personaje::getId)).toList();
        for (Personaje personaje : result) {
            switch (numColumna) {
                case 0 -> System.out.println(personaje.getId());
                case 1 -> System.out.println(personaje.getIcono());
                case 2 -> System.out.println(personaje.getNombre());
                case 3 -> System.out.println(personaje.getVida());
                case 4 -> System.out.println(personaje.getDaño());
                case 5 -> System.out.println(personaje.getCadencia());
                case 6 -> System.out.println(personaje.getVel_proyectil());
                case 7 -> System.out.println(personaje.getRango());
                case 8 -> System.out.println(personaje.getVelocidad());
                case 9 -> System.out.println(personaje.getSuerte());
            }
        }

        em.getTransaction().commit();
        em.close();
    }

    /**
     * Método para actualizar los campos del registro seleccionado.
     *
     * @param personajeId Id del registro seleccionado.
     * @param numColumna Columna seleccionada para hacer la selección.
     */
    public void updatePersonaje(Integer personajeId, int numColumna) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Personaje personaje = em.find(Personaje.class, personajeId);
        System.out.print("Indica el dato modificado: ");
        switch (numColumna) {
            case 1 -> personaje.setIcono(sc.nextLine());
            case 2 -> personaje.setNombre(sc.nextLine());
            case 3 -> {
                personaje.setVida(sc.nextInt());
                sc.nextLine();
            }
            case 4 -> personaje.setDaño(sc.nextLine());
            case 5 -> personaje.setCadencia(sc.nextLine());
            case 6 -> personaje.setVel_proyectil(sc.nextLine());
            case 7 -> personaje.setRango(sc.nextLine());
            case 8 -> personaje.setVelocidad(sc.nextLine());
            case 9 -> {
                personaje.setSuerte(sc.nextInt());
                sc.nextLine();
            }
        }
        em.merge(personaje);
        em.getTransaction().commit();
        em.close();
        listPersonajes();
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

            // Seleccionar el personaje que coincide con el nombre proporcionado
            Query query = em.createQuery("UPDATE Personaje SET " + nombreColumna + " = :valorNuevo WHERE " + nombreColumna + " = :valorAntiguo");
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
     * @param personajeId Id del registro seleccionado.
     */
    public void deletePersonaje(int personajeId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        Personaje personaje = em.find(Personaje.class, personajeId);
        deleteRelation(personaje);

        em.remove(personaje);
        em.getTransaction().commit();
        em.close();
    }

    public void deleteRelation(Personaje personaje){
        personaje.getObjetosEquipados().forEach(objeto -> {
            objeto.getPersonajesQueEquipan().remove(personaje);
        });
    }

    public void deleteRelation(int personajeId){
        EntityManager em = entityManagerFactory.createEntityManager();

        em.getTransaction().begin();
        Personaje personaje = em.find(Personaje.class, personajeId);

        personaje.getObjetosEquipados().forEach(objeto -> {
            objeto.getPersonajesQueEquipan().remove(personaje);
        });

        em.merge(personaje);
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Método para eliminar todos los registros que cumplan una condición
     * cuya columna seleccionada coincida con el valor indicado.
     *
     * @param numColumna Columna seleccionada para hacer la selección.
     */
    public void eliminarPersonajePorCondicionDeTexto(int numColumna) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        String nombreColumna = colsName.get(numColumna);
        boolean esInteger = colsDataType.get(numColumna).equals("int");

        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            System.out.print("Cuál es el texto condición para eliminar los registros: ");
            // Seleccionar el personaje que coincide con el nombre proporcionado
            TypedQuery<Personaje> query = em.createQuery(
                    "SELECT m FROM Personaje m WHERE "+nombreColumna+" = :texto", Personaje.class);

            if (esInteger){
                query.setParameter("texto", Integer.parseInt(sc.nextLine()));
            }else{
                query.setParameter("texto", sc.nextLine());
            }

            List<Personaje> personajes = query.getResultList();
            // Eliminar el personaje
            for (Personaje m : personajes) {
                em.remove(m);
            }
            transaction.commit();
            System.out.println(personajes.size()+" Registros eliminados.");
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
    public void deletePersonajeTableData() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Personaje> result = em.createQuery("from Personaje", Personaje.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Personaje::getId)).toList();
        for (Personaje personaje : result) {
            deletePersonaje(personaje.getId());
        }
        em.getTransaction().commit();
        em.close();
    }

    /**
     * Método que devuelve el id del último registro introducido en la BBDD.
     */
    public int getPersonajesLastId() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Personaje> result = em.createQuery("from Personaje", Personaje.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Personaje::getId)).toList();
        em.getTransaction().commit();
        em.close();
        return result.get(result.size()-1).getId();
    }

    /**
     * Genera los datos para rellenar los atributos globales colsName y colsDataType.
     */
    public void setHeaders() {
        Personaje personaje = new Personaje();
        List<Field> fields = Arrays.asList(personaje.getClass().getDeclaredFields());
        for (Field field : fields){
            if(!field.getName().equals("objetosEquipados")) colsName.add(field.getName());
            if(field.getType().getName().equals("java.lang.String")) colsDataType.add("String");
            else colsDataType.add("int");
        }
        colsDataType.remove(colsDataType.size()-1);
    }
}
