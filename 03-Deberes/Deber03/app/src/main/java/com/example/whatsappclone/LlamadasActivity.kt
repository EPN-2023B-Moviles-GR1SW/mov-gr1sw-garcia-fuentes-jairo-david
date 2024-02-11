package com.example.whatsappclone

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LlamadasActivity : AppCompatActivity() {
    private var callItems: MutableList<CallItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_llamadas)

        var list = findViewById<RecyclerView>(R.id.recyclerView2)
        iniData()

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = CallAdapter(this, callItems){
            val toast = Toast.makeText(applicationContext, it.name, Toast.LENGTH_LONG)
            toast.show()
        }

        val btn_chats = findViewById<TextView>(R.id.chats)
        btn_chats.setOnClickListener {
            //ir a la actividad de chats
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

    }

    private fun iniData(){
        val image =  resources.obtainTypedArray(R.array.image)
        val name = resources.getStringArray(R.array.name)
        val type = resources.obtainTypedArray(R.array.type)
        val time = resources.getStringArray(R.array.time2)
        val arrow = resources.obtainTypedArray(R.array.arrow)
        callItems.clear()
        for (i in name.indices){
            callItems.add(CallItem(name[i], type.getResourceId(i, 0), image.getResourceId(i, 0), time[i], arrow.getResourceId(i, 1)))
        }
    }
}