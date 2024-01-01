import modelos.Artista
import modelos.Cancion
import java.text.SimpleDateFormat
import java.util.*


fun main(){
    val scanner = Scanner(System.`in`)
    var opcion: Int

    do {
        println("------------------ CRUD DE ARTISTAS Y CANCIONES ------------------")
        println("1. Agregar Artista")
        println("2. Agregar Canción")
        println("3. Mostrar Artistas")
        println("4. Mostrar Canciones")
        println("5. Actualizar Artista")
        println("6. Actualizar Canción")
        println("7. Eliminar Artista")
        println("8. Eliminar Canción")
        println("9. Salir")

        print("Seleccione una opción: ")
        opcion = scanner.nextInt()

        when (opcion) {
            1 -> {
                agregarArtista()
            }
            2 -> {
                agregarCancion()
            }
            3 -> {
                mostrarArtistas()
            }
            4 -> {
                mostrarCanciones()
            }
            5 -> {
                actualizarArtista()
            }
            6 -> {
                actualizarCancion()
            }
            7 -> {
                eliminarArtista()
            }
            8 -> {
                eliminarCancion()
            }
            9 -> {
                println("Saliendo de la aplicación.")
            }
            else -> println("Opción no válida. Inténtelo nuevamente.")
        }

    } while (opcion != 9)

}

// CRUD ARTISTA
fun agregarArtista() {
    println("------------------ AGREGAR ARTISTA ------------------")
    print("Ingrese el id del artista: ")
    val id = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("Id inválido.")

    print("Ingrese el nombre del artista: ")
    val nombre = readLine() ?: throw IllegalArgumentException("Nombre inválido.")

    print("Ingrese la edad del artista: ")
    val edad = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("Edad inválida.")

    print("Ingrese el id de las canciones del artista separadas por coma(1, 2, 3, ...): ")
    val cancionesString = readLine() ?: throw IllegalArgumentException("Canciones inválidas.")

    print("Ingrese si el artista está vivo (true/false): ")
    val vivoStr = readLine()?.toLowerCase() ?: throw IllegalArgumentException("Valor vivo inválido.")
    val vivo = vivoStr == "true"

    print("Ingrese el patrimonio del artista: ")
    val patrimonio = readLine()?.toDoubleOrNull() ?: throw IllegalArgumentException("Patrimonio inválido.")

    val cancionesIndex = cancionesString.split(",")
        .filter { it.isNotEmpty() }
        .map { it.trim().toInt() }


    val canciones = List(cancionesIndex.size) { Cancion.getCancion(cancionesIndex[it]) }
    val artista = Artista(id, nombre, edad, canciones, vivo, patrimonio)
    artista.crearArtista()
}

fun mostrarArtistas() {
    println("------------------ MOSTRAR ARTISTAS ------------------")
    val artistas = Artista.getArtistas()
    artistas.forEach { println(it) }


}

fun actualizarArtista(){
    println("------------------ ACTUALIZAR ARTISTA ------------------")
    print("Ingrese el id del artista a actualizar: ")
    val id = readLine()?.toIntOrNull() ?: return println("Id inválido.")

    val artista = Artista.getArtista(id)
    if (artista != null) {
        print("Ingrese el nuevo nombre del artista: ")
        val nombre = readLine() ?: throw IllegalArgumentException("Nombre inválido.")

        print("Ingrese la nueva edad del artista: ")
        val edad = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("Edad inválida.")

        print("Ingrese el nuevo id de las canciones del artista separadas por coma(1, 2, 3, ...): ")
        val cancionesString = readLine() ?: throw IllegalArgumentException("Canciones inválidas.")

        print("Ingrese si el artista está vivo (true/false): ")
        val vivoStr = readLine()?.toLowerCase() ?: throw IllegalArgumentException("Valor vivo inválido.")
        val vivo = vivoStr == "true"

        print("Ingrese el nuevo patrimonio del artista: ")
        val patrimonio = readLine()?.toDoubleOrNull() ?: throw IllegalArgumentException("Patrimonio inválido.")

        val cancionesIndex = cancionesString.split(",")
            .filter { it.isNotEmpty() }
            .map { it.trim().toInt() }

        val canciones = List(cancionesIndex.size) { Cancion.getCancion(cancionesIndex[it]) }
        val artistaNuevo = Artista(id, nombre, edad, canciones, vivo, patrimonio)
        artista.actualizarArtista(artistaNuevo)
    } else {
        println("No se encontró el artista con el id: $id")
    }
}

