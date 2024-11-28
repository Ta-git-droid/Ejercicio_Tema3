package com.example.ejercicio_tema3;

import androidx.annotation.NonNull;


public class Animal {

    private String nombre;
    private int imagen; // ID del recurso de la imagen
    private TipoAnimal tipo;
    private int edad; // En años
    private String descripcion;

    // Enumeración para representar los tipos de animales.
    public enum TipoAnimal {
        PERRO,
        GATO
    }

    // Constructor.
    public Animal(String nombre, int imagen, TipoAnimal tipo, int edad, String descripcion) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.tipo = tipo;
        this.edad = edad;
        this.descripcion = descripcion;
    }

    // Getters y Setters.
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public TipoAnimal getTipo() {
        return tipo;
    }

    public void setTipo(TipoAnimal tipo) {
        this.tipo = tipo;
    }

    public int getEdad() {
        return edad;
    }

    public void setEdad(int edad) {
        this.edad = edad;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Método toString para mostrar la información del animal.
    @NonNull
    @Override
    public String toString() {
        return "Animal [Nombre: " + nombre + ", Imagen: " + imagen +
                ", Tipo: " + tipo + ", Edad: " + edad +
                " años, Descripción: " + descripcion + "]";
    }
}
