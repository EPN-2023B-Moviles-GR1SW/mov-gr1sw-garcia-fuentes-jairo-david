package com.example.examen2b

class Cancion(
    public var id: String? = null,
    public var nombre: String? = null,
    public var duracion: Int? = null,
    public var album: String? = null,
    public var genero: String? = null,
    public var fechaLanzamiento: String? = null
){
    override fun toString(): String {
        return "$nombre - [ $album ]"
    }


}