fun eliminarArtista() {
    println("------------------ ELIMINAR ARTISTA ------------------")
    print("Ingrese el id del artista a eliminar: ")
    val id = readLine()?.toIntOrNull() ?: return println("Id inválido.")

    val artista = Artista.getArtista(id)
    if (artista != null) {
        artista.eliminarArtista(id)
    } else {
        println("No se encontró el artista con el id: $id")
    }
}

//CRUD CANCIÓN
fun agregarCancion() {
    println("------------------ AGREGAR CANCIÓN ------------------")
    print("Ingrese el id de la canción: ")
    val id = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("Id inválido.")

    print("Ingrese el nombre de la canción: ")
    val nombre = readLine() ?: throw IllegalArgumentException("Nombre inválido.")

    print("Ingrese la duración de la canción: ")
    val duracion = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("Duración inválida.")

    print("Ingrese el género de la canción: ")
    val genero = readLine() ?: throw IllegalArgumentException("Género inválido.")

    print("Ingrese el album de la canción: ")
    val album = readLine() ?: throw IllegalArgumentException("Album inválido.")

    print("Ingrese la fecha de lanzamiento de la canción (dd/MM/yyyy): ")
    val fechaLanzamiento = readLine() ?: throw IllegalArgumentException("Fecha inválida.")

    val dateFormat = SimpleDateFormat("dd/MM/yyyy")
    val fecha = dateFormat.parse(fechaLanzamiento)
    val cancion = Cancion(id, nombre, duracion, album, genero, fecha)
    cancion.crearCancion()
}

fun mostrarCanciones() {
    println("------------------ MOSTRAR CANCIONES ------------------")
    val canciones = Cancion.getCanciones()
    canciones.forEach { println(it) }
}

fun actualizarCancion() {
    println("------------------ ACTUALIZAR CANCIÓN ------------------")
    print("Ingrese el id de la canción a actualizar: ")
    val id = readLine()?.toIntOrNull() ?: return println("Id inválido.")

    val cancion = Cancion.getCancion(id)
    if (cancion != null) {
        print("Ingrese el nuevo nombre de la canción: ")
        val nombre = readLine() ?: throw IllegalArgumentException("Nombre inválido.")

        print("Ingrese la nueva duración de la canción: ")
        val duracion = readLine()?.toIntOrNull() ?: throw IllegalArgumentException("Duración inválida.")

        print("Ingrese el nuevo género de la canción: ")
        val genero = readLine() ?: throw IllegalArgumentException("Género inválido.")

        print("Ingrese el nuevo album de la canción: ")
        val album = readLine() ?: throw IllegalArgumentException("Album inválido.")

        print("Ingrese la nueva fecha de lanzamiento de la canción (dd/MM/yyyy): ")
        val fechaLanzamiento = readLine() ?: throw IllegalArgumentException("Fecha inválida.")

        val dateFormat = SimpleDateFormat("dd/MM/yyyy")
        val fecha = dateFormat.parse(fechaLanzamiento)
        val cancionNueva = Cancion(id, nombre, duracion, album, genero, fecha)
        cancion.actualizarCancion(cancionNueva)
    } else {
        println("No se encontró la canción con el id: $id")
    }
}

fun eliminarCancion() {
    println("------------------ ELIMINAR CANCIÓN ------------------")
    print("Ingrese el id de la canción a eliminar: ")
    val id = readLine()?.toIntOrNull() ?: return println("Id inválido.")

    val cancion = Cancion.getCancion(id)
    if (cancion != null) {
        cancion.eliminarCancion(id)
    } else {
        println("No se encontró la canción con el id: $id")
    }
}