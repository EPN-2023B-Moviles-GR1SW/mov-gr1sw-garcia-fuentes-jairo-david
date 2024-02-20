package com.example.examen2b


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
import androidx.lifecycle.lifecycleScope
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import com.google.firebase.ktx.Firebase

class CancionActivity : AppCompatActivity() {
    var arregloCanciones: ArrayList<Cancion> = arrayListOf()
    var posicionItemSeleccionado = -1
    var adaptador: ArrayAdapter<Cancion>? = null
    var idArtista: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cancion)
        idArtista= intent.getStringExtra("idArtista")

        val listView = findViewById<ListView>(R.id.list_cancion)
        adaptador = ArrayAdapter(
            this, //Context
            android.R.layout.simple_list_item_1, //como se va a ver (XML)
            arregloCanciones
        )
        listView.adapter = adaptador
        adaptador!!.notifyDataSetChanged()
        consultarCancionesPorArtista(idArtista!!)


        lifecycleScope.launch(Dispatchers.Main) {

            val artista = consultarArtistaPorId(idArtista!!)

            val txtArtista = findViewById<TextView>(R.id.txt_artista)
            txtArtista.text = artista!!.nombre
            val txtEdad = findViewById<TextView>(R.id.txt_edad)
            txtEdad.text = "Edad: ${artista!!.edad}"
            val txtPatrimonio = findViewById<TextView>(R.id.txt_patrimonio)
            txtPatrimonio.text = "Patrimonio: $${artista!!.patrimonio}"
            val txtEstado = findViewById<TextView>(R.id.txt_estado)
            if (artista.vivo!!){
                txtEstado.text = "Estado: Vivo"
            }else{
                txtEstado.text = "Estado: Muerto"
            }
        }

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

        return when (item.itemId){
            R.id.mi_editar_cancion ->{
                val intent = Intent(this, EditarCancionActivity::class.java)
                intent.putExtra("idCancion", arregloCanciones[posicionItemSeleccionado].id)
                intent.putExtra("idArtista", idArtista )
                startActivity(intent)
                return true
            }
            R.id.mi_eliminar_cancion ->{
                val idArtista= intent.getStringExtra("idArtista")
                eliminarCancion(arregloCanciones[posicionItemSeleccionado].id!!, idArtista!!)

                return true
            }
            else -> super.onContextItemSelected(item)
        }
    }

    fun eliminarCancion(idCancion: String, idArtista: String){
        val db = Firebase.firestore
        val referencia = db.collection("artistas").document(idArtista)

        for(cancion in arregloCanciones){
            if (cancion.id == idCancion){
                arregloCanciones.remove(cancion)
                break
            }
        }
        adaptador!!.notifyDataSetChanged()

        referencia
            .update("canciones", arregloCanciones)
            .addOnSuccessListener {

            }
            .addOnFailureListener { exception ->
            }
    }

    fun consultarCancionesPorArtista(idArtista: String){
        val db = Firebase.firestore
        val referencia = db.collection("artistas").document(idArtista)
        limpiarArreglo()
        adaptador!!.notifyDataSetChanged()
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
                adaptador!!.notifyDataSetChanged()
            }
            .addOnFailureListener {
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        consultarCancionesPorArtista(idArtista!!)
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
    fun limpiarArreglo(){
        arregloCanciones.clear()
    }
}