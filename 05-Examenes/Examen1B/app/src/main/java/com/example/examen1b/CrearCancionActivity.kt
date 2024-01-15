package com.example.examen1b

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
import java.text.SimpleDateFormat

class CrearCancionActivity : AppCompatActivity() {
    val arregloArtistas = BaseDatosMemoria.arrayArtista
    var txtFecha:EditText? = null
    var btnFecha:ImageButton? = null
    var dpFecha:DatePicker? = null
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_cancion)

        val idArtista= intent.getIntExtra("idArtista", -1)

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
            intent = Intent(this, CancionActivity::class.java)
            intent.putExtra("idArtista", idArtista)
            startActivity(intent)
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

        val idArtista= intent.getIntExtra("idArtista", -1)
        val artista = arregloArtistas.find { artista -> artista.id == idArtista }


        val nuevaCancion = Cancion(BaseDatosMemoria.contadorCanciones, nombre, duracion, album, genero, dateFormat.parse(fecha)!!)

        BaseDatosMemoria.agregarCancion(nuevaCancion)
        artista!!.canciones.add(nuevaCancion)

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