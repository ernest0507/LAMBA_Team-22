package screens.home

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.border


@Composable
fun TimelineExpensesAndEvents(
    onBackClick: () -> Unit = {}
) {
    val expenses = listOf(
        ExpenseItem("Замена масла", "12 мая 2026", "9 800 ₽", Color(0xFF243F7A)),
        ExpenseItem("Страховка", "10 марта 2026", "15 900 ₽", Color(0xFFA98D7A)),
        ExpenseItem("Шиномонтаж", "22 января 2026", "5 600 ₽", Color(0xFF5A13B8)),
        ExpenseItem("Техническое обслуживание", "5 января 2026", "26 500 ₽", Color(0xFF2E8B3C))
    )
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2EA))
            .padding(15.dp)
    ) {

        Header(onBackClick = onBackClick)

        Spacer(modifier = Modifier.height(16.dp))

        ExpensesMainInfo()

        Spacer(modifier = Modifier.height(24.dp))

        TimelineExpense(expenses)
    }

}


@Composable
fun Header(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Button(
            onClick = {
               onBackClick()
            },
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFEDE7DA)
            )

        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "Назад",
                tint = Color.Black
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        Text(
            text = "История расходов",
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp,
            color = Color.Black,
            modifier = Modifier.weight(1f)
        )

        PeriodDropdown()
    }
}


@Composable
fun PeriodDropdown() {
    var expanded by remember { mutableStateOf(false) }
    var selectedPeriod by remember { mutableStateOf("За все время") }

    val periods = listOf(
        "За все время",
        "Год",
        "Месяц",
        "Неделя"
    )

    Box {
        Button(
            onClick = { expanded = true },
            modifier = Modifier
                .height(48.dp)
                .width(120.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFFA98D7A)
            ),
            contentPadding = PaddingValues(
                start = 14.dp,
                end = 10.dp
            )
        ) {
            Text(selectedPeriod)
            Spacer(modifier = Modifier.width(4.dp))
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            periods.forEach { period ->
                DropdownMenuItem(
                    text = { Text(period) },
                    onClick = {
                        selectedPeriod = period
                        expanded = false
                    }
                )
            }
        }

    }
}

@Composable
fun ExpensesMainInfo() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(160.dp)
            .background(
                Color(0xFF243F7A),
                shape = RoundedCornerShape(18.dp)
            )
    ) {
        Column {
            Text(
                text = "Всего потрачено",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 32.dp, top = 32.dp)
            )


            Text(
                text = "70 000 ₽",
                fontSize = 36.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(start = 32.dp, top = 8.dp)
            )

            Text(
                text = "Отслежено 5 расходов",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White.copy(alpha = 0.7f),
                modifier = Modifier.padding(start = 32.dp, top = 16.dp)
            )

        }

    }
}


data class ExpenseItem(
    val title: String,
    val date: String,
    val amount: String,
    val color: Color
)

@Composable
fun TimelineExpense(expenses: List<ExpenseItem>) {
    Column(modifier = Modifier.fillMaxWidth()) {
        expenses.forEachIndexed { index, item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(IntrinsicSize.Min)
            ) {
                Column(
                    modifier = Modifier.width(44.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimelineDot(color = item.color)
                    
                    if (index != expenses.lastIndex) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .weight(1f)
                                .background(Color(0xFFD8D3BF))
                        )
                    }
                }

                Spacer(modifier = Modifier.width(16.dp))

                Column(modifier = Modifier.weight(1f)) {
                    ExpanseCard(item)
                    if (index != expenses.lastIndex) {
                        Spacer(modifier = Modifier.height(20.dp))
                    }
                }
            }
        }
    }
}


@Composable
fun TimelineDot(color: Color) {
    Box(
        modifier = Modifier.size(44.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .border(width = 3.dp, color = color, shape = RoundedCornerShape(50))
        )
        Box(
            modifier = Modifier
                .size(12.dp)
                .background(color = color, shape = RoundedCornerShape(50))
        )
    }
}



@Composable
fun ExpanseCard(item: ExpenseItem) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                Color(0xFFD8D3BF),
                shape = RoundedCornerShape(24.dp)
            )
            .padding(18.dp)
    ) {
        Text(
            text = item.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
        )

        Text(
            text = item.date,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = Color.DarkGray
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = item.amount,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = item.color
        )
    }
}
