package com.lamba.app.screens.home


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.lamba.app.data.cars.CarResponse
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaSurface
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaVehicleBlue
import com.lamba.app.ui.theme.LambaVehicleGraphite
import com.lamba.app.ui.theme.LambaVehicleGreen
import com.lamba.app.ui.theme.LambaVehicleRed
import com.lamba.app.ui.theme.LambaVehicleSilver
import components.CarImage


@Composable
fun CarSummaryCard(
    car: CarResponse? = null,
    modifier: Modifier = Modifier
) {
    val vehicleColor = car?.color.toVehicleColor()
    val title = car?.displayName().orEmpty().ifBlank { "Модель автомобиля" }
    val subtitle = car?.bodyType?.takeIf { it.isNotBlank() }
        ?: car?.color?.takeIf { it.isNotBlank() }
        ?: "Цифровой двойник"
    val details = car?.let {
        "${it.currentMileageKm.formatMileage()} км · ${it.year}"
    } ?: "Данные авто загружаются"

    Column(
        modifier = modifier
            .background(LambaSurface)
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Column()
        {

            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = LambaInk
            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.labelLarge,
                color = LambaInk
            )

        }


        Spacer(modifier = Modifier.height(8.dp))

        CarImage(
            bodyColor = vehicleColor,
            bodyType = car?.bodyType,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = details,
                style = MaterialTheme.typography.labelSmall,
                color = LambaInkMuted
            )

            Text(
                text = "Состояние 92%",
                style = MaterialTheme.typography.labelSmall,
                color = LambaInkMuted
            )
        }
    }
}

private fun String?.toVehicleColor(): Color {
    return when (this?.lowercase()) {
        "red" -> LambaVehicleRed
        "blue" -> LambaVehicleBlue
        "green" -> LambaVehicleGreen
        "graphite" -> LambaVehicleGraphite
        "silver" -> LambaVehicleSilver
        else -> LambaVehicleSilver
    }
}












