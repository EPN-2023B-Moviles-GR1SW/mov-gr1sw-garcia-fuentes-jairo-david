package com.example.b2023_gr1sw_jdgf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class IFirestore : AppCompatActivity() {

    var query: Query? = null
    val arreglo: ArrayList<ICities> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ifirestore)

        //Configurando el list view
        val listView = findViewById<ListView>(R.id.lv_firestore)
        val adaptador = ArrayAdapter(
            this,
            android.R.layout.simple_list_item_1,
            arreglo
        )
        listView.adapter = adaptador
        adaptador.notifyDataSetChanged()

        //Botones
        //Crear Datos Prueba
        val botonDatosPrueba = findViewById<Button>(R.id.btn_fs_datos_prueba)
        botonDatosPrueba.setOnClickListener {
            crearDatosPrueba()
        }

        //Order By
        val botonOrderBy = findViewById<Button>(R.id.btn_fs_order_by)
        botonOrderBy.setOnClickListener {
            consultarConOrderBy(adaptador)
        }

        //Obtener Documento
        val botonObtenerDocumento = findViewById<Button>(R.id.btn_fs_odoc)
        botonObtenerDocumento.setOnClickListener {
            consultarDocumento(adaptador)
        }

        //Consultar Indice compuesto
        val botonConsultaIndiceCompuesto = findViewById<Button>(R.id.btn_fs_ind_comp)
        botonConsultaIndiceCompuesto.setOnClickListener {
            consultarIndiceCompuesto(adaptador)
        }

        //Crear datos
        val botonCrear = findViewById<Button>(R.id.btn_fs_crear)
        botonCrear.setOnClickListener {
            crearEjemplo()
        }

        //Boton eliminar
        val botonEliminar = findViewById<Button>(R.id.btn_fs_eliminar)
        botonEliminar.setOnClickListener {
            eliminarRegistro()
        }

        //Empezar a paginar
        val botonEmpezarPaginar = findViewById<Button>(R.id.btn_fs_epaginar)
        botonEmpezarPaginar.setOnClickListener {
            query = null;
            consultarCiudades(adaptador);
        }

        //Paginar
        val botonPaginar = findViewById<Button>(R.id.btn_fs_paginar)
        botonPaginar.setOnClickListener {
            consultarCiudades(adaptador)
        }
    }

    fun eliminarRegistro(){
        val db = Firebase.firestore
        val referenciaEjemploEstudiante = db
            .collection("ejemplo")
        referenciaEjemploEstudiante
            .document("12345678")
            .delete()
            .addOnSuccessListener {
                //Se eliminó
            }
            .addOnFailureListener {
                //No se eliminó
            }
    }

    fun consultarCiudades(adaptador: ArrayAdapter<ICities>){
        val db = Firebase.firestore
        val citiesRefUnico = db.collection("cities")
            .orderBy("population")
            .limit(1)
        var tarea: Task<QuerySnapshot>? = null
        if (query == null){
            tarea = citiesRefUnico.get()
            limpiarArreglo()
            adaptador.notifyDataSetChanged()
        } else{
            tarea = query!!.get()
        }
        if (tarea != null){
            tarea.addOnSuccessListener { documentSnapshots ->
                guardarQuery(documentSnapshots, citiesRefUnico)
                for (ciudad in documentSnapshots){
                    anadirAArregloCiudad(ciudad)
                }
                adaptador.notifyDataSetChanged()
            }.addOnFailureListener{}
        }
    }

    fun guardarQuery(documentSnapshots: QuerySnapshot,
                     refCities: Query
    ){
        if (documentSnapshots.size() > 0){
            val ultimoDocumento = documentSnapshots.documents[documentSnapshots.size() - 1]
            query = refCities.startAfter(ultimoDocumento)
        }
    }
    fun crearEjemplo(){
        val db = Firebase.firestore
        val referenciaEjemploEstudiante = db
            .collection("ejemplo")
            //.document("id_hijo")
            //.collection("estudiante")
        val datosEstudiante = hashMapOf(
            "nombre" to "Jairo",
            "graduado" to false,
            "promedio" to 19.00,
            "direccion" to hashMapOf(
                "calle" to "Galo Plaza Lasso",
                "numero" to 401
            ),
            "materias" to listOf(
                "web",
                "moviles"
            )
        )

        //identificador quemado
        referenciaEjemploEstudiante
            .document("12345678")
            .set(datosEstudiante)
            .addOnSuccessListener {
                //Se guardó
            }
            .addOnFailureListener {
                //No se guardó
            }
        // identificador quemado pero autogenerado con Date()
        val identificador = Date().time
        referenciaEjemploEstudiante
            .document(identificador.toString())
            .set(datosEstudiante)
            .addOnSuccessListener {
                //Se guardó
            }
            .addOnFailureListener {
                //No se guardó
            }

        //identificador autogenerado
        referenciaEjemploEstudiante
            .add(datosEstudiante)
            .addOnSuccessListener {
                //Se guardó
            }
            .addOnFailureListener {
                //No se guardó
            }
    }

    fun consultarIndiceCompuesto(adaptador: ArrayAdapter<ICities>){
        val db = Firebase.firestore
        val citiesRefUnico = db.collection("cities")
        limpiarArreglo()
        adaptador.notifyDataSetChanged()
        citiesRefUnico
            .whereEqualTo("capital", false)
            .whereLessThanOrEqualTo("population", 4000000)
            .orderBy("population", Query.Direction.DESCENDING)
            .get()
            .addOnSuccessListener {
                for (ciudad in it){
                    anadirAArregloCiudad(ciudad)
                }
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener {
                //Errores
            }
    }

    fun consultarDocumento(adaptador: ArrayAdapter<ICities>){
        val db = Firebase.firestore
        val citiesRefUnico = db.collection("cities")
        limpiarArreglo()
        adaptador.notifyDataSetChanged()
        citiesRefUnico
            .document("BJ")
            .get()
            .addOnSuccessListener {
                //it es un objeto
                arreglo.add(
                    ICities(
                        it.data?.get("name") as String?,
                        it.data?.get("state") as String?,
                        it.data?.get("country") as String?,
                        it.data?.get("capital") as Boolean?,
                        it.data?.get("population") as Long?,
                        it.data?.get("regions") as ArrayList<String>?
                    )
                )
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener {
                //salió mal
            }
    }

    fun limpiarArreglo(){
        arreglo.clear()
    }

    fun anadirAArregloCiudad(
        ciudad: QueryDocumentSnapshot
    ){
        val nuevaCiudad = ICities(
            ciudad.data.get("name") as String?,
            ciudad.data.get("state") as String?,
            ciudad.data.get("country") as String?,
            ciudad.data.get("capital") as Boolean?,
            ciudad.data.get("population") as Long?,
            ciudad.data.get("regions") as ArrayList<String>?
        )
        arreglo.add(nuevaCiudad)
    }

    fun consultarConOrderBy(adaptador: ArrayAdapter<ICities>){
        val db = Firebase.firestore
        val citiesRefUnico = db.collection("cities")
        limpiarArreglo()
        adaptador.notifyDataSetChanged()
        citiesRefUnico.orderBy("population", Query.Direction.ASCENDING)
            .get()
            .addOnSuccessListener {
                for (ciudad in it){
                    ciudad.id
                    anadirAArregloCiudad(ciudad)
                }
                adaptador.notifyDataSetChanged()
            }
            .addOnFailureListener {
                //Errores
            }
    }

    fun crearDatosPrueba(){
        val db = Firebase.firestore
        // Copiar Ejemplo web
        val cities = db.collection("cities")

        val data1 = hashMapOf(
            "name" to "San Francisco",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 860000,
            "regions" to listOf("west_coast", "norcal"),
        )
        cities.document("SF").set(data1)

        val data2 = hashMapOf(
            "name" to "Los Angeles",
            "state" to "CA",
            "country" to "USA",
            "capital" to false,
            "population" to 3900000,
            "regions" to listOf("west_coast", "socal"),
        )
        cities.document("LA").set(data2)

        val data3 = hashMapOf(
            "name" to "Washington D.C.",
            "state" to null,
            "country" to "USA",
            "capital" to true,
            "population" to 680000,
            "regions" to listOf("east_coast"),
        )
        cities.document("DC").set(data3)

        val data4 = hashMapOf(
            "name" to "Tokyo",
            "state" to null,
            "country" to "Japan",
            "capital" to true,
            "population" to 9000000,
            "regions" to listOf("kanto", "honshu"),
        )
        cities.document("TOK").set(data4)

        val data5 = hashMapOf(
            "name" to "Beijing",
            "state" to null,
            "country" to "China",
            "capital" to true,
            "population" to 21500000,
            "regions" to listOf("jingjinji", "hebei"),
        )
        cities.document("BJ").set(data5)
    }
}