package ru.esm.tspiot.ui.items

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.esm.tspiot.domain.EsmResult

@Composable
fun MethodSection(
    title: String,
    onClick: () -> Unit,
    result: EsmResult<String>?
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = title, style = MaterialTheme.typography.titleMedium)
        Button(onClick = onClick) {
            Text("Выполнить")
        }
        ResultDisplay(result)
    }
}