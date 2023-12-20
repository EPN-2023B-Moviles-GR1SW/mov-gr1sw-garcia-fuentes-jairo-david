package com.example.b2023_gr1sw_jdgf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.snackbar.Snackbar
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts


class CIntentExplicitoParametros : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cintent_explicito_parametros)
        val nombre = intent.getStringExtra("nombre")
        val apellido = intent.getStringExtra("apellido")
        val edad = intent.getIntExtra("edad", 0)
        mostrarSnackbar("${nombre} ${apellido} ${edad}")
        val boton = findViewById<Button>(R.id.btn_devolver_respuesta)
        boton.setOnClickListener { devolverRespuesta() }
    }

    fun devolverRespuesta(){
        val intentDavolverParametros = Intent()
        intentDavolverParametros.putExtra("nombreModificado", "David")
        intentDavolverParametros.putExtra("edadModificado", 23)
        setResult(
            RESULT_OK,
            intentDavolverParametros //variabler de intent
        )// ponemos resultado ok y opcional retornamos variables de intent
        finish() // cerramos la actividad
    }

    fun mostrarSnackbar(texto:String){
        Snackbar.make(
            findViewById(R.id.id_layout_intents), //view
            texto, //texto
        Snackbar.LENGTH_LONG //tiempo
        ).show()
    }
}