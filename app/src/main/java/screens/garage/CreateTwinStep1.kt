package screens.garage

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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import screens.garage.model.CarProfile

private val Background = Color(0xFFF5EFE6)
private val DarkBlue = Color(0xFF233B78)
private val Beige = Color(0xFFEFE7D8)
private val TextDark = Color(0xFF2A2522)

@Composable
fun CreateTwinStep1(
    initialData: CarProfile = CarProfile(),
    onBack: () -> Unit,
    onNext: (CarProfile) -> Unit
) {
    var model by remember { mutableStateOf(initialData.model) }
    var year by remember { mutableStateOf(initialData.year) }
    var mileage by remember { mutableStateOf(initialData.mileage) }
    var expenses by remember { mutableStateOf(initialData.expenses) }
    var notes by remember { mutableStateOf(initialData.notes) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
            .padding(start = 20.dp, end = 20.dp, top = 44.dp)
            .verticalScroll(rememberScrollState())
    ) {
        /* Back button */
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

        Spacer(modifier = Modifier.height(16.dp))

        /* Heading */
        Text(
            text = "Create digital twin",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = TextDark
        )

        Spacer(modifier = Modifier.height(16.dp))

        /* Progress bar — 1 of 2 segments filled */
        StepProgressBar(filledSegments = 1, totalSegments = 2)

        Spacer(modifier = Modifier.height(28.dp))

        /* Form fields */
        FormField(value = model, onValueChange = { model = it }, label = "Car Model")
        Spacer(modifier = Modifier.height(14.dp))
        FormField(value = year, onValueChange = { year = it }, label = "Year of manufacture")
        Spacer(modifier = Modifier.height(14.dp))
        FormField(value = mileage, onValueChange = { mileage = it }, label = "Mileage")
        Spacer(modifier = Modifier.height(14.dp))
        FormField(value = expenses, onValueChange = { expenses = it }, label = "Expenses")
        Spacer(modifier = Modifier.height(14.dp))
        FormField(value = notes, onValueChange = { notes = it }, label = "Notes (optional)", singleLine = false)

        Spacer(modifier = Modifier.weight(1f))

        /* Next step button */
        Button(
            onClick = {
                onNext(CarProfile(model, year, mileage, expenses, notes))
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(containerColor = DarkBlue)
        ) {
            Text("Next step", fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))
    }
}

@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = true
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, fontSize = 14.sp) },
        modifier = Modifier.fillMaxWidth(),
        singleLine = singleLine,
        shape = RoundedCornerShape(18.dp),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Beige,
            unfocusedContainerColor = Beige,
            focusedIndicatorColor = DarkBlue,
            unfocusedIndicatorColor = Color.Transparent
        )
    )
}

@Composable
private fun StepProgressBar(filledSegments: Int, totalSegments: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        for (i in 0 until totalSegments) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(if (i < filledSegments) DarkBlue else Beige)
            )
        }
    }
}
