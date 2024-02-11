package com.example.whatsappclone

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.whatsappclone.ui.theme.WhatsAppCloneTheme

class MainActivity : AppCompatActivity() {
    private var chatItems: MutableList<ChatItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var list = findViewById<RecyclerView>(R.id.recyclerView)
        iniData()

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = ChatAdapter(this, chatItems){
            val toast = Toast.makeText(applicationContext, it.name, Toast.LENGTH_LONG)
            toast.show()
        }

        val btn_llamadas = findViewById<TextView>(R.id.llamadas)
        btn_llamadas.setOnClickListener {
            //ir a la actividad de llamadas
            val intent = Intent(this, LlamadasActivity::class.java)
            startActivity(intent)
        }

    }

    private fun iniData(){
        val image =  resources.obtainTypedArray(R.array.image)
        val name = resources.getStringArray(R.array.name)
        val status = resources.getStringArray(R.array.status)
        val time = resources.getStringArray(R.array.time)
        chatItems.clear()
        for (i in name.indices){
            chatItems.add(ChatItem(name[i], status[i], image.getResourceId(i, 0), time[i]))
        }
    }
}