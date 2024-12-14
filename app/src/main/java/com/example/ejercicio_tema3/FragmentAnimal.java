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

    private ArrayList<Animal> listaAnimales; // Lista completa de animales
    private RecyclerView recyclerViewAnimal; // RecyclerView para mostrar la lista
    private AdaptadorAnimal adaptadorAnimal; // Adaptador para RecyclerView


    public FragmentAnimal() {
        // Constructor vacío necesario para el Fragment
    }

    // Método para crear una nueva instancia del fragmento y pasarle la lista de animales
    public static FragmentAnimal newInstance(ArrayList<Animal> animalArrayList) {
        FragmentAnimal fragment = new FragmentAnimal();
        Bundle args = new Bundle();
        args.putParcelableArrayList("animales", animalArrayList);  // Guardamos la lista en el Bundle
        fragment.setArguments(args);  // Asignamos los argumentos al fragmento
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Recuperamos los datos del Bundle solo una vez
        if (getArguments() != null) {
            listaAnimales = getArguments().getParcelableArrayList("animales");
            Log.d("FragmentAnimal", "Datos en onCreate: " + listaAnimales);
        } else {
            listaAnimales = new ArrayList<>(); // Si no hay datos, inicializamos con una lista vacía
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_animal, container, false);

        // Inicializa RecyclerView
        recyclerViewAnimal = view.findViewById(R.id.recyclerViewAnimal);

        // Asignar un LayoutManager al RecyclerView (vertical en este caso)
        recyclerViewAnimal.setLayoutManager(new LinearLayoutManager(getContext()));

        // Crear el adaptador con la lista de animales
        adaptadorAnimal = new AdaptadorAnimal(listaAnimales);

        // Asignar el adaptador al RecyclerView
        recyclerViewAnimal.setAdapter(adaptadorAnimal);

        return view;
    }






    public void actualizarLista(List<Animal> listaFiltrada) {
        adaptadorAnimal.actualizarLista(listaFiltrada);
        adaptadorAnimal.notifyDataSetChanged(); // Notificar al adaptador que los datos han cambiado
    }


}