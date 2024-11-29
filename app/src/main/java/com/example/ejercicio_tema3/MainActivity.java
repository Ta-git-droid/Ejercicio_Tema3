package com.example.ejercicio_tema3;

import static android.content.ContentValues.TAG;

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

    }

    /**
     * Inicializar las listas de animales (array, perros y gatos):
     * Crear objetos de tipo Animal.
     */
    private void inicializarListas() {
        listaAnimales = new ArrayList<>();
        for (int i = 0; i < 999999; i++) {
            String nombre = "Animal " + (i + 1);
            Animal.TipoAnimal tipo = (i % 2 == 0) ? Animal.TipoAnimal.PERRO : Animal.TipoAnimal.GATO;
            int edad = (int) (Math.random() * 10) + 1;  // Edad aleatoria entre 1 y 10
            String descripcion = "Descripción del animal " + (i + 1);
            int imagen = (i % 2 == 0) ? R.drawable.imagen_perro_1 : R.drawable.imagen_gato_1;
            listaAnimales.add(new Animal(nombre, imagen, tipo, edad, descripcion));
        }
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