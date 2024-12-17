package com.example.ejercicio_tema3;


import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private AdaptadorAnimal adaptadorFavoritos;
    private Set<Animal> favoritos;
    public static ArrayList<Animal> listaAnimales;
    private FragmentAnimal fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        setContentView ( R.layout.activity_main );

        // Configurar los márgenes del sistema de barras
        ViewCompat.setOnApplyWindowInsetsListener ( findViewById ( R.id.main ) , (v , insets) -> {
            Insets systemBars = insets.getInsets ( WindowInsetsCompat.Type.systemBars () );
            v.setPadding ( systemBars.left , systemBars.top , systemBars.right , systemBars.bottom );
            return insets;
        } );

        inicializarListas(); // Inicializa las listas de animales

        if (savedInstanceState == null) {
            fragment = new FragmentAnimal();
            Bundle datos = new Bundle();
            datos.putParcelableArrayList("animales", listaAnimales); // Pasar la lista de animales
            fragment.setArguments(datos);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragmentContainerViewListaAnimales, fragment)
                    .commit();
            Log.d("MainActivity", "Fragmento inicial añadido.");
        }

        // Configuración de BottomNavigationView
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        // Mostrar fragmento inicial al abrir la app
//        if (savedInstanceState == null) {
//            reemplazarFragmento(new FragmentAnimal()); // Fragmento inicial
//            Log.d("MainActivity", "Fragmento de inicio mostrado.");
//        }

        // Manejo de selección de menú
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            Log.d("MainActivity", "Opción seleccionada: " + itemId);

            if (itemId == R.id.menu_inicio) {
                //reemplazarFragmento(new FragmentAnimal());
                if (listaAnimales == null || listaAnimales.isEmpty()) {
                    inicializarListas();
                    Log.d("MainActivity", "Lista de animales inicializada.");
                }
                reemplazarFragmento(new FragmentAnimal());

            } else if (itemId == R.id.menu_filtrar_perros) {
                reemplazarFragmento(FragmentoFiltrarPerros.newInstance(listaAnimales));
            } else if (itemId == R.id.menu_filtrar_gatos) {
                reemplazarFragmento(FragmentoFiltrarGatos.newInstance(listaAnimales));
            } else if (itemId == R.id.menu_agregar_animal) {
                agregarNuevoAnimal();
            } else if (itemId == R.id.menu_eliminar_animal) {
                eliminarAnimalSeleccionado();
            }
            return true;
        });

        // Configurar RecyclerView de favoritos
        configurarRecyclerViewFavoritos();
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
        Log.d("MainActivity", "Animales predeterminados añadidos.");
    }

    // Método para reemplazar el fragmento en el contenedor
    private void reemplazarFragmento(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragmentContainerViewListaAnimales, fragment); // Cambia el fragmento activo
        transaction.addToBackStack(null); // Añadir a la pila de retroceso
        transaction.commit();
        Log.d("MainActivity", "Fragmento reemplazado.");
    }

    // Método para agregar un nuevo animal y actualizar la lista
    private void agregarNuevoAnimal() {
        // Crear un nuevo animal
        Animal nuevoAnimal = new Animal(
                "Nuevo Animal",
                R.drawable.imagen_perro_1, // Imagen por defecto
                Animal.TipoAnimal.PERRO,
                1,
                "Descripción por defecto"
        );

        // Añadir el nuevo animal a la lista global
        listaAnimales.add(nuevoAnimal);
        Log.d("MainActivity", "Animal añadido: " + nuevoAnimal.getNombre());

        // Buscar el fragmento activo para actualizar la lista
        Fragment fragmentActivo = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewListaAnimales);
        if (fragmentActivo instanceof FragmentAnimal) {
            ((FragmentAnimal) fragmentActivo).actualizarLista(new ArrayList<>(listaAnimales));
        }

        // Mostrar un mensaje al usuario
        Toast.makeText(this, "Animal añadido: " + nuevoAnimal.getNombre(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        Log.d("MainActivity", "Opción seleccionada desde el menú: " + itemId);

        if (itemId == R.id.menu_agregar_animal) {
            agregarNuevoAnimal();
            return true;
        } else if (itemId == R.id.menu_eliminar_animal) {
            eliminarAnimalSeleccionado();
            return true;
        } else if (itemId == R.id.menu_inicio) {
            reemplazarFragmento(new FragmentAnimal());
            return true;
        } else if (itemId == R.id.menu_filtrar_gatos) {
            reemplazarFragmento(FragmentoFiltrarGatos.newInstance(listaAnimales));
            return true;
        } else if (itemId == R.id.menu_filtrar_perros) {
            reemplazarFragmento(FragmentoFiltrarPerros.newInstance(listaAnimales));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    // Método para eliminar el animal seleccionado
    private void eliminarAnimalSeleccionado() {
        // Obtener el fragmento activo
        Fragment fragmentActivo = getSupportFragmentManager().findFragmentById(R.id.fragmentContainerViewListaAnimales);

        if (fragmentActivo != null && fragmentActivo instanceof FragmentAnimal) {
            // Si el fragmento activo es una instancia de FragmentAnimal
            FragmentAnimal fragmentAnimal = (FragmentAnimal) fragmentActivo;
            AdaptadorAnimal adaptador = fragmentAnimal.getAdaptadorAnimal();

            if (adaptador != null) {
                Animal seleccionado = adaptador.obtenerAnimalSeleccionado();

                if (seleccionado != null) {
                    listaAnimales.remove(seleccionado);
                    Log.d("MainActivity", "Animal eliminado: " + seleccionado.getNombre());
                    fragmentAnimal.actualizarLista(new ArrayList<>(listaAnimales));
                    Toast.makeText(this, "Animal eliminado: " + seleccionado.getNombre(), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Selecciona un animal para eliminar", Toast.LENGTH_SHORT).show();
                    Log.d("MainActivity", "No se seleccionó un animal para eliminar.");
                }
            } else {
                Log.e("MainActivity", "Adaptador no encontrado");
            }
        } else {
            // Si el fragmento activo no es FragmentAnimal
            Log.e("MainActivity", "Fragmento activo no es FragmentAnimal");
        }
    }

    /**
     * Configura el RecyclerView para la lista de favoritos.
     * Los favoritos se muestran en una lista horizontal debajo del RecyclerView principal.
     */
    private void configurarRecyclerViewFavoritos() {
        RecyclerView rvFavoritos = findViewById(R.id.recyclerViewFavoritos);
        rvFavoritos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        favoritos = new HashSet<>(); // Inicializar el set de favoritos
        adaptadorFavoritos = new AdaptadorAnimal(this, new ArrayList<>(), new ArrayList<>());
        rvFavoritos.setAdapter(adaptadorFavoritos);

        // Verificación de que el fragmento y el adaptador no sean nulos
        if (fragment != null && fragment.getAdaptadorAnimal() != null) {
            fragment.getAdaptadorAnimal().setFavoritosActualizadosListener(listaDeFavoritos -> {
                favoritos.clear();
                favoritos.addAll(listaDeFavoritos);  // Sincroniza favoritos
                adaptadorFavoritos.actualizarLista(new ArrayList<>(favoritos));  // Actualiza el RecyclerView de favoritos
                Log.d("MainActivity", "Favoritos actualizados.");
            });
        } else {
            Toast.makeText(this, "Error al configurar favoritos", Toast.LENGTH_SHORT).show();
            Log.e("MainActivity", "Error al configurar favoritos.");
        }
    }

    /**
     * Método para actualizar la lista de favoritos en la MainActivity.
     *
     * @param listaFavoritos Lista de animales favoritos actualizada.
     */
    public void actualizarFavoritos(List<Animal> listaFavoritos) {
        if (adaptadorFavoritos != null) {
            favoritos.clear();
            favoritos.addAll(listaFavoritos);
            adaptadorFavoritos.actualizarLista(new ArrayList<>(favoritos));
            Log.d("MainActivity", "Favoritos actualizados: " + favoritos.size());
        }
    }
}