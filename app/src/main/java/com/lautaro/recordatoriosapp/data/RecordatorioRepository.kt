
package com.lautaro.recordatoriosapp.data

import com.lautaro.recordatoriosapp.Recordatorio

interface RecordatorioRepository {
    suspend fun obtenerRecordatorios(): List<Recordatorio>

    suspend fun obtenerRecordatorioPorId(id: Int): Recordatorio?

    suspend fun guardarRecordatorio(recordatorio: Recordatorio): Recordatorio
}
