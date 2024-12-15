package com.example.ejercicio_tema3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflamos el layout del fragmento
        View rootView = inflater.inflate(R.layout.fragment_animal, container, false);
        recyclerView = rootView.findViewById(R.id.recyclerViewAnimal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Verificamos si la lista de animales no es nula antes de crear el adaptador
        if (listaAnimales != null) {
            // Inicializamos el adaptador con la lista de animales filtrados
            adaptadorAnimal = new AdaptadorAnimal(getContext(), listaAnimalesFiltrados, new ArrayList<>());
            recyclerView.setAdapter(adaptadorAnimal);
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
            this.listaAnimales = listaAnimales;
            listaAnimalesFiltrados = new ArrayList<>(listaAnimales); // Reiniciamos la lista filtrada
            // Actualizamos el adaptador con la nueva lista filtrada
            adaptadorAnimal.actualizarLista(listaAnimalesFiltrados);
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