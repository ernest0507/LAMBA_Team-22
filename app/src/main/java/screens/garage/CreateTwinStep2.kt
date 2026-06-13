package screens.garage

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import screens.garage.model.CarProfile

private val Background = Color(0xFFF5EFE6)
private val DarkBlue = Color(0xFF233B78)
private val Beige = Color(0xFFEFE7D8)
private val TextDark = Color(0xFF2A2522)

data class CarColorOption(val name: String, val color: Color)

private val carColors = listOf(
    CarColorOption("Blue", Color(0xFF2196F3)),
    CarColorOption("Red", Color(0xFFF44336)),
    CarColorOption("Green", Color(0xFF4CAF50)),
    CarColorOption("Black", Color(0xFF212121)),
    CarColorOption("Purple", Color(0xFF9C27B0))
)

private val bodyTypes = listOf(
    "Седан", "Хэтчбек", "Кроссовер", "Купе", "Универсал", "Пикап"
)

@Composable
fun CreateTwinStep2(
    initialData: CarProfile,
    onBack: () -> Unit,
    onCreateTwin: (CarProfile) -> Unit
) {
    var selectedColorName by remember { mutableStateOf(carColors.first().name) }
    var selectedBodyType by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(start = 20.dp, end = 20.dp, top = 36.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Beige)
                .clickable(onClick = onBack),
            contentAlignment = Alignment.Center
        ) {
            Text("←", fontSize = 20.sp, color = TextDark, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Настройте автомобиль",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(DarkBlue)
            )
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(DarkBlue)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
                .clip(RoundedCornerShape(26.dp))
                .background(Beige),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Car preview placeholder",
                fontSize = 14.sp,
                color = TextDark.copy(alpha = 0.5f),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Цвет автомобиля",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDark,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(10.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            carColors.forEach { option ->
                val isSelected = selectedColorName == option.name
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(CircleShape)
                        .background(option.color)
                        .then(
                            if (isSelected) Modifier.border(3.dp, DarkBlue, CircleShape)
                            else Modifier
                        )
                        .clickable { selectedColorName = option.name }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Тип кузова",
            fontSize = 18.sp,
            fontWeight = FontWeight.SemiBold,
            color = TextDark,
            modifier = Modifier.align(Alignment.Start)
        )

        Spacer(modifier = Modifier.height(10.dp))

        /* Body type grid: 3 columns x 2 rows, equal-sized buttons */
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            bodyTypes.chunked(3).forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowItems.forEach { type ->
                        val isSelected = selectedBodyType == type
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(if (isSelected) DarkBlue else Beige)
                                .clickable {
                                    selectedBodyType = if (isSelected) "" else type
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = type,
                                fontSize = 15.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isSelected) Color.White else TextDark
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                onCreateTwin(
                    initialData.copy(
                        color = selectedColorName,
                        bodyType = selectedBodyType
                    )
                )
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp),
            shape = RoundedCornerShape(18.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
        ) {
            Text("Создать двойник", fontSize = 18.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(8.dp))
    }
}
