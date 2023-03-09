package model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity
@Access(AccessType.FIELD)
@Table(name = "personaje")
public class Personaje implements Serializable {
    @Id
    @Column(name = "idpersonaje")
    int id;

    @Column(name = "icono", length = 500)
    String icono;

    @Column(name = "nombre", length = 45)
    String nombre;

    @Column(name = "vida")
    int vida;

    @Column(name = "daño", length = 45)
    private String daño;

    @Column(name = "cadencia", length = 45)
    private String cadencia;

    @Column(name = "vel_proyectil", length = 45)
    private String vel_proyectil;

    @Column(name = "rango", length = 45)
    private String rango;

    @Column(name = "velocidad", length = 45)
    private String velocidad;

    @Column(name = "suerte", length = 45)
    private int suerte;

    @ManyToMany(cascade = {
            CascadeType.PERSIST,
            CascadeType.MERGE
    })
    @JoinTable(name = "equipar",
            joinColumns = @JoinColumn(name = "idpersonaje"),
            inverseJoinColumns = @JoinColumn(name = "idobjeto")
    )
    private List<Objeto> objetoEquipado = new ArrayList<>();

    /**
     * Constrctor simple de la Clase necesario para el formateado a XML.
     */
    public Personaje(){}

    /**
     * Constructor utilizado por la clase Personajes para crear
     * todos los objetos Personaje encontrados en la página web.
     * Cabe destacar que se filtran algunos de los atributos utilizando
     * una lista auxiliar para obtener información más limpia.
     *
     * @param id
     * @param icono
     * @param nombre
     * @param vida
     * @param daño
     * @param cadencia
     * @param vel_proyectil
     * @param rango
     * @param velocidad
     * @param suerte
     */
    public Personaje(int id, String icono, String nombre, int vida, String daño,
                     String cadencia, String vel_proyectil, String rango,
                     String velocidad, int suerte) {
        this.id = id;
        this.icono = icono;
        this.nombre = nombre;
        this.vida = vida;
        this.daño = daño;
        this.cadencia = cadencia;
        this.vel_proyectil = vel_proyectil;
        this.rango = rango;
        this.velocidad = velocidad;
        this.suerte = suerte;
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

    public String getDaño() {
        return daño;
    }

    public void setDaño(String daño) {
        this.daño = daño;
    }

    public String getCadencia() {
        return cadencia;
    }

    public void setCadencia(String cadencia) {
        this.cadencia = cadencia;
    }

    public String getVel_proyectil() {
        return vel_proyectil;
    }

    public void setVel_proyectil(String vel_proyectil) {
        this.vel_proyectil = vel_proyectil;
    }

    public String getRango() {
        return rango;
    }

    public void setRango(String rango) {
        this.rango = rango;
    }

    public String getVelocidad() {
        return velocidad;
    }

    public void setVelocidad(String velocidad) {
        this.velocidad = velocidad;
    }

    public int getSuerte() {
        return suerte;
    }

    public void setSuerte(int suerte) {
        this.suerte = suerte;
    }

    public List<Objeto> getObjetoEquipado() {
        return objetoEquipado;
    }

    public void setObjetoEquipado(List<Objeto> objetoEquipado) {
        this.objetoEquipado = objetoEquipado;
    }

    /**
     * Función que devuelve una cadena de texto con los nombres de los atributos y los valores
     * de la clase en un formato específico para evitar errores y pérdidas de información.
     *
     * @return Cadena de texto con los nombres de los atributos y sus valores.
     */
    @Override
    public String toString() {
        String objetosEquipados = getObjetoEquipado().toString();
        return "idPersonaje: "+id+"\t|\t" +
                "nombre: "+nombre+"\t|\t" +
                "vida: "+vida+"\t|\t" +
                "daño: "+daño+"\t|\t" +
                "cadencia: "+cadencia+"\t|\t" +
                "vel_proyectil: "+vel_proyectil+"\t|\t" +
                "rango: "+rango+"\t|\t" +
                "velocidad: "+velocidad+"\t|\t" +
                "suerte: "+suerte+"\t|\t" +
                "Objetos Equipados: ["+getObjetoEquipado().toString()+"]";
    }
}