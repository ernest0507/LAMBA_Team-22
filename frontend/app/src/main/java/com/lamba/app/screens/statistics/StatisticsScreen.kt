package com.lamba.app.screens.statistics

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme

private val StatisticsScreenBackground = Color(0xFFF2F7F6)
private val StatisticsCardBackground = Color(0xFFFFFFFF)
private val StatisticsPrimaryText = Color(0xFF172225)
private val StatisticsSecondaryText = Color(0xFF6D7A7E)
private val StatisticsAccent = Color(0xFF1EAAA8)
private val StatisticsAccentDark = Color(0xFF118B89)
private val StatisticsAccentLight = Color(0xFFDDF4F2)
private val StatisticsGreenAccent = Color(0xFF33A06F)
private val StatisticsGreenSurface = Color(0xFFE7F7EE)
private val StatisticsBarMuted = Color(0xFFB9E1DF)
private val StatisticsCategoryRepair = Color(0xFF65C4BF)
private val StatisticsCategoryOther = Color(0xFF9EDDD8)

private data class MonthlyExpense(
    val month: String,
    val amount: Int
)

private data class CategoryExpense(
    val title: String,
    val amount: String,
    val color: Color
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onBackClick: () -> Unit = {}
) {
    val periods = listOf("Месяц", "6 месяцев", "Год")
    var selectedPeriod by remember { mutableStateOf(periods.first()) }

    val monthlyExpenses = listOf(
        MonthlyExpense("Янв", 11000),
        MonthlyExpense("Фев", 13500),
        MonthlyExpense("Мар", 9800),
        MonthlyExpense("Апр", 17400),
        MonthlyExpense("Май", 26100),
        MonthlyExpense("Июн", 23000)
    )

    val categoryExpenses = listOf(
        CategoryExpense("Бензин", "15 500 ₽", StatisticsAccent),
        CategoryExpense("Ремонт", "5 000 ₽", StatisticsCategoryRepair),
        CategoryExpense("Прочее", "2 500 ₽", StatisticsCategoryOther)
    )

    Scaffold(
        containerColor = StatisticsScreenBackground,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = StatisticsScreenBackground,
                    titleContentColor = StatisticsPrimaryText,
                    navigationIconContentColor = StatisticsPrimaryText
                ),
                title = {
                    Text(
                        text = "Статистика",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text(
                            text = "\u2039",
                            style = MaterialTheme.typography.headlineMedium,
                            color = StatisticsPrimaryText
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PeriodSelector(
                    periods = periods,
                    selectedPeriod = selectedPeriod,
                    onPeriodSelected = { selectedPeriod = it }
                )
            }

            item {
                SummaryCard()
            }

            item {
                MonthlyExpensesCard(monthlyExpenses = monthlyExpenses)
            }

            item {
                CategoryExpensesCard(categoryExpenses = categoryExpenses)
            }

            item {
                InsightCard(
                    text = "Новая идея: показываем прогноз бюджета на следующий месяц прямо здесь, чтобы статистика была не только архивом, но и подсказкой."
                )
            }
        }
    }
}

@Composable
private fun PeriodSelector(
    periods: List<String>,
    selectedPeriod: String,
    onPeriodSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        periods.forEach { period ->
            FilterChip(
                selected = period == selectedPeriod,
                onClick = { onPeriodSelected(period) },
                label = {
                    Text(
                        text = period,
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Medium
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    containerColor = StatisticsCardBackground,
                    labelColor = StatisticsSecondaryText,
                    selectedContainerColor = StatisticsAccent,
                    selectedLabelColor = Color.White
                ),
                border = FilterChipDefaults.filterChipBorder(
                    enabled = true,
                    selected = period == selectedPeriod,
                    borderColor = Color.Transparent,
                    selectedBorderColor = Color.Transparent
                )
            )
        }
    }
}

