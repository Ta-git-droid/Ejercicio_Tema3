package com.example.ejercicio_tema3;

import static android.content.ContentValues.TAG;

import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
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
    private List<Animal> listaAnimales; // Lista completa de animales.

    private AdaptadorAnimal adaptadorFavoritos; // Adaptador para la lista de favoritos.

    private ToggleButton togglePerros; // Botón de filtro para perros.
    private ToggleButton toggleGatos; // Botón de filtro para gatos.

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

        // Inicialización de listas de animales
        inicializarListas ();

        // Configurar RecyclerView principal
        configurarRecyclerView ();

        // Configurar botones de filtro
        configurarFiltros ();

        // Configurar botones de acciones (agregar/eliminar)
        configurarBotones ();

        // Configurar RecyclerView de favoritos
        configurarRecyclerViewFavoritos ();

        this.setRequestedOrientation(
                ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);

    }

    /**
     * Inicializar las listas de animales (array, perros y gatos):
     * Crear objetos de tipo Animal.
     */
    private void inicializarListas() {
        listaAnimales = new ArrayList<> ( Arrays.asList (
                new Animal ( "Luna" , R.drawable.imagen_perro_1 , Animal.TipoAnimal.PERRO , 3 , "Cariñosa y juguetona." ) ,
                new Animal ( "Simba" , R.drawable.imagen_gato_1 , Animal.TipoAnimal.GATO , 2 , "Tranquilo y observador." ) ,
                new Animal ( "Max" , R.drawable.imagen_perro_2 , Animal.TipoAnimal.PERRO , 5 , "Fiel y protector." ) ,
                new Animal ( "Milo" , R.drawable.imagen_gato_2 , Animal.TipoAnimal.GATO , 1 , "Curioso y activo." ) ,
                new Animal ( "Nala" , R.drawable.imagen_perro_3 , Animal.TipoAnimal.PERRO , 4 , "Amigable y obediente." ) ,
                new Animal ( "Bella" , R.drawable.imagen_gato_3 , Animal.TipoAnimal.GATO , 3 , "Cariñosa y tranquila." ) ,
                new Animal ( "Buddy" , R.drawable.imagen_perro_4 , Animal.TipoAnimal.PERRO , 6 , "Leal y energético." ) ,
                new Animal ( "Cleo" , R.drawable.imagen_gato_4 , Animal.TipoAnimal.GATO , 2 , "Independiente y elegante." ) ,
                new Animal ( "Rocky" , R.drawable.imagen_perro_5 , Animal.TipoAnimal.PERRO , 2 , "Valiente y juguetón." ) ,
                new Animal ( "Lily" , R.drawable.imagen_gato_5 , Animal.TipoAnimal.GATO , 1 , "Dulce y afectuosa." ) ,
                new Animal ( "Thor" , R.drawable.imagen_perro_6 , Animal.TipoAnimal.PERRO , 4 , "Enérgico y divertido." ) ,
                new Animal ( "Oliver" , R.drawable.imagen_gato_6 , Animal.TipoAnimal.GATO , 3 , "Amigable y curioso." ) ,
                new Animal ( "Duke" , R.drawable.imagen_perro_7 , Animal.TipoAnimal.PERRO , 5 , "Cariñoso y leal." ) ,
                new Animal ( "Chloe" , R.drawable.imagen_gato_7 , Animal.TipoAnimal.GATO , 2 , "Tranquila y mimosa." ) ,
                new Animal ( "Zeus" , R.drawable.imagen_perro_8 , Animal.TipoAnimal.PERRO , 3 , "Activo y protector." ) ,
                new Animal ( "Oscar" , R.drawable.imagen_gato_8 , Animal.TipoAnimal.GATO , 1 , "Juguetón y curioso." ) ,
                new Animal ( "Bailey" , R.drawable.imagen_perro_9 , Animal.TipoAnimal.PERRO , 6 , "Amistoso y cariñoso." ) ,
                new Animal ( "Mia" , R.drawable.imagen_gato_9 , Animal.TipoAnimal.GATO , 4 , "Elegante y cariñosa." ) ,
                new Animal ( "Charlie" , R.drawable.imagen_perro_10 , Animal.TipoAnimal.PERRO , 2 , "Enérgico y amigable." ) ,
                new Animal ( "Luna" , R.drawable.imagen_gato_10 , Animal.TipoAnimal.GATO , 3 , "Curiosa y tranquila." )
        ) );
    }

    /**
     * Configurar el RecyclerView principal:
     * Crear adaptador.
     * Referenciar el RecyclerView.
     * Asignar LayoutManager.
     * Asignar adaptador.
     */
    private void configurarRecyclerView() {
        animalAdapter = new AdaptadorAnimal ( listaAnimales );
        RecyclerView rvAnimales = findViewById ( R.id.recyclerView );
        rvAnimales.setLayoutManager ( new LinearLayoutManager ( this ) );
        rvAnimales.setAdapter ( animalAdapter );
    }

    /**
     * Configura los ToggleButtons:
     * Filtrar por tipo de animal.
     * Los filtros actualizan la lista.
     */
    private void configurarFiltros() {
        togglePerros = findViewById ( R.id.filtroPerros );
        toggleGatos = findViewById ( R.id.filtroGatos );
        togglePerros.setOnCheckedChangeListener ( (buttonView , isChecked) ->
                actualizarListaFiltrada () );
        toggleGatos.setOnCheckedChangeListener ( (buttonView , isChecked) ->
                actualizarListaFiltrada () );
    }

    /**
     * Configura los botones de acción:
     * Agregar.
     * Eliminar.
     */
    private void configurarBotones() {
        ImageButton botonAgregar = findViewById ( R.id.agregar );
        botonAgregar.setOnClickListener ( v -> agregarAnimal () );
        ImageButton botonEliminar = findViewById ( R.id.eliminar );
        botonEliminar.setOnClickListener ( v -> eliminarAnimal () );
    }

    /**
     * Configura el RecyclerView para la lista de favoritos.
     * Los favoritos se muestran en una lista horizontal debajo del RecyclerView principal.
     */
    private void configurarRecyclerViewFavoritos() {
        List<Animal> listaFavoritos1 = new ArrayList<> ();
        RecyclerView rvFavoritos = findViewById ( R.id.recyclerViewFavoritos );
        rvFavoritos.setLayoutManager ( new LinearLayoutManager ( this , LinearLayoutManager.HORIZONTAL , false ) );
        adaptadorFavoritos = new AdaptadorAnimal ( listaFavoritos1 );
        rvFavoritos.setAdapter ( adaptadorFavoritos );
        // Listener para actualizar la lista de favoritos
        animalAdapter.setFavoritosActualizadosListener ( listaFavoritos -> adaptadorFavoritos.actualizarLista ( listaFavoritos ) );
    }

    /**
     * Actualiza la lista mostrada en el RecyclerView según los filtros seleccionados.
     */
    private void actualizarListaFiltrada() {
        List<Animal> listaFiltrada = new ArrayList<> ();
        boolean filtroPerrosActivo = togglePerros.isChecked ();
        boolean filtroGatosActivo = toggleGatos.isChecked ();
        // Verifica si la lista de animales no está vacía
        if (listaAnimales != null && !listaAnimales.isEmpty ()) {
            if (filtroPerrosActivo || filtroGatosActivo) {
                // Filtrar por tipo de animal
                for (Animal animal : listaAnimales) {
                    if (filtroPerrosActivo && animal.getTipo () == Animal.TipoAnimal.PERRO) {
                        listaFiltrada.add ( animal );
                    }
                    if (filtroGatosActivo && animal.getTipo () == Animal.TipoAnimal.GATO) {
                        listaFiltrada.add ( animal );
                    }
                }
            } else {
                // Si no hay filtros activos, mostrar todos los animales
                listaFiltrada.addAll ( listaAnimales );
            }
        }
        // Actualizar la lista del adaptador
        animalAdapter.actualizarLista ( listaFiltrada );
        // Si la lista filtrada está vacía, mostrar mensaje de advertencia
        if (listaFiltrada.isEmpty ()) {
            Log.w ( TAG , getString ( R.string.no_se_encontraron_animales_que_coincidan_con_los_filtros ) );
            Toast.makeText ( this , R.string.no_se_encontraron_animales_que_coincidan_con_los_filtros , Toast.LENGTH_SHORT ).show ();
        }
    }

    /**
     * Agrega un nuevo animal con valores predeterminados a la lista principal.
     */
    private void agregarAnimal() {
        try {
            if (listaAnimales == null) {
                listaAnimales = new ArrayList<>(); // Si es null, inicializa la lista
            }
            Animal nuevoAnimal = new Animal ( "Nuevo" , R.drawable.imagen_perro_1 , Animal.TipoAnimal.PERRO , 1 , getString ( R.string.nuevo_animal_a_adido ) );
            listaAnimales.add ( nuevoAnimal );
            animalAdapter.actualizarLista ( listaAnimales );
            Toast.makeText ( this , getString ( R.string.animal_a_adido_a_la_lista ) , Toast.LENGTH_SHORT ).show ();
            SpannableString toastMessage = new SpannableString ( getString ( R.string.pr_ximamente_se_implementar_una_forma_m_s_avanzada_de_a_adir_animales ) );
            toastMessage.setSpan ( new ForegroundColorSpan ( Color.RED ) , 0 , toastMessage.length () , 0 );
            Toast.makeText ( this , toastMessage , Toast.LENGTH_LONG ).show ();
        } catch (Exception e) {
            Log.e(TAG, getString( R.string.error_al_agregar_un_nuevo_animal) + e.getMessage(), e);
            Toast.makeText(this, R.string.error_al_agregar_el_animal, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Elimina el animal seleccionado de la lista, si no hay filtros activos.
     */
    private void eliminarAnimal() {
        if (!togglePerros.isChecked() && !toggleGatos.isChecked()) {
            // Asegúrate de que listaAnimales no sea null antes de usarla
            if (listaAnimales != null && !listaAnimales.isEmpty()) {
                // Obtener la posición seleccionada desde el adaptador
                int posicionSeleccionada = animalAdapter.getPosicionSeleccionada();

                // Verificar si hay un animal seleccionado
                if (posicionSeleccionada != -1) {
                    // Eliminar el animal de la lista en la posición seleccionada
                    Animal animalEliminado = listaAnimales.remove(posicionSeleccionada);
                    animalAdapter.actualizarLista(listaAnimales); // Actualizar la lista en el adaptador
                    // Mostrar mensaje de eliminación
                    Toast.makeText(this, animalEliminado.getNombre() + getString(R.string.ha_sido_eliminado), Toast.LENGTH_SHORT).show();
                    // Restablecer la posición seleccionada en el adaptador
                    animalAdapter.setPosicionSeleccionada(-1); // Si tienes un método para resetear la posición
                } else {
                    // Mostrar mensaje si no hay selección
                    Toast.makeText(this, R.string.no_hay_animales_seleccionados_o_la_lista_est_vac_a, Toast.LENGTH_SHORT).show();
                    Log.w(TAG, getString(R.string.no_se_seleccion_un_animal_para_eliminar));
                }
            } else {
                // Si la lista es null o está vacía
                Toast.makeText(this, R.string.lista_vacia, Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, R.string.desactiva_los_filtros_para_poder_eliminar_un_animal, Toast.LENGTH_SHORT).show();
            Log.w(TAG, getString(R.string.no_se_puede_eliminar_con_filtros_activos));
        }
    }
}