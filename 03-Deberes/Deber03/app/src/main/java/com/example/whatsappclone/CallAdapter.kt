package com.example.whatsappclone

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class CallAdapter(private val contex: Context, private val callItems: List<CallItem>, private val listener: (CallItem) -> Unit)
    : RecyclerView.Adapter<CallAdapter.ViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        LayoutInflater.from(contex).inflate(R.layout.calls_items, parent, false)
    )
    override fun getItemCount(): Int = callItems.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItem(callItems[position], listener)
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.name2)
        val type = view.findViewById<ImageView>(R.id.type)
        val image = view.findViewById<ImageView>(R.id.image2)
        val time = view.findViewById<TextView>(R.id.time2)
        val arrow = view.findViewById<ImageView>(R.id.arrow2)
        fun bindItem(items: CallItem, listener: (CallItem) -> Unit){
            name.text = items.name
            time.text = items.time

            Glide.with(itemView.context)
                .load(items.image)
                .into(image)

            Glide.with(itemView.context)
                .load(items.type)
                .into(type)

            Glide.with(itemView.context)
                .load(items.arrow)
                .into(arrow)

            itemView.setOnClickListener{
                listener(items)
            }
        }
    }
}