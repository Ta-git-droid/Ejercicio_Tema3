package com.example.ejercicio_tema3;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;


/**
 * Representa un animal con sus características.
 * La clase implementa Parcelable para permitir pasar objetos de tipo Animal entre actividades.
 */
public class Animal implements Parcelable {

    // Atributos de la clase
    private String nombre; // Nombre del animal
    private int imagen; // ID del recurso de la imagen que representa al animal
    private TipoAnimal tipo; // Tipo de animal (perro o gato)
    private int edad; // Edad del animal en años
    private String descripcion; // Descripción del animal


    /**
     * Enumeración para representar los tipos de animales.
     * Contiene dos valores: PERRO y GATO.
     */
    public enum TipoAnimal {
        PERRO, // Representa un perro
        GATO   // Representa un gato
    }


    /**
     * Constructor que inicializa los atributos de la clase.
     * @param nombre Nombre del animal.
     * @param imagen ID del recurso de la imagen.
     * @param tipo Tipo de animal (PERRO o GATO).
     * @param edad Edad del animal.
     * @param descripcion Descripción del animal.
     */
    public Animal(String nombre, int imagen, TipoAnimal tipo, int edad, String descripcion) {
        this.nombre = nombre;
        this.imagen = imagen;
        this.tipo = tipo;
        this.edad = edad;
        this.descripcion = descripcion;
    }


    // Métodos Getters y Setters para acceder y modificar los atributos

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


    /**
     * Establece la edad del animal, asegurándose de que no sea negativa.
     * @param edad Edad del animal en años.
     * @throws IllegalArgumentException Si la edad es negativa.
     */
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


    /**
     * Establece la descripción del animal, asegurándose de que no sea nula ni vacía.
     * @param descripcion Descripción del animal.
     * @throws IllegalArgumentException Si la descripción está vacía o es nula.
     */
    public void setDescripcion(String descripcion) {
        if (descripcion != null && !descripcion.trim().isEmpty()) {
            this.descripcion = descripcion;
        } else {
            throw new IllegalArgumentException("La descripción no puede estar vacía");
        }
    }


    /**
     * Métodos para la implementación de Parcelable, necesarios para pasar objetos entre componentes de la aplicación.

     * Constructor que recibe un Parcel para restaurar los valores de los atributos.
     * @param in Parcel que contiene los datos serializados del objeto.
     */
    protected Animal(Parcel in) {
        nombre = in.readString(); // Lee el nombre del animal
        imagen = in.readInt(); // Lee el ID de la imagen
        tipo = TipoAnimal.valueOf(in.readString()); // Lee el tipo de animal (PERRO o GATO)
        edad = in.readInt(); // Lee la edad
        descripcion = in.readString(); // Lee la descripción
    }

    // Creador que permite crear instancias de Animal a partir de un Parcel
    public static final Creator<Animal> CREATOR = new Creator<Animal>() {
        @Override
        public Animal createFromParcel(Parcel parcel) {
            return new Animal(parcel); // Crea un nuevo objeto Animal a partir de un Parcel
        }

        @Override
        public Animal[] newArray(int i) {
            return new Animal[i]; // Crea un array de objetos Animal
        }
    };

    // Devuelve un valor entero que describe el contenido del objeto (generalmente 0 si no hay ficheros o recursos especiales).
    @Override
    public int describeContents() {
        return 0;
    }


    /**
     * Método necesario para la implementación de Parcelable.
     * Escribe los datos del objeto en un Parcel para que pueda ser transmitido entre componentes.
     * @param parcel El Parcel donde se escribirán los datos.
     * @param flags Flags adicionales que pueden influir en la escritura (generalmente 0).
     */
    @Override
    public void writeToParcel(@NonNull Parcel parcel, int flags) {
        parcel.writeString(nombre); // Escribe el nombre
        parcel.writeInt(imagen); // Escribe el ID de la imagen
        parcel.writeString(tipo.name()); // Escribe el nombre del tipo de animal
        parcel.writeInt(edad); // Escribe la edad
        parcel.writeString(descripcion); // Escribe la descripción
    }


    /**
     * Método toString para obtener una representación textual del objeto Animal.
     * @return Una cadena que representa el objeto Animal.
     */
    @NonNull
    @Override
    public String toString() {
        return "Animal [Nombre: " + nombre + ", Imagen: " + imagen +
                ", Tipo: " + tipo + ", Edad: " + edad +
                " años, Descripción: " + descripcion + "]";
    }
}