@Composable
private fun SummaryCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = StatisticsCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 22.dp)
        ) {
            Text(
                text = "За этот месяц вы потратили",
                style = MaterialTheme.typography.bodyLarge,
                color = StatisticsSecondaryText
            )
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "23 000 ₽",
                style = MaterialTheme.typography.headlineMedium,
                color = StatisticsPrimaryText,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "-12% по сравнению с маем",
                style = MaterialTheme.typography.bodyMedium,
                color = StatisticsAccentDark,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun MonthlyExpensesCard(
    monthlyExpenses: List<MonthlyExpense>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = StatisticsCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 22.dp)
        ) {
            Text(
                text = "Расходы по месяцам",
                style = MaterialTheme.typography.titleMedium,
                color = StatisticsPrimaryText,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(22.dp))
            MonthlyBarChart(monthlyExpenses = monthlyExpenses)
        }
    }
}

@Composable
private fun MonthlyBarChart(
    monthlyExpenses: List<MonthlyExpense>
) {
    val maxAmount = monthlyExpenses.maxOf { it.amount }.toFloat()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(190.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.Bottom
    ) {
        monthlyExpenses.forEachIndexed { index, item ->
            val isCurrentMonth = index == monthlyExpenses.lastIndex
            val barColor = if (isCurrentMonth) StatisticsAccent else StatisticsBarMuted
            val barHeight = (item.amount / maxAmount) * 110f + 26f

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "${item.amount / 1000}k",
                    style = MaterialTheme.typography.labelSmall,
                    color = StatisticsSecondaryText
                )
                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .width(28.dp)
                        .height(barHeight.dp)
                        .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp, bottomStart = 14.dp, bottomEnd = 14.dp))
                        .background(barColor)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = item.month,
                    style = MaterialTheme.typography.labelMedium,
                    color = if (isCurrentMonth) StatisticsPrimaryText else StatisticsSecondaryText,
                    fontWeight = if (isCurrentMonth) FontWeight.SemiBold else FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun CategoryExpensesCard(
    categoryExpenses: List<CategoryExpense>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = StatisticsCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 22.dp, vertical = 22.dp)
        ) {
            Text(
                text = "Расходы по категориям",
                style = MaterialTheme.typography.titleMedium,
                color = StatisticsPrimaryText,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(18.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CategoryDonutChart(
                    modifier = Modifier.size(150.dp)
                )

                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    categoryExpenses.forEach { item ->
                        CategoryLegendItem(item = item)
                    }
                }
            }
        }
    }
}

@Composable
private fun CategoryDonutChart(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidth = 26.dp.toPx()
            val diameter = size.minDimension
            val arcSize = Size(diameter, diameter)
            val topLeft = Offset(
                x = (size.width - diameter) / 2f,
                y = (size.height - diameter) / 2f
            )

            var startAngle = -90f
            val sweepAngles = listOf(243f, 78f, 39f)
            val colors = listOf(
                StatisticsAccent,
                StatisticsCategoryRepair,
                StatisticsCategoryOther
            )

            colors.forEachIndexed { index, color ->
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngles[index],
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )
                startAngle += sweepAngles[index] + 8f
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.offset(y = 2.dp)
        ) {
            Text(
                text = "23 000 ₽",
                style = MaterialTheme.typography.titleMedium,
                color = StatisticsPrimaryText,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Июнь",
                style = MaterialTheme.typography.bodySmall,
                color = StatisticsSecondaryText
            )
        }
    }
}

@Composable
private fun CategoryLegendItem(
    item: CategoryExpense
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(item.color)
        )

        Column {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyMedium,
                color = StatisticsPrimaryText,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = item.amount,
                style = MaterialTheme.typography.bodyMedium,
                color = StatisticsSecondaryText
            )
        }
    }
}

@Composable
private fun InsightCard(
    text: String
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = StatisticsCardBackground
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 20.dp, vertical = 18.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .width(6.dp)
                    .height(92.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(StatisticsGreenAccent)
            )

            Column {
                Text(
                    text = "LAMBA AI",
                    style = MaterialTheme.typography.labelLarge,
                    color = StatisticsGreenAccent,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .background(StatisticsGreenSurface)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyLarge,
                    color = StatisticsPrimaryText
                )
            }
        }
    }
}

//@Preview(showBackground = true, backgroundColor = 0xFFF2F7F6)
//@Composable
//private fun StatisticsScreenPreview() {
//    LAMBA_MVPv0Theme(
//        darkTheme = false,
//        dynamicColor = false
//    ) {
//        Surface(color = StatisticsScreenBackground) {
//            StatisticsScreen()
//        }
//    }
//}
