package controller;

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

    public void getDataFromMonstruoFile(){
        List<Monstruo> listMonstruo = new ArrayList<>();
        List<String[]> listCSV = MainController.GetDataFromCSV("monstruo");
        listCSV.forEach(row -> {
            listMonstruo.add(new Monstruo(Integer.parseInt(row[0]),row[1],row[2],Integer.parseInt(row[3]),row[4]));
        });
        listMonstruo.forEach(this::addMonstruo);
    }

    /* Method to READ all Monstruo */
    public List<Monstruo> listMonstruos() {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        List<Monstruo> result = em.createQuery("from Monstruo", Monstruo.class).getResultList();
        result = result.stream().sorted(Comparator.comparingInt(Monstruo::getId)).toList();
        for (Monstruo monstruo : result) {
            System.out.println(monstruo.toString());
        }
        em.getTransaction().commit();
        em.close();
        return result;
    }

    /* Method to CREATE a Monstruo in the database */
    public void addMonstruo(Monstruo monstruo) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Monstruo MonstruoExists = em.find(Monstruo.class, monstruo.getId());
        if (MonstruoExists == null) {
            System.out.print("insert monstruo -> ");
            System.out.println(monstruo);
            em.persist(monstruo);
        }
        em.getTransaction().commit();
        em.close();
    }

    public void addNewMonstruo(){
        Scanner sc = new Scanner(System.in);
        String icono, nombre, descripcion;
        int id, vida=0;
        boolean valorValido;

        id = getMonstruosLastId()+1;

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

        System.out.print("Escribe el valor para la columna descripcion: ");
        descripcion = sc.nextLine();



        addMonstruo(new Monstruo(id, icono, nombre, vida, descripcion));
    }

    /* Method to SELECT Monstruo's registers */
    public void selectRegisterByCondition(int numColumna){
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        String nombreColumna = colsName.get(numColumna);
        boolean esInteger = colsDataType.get(numColumna).equals("int");

        System.out.print("Cuál es el valor que quieres seleccionar: ");
        String valor = sc.nextLine();

        TypedQuery<Monstruo> query = em.createQuery("FROM Monstruo WHERE "+nombreColumna+" = :texto", Monstruo.class);
        if (esInteger){
            query.setParameter("texto", Integer.parseInt(valor));
        }else{
            query.setParameter("texto", valor);
        }
        query.getResultList().forEach(System.out::println);

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

    /* Method to UPDATE activity for Monstruo */
    public void updateMonstruo(Integer monstruoId, int column) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();

        Monstruo monstruo = em.find(Monstruo.class, monstruoId);

        System.out.print("Indica el dato modificado: ");
        switch (column) {
            case 1 -> monstruo.setIcono(sc.nextLine());
            case 2 -> monstruo.setNombre(sc.nextLine());
            case 3 -> {
                monstruo.setVida(sc.nextInt());
                sc.nextLine();
            }
            case 4 -> monstruo.setDescripcion(sc.nextLine());
        }
        em.merge(monstruo);
        em.getTransaction().commit();
        em.close();

        listMonstruos();
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

            // Seleccionar el monstruo que coincide con el nombre proporcionado
            Query query = em.createQuery("UPDATE Monstruo SET " + nombreColumna + " = :valorNuevo WHERE " + nombreColumna + " = :valorAntiguo");
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

    /* Method to DELETE Monstruo from the records */
    public void deleteMonstruo(int monstruoId) {
        EntityManager em = entityManagerFactory.createEntityManager();
        em.getTransaction().begin();
        Monstruo monstruo = em.find(Monstruo.class, monstruoId);
        em.remove(monstruo);
        em.getTransaction().commit();
        em.close();
    }

    /* DELETE by text */
    public void eliminarMonstruoPorCondicionDeTexto(int numColumna) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        String nombreColumna = colsName.get(numColumna);
        boolean esInteger = colsDataType.get(numColumna).equals("int");

        try {
            em = entityManagerFactory.createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();

            System.out.print("Cuál es el texto condición para eliminar los registros: ");
            // Seleccionar el monstruo que coincide con el nombre proporcionado
            TypedQuery<Monstruo> query = em.createQuery(
                    "SELECT m FROM Monstruo m WHERE "+nombreColumna+" = :texto", Monstruo.class);

            if (esInteger){
                query.setParameter("texto", Integer.parseInt(sc.nextLine()));
            }else{
                query.setParameter("texto", sc.nextLine());
            }

            List<Monstruo> monstruos = query.getResultList();
            // Eliminar el monstruo
            for (Monstruo m : monstruos) {
                em.remove(m);
            }
            transaction.commit();
            System.out.println(monstruos.size()+" Registros eliminados.");
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
