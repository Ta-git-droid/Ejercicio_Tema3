package com.example.ejercicio_tema3;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentAnimal#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentAnimal extends Fragment {


    // Lista de animales que se mostraran en el RecyclerView
    private ArrayList<Animal> animalArrayList;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FragmentAnimal() {
        // Required empty public constructor
    }


    // Constructor del fragmento en el que se pasa el conjunto de datos
    public FragmentAnimal (ArrayList<Animal> animalArrayList) {
        this.animalArrayList = animalArrayList;
    }


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragmentAnimal.
     */
    // TODO: Rename and change types and number of parameters
    public static FragmentAnimal newInstance(String param1 , String param2) {
        FragmentAnimal fragment = new FragmentAnimal ();
        Bundle args = new Bundle ();
        args.putString ( ARG_PARAM1 , param1 );
        args.putString ( ARG_PARAM2 , param2 );
        fragment.setArguments ( args );
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        // comprobamos si tenemos datos del ArrayList
        if (this.animalArrayList == null) {
            // Comprobamos si los datos se han pasado en un Bundel
            if (getArguments () != null) {
                this.animalArrayList = getArguments ().getParcelableArrayList ("animales");
                System.out.println ("Datos en OnCreate: " + this.animalArrayList);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater , ViewGroup container ,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate (R.layout.fragment_animal, container, false);

        return inflater.inflate ( R.layout.fragment_animal , container , false );
    }
}