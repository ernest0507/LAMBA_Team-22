package com.lamba.app.screens.expenses

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaError
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import com.lamba.app.ui.theme.LambaSurface
import components.BackButton
import components.ContinueButton
import components.LambaTextField

data class ExpenseEntry(
    val amount: Int,
    val description: String

    /* TODO: Add user_id, car_id, category, created_at for API contract */

)

@Composable
fun AddExpensesScreen(
    onBack: () -> Unit,
    isLoading: Boolean = false,
    backendErrorMessage: String? = null,
    onSave: (ExpenseEntry) -> Unit
) {
    var amountText by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var validationError by remember { mutableStateOf<String?>(null) }

    val amount = amountText.toIntOrNull()
    val isFormValid = amountText.isNotBlank()

    fun validate(): Boolean {
        return when {
            amountText.isBlank() -> {
                validationError = "Введите сумму"
                false
            }
            amount == null || amount <= 0 -> {
                validationError = "Сумма должна быть положительным числом"
                false
            }
            else -> {
                validationError = null
                true
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LambaCanvas)
            .verticalScroll(rememberScrollState())
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = LambaSpacing.ScreenHorizontal,
                    end = LambaSpacing.ScreenHorizontal,
                    top = LambaSpacing.ScreenTop,
                    bottom = LambaSpacing.ScreenBottom
                )
        ) {
            BackButton(onClick = onBack)

            Spacer(modifier = Modifier.height(LambaSpacing.Step * 4))

            Text(
                text = "Добавить расход",
                style = MaterialTheme.typography.headlineMedium,
                color = LambaInk
            )

            Spacer(modifier = Modifier.height(LambaSpacing.Step))

            Text(
                text = "Запишите новую трату на автомобиль",
                style = MaterialTheme.typography.bodyMedium,
                color = LambaInkMuted
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = LambaSurface,
                        shape = RoundedCornerShape(LambaRadius.Medium)
                    )
                    .padding(LambaSpacing.CardPadding)
            ) {
                LambaTextField(
                    label = "Сумма (₽)",
                    value = amountText,
                    onValueChange = { input ->
                        if (input.all { it.isDigit() }) {
                            amountText = input
                            if (validationError != null) validationError = null
                        }
                    },
                    placeholder = "0",
                    isError = validationError != null,
                    errorMessage = validationError,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                )

                Spacer(modifier = Modifier.height(16.dp))

                LambaTextField(
                    label = "Описание",
                    value = description,
                    onValueChange = { description = it },
                    placeholder = "Например: замена масла",
                    singleLine = false,
                    minHeight = 100.dp,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
                )
            }

            Spacer(modifier = Modifier.height(LambaSpacing.BottomNavigationSpace))

            if (!backendErrorMessage.isNullOrBlank()) {
                Text(
                    text = backendErrorMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = LambaError
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            ContinueButton(
                text = "Сохранить",
                enabled = isFormValid && !isLoading,
                onClick = {
                    if (validate()) {
                        onSave(ExpenseEntry(
                            amount = amountText.toInt(),
                            description = description
                        ))
                    }
                }
            )
        }
    }
}
