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
 * Maneja y muestra una lista de objetos Animal en un RecyclerView.
 * Permite seleccionar un elemento.
 * Gestionar favoritos.
 * Actualizar dinámicamente la lista.
 */
public class AdaptadorAnimal extends RecyclerView.Adapter<AdaptadorAnimal.AnimalViewHolder> {

    private List<Animal> listaAnimalesOriginal; // Lista original de animales (sin filtrar)
    private List<Animal> listaAnimalesFiltrada; // Lista filtrada de animales
    private int posicionSeleccionada = -1; // Posición del elemento actualmente seleccionado.
    private final Set<Animal> favoritos = new HashSet<>(); // Conjunto de animales marcados como favoritos.
    private favoritosActualizadosListener listenerFavoritos; // Listener para gestionar actualizaciones de favoritos.
    private Context context;
    private List<Animal> listaFavoritos; // Lista de favoritos
    private MainActivity mainActivity;

    // Constructor modificado para recibir lista de favoritos y MainActivity
    public AdaptadorAnimal(Context context, List<Animal> listaAnimales, List<Animal> listaFavoritos, MainActivity mainActivity) {
        this.context = context;
        this.listaAnimalesOriginal = listaAnimales != null ? listaAnimales : new ArrayList<>();
        this.listaAnimalesFiltrada = new ArrayList<>(this.listaAnimalesOriginal);
        this.listaFavoritos = listaFavoritos != null ? listaFavoritos : new ArrayList<>();
        this.mainActivity = mainActivity; // Guardamos la referencia de MainActivity
    }




    /**
     * Obtiene la posición del elemento seleccionado.
     * Posición seleccionada (-1 si no hay ninguno).
     */
    public int getPosicionSeleccionada() {
        return posicionSeleccionada;
    }

    /**
     * Establece la posición seleccionada.
     * Actualiza visualmente el RecyclerView.
     */
    public void setPosicionSeleccionada(int posicion) {
        if (posicion >= listaAnimalesFiltrada.size()) {
            posicionSeleccionada = -1; // Reinicia si la posición ya no es válida
        } else {
            posicionSeleccionada = posicion;
        }
        notifyDataSetChanged(); // Refrescar la vista
    }

