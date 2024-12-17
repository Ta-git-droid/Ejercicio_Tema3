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


public class FragmentoFiltrarGatos extends Fragment {

    private List<Animal> listaAnimalesFiltrada;
    private List<Animal> listaAnimales;

    public FragmentoFiltrarGatos() {
        // Constructor vacío
    }

    // Método para crear una nueva instancia del fragmento y pasar los datos necesarios
    public static FragmentoFiltrarGatos newInstance(ArrayList<Animal> animales) {
        FragmentoFiltrarGatos fragment = new FragmentoFiltrarGatos();
        Bundle args = new Bundle();
        args.putParcelableArrayList("animales", animales);  // Pasamos la lista de animales
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Recibe los datos de la actividad principal
        if (getArguments() != null) {
            listaAnimales = getArguments().getParcelableArrayList("animales");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Infla el layout para este fragmento
        View view = inflater.inflate(R.layout.fragment_fragmento_filtrar_gatos, container, false);

        // Filtra la lista de animales para mostrar solo los gatos
        listaAnimalesFiltrada = new ArrayList<>();
        if (listaAnimales != null) {
            for (Animal animal : listaAnimales) {
                if (animal.getTipo() == Animal.TipoAnimal.GATO) {
                    listaAnimalesFiltrada.add(animal);
                }
            }
        }

        // Actualizar la UI con los gatos filtrados
        RecyclerView recyclerView = view.findViewById(R.id.rv_filtrar_gatos);

        // Asegúrate de configurar un LayoutManager para el RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager (getContext()));

        // Configura el adaptador para mostrar los gatos filtrados
        AdaptadorAnimal adaptador = new AdaptadorAnimal(getContext(), listaAnimalesFiltrada, new ArrayList<>());
        recyclerView.setAdapter(adaptador);

        return view;
    }
}