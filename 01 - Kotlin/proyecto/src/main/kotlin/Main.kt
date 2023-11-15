import java.util.*

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
        dos: Int // parametros
    ) : this(  // llamada constructor primario
        if (uno == null) 0 else uno,
        dos
    ) { // si necesitamos bloque de codigo lo usamos
        numeroUno;
    }

    constructor(//  tercer constructor
        uno: Int, // parametros
        dos: Int? // parametros
    ) : this(  // llamada constructor primario
        uno,
        if (dos == null) 0 else uno
    ) // Si no lo necesitamos al bloque de codigo "{}" lo omitimos
}