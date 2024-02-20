package com.example.examen2b

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import androidx.lifecycle.lifecycleScope
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList
import android.widget.Button
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditarCancionActivity : AppCompatActivity() {
    var txtEditFecha: EditText? = null
    var btnEditFecha: ImageButton? = null
    var dpEditFecha: DatePicker? = null

    var arregloCanciones: ArrayList<Cancion> = arrayListOf()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cancion)

        val idCancion = intent.getStringExtra("idCancion")
        val idArtista = intent.getStringExtra("idArtista")
        consultarCancionesPorArtista(idArtista!!)

        //Autollenado de datos
        val inputNombre = findViewById<EditText>(R.id.input_nombre_edit_cancion)
        val inputDuracion = findViewById<EditText>(R.id.input_edit_duracion)
        val inputAlbum = findViewById<EditText>(R.id.input_edit_album)
        val inputGenero = findViewById<EditText>(R.id.input_edit_genero)
        txtEditFecha = findViewById<EditText>(R.id.input_edit_fecha)
        btnEditFecha = findViewById<ImageButton>(R.id.btn_edit_fecha)
        dpEditFecha = findViewById<DatePicker>(R.id.dp_edit_fecha)

        dpEditFecha?.setOnDateChangedListener{ dpEditFecha, anio, mes, dia ->
            txtEditFecha?.setText(getFechaDtPicker())
            dpEditFecha.visibility = View.GONE
        }

        lifecycleScope.launch(Dispatchers.Main) {
            val cancion = consultarCancionPorId(idCancion!!)
            txtEditFecha?.setText(cancion!!.fechaLanzamiento)

            cancion?.let {
                inputNombre.setText(it.nombre)
                inputDuracion.setText(it.duracion.toString())
                inputAlbum.setText(it.album)
                inputGenero.setText(it.genero)
            }

            val botonEditarCancion = findViewById<Button>(R.id.btn_guardar_cancion)
            botonEditarCancion.setOnClickListener {
                guardarCancion(cancion!!, idArtista!!)
                intent = Intent(this@EditarCancionActivity, CancionActivity::class.java)
                intent.putExtra("idArtista", idArtista)
                startActivity(intent)
            }
        }
    }

    suspend fun consultarCancionPorId(idCancion: String): Cancion? {
        return try {
            val document = FirebaseFirestore.getInstance()
                .collection("canciones")
                .document(idCancion)
                .get()
                .await()

            val cancion = document.toObject<Cancion>()
            cancion?.id = document.id
            cancion

        } catch (e: Exception) {
            null
        }
    }

    fun guardarCancion(cancion: Cancion, idArtista: String){

        cancion?.let {
            val inputNombre = findViewById<EditText>(R.id.input_nombre_edit_cancion)
            val inputDuracion = findViewById<EditText>(R.id.input_edit_duracion)
            val inputAlbum = findViewById<EditText>(R.id.input_edit_album)
            val inputGenero = findViewById<EditText>(R.id.input_edit_genero)

            it.nombre = inputNombre.text.toString()
            it.duracion = inputDuracion.text.toString().toInt()
            it.album = inputAlbum.text.toString()
            it.genero = inputGenero.text.toString()
            it.fechaLanzamiento = txtEditFecha?.text.toString()

            val db = Firebase.firestore
            val referenciaCancion = db.collection("canciones").document(cancion.id!!)
            val cancionMap = hashMapOf(
                "nombre" to it.nombre,
                "duracion" to it.duracion,
                "album" to it.album,
                "genero" to it.genero,
                "fechaLanzamiento" to it.fechaLanzamiento
            )

            referenciaCancion
                .set(cancionMap)
                .addOnSuccessListener {
                    val referencia = db.collection("artistas").document(idArtista)

                    for (cancionArtista in arregloCanciones){
                        if (cancionArtista.id == cancion.id){
                            arregloCanciones[arregloCanciones.indexOf(cancionArtista)] = cancion
                        }
                    }
                    referencia.update("canciones", arregloCanciones)
                        .addOnSuccessListener {
                        }
                        .addOnFailureListener {
                        }
                }
                .addOnFailureListener {
                }

            //Limpiar campos
            inputNombre.text.clear()
            inputDuracion.text.clear()
            inputAlbum.text.clear()
            inputGenero.text.clear()
        }
    }

    fun getFechaDtPicker(): String {
        val dia = dpEditFecha?.dayOfMonth.toString().padStart(2, '0')
        val mes = (dpEditFecha!!.month+1).toString().padStart(2, '0')
        val anio = dpEditFecha?.year.toString().padStart(4, '0')
        return "$dia/$mes/$anio"
    }

    fun mostrarCalendario(view: View){
        dpEditFecha?.visibility = View.VISIBLE
    }

    fun consultarCancionesPorArtista(idArtista: String){
        val db = Firebase.firestore
        val referencia = db.collection("artistas").document(idArtista)
        referencia
            .get()
            .addOnSuccessListener {
                val canciones = it.data?.get("canciones") as ArrayList<HashMap<String, Any>>
                if (canciones != null) {
                    for (cancion in canciones){
                        val cancionObj = Cancion(
                            cancion["id"] as String,
                            cancion["nombre"] as String,
                            (cancion["duracion"] as Long).toInt(),
                            cancion["album"] as String,
                            cancion["genero"] as String,
                            cancion["fechaLanzamiento"] as String
                        )
                        arregloCanciones.add(cancionObj)
                    }
                }
            }
            .addOnFailureListener {
            }
    }

    fun mostrarSnackbar(texto:String){
        Snackbar.make(
            findViewById(R.id.cl_editar_cancion),//view
            texto, //texto
            Snackbar.LENGTH_LONG //tiempo
        ).show()
    }
}