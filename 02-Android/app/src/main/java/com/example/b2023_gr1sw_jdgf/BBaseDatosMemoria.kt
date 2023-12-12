package com.example.b2023_gr1sw_jdgf

class BBaseDatosMemoria {
    // EMPEZAR EL COMPANION OBJECT
    companion object {
        val arregloBEntrenador = arrayListOf<BEntrenador>()

        init {
            arregloBEntrenador.add(BEntrenador(1, "Jairo", "a@a.com"))
            arregloBEntrenador.add(BEntrenador(2, "David", "b@b.com"))
            arregloBEntrenador.add(BEntrenador(3, "Garcia", "c@c.com"))
        }
    }
}