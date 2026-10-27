
package com.lautaro.recordatoriosapp

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BusquedaRecordatorioTest {

    @Test
    fun buscarRecordatorioExistente() {
        val resultado = buscarRecordatorio(1)

        assertTrue(resultado is ResultadoBusqueda.Encontrado)

        if (resultado is ResultadoBusqueda.Encontrado) {
            assertEquals(
                "Estudiar Kotlin",
                resultado.recordatorio.titulo
            )
        }
    }

    @Test
    fun buscarRecordatorioInexistente() {
        val resultado = buscarRecordatorio(99)

        assertEquals(
            ResultadoBusqueda.NoEncontrado,
            resultado
        )
    }
}
