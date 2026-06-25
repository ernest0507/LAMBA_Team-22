package com.lamba.app.screens.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaError
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaOutlineSoft
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import com.lamba.app.ui.theme.LambaSurface
import components.BackButton
import components.ContinueButton
import components.LambaTextField

data class ExpenseEntry(
    val title: String,
    val category: String,
    val amount: Int,
    val description: String
)

private val expenseCategories = listOf("Заправка", "Деталь", "Мойка", "Прочее")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpensesScreen(
    onBack: () -> Unit,
    isLoading: Boolean = false,
    backendErrorMessage: String? = null,
    onSave: (ExpenseEntry) -> Unit
) {
    var title by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("") }
    var amountText by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var validationError by rememberSaveable { mutableStateOf<String?>(null) }

    val amount = amountText.toIntOrNull()
    val isFormValid = title.isNotBlank() && selectedCategory.isNotBlank() && amountText.isNotBlank()

    fun validate(): Boolean {
        return when {
            title.isBlank() -> {
                validationError = "Введите название"
                false
            }
            selectedCategory.isBlank() -> {
                validationError = "Выберите категорию"
                false
            }
            amountText.isBlank() -> {
                validationError = "Введите стоимость"
                false
            }
            amount == null || amount <= 0 -> {
                validationError = "Стоимость должна быть положительным числом"
                false
            }
            else -> {
                validationError = null
                true
            }
        }
    }

    Scaffold(
        containerColor = LambaCanvas,
        bottomBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding()
                    .padding(
                        start = LambaSpacing.ScreenHorizontal,
                        end = LambaSpacing.ScreenHorizontal,
                        top = LambaSpacing.Step * 2,
                        bottom = LambaSpacing.ScreenBottom
                    )
            ) {
                ContinueButton(
                    onClick = {
                        if (validate()) {
                            onSave(ExpenseEntry(
                                title = title,
                                category = selectedCategory,
                                amount = amountText.toInt(),
                                description = description
                            ))
                        }
                    },
                    text = "Сохранить",
                    enabled = isFormValid && !isLoading
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .padding(
                    start = LambaSpacing.ScreenHorizontal,
                    end = LambaSpacing.ScreenHorizontal,
                    top = LambaSpacing.ScreenTop,
                    bottom = LambaSpacing.ScreenBottom
                ),
            verticalArrangement = Arrangement.spacedBy(LambaSpacing.CardPadding)
        ) {
            BackButton(onClick = onBack)

            Spacer(modifier = Modifier.height(LambaSpacing.Step * 2))

            Text(
                text = "Новая запись",
                style = MaterialTheme.typography.headlineMedium,
                color = LambaInk
            )

            Text(
                text = "Траты",
                style = MaterialTheme.typography.bodyMedium,
                color = LambaInkMuted
            )

            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = LambaSurface,
                shape = RoundedCornerShape(LambaRadius.Large)
            ) {
                Column(
                    modifier = Modifier.padding(LambaSpacing.CardPadding),
                    verticalArrangement = Arrangement.spacedBy(LambaSpacing.CardPadding)
                ) {
                    LambaTextField(
                        label = "Название",
                        value = title,
                        onValueChange = {
                            title = it
                            if (validationError != null) validationError = null
                        },
                        placeholder = "Например, Заправка"
                    )

                    CategoryDropdown(
                        selected = selectedCategory,
                        onSelected = {
                            selectedCategory = it
                            if (validationError != null) validationError = null
                        }
                    )

                    LambaTextField(
                        label = "Стоимость",
                        value = amountText,
                        onValueChange = { input ->
                            if (input.all { it.isDigit() }) {
                                amountText = input
                                if (validationError != null) validationError = null
                            }
                        },
                        placeholder = "0 ₽",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    LambaTextField(
                        label = "Описание",
                        value = description,
                        onValueChange = { description = it },
                        placeholder = "Введите описание (необязательно)",
                        singleLine = false,
                        minHeight = 80.dp
                    )

                    if (!backendErrorMessage.isNullOrBlank()) {
                        Text(
                            text = backendErrorMessage,
                            style = MaterialTheme.typography.bodySmall,
                            color = LambaError
                        )
                    }

                    if (validationError != null) {
                        Text(
                            text = validationError!!,
                            style = MaterialTheme.typography.bodySmall,
                            color = LambaError
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CategoryDropdown(
    selected: String,
    onSelected: (String) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val fieldShape = RoundedCornerShape(LambaRadius.Medium)

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = "Категория",
            style = MaterialTheme.typography.labelSmall,
            color = LambaInkMuted
        )

        Spacer(modifier = Modifier.height(LambaSpacing.Step))

        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            TextField(
                value = selected,
                onValueChange = {},
                readOnly = true,
                modifier = Modifier
                    .menuAnchor(
                        type = MenuAnchorType.PrimaryNotEditable,
                        enabled = true
                    )
                    .fillMaxWidth()
                    .clip(fieldShape)
                    .background(LambaSurface, fieldShape)
                    .border(
                        width = 1.dp,
                        color = LambaOutlineSoft,
                        shape = fieldShape
                    ),
                placeholder = {
                    Text(
                        text = "Выберите категорию",
                        style = MaterialTheme.typography.bodyMedium,
                        color = LambaInkMuted
                    )
                },
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = LambaInk),
                shape = fieldShape,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = LambaInk,
                    unfocusedTextColor = LambaInk,
                    focusedContainerColor = LambaSurface,
                    unfocusedContainerColor = LambaSurface,
                    disabledContainerColor = LambaSurface,
                    errorContainerColor = LambaSurface,
                    focusedTrailingIconColor = LambaInkMuted,
                    unfocusedTrailingIconColor = LambaInkMuted,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                expenseCategories.forEach { category ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = category,
                                style = MaterialTheme.typography.bodyMedium,
                                color = LambaInk
                            )
                        },
                        onClick = {
                            onSelected(category)
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}
