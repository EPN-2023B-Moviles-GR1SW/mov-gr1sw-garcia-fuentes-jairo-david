package com.example.examen2b


class Artista (
    public var id: String? = null,
    public var nombre: String? = null,
    public var canciones: ArrayList<Cancion>? = null,
    public var edad: Int? = null,
    public var vivo: Boolean? = null,
    public var patrimonio: Double? = null

) {

    override fun toString(): String {
        return "$nombre - [ ${canciones!!.size}" + if (canciones!!.size == 1) " canción ]" else " canciones ]"
    }


}