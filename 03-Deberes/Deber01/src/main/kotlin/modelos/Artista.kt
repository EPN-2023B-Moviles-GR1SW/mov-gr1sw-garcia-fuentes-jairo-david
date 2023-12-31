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
        val nombresCanciones = this.canciones.joinToString(",") { it.nombre }
        val artista = "${this.id},${this.nombre},${this.edad}," +
                "${nombresCanciones},${this.vivo},${this.patrimonio}\n"
        File(fileName).appendText(artista)
        println("Guardando artista: $nombre")
    }

    fun getArtistas(): List<Artista>{
        val artistas = File(fileName).readLines()
        val listaArtistas = mutableListOf<Artista>()
        artistas.forEach { artista ->
            val valores = artista.split(",")
            listaArtistas.add(Artista(valores[0].toInt(), valores[1], valores[2].toInt(), listOf(), valores[4].toBoolean(), valores[5].toDouble()))
        }
        return listaArtistas
    }

    fun actualizarArtista(artistaActualizado: Artista) {
        val artistas = File(fileName).readLines()
        val artistasActualizados = artistas.map { artista ->
            if (artista.startsWith("${artistaActualizado.id},")) {
                "${artistaActualizado.id},${artistaActualizado.nombre},${artistaActualizado.edad}," +
                        "${artistaActualizado.canciones},${artistaActualizado.vivo},${artistaActualizado.patrimonio}"
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
        return "Artista(id=$id, nombre='$nombre', edad=$edad, canciones=${canciones.size} , vivo=$vivo, patrimonio=${patrimonio})"
    }

    companion object {
        private val fileName = System.getProperty("user.dir")+ "\\src\\main\\kotlin\\archivos\\artistas.txt"

        /* fun getArtistasByCancion(id: Int): List<Artista>{
            val artistas = File(fileName).readLines()
            val listaArtistas = mutableListOf<Artista>()
            artistas.forEach { artista ->
                val valores = artista.split(",")
                listaArtistas.add(Artista(valores[0].toInt(), valores[1], valores[2].toInt(), listOf(), valores[4].toBoolean(), valores[5].toDouble()))
            }
            return listaArtistas
        }*/
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
    }
}