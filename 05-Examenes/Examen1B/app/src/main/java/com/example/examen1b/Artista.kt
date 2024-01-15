package com.example.examen1b

import java.util.*

class Artista (
    var id: Int,
    var nombre: String,
    var edad: Int,
    var canciones: ArrayList<Cancion>,
    var vivo: Boolean,
    var patrimonio: Double

) {

    override fun toString(): String {
        return "$nombre - [ ${canciones.size}" + if (canciones.size == 1) " canción ]" else " canciones ]"
    }


}