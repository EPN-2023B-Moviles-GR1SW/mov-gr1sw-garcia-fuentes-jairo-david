package com.example.examen1b


import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    val arreglo = BaseDatosMemoria.arrayArtista
    var posicionItemSeleccionado = -1

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.list_artista)

        val adaptador = ArrayAdapter(
            this, //Context
            android.R.layout.simple_list_item_1, //como se va a ver (XML)
            arreglo
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()


        val botonCrearArtista = findViewById<Button>(R.id.btn_crear_artista)
        botonCrearArtista.setOnClickListener {
            val intent = Intent(this, CrearArtistaActivity::class.java)
            startActivity(intent)
        }
        registerForContextMenu(listView)
    }

    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        //Llnamos las opciones del menu
        val inflater = menuInflater
        inflater.inflate(R.menu.menu_artista, menu)
        // Obtener el id del ArrayListSeleccionado
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.mi_editar_artista ->{
                val intent = Intent(
                    this,
                    EditarArtistaActivity::class.java
                )
                intent.putExtra("idArtista", arreglo[posicionItemSeleccionado].id)
                startActivity(intent)
                return true
            }
            R.id.mi_eliminar_artista ->{
                arreglo.removeAt(posicionItemSeleccionado)
                val listView = findViewById<ListView>(R.id.list_artista)
                (listView.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                return true
            }
            R.id.mi_ver_canciones ->{
                val intent = Intent(
                    this,
                    CancionActivity::class.java
                )
                intent.putExtra("idArtista", arreglo[posicionItemSeleccionado].id)
                startActivity(intent)
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun mostrarSnackbar(texto:String){
        Snackbar.make(
            findViewById(R.id.cl_artistas),//view
            texto, //texto
            Snackbar.LENGTH_LONG //tiempo
        ).show()
    }
}
