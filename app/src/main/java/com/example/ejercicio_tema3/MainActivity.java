package com.example.ejercicio_tema3;


import android.os.Bundle;

import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * Clase principal:
 * Gestiona la interfaz de usuario y la lógica de la aplicación.
 * Muestra una lista de animales, permite filtrar por tipo (perros/gatos).
 * Añade y elimina animales, y gestiona la lista de favoritos.
 */
public class MainActivity extends AppCompatActivity {

    private ArrayList<Animal> listaAnimales; // Lista completa de animales.
    private ArrayList<Animal> listaAnimalesFiltrada; // Lista filtrada de animales (según tipo).
    private FragmentAnimal fragment; // Fragmento donde se muestra la lista de animales.
    private ToggleButton togglePerros, toggleGatos; // Botones para activar el filtro de perros y gatos.
    private AdaptadorAnimal adaptadorFavoritos; // Adaptador para la lista de favoritos.

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView(R.layout.activity_main);

        // Configurar los márgenes del sistema de barras
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        inicializarListas(); // Inicializar las listas de animales.
        configurarFiltros(); // Configurar los filtros de perros y gatos.
        configurarBotones(); // Configurar los botones de agregar y eliminar animales.

        // Crear y añadir el fragmento de animales en el onCreate, utilizando setArguments para pasar datos.
        if (savedInstanceState == null) {
            fragment = new FragmentAnimal();
            Bundle datos = new Bundle();
            datos.putParcelableArrayList("animales", listaAnimales);  // Pasar la lista de animales al fragmento.
            fragment.setArguments(datos);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerViewListaAnimales, fragment)
                    .commit();
        }

        // Configurar RecyclerView de favoritos
        configurarRecyclerViewFavoritos ();
    }

    /**
     * Inicializa la lista de animales con algunos datos predefinidos.
     */
    private void inicializarListas() {
        listaAnimales = new ArrayList<>(Arrays.asList(
                new Animal("Luna", R.drawable.imagen_perro_1, Animal.TipoAnimal.PERRO, 3, "Cariñosa y juguetona."),
                new Animal("Simba", R.drawable.imagen_gato_1, Animal.TipoAnimal.GATO, 2, "Tranquilo y observador."),
                new Animal("Max", R.drawable.imagen_perro_2, Animal.TipoAnimal.PERRO, 5, "Fiel y protector."),
                new Animal("Milo", R.drawable.imagen_gato_2, Animal.TipoAnimal.GATO, 1, "Curioso y activo."),
                new Animal("Nala", R.drawable.imagen_perro_3, Animal.TipoAnimal.PERRO, 4, "Amigable y obediente."),
                new Animal("Bella", R.drawable.imagen_gato_3, Animal.TipoAnimal.GATO, 3, "Cariñosa y tranquila."),
                new Animal("Buddy", R.drawable.imagen_perro_4, Animal.TipoAnimal.PERRO, 6, "Leal y energético."),
                new Animal("Cleo", R.drawable.imagen_gato_4, Animal.TipoAnimal.GATO, 2, "Independiente y elegante."),
                new Animal("Rocky", R.drawable.imagen_perro_5, Animal.TipoAnimal.PERRO, 2, "Valiente y juguetón."),
                new Animal("Lily", R.drawable.imagen_gato_5, Animal.TipoAnimal.GATO, 1, "Dulce y afectuosa."),
                new Animal("Thor", R.drawable.imagen_perro_6, Animal.TipoAnimal.PERRO, 4, "Enérgico y divertido."),
                new Animal("Oliver", R.drawable.imagen_gato_6, Animal.TipoAnimal.GATO, 3, "Amigable y curioso."),
                new Animal("Duke", R.drawable.imagen_perro_7, Animal.TipoAnimal.PERRO, 5, "Cariñoso y leal."),
                new Animal("Chloe", R.drawable.imagen_gato_7, Animal.TipoAnimal.GATO, 2, "Tranquila y mimosa."),
                new Animal("Zeus", R.drawable.imagen_perro_8, Animal.TipoAnimal.PERRO, 3, "Activo y protector."),
                new Animal("Oscar", R.drawable.imagen_gato_8, Animal.TipoAnimal.GATO, 1, "Juguetón y curioso.")
        ));
        listaAnimalesFiltrada = new ArrayList<>(listaAnimales); // Inicializar la lista filtrada como la lista completa.
    }

    /**
     * Configura los filtros de perros y gatos usando ToggleButtons.
     * Los botones permiten al usuario filtrar los animales por tipo.
     */
    private void configurarFiltros() {
        togglePerros = findViewById(R.id.filtroPerros);
        toggleGatos = findViewById(R.id.filtroGatos);
        // Al cambiar el estado de los filtros, actualizamos la lista filtrada.
        togglePerros.setOnCheckedChangeListener((buttonView, isChecked) -> actualizarListaFiltrada());
        toggleGatos.setOnCheckedChangeListener((buttonView, isChecked) -> actualizarListaFiltrada());
    }

    /**
     * Configura los botones de agregar y eliminar animales.
     * El botón de agregar añade un nuevo animal a la lista.
     * El botón de eliminar elimina un animal seleccionado.
     */
    private void configurarBotones() {
        ImageButton botonAgregar = findViewById(R.id.agregar);
        botonAgregar.setOnClickListener(v -> agregarAnimal()); // Al hacer clic, agregamos un nuevo animal.
        ImageButton botonEliminar = findViewById(R.id.eliminar);
        botonEliminar.setOnClickListener(v -> {
            // Aseguramos que el adaptador esté disponible y el fragmento tenga datos.
            if (fragment != null && fragment.getAdaptadorAnimal() != null) {
                // Obtenemos el animal seleccionado en el adaptador.
                Animal animalSeleccionado = fragment.getAdaptadorAnimal().obtenerAnimalSeleccionado();

                if (animalSeleccionado != null) {
                    // Eliminar el animal seleccionado de la lista.
                    eliminarAnimal(animalSeleccionado);
                    Toast.makeText(MainActivity.this, "Animal eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No hay animal seleccionado para eliminar", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(MainActivity.this, "Adaptador no disponible", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Elimina un animal de la lista.
     * @param animal El animal a eliminar.
     */
    private void eliminarAnimal(Animal animal) {
        // Elimina el animal de la lista de animales completa.
        listaAnimales.remove(animal);
        // También eliminarlo de la lista filtrada si está presente.
        listaAnimalesFiltrada.remove(animal);
        // Actualiza la lista filtrada en el adaptador.
        actualizarListaFiltrada();
    }

    /**
     * Actualiza la lista filtrada según los filtros activados (perros y/o gatos).
     * También actualiza la vista del fragmento con la lista filtrada.
     */
    private void actualizarListaFiltrada() {
        listaAnimalesFiltrada.clear(); // Limpiar la lista filtrada antes de agregar nuevos elementos.
        boolean filtroPerrosActivo = togglePerros.isChecked(); // Verificar si el filtro de perros está activado.
        boolean filtroGatosActivo = toggleGatos.isChecked(); // Verificar si el filtro de gatos está activado.
        // Filtrar animales según los filtros activos.
        for (Animal animal : listaAnimales) {
            if (filtroPerrosActivo && animal.getTipo() == Animal.TipoAnimal.PERRO) {
                listaAnimalesFiltrada.add(animal); // Agregar perros a la lista filtrada.
            }
            if (filtroGatosActivo && animal.getTipo() == Animal.TipoAnimal.GATO) {
                listaAnimalesFiltrada.add(animal); // Agregar gatos a la lista filtrada.
            }
        }
        // Si no hay filtros activos, mostrar todos los animales.
        if (!filtroPerrosActivo && !filtroGatosActivo) {
            listaAnimalesFiltrada.addAll(listaAnimales);
        }
        // Actualizar la vista del fragmento con la lista filtrada.
        if (fragment != null) {
            fragment.actualizarLista(listaAnimalesFiltrada);
        }
        // Si no se encuentran animales después del filtrado, mostrar un mensaje de advertencia.
        if (listaAnimalesFiltrada.isEmpty()) {
            Toast.makeText(this, "No se encontraron animales que coincidan con los filtros.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Agrega un nuevo animal a la lista.
     */
    private void agregarAnimal() {
        Animal nuevoAnimal = new Animal("Nuevo", R.drawable.imagen_perro_1, Animal.TipoAnimal.PERRO, 1, "Nuevo animal añadido.");
        listaAnimales.add(nuevoAnimal);
        actualizarListaFiltrada(); // Actualizar la lista filtrada para mostrar el nuevo animal.
        Toast.makeText(this, "Animal añadido a la lista", Toast.LENGTH_SHORT).show();
    }

    /**
     * Configura el RecyclerView para la lista de favoritos.
     * Los favoritos se muestran en una lista horizontal debajo del RecyclerView principal.
     */
    private void configurarRecyclerViewFavoritos() {
        Set<Animal> favoritos = new HashSet<>();
        List<Animal> listaFavoritos = new ArrayList<>();
        RecyclerView rvFavoritos = findViewById ( R.id.recyclerViewFavoritos );
        rvFavoritos.setLayoutManager ( new LinearLayoutManager( this , LinearLayoutManager.HORIZONTAL , false ) );
        adaptadorFavoritos = new AdaptadorAnimal( listaFavoritos);
        rvFavoritos.setAdapter ( adaptadorFavoritos );
        adaptadorFavoritos.setFavoritosActualizadosListener(listaDeFavoritos -> adaptadorFavoritos.actualizarLista((List<Animal>) favoritos));
    }
}