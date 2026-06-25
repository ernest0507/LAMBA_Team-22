package com.lamba.app.screens.history

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import components.LambaTextField

data class RepairRecordFormData(
    val category: String,
    val description: String,
    val mileage: String,
    val breakdownDate: String
)

private val RepairCategories = listOf(
    "Двигатель",
    "Трансмиссия",
    "Ходовая часть",
    "Электрика",
    "Тормозная система",
    "Другое"
)

@Composable
fun RepairRecordScreen(
    onBack: () -> Unit,
    onSave: (RepairRecordFormData) -> Unit
) {
    var category by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var mileage by rememberSaveable { mutableStateOf("") }
    var breakdownDateMillis by rememberSaveable { mutableStateOf<Long?>(null) }

    RecordFormScreen(
        subtitle = "Поломки",
        onBack = onBack,
        onSave = {
            onSave(
                RepairRecordFormData(
                    category = category,
                    description = description,
                    mileage = mileage,
                    breakdownDate = breakdownDateMillis?.let(::formatRecordDate).orEmpty()
                )
            )
        },
        saveEnabled = category.isNotBlank() && description.isNotBlank()
    ) {
        RecordDropdownField(
            label = "Категория поломки",
            value = category,
            placeholder = "Выберите категорию",
            options = RepairCategories,
            onValueChange = { category = it }
        )

        LambaTextField(
            label = "Описание",
            value = description,
            onValueChange = { description = it },
            placeholder = "Введите описание",
            singleLine = false,
            minHeight = RecordMultilineFieldMinHeight
        )

        LambaTextField(
            label = "Пробег",
            value = mileage,
            onValueChange = { mileage = it.filter(Char::isDigit) },
            placeholder = "0 км",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        RecordDateField(
            label = "Дата поломки",
            value = breakdownDateMillis?.let(::formatRecordDate).orEmpty(),
            placeholder = "Выберите дату",
            selectedDateMillis = breakdownDateMillis,
            onDateSelected = { breakdownDateMillis = it }
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEF4F2)
@Composable
private fun RepairRecordScreenPreview() {
    LAMBA_MVPv0Theme {
        RepairRecordScreen(
            onBack = {},
            onSave = {}
        )
    }
}
