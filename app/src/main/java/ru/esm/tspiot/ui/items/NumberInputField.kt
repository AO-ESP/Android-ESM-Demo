package ru.esm.tspiot.ui.items

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun NumberInputField(
    value: Int?,
    onValueChange: (Int?) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Введите число"
) {
    var textState by remember(value) { mutableStateOf(value?.toString() ?: "") }

    OutlinedTextField(
        value = textState,
        onValueChange = { newValue ->
            // Фильтруем только цифры
            val filtered = newValue.filter { it.isDigit() }

            textState = filtered

            // Преобразуем в число
            val number = if (filtered.isNotEmpty()) {
                filtered.toIntOrNull()
            } else {
                null
            }

            onValueChange(number)
        },
        modifier = modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

