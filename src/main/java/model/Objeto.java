package model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Access(AccessType.FIELD)
@Table(name = "objeto")
public class Objeto implements Serializable {
    @Id
    @Column(name = "idobjeto")
    int id;

    @Column(name = "icono", length = 500)
    String icono;

    @Column(name = "nombre", length = 45)
    String nombre;

    @Column(name = "descripcion", length = 500)
    String descripcion;

    /**
     * Constrctor simple de la Clase necesario para el formateado a XML.
     */
    public Objeto(){}

    /**
     * Constructor para crear todos los objetos.
     *
     * @param id
     * @param icono
     * @param nombre
     * @param descripcion
     */
    public Objeto(int id, String icono, String nombre, String descripcion) {
        this.id = id;
        this.icono = icono;
        this.nombre = nombre;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    @Override
    public String toString() {
        return "idObjeto: "+id+"\t|\t" +
                "nombre: "+nombre+"\t|\t" +
                "descripcion: "+descripcion+"\t|\t" +
                "icono: "+icono;
    }
}