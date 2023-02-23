package model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * Clase que almacena la entidad del UML Monstruo.
 *
 * @author Ángel Castro Merino
 */
@Entity
@Access(AccessType.FIELD)
@Table(name = "monstruo")
public class Monstruo implements Serializable {
    @Id
    @Column(name = "idmonstruo")
    int id;

    @Column(name = "icono", length = 500)
    String icono;

    @Column(name = "nombre", length = 45)
    String nombre;

    @Column(name = "vida")
    int vida;

    @Column(name = "descripcion", length = 500)
    String descripcion;

    /**
     * Constrctor simple de la Clase.
     */
    public Monstruo(){}

    /**
     *
     *
     * @param id            Identificador del objeto.
     * @param icono         Atributo que indica la dirección de la imagen recogida en la web.
     * @param nombre        Atributo que indica el nombre recogido en la web.
     * @param vida          Atributo que indica la vida recogida en la web.
     * @param descripcion   Atributo que indica la descripción recogida en la web.
     */
    public Monstruo(int id, String icono, String nombre, int vida, String descripcion) {
        this.id = id;
        this.icono = icono;
        this.nombre = nombre;
        this.vida = vida;
        this.descripcion = descripcion;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getIcono() {
        return icono;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getVida() {
        return vida;
    }

    public void setVida(int vida) {
        this.vida = vida;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    /**
     * Función que devuelve una cadena de texto con los nombres de los atributos y los valores
     * de la clase en un formato específico para evitar errores y pérdidas de información.
     *
     * @return Cadena de texto con los nombres de los atributos y sus valores.
     */
    @Override
    public String toString() {
        return "idmonstruo: "+id+"\t|\t" +
               "nombre: "+nombre+"\t|\t" +
               "vida: "+vida+"\t|\t" +
               "descripcion: "+descripcion+"\t|\t" +
               "icono: "+icono;
    }
}