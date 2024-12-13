package com.example.ejercicio_tema3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

/**
 * Representa un animal con sus características.
 */

public class Animal  implements Parcelable {

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
        if (edad >= 0) {
            this.edad = edad;
        } else {
            throw new IllegalArgumentException("La edad no puede ser negativa");
        }
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            this.descripcion = descripcion;
        } else {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
    }


    // Métodos para Parceable

    protected Animal (Parcel in) {
        nombre = in.readString ();
        imagen = in.readInt();
        tipo = TipoAnimal.valueOf ( in.readString () );
        edad = in.readInt ();
        descripcion = in.readString ();
    }

    public static final Creator<Animal> CREATOR = new Creator<Animal> () {
        @Override
        public Animal createFromParcel(Parcel parcel) {
            return new Animal(parcel);
        }

        @Override
        public Animal[] newArray(int i) {
            return new Animal[i];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel , int i) {
        parcel.writeString( nombre );
        parcel.writeInt ( imagen );
        parcel.writeString ( tipo.name () );
        parcel.writeInt ( edad );
        parcel.writeString ( descripcion );
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
