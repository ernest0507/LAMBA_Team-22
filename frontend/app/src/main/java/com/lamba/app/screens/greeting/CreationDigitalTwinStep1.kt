package com.lamba.app.screens.greeting

import androidx.compose.foundation.background
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import components.ContinueButton

@Composable
fun CreationDigitalTwinStep1(
    onBack: () -> Unit = {},
    onContinue: () -> Unit = {}
) {
    var carModel by remember { mutableStateOf("") }
    var carYear by remember { mutableStateOf("") }
    var mileage by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }

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
            LambaTextField(
                label = "Модель автомобиля",
                value = carModel,
                onValueChange = { carModel = it },
                placeholder = "Введите модель",
                modifier = Modifier.fillMaxWidth()

            )

            Spacer(modifier = Modifier.height(LambaSpacing.CardPadding))

            LambaTextField(
                label = "Год выпуска",
                value = carYear,
                onValueChange = { newValue ->
                    val year = newValue.toIntOrNull()
                    if (
                        newValue.length <= 4 &&
                        newValue.all { it.isDigit() }
                    ) {
                        carYear = newValue
                    }

                },
                placeholder = "Год выпуска",
                modifier = Modifier.fillMaxWidth()
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
                modifier = Modifier.fillMaxWidth()
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
                onClick = onContinue,
                text = "Продолжить"
            )

        }
            val year = carYear.toIntOrNull()
            val isYearValid = carYear.length == 4 && year in 1950..2026
    }

}

