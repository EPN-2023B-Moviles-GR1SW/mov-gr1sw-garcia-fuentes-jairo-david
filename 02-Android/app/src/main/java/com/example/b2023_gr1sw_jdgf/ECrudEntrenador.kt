package com.example.b2023_gr1sw_jdgf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.google.android.material.snackbar.Snackbar
import android.widget.EditText

class ECrudEntrenador : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ecrud_entrenador)

        // logica buscar entrenador
        val botonBuscarBDD = findViewById<Button>(R.id.btn_buscar_bdd)
        botonBuscarBDD.setOnClickListener {
            // Obtener componentes visuales
            val id = findViewById<EditText>(R.id.input_id)
            val nombre = findViewById<EditText>(R.id.input_nombre)
            val descripcion = findViewById<EditText>(R.id.input_descripcion)
            // Buscar en la base de datos
            val entrenador =
                EBaseDeDatos.tablaEntrenador!!.consularEntrenadorPorID(id.text.toString().toInt())
            // Setear los valores en los componentes visuales
            id.setText(entrenador.id.toString())
            nombre.setText(entrenador.nombre)
            descripcion.setText(entrenador.descripcion)
        }

        // logica crear entrenador
        val botonCrearBDD = findViewById<Button>(R.id.btn_crear_bdd)
        botonCrearBDD.setOnClickListener {
            // Obtener componentes visuales
            val nombre = findViewById<EditText>(R.id.input_nombre)
            val descripcion = findViewById<EditText>(R.id.input_descripcion)
            val respuesta = EBaseDeDatos.tablaEntrenador!!.crearEntrenador(
                nombre.text.toString(),
                descripcion.text.toString()
            )
            if (respuesta) mostrarSnackbar("Ent. creada")
        }

        // logica actualizar entrenador
        val botonActualizarBDD = findViewById<Button>(R.id.btn_actualizar_bdd)
        botonActualizarBDD.setOnClickListener {
            // Obtener componentes visuales
            val id = findViewById<EditText>(R.id.input_id)
            val nombre = findViewById<EditText>(R.id.input_nombre)
            val descripcion = findViewById<EditText>(R.id.input_descripcion)
            val respuesta = EBaseDeDatos.tablaEntrenador!!.actualizarEntrenadorFormulario(
                nombre.text.toString(),
                descripcion.text.toString(),
                id.text.toString().toInt()
            )
            if (respuesta) mostrarSnackbar("Ent. actualizada")
        }

        // logica eliminar entrenador
        val botonEliminarBDD = findViewById<Button>(R.id.btn_eliminar_bdd)
        botonEliminarBDD.setOnClickListener {
            // Obtener componentes visuales
            val id = findViewById<EditText>(R.id.input_id)
            val respuesta = EBaseDeDatos.tablaEntrenador!!.eliminarEntrenadorFormulario(
                id.text.toString().toInt()
            )
            if (respuesta) mostrarSnackbar("Ent. eliminada")
        }
    }

    fun mostrarSnackbar(texto:String){
        Snackbar.make(
            findViewById(R.id.cl_sqlite),//view
            texto, //texto
            Snackbar.LENGTH_LONG //tiempo
        ).show()
    }
}

