package com.example.ejercicio_tema3;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
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
 * Adaptador para manejar y mostrar una lista de objetos Animal en un RecyclerView,
 * permitiendo seleccionar, agregar o eliminar animales de la lista de favoritos.
 */
public class AdaptadorAnimal extends RecyclerView.Adapter<AdaptadorAnimal.AnimalViewHolder> {

    private final Context context;
    private final List<Animal> listaAnimales; // Lista de animales a mostrar.
    private final Set<Animal> favoritos = new HashSet<>(); // Conjunto para almacenar animales favoritos, evitando duplicados.
    private int posicionSeleccionada = -1; // Variable para almacenar la posición del animal seleccionado.
    private OnAnimalSeleccionadoListener listener; // Listener para notificar la selección de un animal.
    private FavoritosActualizadosListener favoritosListener; // Listener para actualizar la lista de favoritos.

    /**
     * Constructor del adaptador.
     *
     * @param context        Contexto de la aplicación.
     * @param listaAnimales  Lista de animales a mostrar.
     * @param listaFavoritos Lista inicial de favoritos.
     */
    public AdaptadorAnimal(Context context, List<Animal> listaAnimales, List<Animal> listaFavoritos) {
        this.context = context;
        // Si la lista de animales es nula, inicializamos con una lista vacía.
        this.listaAnimales = listaAnimales != null ? listaAnimales : new ArrayList<>();
        // Si la lista de favoritos no es nula, la agregamos al conjunto de favoritos.
        if (listaFavoritos != null) {
            this.favoritos.addAll(listaFavoritos);
        }
    }

    /**
     * Establece un listener para los cambios en la lista de favoritos.
     *
     * @param favoritosListener Listener para actualizar favoritos.
     */
    public void setFavoritosActualizadosListener(FavoritosActualizadosListener favoritosListener) {
        this.favoritosListener = favoritosListener;
    }

    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflar la vista para cada item de la lista de animales.
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal, parent, false);
        return new AnimalViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        Animal animal = listaAnimales.get(position);
        // Configurar las vistas con los datos del animal
        holder.nombre.setText(animal.getNombre());
        holder.edad.setText(context.getString(R.string.edad_placeholder, animal.getEdad()));
        holder.descripcion.setText(animal.getDescripcion());
        holder.imagen.setImageResource(animal.getImagen());
        // Cambiar el estado del icono de favorito según si el animal está en la lista de favoritos
        holder.iconoFavorito.setImageResource(favoritos.contains(animal) ? R.drawable.corazon_relleno : R.drawable.corazon_vacio);

        // Configurar el evento de clic en el icono de favorito
        holder.iconoFavorito.setOnClickListener(v -> {
            if (favoritos.contains(animal)) {
                favoritos.remove(animal); // Eliminar de favoritos si ya está.
                holder.iconoFavorito.setImageResource(R.drawable.corazon_vacio);
            } else {
                favoritos.add(animal); // Añadir a favoritos si no está.
                holder.iconoFavorito.setImageResource(R.drawable.corazon_relleno);
            }
            Log.d("AdaptadorAnimal", "onClick: Favoritos actualizados, tamaño de lista de favoritos: " + favoritos.size());
            // Notificar cambios en la lista de favoritos si el listener está configurado
            if (favoritosListener != null) {
                favoritosListener.onFavoritosActualizados(new ArrayList<>(favoritos));
            }
        });

        // Cambiar el color de fondo si el animal está seleccionado
        holder.itemView.setBackgroundColor(holder.getAdapterPosition() == posicionSeleccionada ? Color.LTGRAY : Color.TRANSPARENT);

        // Configurar el evento de clic en el item
        holder.itemView.setOnClickListener(v -> {
            // Verifica si la posición seleccionada cambió
            if (posicionSeleccionada != holder.getAdapterPosition()) {
                int posicionAnterior = posicionSeleccionada;
                posicionSeleccionada = holder.getAdapterPosition();  // Actualizar la posición seleccionada
                notifyItemChanged(posicionAnterior); // Notificar cambio del item anterior
                notifyItemChanged(posicionSeleccionada); // Notificar cambio del item actual
                // Notificar al listener si está configurado
                if (listener != null) {
                    listener.onAnimalSeleccionado(posicionSeleccionada);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listaAnimales.size(); // Retorna la cantidad de animales en la lista.
    }

    /**
     * Actualiza la lista de animales mostrados en el adaptador.
     *
     * @param nuevaListaAnimales Nueva lista de animales.
     */
    public void actualizarLista(List<Animal> nuevaListaAnimales) {
        listaAnimales.clear(); // Limpiar la lista actual.
        if (nuevaListaAnimales != null) {
            listaAnimales.addAll(nuevaListaAnimales); // Añadir los nuevos animales.
        }
        // Eliminar de favoritos aquellos animales que ya no están en la lista
        favoritos.removeIf(animal -> !listaAnimales.contains(animal));
        notifyDataSetChanged(); // Notificar que la lista ha sido actualizada.
    }

    /**
     * Interfaz para manejar la selección de un animal.
     */
    public interface OnAnimalSeleccionadoListener {
        void onAnimalSeleccionado(int posicion); // Método que se llama cuando se selecciona un animal.
    }

    /**
     * Interfaz para manejar actualizaciones en la lista de favoritos.
     */
    public interface FavoritosActualizadosListener {
        void onFavoritosActualizados(List<Animal> listaFavoritos); // Método para actualizar la lista de favoritos.
    }

    /**
     * ViewHolder para los elementos de la lista.
     */
    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, edad, descripcion;
        ImageView imagen, iconoFavorito;

        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar las vistas del item
            nombre = itemView.findViewById(R.id.nombreAnimal);
            edad = itemView.findViewById(R.id.edadAnimal);
            descripcion = itemView.findViewById(R.id.descripcionAnimal);
            imagen = itemView.findViewById(R.id.imagenAnimal);
            iconoFavorito = itemView.findViewById(R.id.iconoFavorito);
        }
    }

    /**
     * Devuelve el animal actualmente seleccionado.
     *
     * @return El animal seleccionado, o null si no hay selección.
     */
    public Animal obtenerAnimalSeleccionado() {
        if (posicionSeleccionada != -1 && posicionSeleccionada < listaAnimales.size()) {
            return listaAnimales.get(posicionSeleccionada); // Retorna el animal seleccionado si hay uno.
        }
        return null; // Retorna null si no hay animal seleccionado.
    }
}