
package com.lautaro.recordatoriosapp.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.ui.unit.dp

@Composable
fun EstadoCargando(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator()
        Text("Cargando recordatorios...")
    }
}

@Composable
fun EstadoVacio(
    modifier: Modifier = Modifier,
    alCrearRecordatorio: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Todavía no hay recordatorios.")

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(12.dp)
        )

        androidx.compose.material3.Button(
            onClick = alCrearRecordatorio
        ) {
            Text("Crear primer recordatorio")
        }
    }
}


@Composable
fun EstadoError(
    mensaje: String,
    modifier: Modifier = Modifier,
    alReintentar: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = mensaje)

        androidx.compose.foundation.layout.Spacer(
            modifier = Modifier.height(12.dp)
        )

        Button(
            onClick = alReintentar
        ) {
            Text("Reintentar")
        }
    }
}

