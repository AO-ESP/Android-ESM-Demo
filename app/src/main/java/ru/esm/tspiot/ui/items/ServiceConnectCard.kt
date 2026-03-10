package ru.esm.tspiot.ui.items

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ru.esm.tspiot.ui.viewmodels.MainViewModel

@Composable
fun ServiceConnectCard(
    viewModel: MainViewModel
) {
    val isConnected by viewModel.isConnected.collectAsStateWithLifecycle()

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isConnected -> MaterialTheme.colorScheme.primaryContainer
                else -> MaterialTheme.colorScheme.errorContainer
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Статус подключения",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = if (isConnected) "✓ Подключено" else "✗ Не подключено",
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }

    // Кнопки управления
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Button(
            onClick = { viewModel.connect() },
            enabled = !isConnected,
            modifier = Modifier.weight(1f)
        ) {
            Text("Подключить")
        }

        Button(
            onClick = { viewModel.disconnect() },
            enabled = isConnected,
            modifier = Modifier.weight(1f),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer
            )
        ) {
            Text("Отключить")
        }
    }
}