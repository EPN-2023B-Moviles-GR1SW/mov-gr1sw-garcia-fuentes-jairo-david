package com.example.deber02

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.EditText
import com.example.deber02.sqlite.SqliteBDD
import com.google.android.material.snackbar.Snackbar

class EditarArtistaActivity : AppCompatActivity() {
    val arregloCanciones = SqliteBDD.BDMundoMuscial!!.consultarCanciones()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_artista)

        val idArtista = intent.getIntExtra("idArtista", -1)
        val artista = SqliteBDD.BDMundoMuscial!!.consultarArtistaPorID(idArtista)

        //Autollenado de datos
        val inputNombre = findViewById<EditText>(R.id.input_edit_nombre)
        val inputEdad = findViewById<EditText>(R.id.input_edit_edad)
        val linearLayout = findViewById<LinearLayout>(R.id.ll_edit_canciones)
        val rdbVivo = findViewById<RadioButton>(R.id.rdb_edit_vivo)
        val rdbMuerto = findViewById<RadioButton>(R.id.rdb_edit_muerto)
        val inputPatrimonio = findViewById<EditText>(R.id.input_edit_patrimonio)

        artista?.let {
            inputNombre.setText(it.nombre)
            inputEdad.setText(it.edad.toString())

            // Itera sobre tu arreglo de canciones y crea CheckBox dinámicamente
            for (cancion in arregloCanciones) {
                val checkBox = CheckBox(this)
                checkBox.text = cancion.nombre // Asigna el nombre de la canción al texto del CheckBox
                checkBox.tag = cancion.id // Asigna el ID de la canción como una etiqueta al CheckBox

                // Verifica si la canción está presente en la lista de canciones del artista
                if (cancion.idArtista == it.id ) {
                    checkBox.isChecked = true
                }

                // Agrega el CheckBox al LinearLayout
                linearLayout.addView(checkBox)
            }

            if (it.vivo) {
                rdbVivo.isChecked = true
            } else {
                rdbMuerto.isChecked = true
            }
            inputPatrimonio.setText(it.patrimonio.toString())
        }

        val botonEditarArtista = findViewById<Button>(R.id.btn_editar_artista)
        botonEditarArtista.setOnClickListener {
            guardarArtista(artista!!)
            intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
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

            // Actualizar el artista en la base de datos
            SqliteBDD.BDMundoMuscial!!.actualizarArtista(
                it.nombre,
                it.edad,
                it.vivo,
                it.patrimonio,
                it.id,
            )

            // Iterar sobre los CheckBox en el LinearLayout de canciones
            for (i in 0 until llCanciones.childCount) {
                val checkBox = llCanciones.getChildAt(i) as CheckBox
                val idCancion = checkBox.tag as Int
                val nuevaCancion = obtenerCancionPorId(idCancion)
                if (checkBox.isChecked) {
                    if (nuevaCancion != null) {
                        SqliteBDD.BDMundoMuscial!!.asignarArtistaACancion(
                            it.id,
                            idCancion
                        )
                    }
                }else{
                    SqliteBDD.BDMundoMuscial!!.asignarArtistaACancion(
                        null,
                        idCancion
                    )
                }
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

    // Función para obtener la canción por su ID
    fun obtenerCancionPorId(idCancion: Int): Cancion? {
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