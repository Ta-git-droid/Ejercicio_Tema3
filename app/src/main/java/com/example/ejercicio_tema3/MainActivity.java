package com.example.ejercicio_tema3;

import static android.content.ContentValues.TAG;

import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


/**
 * Clase principal:
 * Gestiona la interfaz de usuario y la lógica.
 * Muestra una lista de animales, filtra por tipo (perros/gatos).
 * Añade y elimina animales, y gestiona una lista de favoritos.
 */
public class MainActivity extends AppCompatActivity {

    private AdaptadorAnimal animalAdapter; // Adaptador para la lista de animales.
    private ArrayList<Animal> listaAnimales; // Lista completa de animales.
    private FragmentAnimal fragment; // Declaración correcta de la variable fragment.
    private ToggleButton togglePerros, toggleGatos;
    private AdaptadorAnimal adaptadorFavoritos; // Adaptador para la lista de favoritos.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        EdgeToEdge.enable ( this );
        setContentView ( R.layout.activity_main );
        ViewCompat.setOnApplyWindowInsetsListener ( findViewById ( R.id.main ) , (v , insets) -> {
            Insets systemBars = insets.getInsets ( WindowInsetsCompat.Type.systemBars () );
            v.setPadding ( systemBars.left , systemBars.top , systemBars.right , systemBars.bottom );
            return insets;
        } );





        // Configurar RecyclerView para mostrar la lista de animales
        //configurarRecyclerViewAnimales();

        // Configurar botones de acción (agregar/eliminar)
        // configurarBotones();

        inicializarListas();
        configurarRecyclerViewFavoritos();
        configurarFiltros();

        // Crear y añadir el fragmento en onCreate()
        if (savedInstanceState == null) {
            fragment = FragmentAnimal.newInstance(listaAnimales);  // Usar newInstance() para pasar la lista
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerViewListaAnimales, fragment)
                    .commit();
        }

    }

    /**
     * Inicializa la lista de animales con valores predeterminados.
     */
    private void inicializarListas() {
        listaAnimales = new ArrayList<>();
        // Se agregan animales a la lista con valores de ejemplo
        listaAnimales.add(new Animal("Luna", R.drawable.imagen_perro_1, Animal.TipoAnimal.PERRO, 3, "Cariñosa y juguetona."));
        listaAnimales.add(new Animal("Simba", R.drawable.imagen_gato_1, Animal.TipoAnimal.GATO, 2, "Tranquilo y observador."));
        listaAnimales.add(new Animal("Max", R.drawable.imagen_perro_2, Animal.TipoAnimal.PERRO, 5, "Fiel y protector."));
        listaAnimales.add(new Animal("Milo", R.drawable.imagen_gato_2, Animal.TipoAnimal.GATO, 1, "Curioso y activo."));
        listaAnimales.add(new Animal("Nala", R.drawable.imagen_perro_3, Animal.TipoAnimal.PERRO, 4, "Amigable y obediente."));
        listaAnimales.add(new Animal("Bella", R.drawable.imagen_gato_3, Animal.TipoAnimal.GATO, 3, "Cariñosa y tranquila."));
        listaAnimales.add(new Animal("Buddy", R.drawable.imagen_perro_4, Animal.TipoAnimal.PERRO, 6, "Leal y energético."));
        listaAnimales.add(new Animal("Cleo", R.drawable.imagen_gato_4, Animal.TipoAnimal.GATO, 2, "Independiente y elegante."));
        listaAnimales.add(new Animal("Rocky", R.drawable.imagen_perro_5, Animal.TipoAnimal.PERRO, 2, "Valiente y juguetón."));
        listaAnimales.add(new Animal("Lily", R.drawable.imagen_gato_5, Animal.TipoAnimal.GATO, 1, "Dulce y afectuosa."));
        listaAnimales.add(new Animal("Thor", R.drawable.imagen_perro_6, Animal.TipoAnimal.PERRO, 4, "Enérgico y divertido."));
        listaAnimales.add(new Animal("Oliver", R.drawable.imagen_gato_6, Animal.TipoAnimal.GATO, 3, "Amigable y curioso."));
        listaAnimales.add(new Animal("Duke", R.drawable.imagen_perro_7, Animal.TipoAnimal.PERRO, 5, "Cariñoso y leal."));
        listaAnimales.add(new Animal("Chloe", R.drawable.imagen_gato_7, Animal.TipoAnimal.GATO, 2, "Tranquila y mimosa."));
        listaAnimales.add(new Animal("Zeus", R.drawable.imagen_perro_8, Animal.TipoAnimal.PERRO, 3, "Activo y protector."));
        listaAnimales.add(new Animal("Oscar", R.drawable.imagen_gato_8, Animal.TipoAnimal.GATO, 1, "Juguetón y curioso."));
        listaAnimales.add(new Animal("Bailey", R.drawable.imagen_perro_9, Animal.TipoAnimal.PERRO, 6, "Amistoso y cariñoso."));
        listaAnimales.add(new Animal("Mia", R.drawable.imagen_gato_9, Animal.TipoAnimal.GATO, 4, "Elegante y cariñosa."));
        listaAnimales.add(new Animal("Charlie", R.drawable.imagen_perro_10, Animal.TipoAnimal.PERRO, 2, "Enérgico y amigable."));
        listaAnimales.add(new Animal("Luna", R.drawable.imagen_gato_10, Animal.TipoAnimal.GATO, 3, "Curiosa y tranquila."));
    }


    private void configurarFiltros() {
        togglePerros = findViewById(R.id.filtroPerros);
        toggleGatos = findViewById(R.id.filtroGatos);

        if (togglePerros != null && toggleGatos != null) {
            togglePerros.setOnCheckedChangeListener((buttonView, isChecked) -> actualizarListaFiltrada());
            toggleGatos.setOnCheckedChangeListener((buttonView, isChecked) -> actualizarListaFiltrada());
        }
    }

    private void actualizarListaFiltrada() {
        boolean perrosSeleccionados = togglePerros.isChecked();
        boolean gatosSeleccionados = toggleGatos.isChecked();

        List<Animal> listaFiltrada = new ArrayList<>();

        if (perrosSeleccionados && gatosSeleccionados) {
            listaFiltrada = new ArrayList<>(listaAnimales);
        } else if (perrosSeleccionados) {
            listaFiltrada = filtrarPorTipo(Animal.TipoAnimal.PERRO);
        } else if (gatosSeleccionados) {
            listaFiltrada = filtrarPorTipo(Animal.TipoAnimal.GATO);
        } else {
            listaFiltrada = new ArrayList<>(listaAnimales);
        }

        // Verifica que el fragmento no sea nulo antes de actualizar
        if (fragment != null) {
            fragment.actualizarLista(listaFiltrada);
        }
    }

    private List<Animal> filtrarPorTipo(Animal.TipoAnimal tipo) {
        List<Animal> listaFiltrada = new ArrayList<>();
        for (Animal animal : listaAnimales) {
            if (animal.getTipo() == tipo) {
                listaFiltrada.add(animal);
            }
        }
        return listaFiltrada;
    }




    /**
     * Configura el RecyclerView para mostrar la lista de animales.
     */
