package modelos

import java.io.File
import java.text.SimpleDateFormat
import java.util.*
class Cancion (
    var id: Int,
    var nombre: String,
    var duracion: Int,
    var album: String,
    var genero: String,
    var fechaLanzamiento: Date
) {
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    private val fileName = System.getProperty("user.dir")+ "\\src\\main\\kotlin\\archivos\\canciones.txt"

    fun crearCancion() {
        val registro = "${this.id},${this.nombre},${this.duracion}," +
                "${this.album},${this.genero},${dateFormat.format(this.fechaLanzamiento)}\n"
        File(fileName).appendText(registro)
        println("Guardando canción: $nombre")
    }



    fun actualizarCancion(cancionActualizada: Cancion) {
        val canciones = File(fileName).readLines()
        val cancionesActualizadas = canciones.map { cancion ->
            if (cancion.startsWith("${cancionActualizada.id},")) {
                "${cancionActualizada.id},${cancionActualizada.nombre},${cancionActualizada.duracion}," +
                        "${cancionActualizada.album},${cancionActualizada.genero},${dateFormat.format(cancionActualizada.fechaLanzamiento)}"
            } else {
                cancion
            }
        }
        File(fileName).writeText(cancionesActualizadas.joinToString("\n"))

    }

    fun eliminarCancion(id: Int) {
        // Eliminar la canción del archivo de canciones
        val canciones = File(fileName).readLines()

        // Obtener artistas relacionados con la canción
        val artistas = Artista.getArtistasByCancion(id)

        // Actualizar cada artista y guardar los cambios
        artistas.forEach { artista ->
            val cancionesSinCancionEliminada = artista.canciones.filter { it.id != id }
            val artistaActualizado = Artista(
                artista.id,
                artista.nombre,
                artista.edad,
                cancionesSinCancionEliminada,
                artista.vivo,
                artista.patrimonio
            )
            artista.actualizarArtista(artistaActualizado)
        }
        val cancionesActualizadas = canciones.filter { !it.startsWith("$id,") }
        File(fileName).writeText(cancionesActualizadas.joinToString("\n"))
    }


    override fun toString(): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        return "Cancion(id=$id, nombre='$nombre', duracion=$duracion min, album='${album}' , genero='$genero', fechaLanzamiento=${dateFormat.format(fechaLanzamiento)})"
    }

    companion object {
        fun getCancion(id: Int): Cancion {
            val canciones = File(fileName).readLines()
            val cancion = canciones.find{it.startsWith("$id,")}
            val cancionSplit = cancion?.split(",") ?: throw Exception("No se encontró la canción con el id: $id")
            return Cancion(
                cancionSplit[0].toInt(),
                cancionSplit[1],
                cancionSplit[2].toInt(),
                cancionSplit[3],
                cancionSplit[4],
                dateFormat.parse(cancionSplit[5])
            )
        }

        private val fileName = System.getProperty("user.dir")+ "\\src\\main\\kotlin\\archivos\\canciones.txt"
        private val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        fun getCanciones(): List<Cancion> {
            val canciones = File(fileName).readLines()
            return canciones.map { cancion ->
                val cancionSplit = cancion.split(",")
                Cancion(
                    cancionSplit[0].toInt(),
                    cancionSplit[1],
                    cancionSplit[2].toInt(),
                    cancionSplit[3],
                    cancionSplit[4],
                    dateFormat.parse(cancionSplit[5])
                )
            }
        }
    }

}