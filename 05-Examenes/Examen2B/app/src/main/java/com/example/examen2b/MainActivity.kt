package com.example.examen2b


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
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlin.collections.ArrayList
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    var arreglo: ArrayList<Artista> = arrayListOf()
    var posicionItemSeleccionado = -1
    var adaptador: ArrayAdapter<Artista>? = null


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView = findViewById<ListView>(R.id.list_artista)
        adaptador = ArrayAdapter(
            this, //Context
            android.R.layout.simple_list_item_1, //como se va a ver (XML)
            arreglo
        )
        listView.adapter = adaptador
        adaptador!!.notifyDataSetChanged()

        consultarArtistas(adaptador!!)

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
                intent.putExtra("idArtista", arreglo[posicionItemSeleccionado].id!!)
                startActivity(intent)
                return true
            }
            R.id.mi_eliminar_artista ->{
                eliminarArtista(arreglo[posicionItemSeleccionado].id!!)

                return true
            }
            R.id.mi_ver_canciones ->{
                val intent = Intent(
                    this,
                    CancionActivity::class.java
                )
                intent.putExtra("idArtista", arreglo[posicionItemSeleccionado].id!!)
                startActivity(intent)
                return true
            }
            else -> super.onContextItemSelected(item)

        }

    }

    fun consultarArtistas(adaptador: ArrayAdapter<Artista>){
        val db = Firebase.firestore
        val referencia = db.collection("artistas")
        limpiarArreglo()
        adaptador.notifyDataSetChanged()
        referencia
            .get()
            .addOnSuccessListener {
                for (artista in it){
                    artista.id
                    anadirAArregloArtista(artista)
                }
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener {
            }

    }

    fun eliminarArtista(idArtista: String){
        val db = Firebase.firestore
        val referencia = db.collection("artistas")
        referencia
            .document(idArtista)
            .delete()
            .addOnSuccessListener {
                arreglo.removeIf { it.id == idArtista }
                adaptador!!.notifyDataSetChanged()
            }
            .addOnFailureListener {
            }
    }

    fun anadirAArregloArtista(artista: QueryDocumentSnapshot){
        val nuevoArtista = Artista(
            artista.id,
            artista.data.get("nombre") as String?,
            artista.data.get("canciones") as ArrayList<Cancion>?,
            (artista.data.get("edad") as? Long)?.toInt(),
            artista.data.get("vivo") as Boolean?,
            artista.data.get("patrimonio") as Double?
        )
        arreglo.add(nuevoArtista)
    }
    fun mostrarSnackbar(texto:String){
        Snackbar.make(
            findViewById(R.id.cl_artistas),//view
            texto, //texto
            Snackbar.LENGTH_LONG //tiempo
        ).show()
    }

    fun limpiarArreglo(){
        arreglo.clear()
    }
}
