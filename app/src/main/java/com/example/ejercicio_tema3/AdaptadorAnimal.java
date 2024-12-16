package com.example.ejercicio_tema3;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Adaptador:
 * Este adaptador maneja y muestra una lista de objetos Animal en un RecyclerView.
 * Permite seleccionar un animal, agregarlo o eliminarlo de los favoritos.
 * Además, permite actualizar dinámicamente la lista de animales y favoritos.
 */
public class AdaptadorAnimal extends RecyclerView.Adapter<AdaptadorAnimal.AnimalViewHolder> {

    private Context context; // Contexto asociado a la aplicación o actividad.
    private List<Animal> listaAnimales; // Lista de animales (puede estar filtrada).
    private Set<Animal> favoritos = new HashSet<>(); // Conjunto de animales favoritos (para evitar duplicados).
    private int posicionSeleccionada = -1; // Índice del animal actualmente seleccionado.
    private OnAnimalSeleccionadoListener listener; // Listener para manejar la selección de un animal.
    private FavoritosActualizadosListener favoritosListener; // Listener para manejar actualizaciones en la lista de favoritos.
    private Animal animalSeleccionado; // Referencia al animal seleccionado.
    private List<Animal> listaFavoritos; // Listener para gestionar actualizaciones de favoritos.

    /**
     * Constructor con listas de animales y favoritos.
     * @param context Contexto de la aplicación.
     * @param listaAnimales Lista de animales que se mostrará en el RecyclerView.
     * @param listaFavoritos Lista de animales marcados como favoritos.
     */
    public AdaptadorAnimal(Context context, List<Animal> listaAnimales, List<Animal> listaFavoritos) {
        this.context = context;
        this.listaAnimales = listaAnimales;
        this.favoritos.addAll(listaFavoritos); // Sincronizamos la lista de favoritos con los animales favoritos.
    }

    public AdaptadorAnimal(List<Animal> listaAnimales) {
        this.listaAnimales = listaAnimales;
        this.favoritos = new HashSet<>();
    }

    public void setFavoritosActualizadosListener(FavoritosActualizadosListener favoritosListener) {
        this.favoritosListener = favoritosListener;
    }


