package com.example.examen1b.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.examen1b.Artista
import com.example.examen1b.Cancion
import java.text.SimpleDateFormat

class SqliteHelper (
    contexto: Context?,
): SQLiteOpenHelper(
    contexto,
    "BDMundoMusical", //nombre BDD
    null,
    1
){
    val dateFormat = SimpleDateFormat("dd/MM/yyyy")

    override fun onCreate(db: SQLiteDatabase?) {
        // Crear la tabla de artistas
        val crearTablaArtistas = """
            CREATE TABLE ARTISTAS(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre VARCHAR(50),
            edad INTEGER,
            vivo BOOLEAN,
            patrimonio DOUBLE
            )
        """.trimIndent()
        db?.execSQL(crearTablaArtistas)

        // Crear la tabla de canciones
        val crearTablaCanciones = """
            CREATE TABLE CANCIONES(
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            nombre VARCHAR(100),
            idArtista INTEGER,
            duracion INTEGER,
            album VARCHAR(50),
            genero VARCHAR(50),
            fechaLanzamiento DATE,
            FOREIGN KEY(idArtista) REFERENCES ARTISTAS(id)
            )
        """.trimIndent()
        db?.execSQL(crearTablaCanciones)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }

    // CRUD ARTISTAS
    fun crearArtista(
        nombre: String,
        edad: Int,
        vivo: Boolean,
        patrimonio: Double
    ): Int {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("edad", edad)
        valoresAGuardar.put("vivo", vivo)
        valoresAGuardar.put("patrimonio", patrimonio)
        val id = basedatosEscritura
            .insert(
                "ARTISTAS", // nombre tabla
                null,
                valoresAGuardar
            )
        basedatosEscritura.close()
        return id.toInt()
    }

    fun eliminarArtista(id: Int): Boolean {
        val conexionEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminacion = conexionEscritura
            .delete(
                "ARTISTAS", // nombre tabla
                "id=?", // claúsula WHERE
                parametrosConsultaDelete
            )
        conexionEscritura.close()
        return if (resultadoEliminacion.toInt() == -1) false else true
    }

    fun actualizarArtista(
        nombre: String,
        edad: Int,
        vivo: Boolean,
        patrimonio: Double,
        id: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("edad", edad)
        valoresAActualizar.put("vivo", vivo)
        valoresAActualizar.put("patrimonio", patrimonio)
        //where id...
        val parametrosConsultaUpdate = arrayOf(id.toString())
        val resultadoActualizacion = conexionEscritura
            .update(
                "ARTISTAS", // nombre tabla
                valoresAActualizar,
                "id=?", // claúsula WHERE
                parametrosConsultaUpdate
            )
        conexionEscritura.close()
        return if (resultadoActualizacion == -1) false else true
    }

    fun consultarArtistaPorID(id: Int): Artista {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM ARTISTAS WHERE id = ?
        """.trimIndent()
        val parametrosConsulta = arrayOf(id.toString())
        val cursor = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            parametrosConsulta
        )
        val existeArtista = cursor.moveToFirst()
        // Si no existe el artista
        if (!existeArtista) return Artista(-1, "", 0, false, 0.0)
        // Si existe el artista
        val nombre = cursor.getString(1)
        val edad = cursor.getInt(2)
        val vivo = cursor.getInt(3) == 1
        val patrimonio = cursor.getDouble(4)
        cursor.close()
        baseDatosLectura.close()
        return Artista(id, nombre, edad, vivo, patrimonio)
    }

    fun consultarArtistas(): ArrayList<Artista> {
        val scriptConsultarArtistas = """
            SELECT * FROM ARTISTAS
        """.trimIndent()
        val baseDatosLectura = readableDatabase
        val resultadoConsulta = baseDatosLectura.rawQuery(
            scriptConsultarArtistas,
            null
        )
        val existeArtista = resultadoConsulta.moveToFirst()
        val arregloArtistas = arrayListOf<Artista>()
        if (existeArtista) {
            do {
                val id = resultadoConsulta.getInt(0) // Columna indice 0 -> ID
                val nombre = resultadoConsulta.getString(1) // Columna indice 1 -> NOMBRE
                val edad = resultadoConsulta.getInt(2) // Columna indice 2 -> EDAD
                val vivo = resultadoConsulta.getInt(3) == 1 // Columna indice 3 -> VIVO
                val patrimonio = resultadoConsulta.getDouble(4) // Columna indice 4 -> PATRIMONIO
                val artista = Artista(id, nombre, edad, vivo, patrimonio)
                arregloArtistas.add(artista)
            } while (resultadoConsulta.moveToNext())
        }
        resultadoConsulta.close()
        baseDatosLectura.close()
        return arregloArtistas
    }

    // CRUD CANCIONES
    fun crearCancion(
        nombre: String,
        idArtista: Int,
        duracion: Int,
        album: String,
        genero: String,
        fechaLanzamiento: String
    ): Boolean {
        val basedatosEscritura = writableDatabase
        val valoresAGuardar = ContentValues()
        valoresAGuardar.put("nombre", nombre)
        valoresAGuardar.put("idArtista", idArtista)
        valoresAGuardar.put("duracion", duracion)
        valoresAGuardar.put("album", album)
        valoresAGuardar.put("genero", genero)
        valoresAGuardar.put("fechaLanzamiento", fechaLanzamiento)
        val resultadoGuardar = basedatosEscritura
            .insert(
                "CANCIONES", // nombre tabla
                null,
                valoresAGuardar
            )
        basedatosEscritura.close()
        return if (resultadoGuardar.toInt() == -1) false else true
    }

    fun eliminarCancion(id: Int): Boolean {
        val conexionEscritura = writableDatabase
        val parametrosConsultaDelete = arrayOf(id.toString())
        val resultadoEliminacion = conexionEscritura
            .delete(
                "CANCIONES", // nombre tabla
                "id=?", // claúsula WHERE
                parametrosConsultaDelete
            )
        conexionEscritura.close()
        return if (resultadoEliminacion.toInt() == -1) false else true
    }

    fun actualizarCancion(
        nombre: String,
        idArtista: Int,
        duracion: Int,
        album: String,
        genero: String,
        fechaLanzamiento: String,
        id: Int
    ): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("nombre", nombre)
        valoresAActualizar.put("idArtista", idArtista)
        valoresAActualizar.put("duracion", duracion)
        valoresAActualizar.put("album", album)
        valoresAActualizar.put("genero", genero)
        valoresAActualizar.put("fechaLanzamiento", fechaLanzamiento)
        //where id...
        val parametrosConsultaUpdate = arrayOf(id.toString())
        val resultadoActualizacion = conexionEscritura
            .update(
                "CANCIONES", // nombre tabla
                valoresAActualizar,
                "id=?", // claúsula WHERE
                parametrosConsultaUpdate
            )
        conexionEscritura.close()
        return if (resultadoActualizacion == -1) false else true
    }

    fun consultarCanciones(): ArrayList<Cancion> {
        val scriptConsultarCanciones = """
            SELECT * FROM CANCIONES
        """.trimIndent()
        val baseDatosLectura = readableDatabase
        val resultadoConsulta = baseDatosLectura.rawQuery(
            scriptConsultarCanciones,
            null
        )
        val existeCancion = resultadoConsulta.moveToFirst()
        val arregloCanciones = arrayListOf<Cancion>()
        if (existeCancion) {
            do {
                val id = resultadoConsulta.getInt(0) // Columna indice 0 -> ID
                val nombre = resultadoConsulta.getString(1) // Columna indice 1 -> NOMBRE
                val idArtista = resultadoConsulta.getInt(2) // Columna indice 2 -> ID ARTISTA
                val duracion = resultadoConsulta.getInt(3) // Columna indice 3 -> DURACION
                val album = resultadoConsulta.getString(4) // Columna indice 4 -> ALBUM
                val genero = resultadoConsulta.getString(5) // Columna indice 5 -> GENERO
                val fechaLanzamiento = resultadoConsulta.getString(6) // Columna indice 6 -> FECHA LANZAMIENTO
                val cancion = Cancion(id, nombre, idArtista, duracion, album, genero, dateFormat.parse(fechaLanzamiento))
                arregloCanciones.add(cancion)
            } while (resultadoConsulta.moveToNext())
        }
        resultadoConsulta.close()
        baseDatosLectura.close()
        return arregloCanciones
    }

    fun consultarCancionPorID(id: Int): Cancion {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT * FROM CANCIONES WHERE id = ?
        """.trimIndent()
        val parametrosConsulta = arrayOf(id.toString())
        val cursor = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            parametrosConsulta
        )
        val existeCancion = cursor.moveToFirst()
        // Si no existe el artista
        if (!existeCancion) return Cancion(-1, "", -1, -1, "", "", dateFormat.parse("01/01/2000"))
        // Si existe el artista
        val nombre = cursor.getString(1)
        val idArtista = cursor.getInt(2)
        val duracion = cursor.getInt(3)
        val album = cursor.getString(4)
        val genero = cursor.getString(5)
        val fechaLanzamiento = cursor.getString(6)
        cursor.close()
        baseDatosLectura.close()
        return Cancion(id, nombre, idArtista, duracion, album, genero, dateFormat.parse(fechaLanzamiento))
    }

    fun consultarCancionesPorArtista(idArtista: Int): ArrayList<Cancion> {
        val scriptConsultarCanciones = """
            SELECT * FROM CANCIONES WHERE idArtista = ?
        """.trimIndent()
        val baseDatosLectura = readableDatabase
        val parametrosConsulta = arrayOf(idArtista.toString())
        val resultadoConsulta = baseDatosLectura.rawQuery(
            scriptConsultarCanciones,
            parametrosConsulta
        )
        val existeCancion = resultadoConsulta.moveToFirst()
        val arregloCanciones = arrayListOf<Cancion>()
        if (existeCancion) {
            do {
                val id = resultadoConsulta.getInt(0) // Columna indice 0 -> ID
                val nombre = resultadoConsulta.getString(1) // Columna indice 1 -> NOMBRE
                val idArtista = resultadoConsulta.getInt(2) // Columna indice 2 -> ID ARTISTA
                val duracion = resultadoConsulta.getInt(3) // Columna indice 3 -> DURACION
                val album = resultadoConsulta.getString(4) // Columna indice 4 -> ALBUM
                val genero = resultadoConsulta.getString(5) // Columna indice 5 -> GENERO
                val fechaLanzamiento = resultadoConsulta.getString(6) // Columna indice 6 -> FECHA LANZAMIENTO
                val cancion = Cancion(id, nombre, idArtista, duracion, album, genero, dateFormat.parse(fechaLanzamiento))
                arregloCanciones.add(cancion)
            } while (resultadoConsulta.moveToNext())
        }
        resultadoConsulta.close()
        baseDatosLectura.close()
        return arregloCanciones
    }

    fun asignarArtistaACancion(idArtista: Int?, idCancion: Int): Boolean {
        val conexionEscritura = writableDatabase
        val valoresAActualizar = ContentValues()
        valoresAActualizar.put("idArtista", idArtista)
        //where id...
        val parametrosConsultaUpdate = arrayOf(idCancion.toString())
        val resultadoActualizacion = conexionEscritura
            .update(
                "CANCIONES", // nombre tabla
                valoresAActualizar,
                "id=?", // claúsula WHERE
                parametrosConsultaUpdate
            )
        conexionEscritura.close()
        return if (resultadoActualizacion == -1) false else true
    }

    fun numeroCanciones(idArtista: Int): Int {
        val baseDatosLectura = readableDatabase
        val scriptConsultaLectura = """
            SELECT COUNT(*) FROM CANCIONES WHERE idArtista = ?
        """.trimIndent()
        val parametrosConsulta = arrayOf(idArtista.toString())
        val cursor = baseDatosLectura.rawQuery(
            scriptConsultaLectura,
            parametrosConsulta
        )
        val existeCancion = cursor.moveToFirst()
        // Si no existe el artista
        if (!existeCancion) return 0
        // Si existe el artista
        val numeroCanciones = cursor.getInt(0)
        cursor.close()
        baseDatosLectura.close()
        return numeroCanciones
    }
}