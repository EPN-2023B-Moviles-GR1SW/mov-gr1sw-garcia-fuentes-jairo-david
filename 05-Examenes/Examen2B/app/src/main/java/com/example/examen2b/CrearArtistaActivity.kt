package com.example.examen2b


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.EditText
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList
import com.google.android.material.snackbar.Snackbar

class CrearArtistaActivity : AppCompatActivity() {
    val arregloCanciones: ArrayList<Cancion> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_artista)

        val linearLayout = findViewById<LinearLayout>(R.id.ll_canciones)
        consultarCanciones(linearLayout)

        val botonAnadirArtista = findViewById<Button>(R.id.btn_anadir_artista)
        botonAnadirArtista.setOnClickListener {
            anadirArtista()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    fun consultarCanciones(linearLayout: LinearLayout) {
        val db = Firebase.firestore
        val referencia = db.collection("canciones")
        referencia
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {

                    val cancion = document.toObject(Cancion::class.java)
                    cancion.id = document.id
                    val checkBox = CheckBox(this)
                    checkBox.text = cancion.nombre // Asigna el nombre de la canción al texto del CheckBox
                    checkBox.tag = cancion.id // Asigna el ID de la canción como una etiqueta al CheckBox

                    // Agrega el CheckBox al LinearLayout
                    linearLayout.addView(checkBox)
                    arregloCanciones.add(cancion)
                }
            }
            .addOnFailureListener {
            }
    }

    fun anadirArtista(){
        // Obtener referencias a los elementos de la interfaz de usuario
        val inputNombre = findViewById<EditText>(R.id.input_nombre_artista)
        val inputEdad = findViewById<EditText>(R.id.input_edad)
        val inputPatrimonio = findViewById<EditText>(R.id.input_patrimonio)
        val llCanciones = findViewById<LinearLayout>(R.id.ll_canciones)
        val rdbVivo = findViewById<RadioButton>(R.id.rdb_vivo)

        // Obtener los valores ingresados
        val nombre = inputNombre.text.toString()
        val edad = inputEdad.text.toString().toInt()
        val patrimonio = inputPatrimonio.text.toString().toDouble()
        val vivo = rdbVivo.isChecked


        // Agregar canciones al nuevo artista
        val cancionesSeleccionadas = arrayListOf<Cancion>()
        for (i in 0 until llCanciones.childCount) {
            val checkBox = llCanciones.getChildAt(i) as CheckBox
            if (checkBox.isChecked) {
                val idCancion = obtenerIdCancionDesdeCheckBox(checkBox)
                val nuevaCancion = obtenerCancionPorId(idCancion)
                if (nuevaCancion != null) {
                    cancionesSeleccionadas.add(nuevaCancion)
                }
            }
        }

        // Crear un nuevo artista con los valores ingresados
        val db = Firebase.firestore
        val referencia = db.collection("artistas")
        val cancionesMap = cancionesSeleccionadas.map { cancion ->
            mapOf(
                "id" to cancion.id,
                "nombre" to cancion.nombre,
                "duracion" to cancion.duracion,
                "album" to cancion.album,
                "genero" to cancion.genero,
                "fechaLanzamiento" to cancion.fechaLanzamiento
            )
        }

        val nuevoArtista = hashMapOf(
            "nombre" to nombre,
            "canciones" to cancionesMap,
            "edad" to edad,
            "vivo" to vivo,
            "patrimonio" to patrimonio
        )

        referencia
            .add(nuevoArtista)
            .addOnSuccessListener {
            }
            .addOnFailureListener {
            }

        // Limpiar los campos después de agregar el artista
        inputNombre.text.clear()
        inputEdad.text.clear()
        inputPatrimonio.text.clear()

        // Limpiar las CheckBox de canciones
        for (i in 0 until llCanciones.childCount) {
            val checkBox = llCanciones.getChildAt(i) as CheckBox
            checkBox.isChecked = false
        }

    }

    // Función para obtener el ID de la canción desde el CheckBox
    fun obtenerIdCancionDesdeCheckBox(checkBox: CheckBox): String {
        return checkBox.tag as? String ?: ""
    }

    // Función para obtener la canción por su ID
    fun obtenerCancionPorId(idCancion: String): Cancion? {
        // Itera sobre el arreglo de canciones y busca la canción con el ID dado
        for (cancion in arregloCanciones) {
            if (cancion.id == idCancion) {
                return cancion
            }
        }
        return null  // Retorna null si no se encuentra la canción con el ID dado
    }

    fun mostrarSnackbar(texto:String){
        Snackbar.make(
            findViewById(R.id.cl_crear_artista),//view
            texto, //texto
            Snackbar.LENGTH_LONG //tiempo
        ).show()
    }
}