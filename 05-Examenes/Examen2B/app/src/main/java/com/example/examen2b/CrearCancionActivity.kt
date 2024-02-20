package com.example.examen2b

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class CrearCancionActivity : AppCompatActivity() {

    var txtFecha:EditText? = null
    var btnFecha:ImageButton? = null
    var dpFecha:DatePicker? = null


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cancion)

        txtFecha = findViewById<EditText>(R.id.input_fecha)
        btnFecha = findViewById<ImageButton>(R.id.btn_fecha)
        dpFecha = findViewById<DatePicker>(R.id.dp_fecha)

        txtFecha?.setText(getFechaDtPicker())

        dpFecha?.setOnDateChangedListener{ dpFecha, anio, mes, dia ->
            txtFecha?.setText(getFechaDtPicker())
            dpFecha.visibility = View.GONE
        }

        val botonAnadirCancion = findViewById<Button>(R.id.btn_anadir_cancion)
        botonAnadirCancion.setOnClickListener() {
            anadirCancion()

        }

    }

    fun anadirCancion (){
        val inputNombre = findViewById<EditText>(R.id.input_nombre_cancion)
        val inputDuracion = findViewById<EditText>(R.id.input_duracion)
        val inputAlbum = findViewById<EditText>(R.id.input_album)
        val inputGenero = findViewById<EditText>(R.id.input_genero)

        val nombre = inputNombre.text.toString()
        val duracion = inputDuracion.text.toString().toInt()
        val album = inputAlbum.text.toString()
        val genero = inputGenero.text.toString()
        val fecha = txtFecha?.text.toString()

        val idArtista= intent.getStringExtra("idArtista")

        val db = Firebase.firestore
        val referenciaCancion = db.collection("canciones")
        val nuevaCancionMap = hashMapOf(
            "nombre" to nombre,
            "duracion" to duracion,
            "album" to album,
            "genero" to genero,
            "fechaLanzamiento" to fecha
        )

        referenciaCancion
            .add(nuevaCancionMap)
            .addOnSuccessListener {
                val idCancion = it.id

                val referenciaArtista = db.collection("artistas").document(idArtista!!)
                referenciaArtista.update(
                    "canciones", FieldValue.arrayUnion(
                        hashMapOf(
                            "id" to idCancion,
                            "nombre" to nombre,
                            "duracion" to duracion,
                            "album" to album,
                            "genero" to genero,
                            "fechaLanzamiento" to fecha
                        )
                    )
                )
                val intent = Intent(this, CancionActivity::class.java)
                intent.putExtra("idArtista", idArtista)
                startActivity(intent)
            }
            .addOnFailureListener {
            }


        //Limpiar campos
        inputNombre.text.clear()
        inputDuracion.text.clear()
        inputAlbum.text.clear()
        inputGenero.text.clear()


    }

    fun getFechaDtPicker(): String {
        val dia = dpFecha?.dayOfMonth.toString().padStart(2, '0')
        val mes = (dpFecha!!.month+1).toString().padStart(2, '0')
        val anio = dpFecha?.year.toString().padStart(4, '0')
        return "$dia/$mes/$anio"
    }

    fun mostrarCalendario(view: View){
        dpFecha?.visibility = View.VISIBLE
    }

    fun mostrarSnackbar(texto:String){
        Snackbar.make(
            findViewById(R.id.cl_crear_cancion),//view
            texto, //texto
            Snackbar.LENGTH_LONG //tiempo
        ).show()
    }
}