package com.example.proyectograndeaadcontactos;

public class Contacto {
    private long Id;
    private String Nombre;
    private String Numero;

    public String getNumero() {
        return Numero;
    }

    public void setNumero(String numero) {
        Numero = numero;
    }

    public Contacto() {
    }

    public Contacto(int id, String nombre) {
        Id = id;
        Nombre = nombre;
    }

    public long getId() {
        return Id;
    }

    public void setId(long id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    @Override
    public String toString() {
        return "Contacto: "+Nombre+" ";
    }
}
