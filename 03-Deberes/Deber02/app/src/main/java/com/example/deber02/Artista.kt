package com.example.deber02

import com.example.deber02.sqlite.SqliteBDD

class Artista (
    var id: Int,
    var nombre: String,
    var edad: Int,
    var vivo: Boolean,
    var patrimonio: Double

) {
    val canciones = SqliteBDD.BDMundoMuscial!!.numeroCanciones(this.id)

    override fun toString(): String {
        return "$nombre - [ ${canciones}" + if (canciones == 1) " canción ]" else " canciones ]"
    }


}