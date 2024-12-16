package com.example.ejercicio_tema3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;



/**
 * Fragmento que representa la lista de animales en la interfaz de usuario.
 * Se encarga de gestionar y mostrar la lista de animales, permitiendo filtrar por tipo o nombre.
 * Además, gestiona la actualización y eliminación de animales en la lista.
 */
public class FragmentAnimal extends Fragment {

    private RecyclerView recyclerView;
    private AdaptadorAnimal adaptadorAnimal;
    private List<Animal> listaAnimales;
    private List<Animal> listaAnimalesFiltrados;
    private List<Animal> listaFavoritos = new ArrayList<>(); // Lista de favoritos

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("FragmentAnimal", "onCreate: Inicializando fragmento");
        // Recuperamos los argumentos pasados al fragmento y verificamos si la lista de animales es null
        if (getArguments() != null) {
            listaAnimales = getArguments().getParcelableArrayList("animales");
            // Si la lista es null, la inicializamos como una lista vacía
            if (listaAnimales == null) {
                listaAnimales = new ArrayList<>();
            }
            // Inicializamos la lista filtrada con los mismos elementos que la lista original
            listaAnimalesFiltrados = new ArrayList<>(listaAnimales);
        } else {
            // Si no hay argumentos, inicializamos las listas como vacías
            listaAnimales = new ArrayList<>();
            listaAnimalesFiltrados = new ArrayList<>();
        }
        Log.d("FragmentAnimal", "onCreate: Lista de animales cargada: " + listaAnimales.size() + " elementos");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflar el layout del fragmento
        View rootView = inflater.inflate(R.layout.fragment_animal, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewAnimal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext())); // Establecer el LayoutManager
        Log.d("FragmentAnimal", "onCreateView: Inicializando RecyclerView");
        if (listaAnimales != null) {
            Log.d("FragmentAnimal", "onCreateView: Lista de animales no es nula, creando adaptador");
            // Crear el adaptador con la lista de animales y favoritos
            adaptadorAnimal = new AdaptadorAnimal(getContext(), listaAnimalesFiltrados, listaFavoritos);
            recyclerView.setAdapter(adaptadorAnimal);
            // Establecer un listener para actualizar los favoritos cuando cambien
            adaptadorAnimal.setFavoritosActualizadosListener(favoritos -> {
                Log.d("FragmentAnimal", "onCreateView: Favoritos actualizados: " + favoritos.size() + " elementos");
                listaFavoritos = favoritos;
                // Notificar a la MainActivity sobre los cambios en favoritos
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).actualizarFavoritos(listaFavoritos);
                }
            });
        } else {
            Log.d("FragmentAnimal", "onCreateView: La lista de animales es nula");
        }

        return rootView;
    }

    /**
     * Método para actualizar la lista de animales en el fragmento.
     * Este método es útil cuando la lista de animales ha cambiado o se ha actualizado.
     *
     * @param listaAnimales Lista de animales actualizada.
     */
    public void actualizarLista(List<Animal> listaAnimales) {
        if (adaptadorAnimal != null && listaAnimales != null) {
            Log.d("FragmentAnimal", "actualizarLista: Actualizando lista de animales");
            this.listaAnimales = listaAnimales;
            listaAnimalesFiltrados = new ArrayList<>(listaAnimales); // Reiniciamos la lista filtrada
            // Actualizamos el adaptador con la nueva lista filtrada
            adaptadorAnimal.actualizarLista(listaAnimalesFiltrados);
            Log.d("FragmentAnimal", "actualizarLista: Nueva lista de animales: " + listaAnimales.size() + " elementos");
        } else {
            Log.d("FragmentAnimal", "actualizarLista: Lista de animales es nula");
        }
    }

    /**
     * Obtener el adaptador actual del RecyclerView.
     * Este método permite acceder al adaptador actual para realizar operaciones como actualizar la lista.
     *
     * @return El adaptador del RecyclerView que maneja la lista de animales.
     */
    public AdaptadorAnimal getAdaptadorAnimal() {
        return adaptadorAnimal;
    }
}