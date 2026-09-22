package ru.esm.tspiot.ui.items

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.esm.tspiot.domain.EsmResult
import ru.esm.tspiot.domain.PiotResult

@Composable
fun ResultDisplay(result: EsmResult<String>?) {
    ResultDisplayCard(result.toDisplayState())
}

@Composable
fun ResultDisplay(result: PiotResult<String>?) {
    ResultDisplayCard(result.toDisplayState())
}

private sealed class ResultDisplayState {
    data object Hidden : ResultDisplayState()
    data object Loading : ResultDisplayState()
    data object ServiceUnavailable : ResultDisplayState()
    data class Error(val code: Int, val message: String?) : ResultDisplayState()
    data class Success(val data: String) : ResultDisplayState()
}

private fun EsmResult<String>?.toDisplayState(): ResultDisplayState = when (this) {
    null -> ResultDisplayState.Hidden
    EsmResult.Loading -> ResultDisplayState.Loading
    EsmResult.ServiceUnavailable -> ResultDisplayState.ServiceUnavailable
    is EsmResult.Error -> ResultDisplayState.Error(code, message)
    is EsmResult.Success -> ResultDisplayState.Success(data)
}

private fun PiotResult<String>?.toDisplayState(): ResultDisplayState = when (this) {
    null -> ResultDisplayState.Hidden
    PiotResult.Loading -> ResultDisplayState.Loading
    PiotResult.ServiceUnavailable -> ResultDisplayState.ServiceUnavailable
    is PiotResult.Error -> ResultDisplayState.Error(code, message)
    is PiotResult.Success -> ResultDisplayState.Success(data)
}

@Composable
private fun ResultDisplayCard(result: ResultDisplayState) {
    when (result) {
        ResultDisplayState.Hidden -> Unit
        ResultDisplayState.Loading -> {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
        }

        ResultDisplayState.ServiceUnavailable ->
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            ) {
                Text(
                    "Service unavailable"
                )
            }

        is ResultDisplayState.Error ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    "Error: ${result.code} - ${result.message}",
                )
            }

        is ResultDisplayState.Success ->
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Text(
                    result.data,
                )
            }
    }
}
