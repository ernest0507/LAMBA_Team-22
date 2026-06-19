package com.lamba.app.screens.greeting


import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaAccentStrong
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaSpacing
import components.BackButton
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing.ScreenHorizontal
import com.lamba.app.ui.theme.LambaSurface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.lamba.app.ui.theme.LambaVehicleBlue
import com.lamba.app.ui.theme.LambaVehicleGraphite
import com.lamba.app.ui.theme.LambaVehicleGreen
import com.lamba.app.ui.theme.LambaVehicleRed
import com.lamba.app.ui.theme.LambaVehicleSilver
import components.CarImage
import components.ContinueButton
import components.TypeCarButton

@Composable
fun CreationDigitalTwinStep2(
    onBack: () -> Unit = {},
    onCreateTwin: () -> Unit = {}
) {
    var selectedColor by remember { mutableStateOf(LambaVehicleBlue) }
    var selectedBodyType by remember { mutableStateOf("Седан") }

    val carColors = listOf(
        LambaVehicleRed,
        LambaVehicleBlue,
        LambaVehicleGreen,
        LambaVehicleSilver,
        LambaVehicleGraphite
    )

    val bodyTypes = listOf(
        "Седан",
        "Хэтчбек",
        "Кроссовер",
        "Купе",
        "Универсал",
        "Пикап"
    )

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
                text = "Настройте автомобиль",
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(LambaSpacing.Step))

            Text(
                text = "Шаг 2 из 2",
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
                        .background(LambaAccentStrong)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(LambaRadius.Large))
                    .background(LambaSurface),
                contentAlignment = Alignment.Center
            ) {
                CarImage(bodyColor = selectedColor)
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Цвет автомобиля",
                style = MaterialTheme.typography.labelMedium,
                color = LambaInkMuted
            )

            Spacer(modifier = Modifier.height(LambaSpacing.Step))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                carColors.forEach { color ->
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(LambaRadius.Pill))
                            .background(color)
                            .border(
                                width = if (selectedColor == color) 3.dp else 0.dp,
                                color = if (selectedColor == color) LambaAccentStrong else Color.Transparent,
                                shape = RoundedCornerShape(LambaRadius.Pill)
                            )
                            .clickable{
                                selectedColor = color
                            }
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Тип кузова",
                style = MaterialTheme.typography.labelMedium,
                color = LambaInkMuted
            )

            Spacer(modifier = Modifier.height(LambaSpacing.Step))

            Column(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                bodyTypes.chunked(3).forEach { rowItems ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        rowItems.forEach { bodyType ->
                            TypeCarButton(
                                text = bodyType,
                                selected = selectedBodyType == bodyType,
                                onClick = {
                                    selectedBodyType = bodyType
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }
            Spacer(modifier = Modifier.weight(1f))
            ContinueButton(
                text = "Создать двойника",
                onClick = onCreateTwin
            )
        }
    }

}

