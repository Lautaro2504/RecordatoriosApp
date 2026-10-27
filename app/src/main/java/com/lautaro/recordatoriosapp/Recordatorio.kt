
package com.lautaro.recordatoriosapp

enum class Prioridad {
    BAJA,
    MEDIA,
    ALTA
}

enum class Estado {
    PENDIENTE,
    EN_PROGRESO,
    COMPLETADO
}

sealed class ResultadoBusqueda {
    data class Encontrado(
        val recordatorio: Recordatorio
    ) : ResultadoBusqueda()

    data object NoEncontrado : ResultadoBusqueda()
}


data class Categoria(
    val nombre: String,
    val colorHex: String
)

data class Recordatorio(
    val id: Int,
    val titulo: String,
    val descripcion: String? = null,
    val categoria: Categoria,
    val prioridad: Prioridad,
    val estado: Estado
)
