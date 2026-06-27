package com.lamba.app.screens.history

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.input.KeyboardType
import components.LambaTextField

data class ExpensesRecordFormData(
    val category: String,
    val name: String,
    val description: String,
    val date: String,
    val cost: String
)

private val ExpenseCategories = listOf("Заправка", "Деталь", "Мойка", "Прочее")

@Composable
fun ExpensesRecordScreen(
    onBack: () -> Unit,
    onSave: (ExpensesRecordFormData) -> Unit,
    isSaving: Boolean = false,
    errorMessage: String? = null
) {
    var category by rememberSaveable { mutableStateOf("") }
    var name by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var dateMillis by rememberSaveable { mutableStateOf<Long?>(null) }
    var cost by rememberSaveable { mutableStateOf("") }

    RecordFormScreen(
        subtitle = "Траты",
        onBack = onBack,
        isSaving = isSaving,
        errorMessage = errorMessage,
        onSave = {
            onSave(
                ExpensesRecordFormData(
                    category = category,
                    name = name,
                    description = description,
                    date = dateMillis?.let(::formatRecordDate).orEmpty(),
                    cost = cost
                )
            )
        },
        saveEnabled = category.isNotBlank() && name.isNotBlank() && cost.isNotBlank()
    ) {
        RecordDropdownField(
            label = "Категория",
            value = category,
            placeholder = "Выберите категорию",
            options = ExpenseCategories,
            onValueChange = { category = it }
        )

        LambaTextField(
            label = "Название",
            value = name,
            onValueChange = { name = it },
            placeholder = "Например, Заправка"
        )

        LambaTextField(
            label = "Описание",
            value = description,
            onValueChange = { description = it },
            placeholder = "Введите описание",
            singleLine = false,
            minHeight = RecordMultilineFieldMinHeight
        )

        RecordDateField(
            label = "Дата траты",
            value = dateMillis?.let(::formatRecordDate).orEmpty(),
            placeholder = "Выберите дату",
            selectedDateMillis = dateMillis,
            onDateSelected = { dateMillis = it }
        )

        LambaTextField(
            label = "Стоимость",
            value = cost,
            onValueChange = { cost = it.filter(Char::isDigit) },
            placeholder = "0 ₽",
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
    }
}
