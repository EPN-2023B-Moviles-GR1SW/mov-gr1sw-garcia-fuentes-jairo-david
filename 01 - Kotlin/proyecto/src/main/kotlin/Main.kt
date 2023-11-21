import java.util.*
import kotlin.collections.ArrayList

fun main(){

    //void -> unit

    fun imprimirNombre(nombre: String): Unit{
        println("Nombre: $nombre")
    }

    fun calcularSueldo(
        sueldo: Double,
        tasa: Double = 12.0,
        bono: Double? = null,
    ): Double {
        if (bono == null) {
            return sueldo * (100 / tasa)
        } else {
            bono.dec()
            return sueldo * (100 / tasa) + bono
        }
    }

    println("Hola Mundo")
    //Tipos de variables
    //Inmutables ( no se reasignan)
    val inmutable: String = "Jairo"

    //Mutables
    var nutable: String = "Jairo"

    // val > var
    var ejemploVariable = " Jairo Garcia "
    val edadEjemplo: Int = 22
    println(ejemploVariable.trim())

    //Variable primitiva
    val nombreProfesor: String = "Adrian Eguez"
    val sueldo: Double =1.2
    val estadoCivil: Char='S'
    val mayorEdad: Boolean = true
    //Clase de Java
    val fechanacimiento: Date = Date()

    //SWITCH
    when (estadoCivil){
        'C' -> {
            println("Casado")
        }
        'S' -> {
            println("soltero")
        }
        else -> {
            println("Otro")
        }
    }
    val coqueteo = if (estadoCivil == 'S') "Si" else "No"
    println(coqueteo)

    calcularSueldo(10.00)
    calcularSueldo(10.00, 15.00)
    calcularSueldo(10.00, 12.00, 20.00)
    calcularSueldo(sueldo = 10.00, tasa = 12.00, bono = 20.00)

    val sumaUno = Suma(1,1)
    val sumaDos = Suma(null, 1)
    val sumaTres = Suma(1, null)
    val sumaCuatro = Suma(null, null)
    sumaUno.sumar()
    sumaDos.sumar()
    sumaTres.sumar()
    sumaCuatro.sumar()
    println(Suma.pi)
    println(Suma.elevarAlCuadrado(2))
    println(Suma.historialSumas)

    //Tipos de Arreglos

    // Arreglo Estático
    val arregloEstatico: Array<Int> = arrayOf(1,2,3)
    println(arregloEstatico)

    // Arreglo Dinámico
    val arregloDinamico: ArrayList<Int> = arrayListOf(
        1,2,3,4,5,6,7,8,9,10
    )
    println(arregloDinamico)
    arregloDinamico.add(11)
    arregloDinamico.add(12)
    println(arregloDinamico)

    // FOR EACH -> UNIT
    // Iterar un arreglo
    val respuestaForEach: Unit = arregloDinamico.forEach{ valorActual: Int ->
        println("Valor actual: $valorActual")
    }

    arregloDinamico.forEach{ println(it) }

    arregloEstatico.forEachIndexed{
        indice: Int, valorActual: Int ->
        println("Valor $valorActual Indice: $indice")
    }

    // MAP -> Muta el arreglo
    // 1. enviamos el nuevo valor de la iteracion
    // 2. nos devuelve un nuevo arreglo con los valores modificados

    val respuestaMap: List<Double> = arregloDinamico.map{
        valorActual: Int ->
        return@map valorActual.toDouble() + 100
    }

    val respuestaMapDos = arregloDinamico.map {it + 15}
    println(respuestaMap)
    println(respuestaMapDos)

    // FILTER
    // 1. Devolver una expresion True or False
    // 2. Nuevo arreglo filtrado
    val respuestaFilter: List<Int> = arregloDinamico.filter {
        valorActual: Int ->
        //Expresion Condicion
        val mayoresACinco: Boolean = valorActual > 5
        return@filter mayoresACinco
    }
    val respuestaFilterDos = arregloDinamico.filter { it <= 5 }
    println(respuestaFilter)
    println(respuestaFilterDos)

    // OR AND
    // Or -> ANY
    // And -> ALL

    val  respuestaAny: Boolean = arregloDinamico.any{
        valorActual: Int ->
        return@any (valorActual > 5)
    }
    println(respuestaAny)

    val respuestaAll: Boolean = arregloDinamico.all {
        valorActual: Int ->
        return@all (valorActual > 5)
    }
    println(respuestaAll)

    //REDUCE -> Acumulador
    //Valor acumulado siempre = 0

    val respuestaReduce: Int = arregloDinamico.reduce{
        acumulado: Int, valorActual: Int ->
        return@reduce (acumulado + valorActual)
    }
    println(respuestaReduce)
}
abstract class NumeroJava{
    protected val numeroUno: Int
    private val numeroDos: Int
    constructor(
        uno: Int,
        dos: Int
    ){
        this.numeroUno = uno
        this.numeroDos = dos
        println("Inizializando")
    }
}

abstract class Numeros( // Contstructor Primario
    //uno: Int, (Parametro (sin modificador de acceso))
    //private var uno: Int, // Propiedad Publica Clase numeros.uno
    // var uno: Int, // Propiedad de la clase (por defecto es PUBLIC)
    //public var uno: Int,
    // Propiedad de la clase protected numeros.numeroUno
    protected val numeroUno: Int,
    // Propiedad de la clase protected numeros.numeroDos
    protected val numeroDos: Int,
){
    // var cedula: string = "" (public es por defecto)
    // private valorCalculado: Int = 0 (private)
    init {
        this.numeroUno; this.numeroDos; // this es opcional
        numeroUno; numeroDos; // sin el "this", es lo mismo
        println("Inicializando")
    }
}

class Suma(
    uno:Int,
    dos: Int
) : Numeros(uno, dos) { // <- constructor del padre
    init {
        this.numeroUno; numeroUno;
        this.numeroDos; numeroDos;
    }
    constructor(//  Segundo constructor
        uno: Int?, // parametros
        dos: Int? // parametros
    ) : this(  // llamada constructor primario
        if (uno == null) 0 else uno,
        if (dos == null) 0 else dos
    )

    public fun sumar(): Int {
        val total = numeroUno + numeroDos
        agregarHistorial(total)
        return total
    }

    companion object {
        // Atributos y métodos 'compartidos' entre las instancias
        val pi = 3.14

        fun elevarAlCuadrado(num: Int): Int {
            return num * num
        }
        val historialSumas = arrayListOf<Int>()

        fun agregarHistorial(valorNuevaSuma: Int){
            historialSumas.add(valorNuevaSuma)
        }
    }
}