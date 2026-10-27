
package com.lautaro.recordatoriosapp.data

import com.lautaro.recordatoriosapp.Recordatorio
import com.lautaro.recordatoriosapp.recordatorios
import kotlinx.coroutines.delay

class InMemoryRecordatorioRepository : RecordatorioRepository {

    private val datos = recordatorios.toMutableList()

    override suspend fun obtenerRecordatorios(): List<Recordatorio> {
        delay(500)
        return datos.toList()
    }

    override suspend fun obtenerRecordatorioPorId(id: Int): Recordatorio? {
        delay(500)
        return datos.find { it.id == id }
    }

    override suspend fun guardarRecordatorio(
        recordatorio: Recordatorio
    ): Recordatorio {
        delay(500)
        val indice = datos.indexOfFirst {
            it.id == recordatorio.id
        }

        if (indice >= 0) {
            datos[indice] = recordatorio
        } else {
            datos.add(recordatorio)
        }

        return recordatorio
    }
}
