package com.example.ejercicio_tema3;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAnimal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAnimal extends Fragment {

    private RecyclerView recyclerView;
    private AdaptadorAnimal adaptador;
    private List<Animal> listaAnimales;

    public FragmentAnimal() {
        // Constructor vacío requerido
    }

    /**
     * Crea una nueva instancia del Fragment y pasa la lista de animales.
     */
    public static FragmentAnimal newInstance(List<Animal> listaAnimales) {
        FragmentAnimal fragment = new FragmentAnimal();
        Bundle args = new Bundle();
        args.putParcelableArrayList("animales", new ArrayList<>(listaAnimales)); // Asegúrate de pasar una lista de Parcelable
        fragment.setArguments(args);
        return fragment;
    }


    /**
     * Infla el layout del Fragment y configura el RecyclerView.
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_animal, container, false);

        recyclerView = view.findViewById(R.id.recyclerViewAnimal);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Asegúrate de que los argumentos no sean null y de que la lista esté bien inicializada
        if (getArguments() != null) {
            listaAnimales = getArguments().getParcelableArrayList("animales");
        }

        // Si la lista es nula, inicialízala para evitar el NullPointerException
        if (listaAnimales == null) {
            listaAnimales = new ArrayList<>();
        }

        // Inicializar y configurar el adaptador
        adaptador = new AdaptadorAnimal(getContext(), listaAnimales, new ArrayList<>(), (MainActivity) getActivity());
        recyclerView.setAdapter(adaptador);

        return view;
    }



    /**
     * Método para actualizar la lista de animales filtrados en el RecyclerView.
     * Se invoca cuando cambian los filtros en la actividad.
     */
    public void actualizarLista(List<Animal> nuevaLista) {
        if (nuevaLista != null) {
            listaAnimales = nuevaLista;  // Actualiza la lista de animales en el Fragment
            adaptador.actualizarLista(listaAnimales);  // Actualiza la vista del RecyclerView
        }
    }

    /**
     * Obtener la posición seleccionada para eliminar un animal.
     * Asume que el adaptador tiene un método para obtener la posición seleccionada.
     */
    public int obtenerPosicionSeleccionada() {
        return adaptador.getPosicionSeleccionada();  // Implementar en el adaptador si es necesario
    }
}