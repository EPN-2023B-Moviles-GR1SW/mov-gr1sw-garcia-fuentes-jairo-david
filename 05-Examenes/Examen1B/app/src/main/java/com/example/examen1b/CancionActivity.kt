package com.example.examen1b


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ContextMenu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.TextView
import com.example.examen1b.sqlite.SqliteBDD
import com.google.android.material.snackbar.Snackbar

class CancionActivity : AppCompatActivity() {

    var arregloCanciones = arrayListOf<Cancion>()
    var posicionItemSeleccionado = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancion)


        val idArtista= intent.getIntExtra("idArtista", -1)
        val artista = SqliteBDD.BDMundoMuscial!!.consultarArtistaPorID(idArtista)


        val txtArtista = findViewById<TextView>(R.id.txt_artista)
        txtArtista.text = artista.nombre
        val txtEdad = findViewById<TextView>(R.id.txt_edad)
        txtEdad.text = "Edad: ${artista.edad}"
        val txtPatrimonio = findViewById<TextView>(R.id.txt_patrimonio)
        txtPatrimonio.text = "Patrimonio: $${artista.patrimonio}"
        val txtEstado = findViewById<TextView>(R.id.txt_estado)
        if (artista.vivo){
            txtEstado.text = "Estado: Vivo"
        }else{
            txtEstado.text = "Estado: Muerto"
        }

        val listView = findViewById<ListView>(R.id.list_cancion)
        arregloCanciones = SqliteBDD.BDMundoMuscial!!.consultarCancionesPorArtista(idArtista)
        val adaptador = ArrayAdapter(
            this, //Context
            android.R.layout.simple_list_item_1, //como se va a ver (XML)
            arregloCanciones
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()



        val botonCrearCancion = findViewById<Button>(R.id.btn_crear_cancion)
        botonCrearCancion.setOnClickListener {
            val intent = Intent(this, CrearCancionActivity::class.java)
            intent.putExtra("idArtista", idArtista)
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
        inflater.inflate(R.menu.menu_cancion, menu)
        // Obtener el id del ArrayListSeleccionado
        val info = menuInfo as AdapterView.AdapterContextMenuInfo
        val posicion = info.position
        posicionItemSeleccionado = posicion
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val idArtista= intent.getIntExtra("idArtista", -1)
        return when (item.itemId){
            R.id.mi_editar_cancion ->{
                val intent = Intent(this, EditarCancionActivity::class.java)
                intent.putExtra("idCancion", arregloCanciones[posicionItemSeleccionado].id)
                startActivity(intent)
                return true
            }
            R.id.mi_eliminar_cancion ->{
                val exitoEliminacion = SqliteBDD.BDMundoMuscial!!.eliminarCancion(arregloCanciones[posicionItemSeleccionado].id)
                if (exitoEliminacion) {
                    arregloCanciones.removeAt(posicionItemSeleccionado)
                    val listViewCancion = findViewById<ListView>(R.id.list_cancion)
                    (listViewCancion.adapter as ArrayAdapter<*>).notifyDataSetChanged()
                } else {
                    mostrarSnackbar("No se pudo eliminar la canción")
                }
                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        super.onBackPressed()
    }

    fun mostrarSnackbar(texto:String){
        Snackbar.make(
            findViewById(R.id.cl_cancion),//view
            texto, //texto
            Snackbar.LENGTH_LONG //tiempo
        ).show()
    }
}