//    private void configurarRecyclerViewAnimales() {
//        RecyclerView rvAnimales = findViewById(R.id.recyclerViewAnimales);
//        rvAnimales.setLayoutManager(new LinearLayoutManager(this));
//
//        animalAdapter = new AdaptadorAnimal(listaAnimales);
//        rvAnimales.setAdapter(animalAdapter);
//    }

    /**
     * Configura los botones de acción (agregar y eliminar animales).
     */
//    private void configurarBotones() {
//        ImageButton botonAgregar = findViewById(R.id.agregar);
//        botonAgregar.setOnClickListener(v -> agregarAnimal());
//        ImageButton botonEliminar = findViewById(R.id.eliminar);
//        botonEliminar.setOnClickListener(v -> eliminarAnimal());
//    }

    /**
     * Configura el RecyclerView para la lista de favoritos.
     */
    private void configurarRecyclerViewFavoritos() {
        List<Animal> listaFavoritos = new ArrayList<>();
        RecyclerView rvFavoritos = findViewById(R.id.recyclerViewFavoritos);
        rvFavoritos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        adaptadorFavoritos = new AdaptadorAnimal(listaFavoritos);
        rvFavoritos.setAdapter(adaptadorFavoritos);

        adaptadorFavoritos.setFavoritosActualizadosListener(lista -> adaptadorFavoritos.actualizarLista(lista));
    }

    /**
     * Agrega un nuevo animal con valores predeterminados a la lista principal.
     */
    private void agregarAnimal() {
        try {
            if (listaAnimales == null) {
                listaAnimales = new ArrayList<>();
            }
            Animal nuevoAnimal = new Animal("Nuevo", R.drawable.imagen_perro_1, Animal.TipoAnimal.PERRO, 1, getString(R.string.nuevo_animal_a_adido));
            listaAnimales.add(nuevoAnimal);
            animalAdapter.actualizarLista(listaAnimales);
            Toast.makeText(this, getString(R.string.animal_a_adido_a_la_lista), Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, getString(R.string.error_al_agregar_un_nuevo_animal) + e.getMessage(), e);
            Toast.makeText(this, R.string.error_al_agregar_el_animal, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Elimina el animal seleccionado de la lista, si no hay filtros activos.
     */
//    private void eliminarAnimal() {
//        if (!togglePerros.isChecked() && !toggleGatos.isChecked()) {
//            if (listaAnimales != null && !listaAnimales.isEmpty()) {
//                int posicionSeleccionada = animalAdapter.getPosicionSeleccionada();
//
//                if (posicionSeleccionada != -1) {
//                    Animal animalEliminado = listaAnimales.remove(posicionSeleccionada);
//                    animalAdapter.actualizarLista(listaAnimales);
//                    Toast.makeText(this, animalEliminado.getNombre() + getString(R.string.ha_sido_eliminado), Toast.LENGTH_SHORT).show();
//                    animalAdapter.setPosicionSeleccionada(-1);
//                } else {
//                    Toast.makeText(this, R.string.no_hay_animales_seleccionados_o_la_lista_est_vac_a, Toast.LENGTH_SHORT).show();
//                    Log.w(TAG, getString(R.string.no_se_seleccion_un_animal_para_eliminar));
//                }
//            } else {
//                Toast.makeText(this, R.string.lista_vacia, Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Toast.makeText(this, R.string.desactiva_los_filtros_para_poder_eliminar_un_animal, Toast.LENGTH_SHORT).show();
//            Log.w(TAG, getString(R.string.no_se_puede_eliminar_con_filtros_activos));
//        }
//    }
}