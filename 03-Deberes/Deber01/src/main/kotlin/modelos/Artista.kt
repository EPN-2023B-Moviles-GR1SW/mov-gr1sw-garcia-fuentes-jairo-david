package modelos

import java.io.File
import java.util.*

class Artista (
    var id: Int,
    var nombre: String,
    var edad: Int,
    var canciones: List<Cancion>,
    var vivo: Boolean,
    var patrimonio: Double

){
    private val fileName = System.getProperty("user.dir")+ "\\src\\main\\kotlin\\archivos\\artistas.txt"

    fun crearArtista() {
        val idCanciones = this.canciones.map { it.id.toString() }.joinToString("-")
        val artista = "${this.id},${this.nombre},${this.edad}," +
                "[$idCanciones],${this.vivo},${this.patrimonio}\n"

        File(fileName).appendText(artista)
        println("Guardando artista: $nombre")
    }



    fun actualizarArtista(artistaActualizado: Artista) {
        val idCanciones = artistaActualizado.canciones.joinToString("-") { it.id.toString() }
        val artistas = File(fileName).readLines()
        val artistasActualizados = artistas.map { artista ->
            if (artista.startsWith("${artistaActualizado.id},")) {
                "${artistaActualizado.id},${artistaActualizado.nombre},${artistaActualizado.edad}," +
                        "[$idCanciones],${artistaActualizado.vivo},${artistaActualizado.patrimonio}"
            } else {
                artista
            }
        }
        File(fileName).writeText(artistasActualizados.joinToString("\n"))
    }


    fun eliminarArtista(id: Int) {
        val artistas = File(fileName).readLines()
        val artistasActualizados = artistas.filter { !it.startsWith("$id,") }
        File(fileName).writeText(artistasActualizados.joinToString("\n"))
    }


    override fun toString(): String {
        val nombresCanciones = canciones.joinToString(", ") { it.nombre }
        return "Artista(id=$id, nombre='$nombre', edad=$edad, canciones=[$nombresCanciones], vivo=$vivo, patrimonio=$patrimonio)"
    }


    companion object {
        private val fileName = System.getProperty("user.dir")+ "\\src\\main\\kotlin\\archivos\\artistas.txt"

        fun getArtistasByCancion(id: Int): List<Artista>{
            // obtener todos los artistas
            val artistas = File(fileName).readLines()

            val artistasCoincidentes = mutableListOf<Artista>()
            // buscar en cada linea un string entre corchetes
            artistas.forEach { artista ->
                // obtener el contenido entre corchetes
                val idCanciones = artista.substringAfter("[").substringBefore("]")
                // separar el contenido por el guion
                val canciones = idCanciones.split("-")
                // si el id de la cancion está en la lista de canciones del artista
                println(canciones)
                if (canciones.contains(id.toString())) {
                    val idCanciones = artista.substringAfter("[").substringBefore("]")
                    val cancionesl = idCanciones.split("-").map { Cancion.getCancion(it.toInt()) }
                    val valores = artista.split(",")
                    // agregar el artista a la lista de artistas coincidentes
                    artistasCoincidentes.add(Artista(valores[0].toInt(), valores[1], valores[2].toInt(), cancionesl, valores[4].toBoolean(), valores[5].toDouble()))
                }
            }
            println(artistasCoincidentes)
            // Devuelve la lista de artistas coincidentes
            return artistasCoincidentes
        }
        fun getArtista(id: Int): Artista{
           val artistas = File(fileName).readLines()
           val artista = artistas.find { it.startsWith("$id,")}
           val valores = artista?.split(",") ?: throw Exception("No se encontró el artista con el id: $id")

           return Artista(
               valores[0].toInt(),
               valores[1],
               valores[2].toInt(),
               listOf(),
               valores[4].toBoolean(),
               valores[5].toDouble())
       }

        fun getArtistas(): List<Artista>{
            val artistas = File(fileName).readLines()
            val listaArtistas = mutableListOf<Artista>()
            artistas.forEach { artista ->
                // obtener el contenido entre corchetes
                val idCanciones = artista.substringAfter("[").substringBefore("]")
                val canciones = idCanciones.split("-").map { Cancion.getCancion(it.toInt()) }

                val valores = artista.split(",")
                listaArtistas.add(Artista(valores[0].toInt(), valores[1], valores[2].toInt(), canciones, valores[4].toBoolean(), valores[5].toDouble()))
            }
            return listaArtistas
        }
    }
}