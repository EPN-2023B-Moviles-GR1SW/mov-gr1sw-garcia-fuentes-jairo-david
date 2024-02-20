package com.example.deber02

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.DatePicker
import android.widget.EditText
import android.widget.ImageButton
import androidx.annotation.RequiresApi
import com.example.deber02.sqlite.SqliteBDD
import com.google.android.material.snackbar.Snackbar
import java.text.SimpleDateFormat

class EditarCancionActivity : AppCompatActivity() {
    var txtEditFecha: EditText? = null
    var btnEditFecha: ImageButton? = null
    var dpEditFecha: DatePicker? = null
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_cancion)

        val idCancion = intent.getIntExtra("idCancion", -1)

        val cancion = SqliteBDD.BDMundoMuscial!!.consultarCancionPorID(idCancion)

        //Autollenado de datos
        val inputNombre = findViewById<EditText>(R.id.input_nombre_edit_cancion)
        val inputDuracion = findViewById<EditText>(R.id.input_edit_duracion)
        val inputAlbum = findViewById<EditText>(R.id.input_edit_album)
        val inputGenero = findViewById<EditText>(R.id.input_edit_genero)
        txtEditFecha = findViewById<EditText>(R.id.input_edit_fecha)
        btnEditFecha = findViewById<ImageButton>(R.id.btn_edit_fecha)
        dpEditFecha = findViewById<DatePicker>(R.id.dp_edit_fecha)

        txtEditFecha?.setText(dateFormat.format(cancion.fechaLanzamiento))

        cancion?.let {
            inputNombre.setText(it.nombre)
            inputDuracion.setText(it.duracion.toString())
            inputAlbum.setText(it.album)
            inputGenero.setText(it.genero)
        }

        dpEditFecha?.setOnDateChangedListener{ dpEditFecha, anio, mes, dia ->
            txtEditFecha?.setText(getFechaDtPicker())
            dpEditFecha.visibility = View.GONE
        }

        val botonEditarCancion = findViewById<android.widget.Button>(R.id.btn_guardar_cancion)
        botonEditarCancion.setOnClickListener() {
            guardarCancion(cancion!!)
            intent = Intent(this, CancionActivity::class.java)
            intent.putExtra("idArtista", cancion.idArtista)
            startActivity(intent)
        }


    }

    fun guardarCancion( cancion: Cancion){

        val inputNombre = findViewById<EditText>(R.id.input_nombre_edit_cancion)
        val inputDuracion = findViewById<EditText>(R.id.input_edit_duracion)
        val inputAlbum = findViewById<EditText>(R.id.input_edit_album)
        val inputGenero = findViewById<EditText>(R.id.input_edit_genero)

        val nombre = inputNombre.text.toString()
        val duracion = inputDuracion.text.toString().toInt()
        val album = inputAlbum.text.toString()
        val genero = inputGenero.text.toString()
        val fechaLanzamiento = txtEditFecha?.text.toString()


        SqliteBDD.BDMundoMuscial!!.actualizarCancion(
            nombre,
            cancion.idArtista,
            duracion,
            album,
            genero,
            fechaLanzamiento,
            cancion.id
        )

        //Limpiar campos
        inputNombre.text.clear()
        inputDuracion.text.clear()
        inputAlbum.text.clear()
        inputGenero.text.clear()

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

    fun mostrarSnackbar(texto:String){
        Snackbar.make(
            findViewById(R.id.cl_editar_cancion),//view
            texto, //texto
            Snackbar.LENGTH_LONG //tiempo
        ).show()
    }
}