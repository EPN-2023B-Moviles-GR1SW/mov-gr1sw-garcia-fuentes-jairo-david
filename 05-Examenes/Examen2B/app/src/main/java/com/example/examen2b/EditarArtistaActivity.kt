package com.example.examen2b

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.EditText
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await


class EditarArtistaActivity : AppCompatActivity() {
    val arregloCanciones: ArrayList<Cancion> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_artista)

        val linearLayout = findViewById<LinearLayout>(R.id.ll_edit_canciones)
        consultarCanciones()

        val idArtista = intent.getStringExtra("idArtista")
        lifecycleScope.launch(Dispatchers.Main) {
            val artista = consultarArtistaPorId(idArtista!!)
            actualizarLinearLayout(linearLayout, artista)
            //Autollenado de datos
            val inputNombre = findViewById<EditText>(R.id.input_edit_nombre)
            val inputEdad = findViewById<EditText>(R.id.input_edit_edad)

            val rdbVivo = findViewById<RadioButton>(R.id.rdb_edit_vivo)
            val rdbMuerto = findViewById<RadioButton>(R.id.rdb_edit_muerto)
            val inputPatrimonio = findViewById<EditText>(R.id.input_edit_patrimonio)

            artista?.let {
                inputNombre.setText(it.nombre)
                inputEdad.setText(it.edad.toString())

                if (it.vivo!!) {
                    rdbVivo.isChecked = true
                } else {
                    rdbMuerto.isChecked = true
                }
                inputPatrimonio.setText(it.patrimonio.toString())
            }

            val botonEditarArtista = findViewById<Button>(R.id.btn_editar_artista)
            botonEditarArtista.setOnClickListener {
                guardarArtista(artista!!)
                intent = Intent(this@EditarArtistaActivity, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }
    fun consultarCanciones(){
        val db = Firebase.firestore
        val referencia = db.collection("canciones")
        referencia
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    val cancion = document.toObject(Cancion::class.java)
                    cancion.id = document.id
                    arregloCanciones.add(cancion)
                }
            }
            .addOnFailureListener {
            }
    }

    fun actualizarLinearLayout(linearLayout: LinearLayout, artista: Artista?) {

        for (cancion in arregloCanciones) {
            val checkBox = CheckBox(this)
            checkBox.text = cancion.nombre // Asigna el nombre de la canción al texto del CheckBox
            checkBox.tag = cancion.id // Asigna el ID de la canción como una etiqueta al CheckBox

            // Agrega el CheckBox al LinearLayout
            linearLayout.addView(checkBox)

            // Marcar las canciones que ya tiene el artista
            artista?.canciones?.forEach { cancionArtista ->
                if (cancionArtista.id == cancion.id) {
                    checkBox.isChecked = true
                }
            }
        }

    }

    suspend fun consultarArtistaPorId(idArtista: String): Artista? {
        return try {
            val document = FirebaseFirestore.getInstance()
                .collection("artistas")
                .document(idArtista)
                .get()
                .await()

            val artista = document.toObject<Artista>()
            artista?.id = document.id

            artista
        } catch (e: Exception) {
            null
        }
    }



    fun guardarArtista (artista: Artista) {
        // Verificar si el artista no es nulo antes de intentar actualizar
        artista?.let {
            // Obtener referencias a los elementos de la interfaz de usuario
            val inputNombre = findViewById<EditText>(R.id.input_edit_nombre)
            val inputEdad = findViewById<EditText>(R.id.input_edit_edad)
            val inputPatrimonio = findViewById<EditText>(R.id.input_edit_patrimonio)
            val llCanciones = findViewById<LinearLayout>(R.id.ll_edit_canciones)
            val rdbVivo = findViewById<RadioButton>(R.id.rdb_edit_vivo)

            // Obtener los valores ingresados
            it.nombre = inputNombre.text.toString()
            it.edad = inputEdad.text.toString().toInt()
            it.patrimonio = inputPatrimonio.text.toString().toDouble()
            it.vivo = rdbVivo.isChecked

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
            val referencia = db.collection("artistas").document(artista.id!!)
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
                "nombre" to it.nombre,
                "canciones" to cancionesMap,
                "edad" to it.edad,
                "vivo" to it.vivo,
                "patrimonio" to it.patrimonio
            )

            referencia
                .update(nuevoArtista)
                .addOnSuccessListener {
                }
                .addOnFailureListener {
                }


            // Limpiar los campos después de guardar el artista
            inputNombre.text.clear()
            inputEdad.text.clear()
            inputPatrimonio.text.clear()

            // Limpiar las CheckBox de canciones
            for (i in 0 until llCanciones.childCount) {
                val checkBox = llCanciones.getChildAt(i) as CheckBox
                checkBox.isChecked = false
            }
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
            findViewById(R.id.cl_editar_artista),//view
            texto, //texto
            Snackbar.LENGTH_LONG //tiempo
        ).show()
    }

}