    /**
     * Se infla la vista del ítem desde el layout XML.
     * La vista del ítem es un archivo XML que describe cómo se ha de ver un solo ítem en el RecyclerView.
     * Crea y devuelve un nuevo AnimalViewHolder con la vista inflada.
     * El ViewHolder es responsable de almacenar las vistas del ítem y rehusarlas.
     */
    @NonNull
    @Override
    public AnimalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_animal, parent, false);
        return new AnimalViewHolder(itemView);
    }

    /**
     * Se valida si la posición seleccionada sigue siendo válida.
     * Si la posición seleccionada está fuera de los límites de la lista, se reinicia a -1.
     * Se obtiene el animal correspondiente a la posición actual en la lista.
     * Se configuran los valores en el ViewHolder para mostrar los datos del animal en la interfaz.
     * Se actualiza el icono de favorito según el estado actual.
     * Maneja el clic en el icono de favoritos.
     * Actualiza el fondo del ítem si está seleccionado.
     * Se establece el evento de clic para seleccionar un elemento.
     */
    @Override
    public void onBindViewHolder(@NonNull AnimalViewHolder holder, int position) {
        // Validar si la posición seleccionada sigue siendo válida
        if (posicionSeleccionada >= listaAnimalesFiltrada.size()) {
            posicionSeleccionada = -1; // Reinicia si la selección ya no es válida
        }
        Animal animal = listaAnimalesFiltrada.get(position); // Obtener el animal correspondiente a la posición actual.
        // Configurar los valores en el ViewHolder.
        holder.nombre.setText(animal.getNombre());
        holder.edad.setText(holder.itemView.getContext().getString(R.string.edad_placeholder, animal.getEdad()));
        holder.descripcion.setText(animal.getDescripcion());
        holder.imagen.setImageResource(animal.getImagen());
        // Actualizar el ícono de favorito según el estado actual.
        holder.iconoFavorito.setImageResource(favoritos.contains(animal) ? R.drawable.corazon_relleno : R.drawable.corazon_vacio );
        // Manejar el clic en el ícono de favorito.
        holder.iconoFavorito.setOnClickListener(v -> {
            if (favoritos.contains(animal)) {
                favoritos.remove(animal);
                holder.iconoFavorito.setImageResource(R.drawable.corazon_vacio);
            } else {
                favoritos.add(animal);
                holder.iconoFavorito.setImageResource(R.drawable.corazon_relleno);
            }
            // Notificar al listener sobre los cambios en la lista de favoritos.
            if (listenerFavoritos != null) {
                listenerFavoritos.favoritosActualizados(new ArrayList<>(favoritos));
            }
        });
        // Actualizar el fondo si está seleccionado
        holder.itemView.setBackgroundColor(posicionSeleccionada == position ? Color.LTGRAY : Color.TRANSPARENT );
        // Evento de clic para seleccionar un elemento.
        holder.itemView.setOnClickListener(v -> {
            int posicionAnterior = posicionSeleccionada;
            posicionSeleccionada = holder.getAdapterPosition();
            // Actualizar el fondo de los elementos afectados.
            if (posicionAnterior != -1) notifyItemChanged(posicionAnterior);
            notifyItemChanged(posicionSeleccionada);
        });
    }


    /**
     * Devuelve el tamaño de la lista filtrada de animales.
     */
    @Override
    public int getItemCount() {
        if (listaAnimalesFiltrada != null) {
            return listaAnimalesFiltrada.size();
        } else {
            return 0;
        }
    }

    /**
     * Actualiza la lista de animales en el adaptador.
     */
    public void actualizarLista(List<Animal> nuevaLista) {
        if (nuevaLista != null) {
            // Actualiza la lista original
            listaAnimalesOriginal = nuevaLista;
            // Actualiza la lista filtrada (puedes aplicar un filtro si es necesario)
            listaAnimalesFiltrada = new ArrayList<>(nuevaLista);
            // Notificar al adaptador que los datos han cambiado
            notifyDataSetChanged();
        }
    }




    /**
     * Establece un listener para gestionar actualizaciones en la lista de favoritos.
     * Recibirá las actualizaciones.
     */
    public void setFavoritosActualizadosListener(favoritosActualizadosListener listener) {
        this.listenerFavoritos = listener;
    }

    /**
     * Interfaz para gestionar actualizaciones en la lista de favoritos.
     */
    public interface favoritosActualizadosListener {
        void favoritosActualizados(List<Animal> listaFavoritos);
    }

    /**
     * ViewHolder para representar cada elemento de la lista de animales.
     * Contiene referencias a las vistas de un ítem.
     */
    public static class AnimalViewHolder extends RecyclerView.ViewHolder {
        TextView nombre, edad, descripcion; // Vistas de texto
        ImageView imagen, iconoFavorito; // Vistas de imagen
        /**
         * Constructor del ViewHolder
         */
        public AnimalViewHolder(@NonNull View itemView) {
            super(itemView);
            // Inicializar las vistas del ítem.
            nombre = itemView.findViewById(R.id.nombreAnimal);
            edad = itemView.findViewById(R.id.edadAnimal);
            descripcion = itemView.findViewById(R.id.descripcionAnimal);
            imagen = itemView.findViewById(R.id.imagenAnimal);
            iconoFavorito = itemView.findViewById(R.id.iconoFavorito);
        }
    }

    // Método para actualizar la lista de favoritos
    public void actualizarFavoritos(List<Animal> nuevosFavoritos) {
        this.listaFavoritos = nuevosFavoritos;
        notifyDataSetChanged();  // Esto actualizará el RecyclerView de favoritos
    }

}