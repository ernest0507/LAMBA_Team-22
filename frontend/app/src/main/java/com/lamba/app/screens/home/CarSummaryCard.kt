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
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaSurface
import com.lamba.app.ui.theme.LambaVehicleSilver
import com.lamba.app.ui.theme.LambaRadius
import components.CarImage


@Composable
fun CarSummaryCard(
    modifier: Modifier = Modifier
) {

    Column(
        modifier = modifier
            .background(LambaSurface)
            .padding(horizontal = 24.dp, vertical = 32.dp)
    ) {
        Column()
        {

            Text(
                text = "Модель автомобиля",
                style = MaterialTheme.typography.titleLarge,
                color = LambaInk
            )
            Text(
                text = "Цифровой двойник",
                style = MaterialTheme.typography.labelLarge,
                color = LambaInk
            )

        }


        Spacer(modifier = Modifier.height(8.dp))

        CarImage(
            bodyColor = LambaVehicleSilver,
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "2021 · 48 230 км",
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











