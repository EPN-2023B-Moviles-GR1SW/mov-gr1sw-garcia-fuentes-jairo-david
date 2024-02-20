package com.example.deber02

import java.util.*

class Cancion (
    var id: Int,
    var nombre: String,
    var idArtista: Int,
    var duracion: Int,
    var album: String,
    var genero: String,
    var fechaLanzamiento: Date
){
    override fun toString(): String {
        return "$nombre - [ $album ]"
    }


}