    /**
     * Inflar el layout de cada item y crear el ViewHolder.
     * @param parent ViewGroup padre donde se inflará el item.
     * @param viewType Tipo de vista a inflar (no usado aquí).
     * @return Un nuevo AnimalViewHolder con el layout inflado.
     */
    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal, parent, false);
        return new AnimalViewHolder(itemView);
    }

    /**
     * Configura los datos de cada ítem de la lista de animales.
     * @param holder El ViewHolder donde se colocarán los datos.
     * @param position Posición del animal en la lista.
     */
    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        // Verificamos que la posición seleccionada no esté fuera de la lista
        if (posicionSeleccionada >= listaAnimales.size()) {
            posicionSeleccionada = -1;
        }

        Animal animal = listaAnimales.get(position);

        // Asignar los valores de texto e imagen del animal al ViewHolder
        holder.nombre.setText(animal.getNombre());
        holder.edad.setText(context.getString(R.string.edad_placeholder, animal.getEdad()));
        holder.descripcion.setText(animal.getDescripcion());
        holder.imagen.setImageResource(animal.getImagen());

        // Actualizar el icono de favorito según si el animal está marcado como favorito
        holder.iconoFavorito.setImageResource(favoritos.contains(animal) ? R.drawable.corazon_relleno : R.drawable.corazon_vacio);

        // Acción cuando el usuario hace clic en el icono de favorito
        holder.iconoFavorito.setOnClickListener(v -> {
            if (favoritos.contains(animal)) {
                favoritos.remove(animal); // Eliminar del conjunto de favoritos
                holder.iconoFavorito.setImageResource(R.drawable.corazon_vacio); // Cambiar el icono a vacío
            } else {
                favoritos.add(animal); // Agregar al conjunto de favoritos
                holder.iconoFavorito.setImageResource(R.drawable.corazon_relleno); // Cambiar el icono a relleno
            }

            // Notificar al listener de favoritos que la lista ha cambiado
            if (favoritosListener != null) {
                favoritosListener.onFavoritosActualizados(new ArrayList<>(favoritos));
            }
        });

        // Cambiar el color de fondo si el animal está seleccionado
        holder.itemView.setBackgroundColor(posicionSeleccionada == position ? Color.LTGRAY : Color.TRANSPARENT);

        // Evento de clic para seleccionar un animal
        holder.itemView.setOnClickListener(v -> {
            int posicionAnterior = posicionSeleccionada;
            posicionSeleccionada = holder.getAdapterPosition(); // Actualizar la posición seleccionada
            if (posicionAnterior != -1) notifyItemChanged(posicionAnterior); // Notificar que el ítem anterior cambió
            notifyItemChanged(posicionSeleccionada); // Notificar que el ítem actual cambió

            if (listener != null) {
                listener.onAnimalSeleccionado(posicionSeleccionada); // Llamar al listener para manejar la selección
            }
        });
    }

    /**
     * Devuelve el tamaño de la lista de animales.
     * @return El número de elementos en la lista de animales.
     */
    @Override
    public int getItemCount() {
        return listaAnimales.size();
    }

    /**
     * Actualiza la lista de animales en el adaptador.
     * @param nuevaListaAnimales La nueva lista de animales.
     */
    public void actualizarLista(List<Animal> nuevaListaAnimales) {
        if (nuevaListaAnimales != null) {
            this.listaAnimales.clear(); // Limpiar la lista de animales actual
            this.listaAnimales.addAll(nuevaListaAnimales); // Añadir los nuevos animales
            // Eliminar animales de la lista de favoritos que ya no están en la lista de animales
            favoritos.removeIf(animal -> !nuevaListaAnimales.contains(animal));
            notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
        }
    }

    public void actualizarListaFavoritos (Set<Animal> favoritos) {
            this.favoritos.clear();
            this.favoritos.addAll(favoritos);
            notifyDataSetChanged();
    }

    /**
     * Obtener el animal seleccionado en el RecyclerView.
     * @return El animal seleccionado o null si no hay ninguno seleccionado.
     */
    public Animal obtenerAnimalSeleccionado() {
        if (posicionSeleccionada != -1 && posicionSeleccionada < listaAnimales.size()) {
            animalSeleccionado = listaAnimales.get(posicionSeleccionada); // Obtener el animal en la posición seleccionada
            return animalSeleccionado;
        }
        return null; // Si no hay animal seleccionado, retornar null
    }

    /**
     * Interfaz para manejar la selección de un animal.
     */
    public interface OnAnimalSeleccionadoListener {
        void onAnimalSeleccionado(int posicion); // Método que se llama cuando un animal es seleccionado
    }

    /**
     * Interfaz para manejar actualizaciones en la lista de favoritos.
     */
    public interface FavoritosActualizadosListener {
        void onFavoritosActualizados(List<Animal> listaFavoritos); // Método que se llama cuando los favoritos se actualizan
    }

    /**
     * ViewHolder que representa un item de la lista de animales.
     */
    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, edad, descripcion; // Vistas de texto para nombre, edad y descripción
        ImageView imagen, iconoFavorito; // Vistas de imagen para la imagen del animal y el icono de favorito

        /**
         * Constructor que inicializa las vistas del item.
         * @param itemView La vista del item en el RecyclerView.
         */
        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            nombre = itemView.findViewById(R.id.nombreAnimal);
            edad = itemView.findViewById(R.id.edadAnimal);
            descripcion = itemView.findViewById(R.id.descripcionAnimal);
            imagen = itemView.findViewById(R.id.imagenAnimal);
            iconoFavorito = itemView.findViewById(R.id.iconoFavorito);
        }
    }
}