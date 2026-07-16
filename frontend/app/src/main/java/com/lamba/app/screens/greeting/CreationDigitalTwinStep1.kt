package com.lamba.app.screens.greeting

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaAccentStrong
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaSpacing
import components.BackButton
import components.LambaTextField
import com.lamba.app.ui.theme.LambaOutlineSoft
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing.ScreenHorizontal
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lamba.app.data.cars.CarDraft
import components.ContinueButton

@Composable
fun CreationDigitalTwinStep1(
    onBack: () -> Unit = {},
    onContinue: (CarDraft) -> Unit = { _ -> }
) {
    var selectedBrand by remember { mutableStateOf("") }
    var carModel by remember { mutableStateOf("") }
    var carYear by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }
    var expandedDropdown by remember { mutableStateOf<String?>(null) }

    var showValidation by remember { mutableStateOf(false) }
    val year = carYear.toIntOrNull()
    val modelOptions = remember(selectedBrand) {
        CarModelsByBrand[selectedBrand].orEmpty()
    }
    val isBrandValid = selectedBrand.isNotBlank()
    val isCarModelValid = carModel.isNotBlank()
    val isCarYearValid = carYear.length == 4 && year in 1950..2026
    val isMileageValid = mileage.isNotBlank()
    val isFormValid = isBrandValid && isCarModelValid && isMileageValid && isCarYearValid

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(LambaCanvas)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = ScreenHorizontal)
                .padding(top = LambaSpacing.ScreenTop)
                .padding(bottom = LambaSpacing.BottomNavigationSpace)
        ) {

            BackButton(modifier = Modifier, onClick = onBack)

            Spacer(modifier = Modifier.height(LambaSpacing.Step))

            Text(
                text = "Создайте цифровой двойник",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(LambaSpacing.Step))

            Text(
                text = "Шаг 1 из 2",
                style = MaterialTheme.typography.bodySmall,
            )

            Spacer(modifier = Modifier.height(LambaSpacing.Step))

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(LambaRadius.Pill))
                        .background(LambaAccentStrong)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(4.dp)
                        .clip(RoundedCornerShape(LambaRadius.Pill))
                        .background(LambaOutlineSoft)
                )
            }
            Spacer(modifier = Modifier.height(44.dp))

            TwinDropdownField(
                label = "Марка автомобиля",
                value = selectedBrand,
                placeholder = "Выберите марку",
                options = CarBrands,
                expanded = expandedDropdown == TwinDropdownKeyBrand,
                onExpandedChange = { expanded ->
                    expandedDropdown = if (expanded) TwinDropdownKeyBrand else null
                },
                onOptionSelected = { brand ->
                    selectedBrand = brand
                    carModel = CarModelsByBrand[brand]?.firstOrNull().orEmpty()
                    expandedDropdown = null
                },
                modifier = Modifier.fillMaxWidth(),
                isError = showValidation && !isBrandValid,
                errorMessage = "Заполните обязательное поле"
            )

            Spacer(modifier = Modifier.height(LambaSpacing.CardPadding))

            TwinDropdownField(
                label = "Модель автомобиля",
                value = carModel,
                placeholder = "Выберите модель",
                options = modelOptions,
                expanded = expandedDropdown == TwinDropdownKeyModel,
                onExpandedChange = { expanded ->
                    if (selectedBrand.isNotBlank()) {
                        expandedDropdown = if (expanded) TwinDropdownKeyModel else null
                    }
                },
                onOptionSelected = { model ->
                    carModel = model
                    expandedDropdown = null
                },
                modifier = Modifier.fillMaxWidth(),
                isError = showValidation && !isCarModelValid,
                errorMessage = "Заполните обязательное поле"
            )

            Spacer(modifier = Modifier.height(LambaSpacing.CardPadding))

            LambaTextField(
                label = "Год выпуска",
                value = carYear,
                onValueChange = { newValue ->
                    if (
                        newValue.length <= 4 &&
                        newValue.all { it.isDigit() }
                    ) {
                        carYear = newValue
                    }

                },
                placeholder = "Год выпуска",
                modifier = Modifier.fillMaxWidth(),
                isError = showValidation && !isCarYearValid,
                errorMessage = "Введите год от 1950 до 2026"
            )

            Spacer(modifier = Modifier.height(LambaSpacing.CardPadding))

            LambaTextField(
                label = "Пробег, км",
                value = mileage,
                onValueChange = {newValue ->
                    if (newValue.all { it.isDigit() }) {
                        mileage = newValue
                    } },
                placeholder = "Пробег, км",
                modifier = Modifier.fillMaxWidth(),
                isError = showValidation && !isMileageValid,
                errorMessage = "Заполните обязательное поле"
            )

            Spacer(modifier = Modifier.height(LambaSpacing.CardPadding))

            LambaTextField(
                label = "Общие заметки",
                value = notes,
                onValueChange = { notes = it },
                placeholder = "Общие заметки",
                modifier = Modifier.fillMaxWidth(),
                minHeight = 116.dp
            )

            Spacer(modifier = Modifier.height(10.dp))

            ContinueButton(
                onClick = {
                    showValidation = true
                    val mileageValue = mileage.toIntOrNull()
                    if (isFormValid && year != null && mileageValue != null) {
                        onContinue(
                            CarDraft(
                                make = selectedBrand.trim(),
                                model = carModel.trim(),
                                year = year,
                                currentMileageKm = mileageValue,
                                notes = notes.trim().takeIf { it.isNotEmpty() }
                            )
                        )
                    }
                },
                text = "Продолжить"
            )

        }
    }

}

private const val TwinDropdownKeyBrand = "brand"
private const val TwinDropdownKeyModel = "model"

@Composable
private fun TwinDropdownField(
    label: String,
    value: String,
    placeholder: String,
    options: List<String>,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    onOptionSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    Box(modifier = modifier.fillMaxWidth()) {
        LambaTextField(
            label = label,
            value = value,
            onValueChange = {},
            placeholder = placeholder,
            modifier = Modifier.fillMaxWidth(),
            isError = isError,
            errorMessage = errorMessage,
            trailingContent = {
                Icon(
                    imageVector = Icons.Filled.KeyboardArrowDown,
                    contentDescription = null
                )
            }
        )

        Box(
            modifier = Modifier
                .matchParentSize()
                .clickable { onExpandedChange(!expanded) }
        )

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { onExpandedChange(false) },
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .background(Color.White)
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = option,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    },
                    onClick = { onOptionSelected(option) },
                    colors = MenuDefaults.itemColors(
                        textColor = MaterialTheme.colorScheme.onSurface
                    )
                )
            }
        }
    }
}

