package com.example.b2023_gr1sw_jdgf

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar


class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Base de datos SQLite
        EBaseDeDatos.tablaEntrenador = ESqliteHelperEntrenador(this)

        val botonCicloVida = findViewById<Button>(R.id.btn_ciclo_vida)
        botonCicloVida.setOnClickListener{
            irActividad(ACicloVida::class.java)
        }

        val botonListView = findViewById<Button>(R.id.btn_ir_list_view)
        botonListView.setOnClickListener {
            irActividad(BListView::class.java)
        }

        val botonIntentImplicito = findViewById<Button>(R.id.btn_ir_intent_implicito)
        botonIntentImplicito.setOnClickListener {
            val intnConRespuesta = Intent(
                Intent.ACTION_PICK,
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI
            )
            callbackIntentPickUri.launch(intnConRespuesta)
        }

        val botonIntentExplicito = findViewById<Button>(R.id.btn_ir_intent_explicito)
        botonIntentExplicito.setOnClickListener {
            abrirActividadConParametros(CIntentExplicitoParametros::class.java)
        }

        val botonSqlite = findViewById<Button>(R.id.btn_sqlite)
        botonSqlite.setOnClickListener {
            irActividad(ECrudEntrenador::class.java)
        }

        val botonRView = findViewById<Button>(R.id.btn_revcycler_view)
        botonRView.setOnClickListener {
            irActividad(FRecyclerView::class.java)
        }

        val botonGoogleMaps = findViewById<Button>(R.id.btn_google_maps)
        botonGoogleMaps.setOnClickListener {
            irActividad(GGoogleMapsActivity::class.java)
        }


    }

    fun abrirActividadConParametros(
        clase: Class<*>
    ){
        val intentExplicito = Intent(this, clase)
        //Enviar parametros (solamente variables primitivas)
        intentExplicito.putExtra("nombre", "Jairo")
        intentExplicito.putExtra("apellido", "Garcia")
        intentExplicito.putExtra("edad", 22)

        callbackContenidoExplicito.launch(intentExplicito)
    }

    fun irActividad(
        clase: Class<*>
    ){
        val intent = Intent(this, clase)
        startActivity(intent)
    }

    val callbackContenidoExplicito = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if(result.resultCode == Activity.RESULT_OK){
            if(result.data != null){
                //Logica del negocio
                val data = result.data
                mostrarSnackbar("${data?.getStringExtra("nombreModificado")}")
            }
        }
    }

    fun mostrarSnackbar(texto:String){
        Snackbar.make(
            findViewById(R.id.id_layout_main),//view
            texto, //texto
            Snackbar.LENGTH_LONG //tiempo
        ).show()
    }

    val callbackIntentPickUri = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ){
        result ->
        if (result.resultCode === Activity.RESULT_OK){
            if (result.data != null){
                if (result.data!!.data != null){
                    val uri: Uri = result.data!!.data!!
                    val cursor = contentResolver.query(
                        uri, null, null, null, null, null)
                    cursor?.moveToFirst()
                    val indiceTelefono = cursor?.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER
                    )
                    val telefono = cursor?.getString(indiceTelefono!!)
                    cursor?.close()
                    mostrarSnackbar("Telefono ${telefono}")
                }
            }
        }
    }


}
