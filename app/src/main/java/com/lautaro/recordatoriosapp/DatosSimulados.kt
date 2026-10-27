
package com.lautaro.recordatoriosapp

val categoriasPorNombre: Map<String, Categoria> = listOf(
    Categoria("Estudio", "#2196F3"),
    Categoria("Trabajo", "#4CAF50"),
    Categoria("Personal", "#FF9800")
).associateBy { it.nombre }

val categorias: Set<Categoria> = categoriasPorNombre.values.toSet()


// Lista de recordatorios de ejemplo.
val recordatorios: List<Recordatorio> = listOf(
    Recordatorio(
        id = 1,
        titulo = "Estudiar Kotlin",
        descripcion = "Repasar clases y objetos",
        categoria = categorias.first { it.nombre == "Estudio" },
        prioridad = Prioridad.ALTA,
        estado = Estado.PENDIENTE
    ),
    Recordatorio(
        id = 2,
        titulo = "Entregar actividad",
        descripcion = "Completar la actividad de programación",
        categoria = categorias.first { it.nombre == "Estudio" },
        prioridad = Prioridad.ALTA,
        estado = Estado.EN_PROGRESO
    ),
    Recordatorio(
        id = 3,
        titulo = "Organizar escritorio",
        descripcion = "Ordenar los materiales de trabajo",
        categoria = categorias.first { it.nombre == "Personal" },
        prioridad = Prioridad.BAJA,
        estado = Estado.COMPLETADO
    ),
    Recordatorio(
        id = 4,
        titulo = "Revisar correos",
        descripcion = "Leer los mensajes pendientes",
        categoria = categorias.first { it.nombre == "Trabajo" },
        prioridad = Prioridad.MEDIA,
        estado = Estado.PENDIENTE
    )
)

// Mapa que relaciona cada estado con una etiqueta visible.
val etiquetasEstado: Map<Estado, String> = mapOf(
    Estado.PENDIENTE to "Pendiente",
    Estado.EN_PROGRESO to "En progreso",
    Estado.COMPLETADO to "Completado"
)

// Función para filtrar recordatorios por estado.
fun filtrarPorEstado(estado: Estado): List<Recordatorio> {
    return recordatorios.filter { it.estado == estado }
}

// Función para filtrar recordatorios por prioridad.
fun filtrarPorPrioridad(prioridad: Prioridad): List<Recordatorio> {
    return recordatorios.filter { it.prioridad == prioridad }
}


fun buscarRecordatorio(id: Int): ResultadoBusqueda {
    val recordatorioEncontrado = recordatorios.find {
        it.id == id
    }

    return if (recordatorioEncontrado != null) {
        ResultadoBusqueda.Encontrado(recordatorioEncontrado)
    } else {
        ResultadoBusqueda.NoEncontrado
    }
}

fun probarBusqueda() {
    val resultado = buscarRecordatorio(1)

    when (resultado) {
        is ResultadoBusqueda.Encontrado ->
            println("Encontrado: ${resultado.recordatorio.titulo}")

        ResultadoBusqueda.NoEncontrado ->
            println("No se encontró el recordatorio")
    }
}