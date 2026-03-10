package ru.esm.tspiot.ui.items

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.esm.tspiot.domain.ScanResult

@Composable
fun ScannedMarks(
    scanResult: Set<ScanResult>?
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(4.dp)
    ) {
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            modifier = Modifier.padding(horizontal = 4.dp),
            text = "Марки",
            style = MaterialTheme.typography.titleMedium
        )
        scanResult?.let { results ->
            results.forEachIndexed { index, result ->
                Text(
                    text = "${index + 1}. ${result.text}",
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
    }
}