package com.lamba.app.screens.statistics

import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalanceWallet
import androidx.compose.material.icons.outlined.LocalGasStation
import androidx.compose.material.icons.outlined.Speed
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.Icon
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.graphics.vector.ImageVector
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import com.lamba.app.ui.theme.LambaAccent
import com.lamba.app.ui.theme.LambaAccentSoft
import com.lamba.app.ui.theme.LambaAccentStrong
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSignal
import com.lamba.app.ui.theme.LambaSpacing
import com.lamba.app.ui.theme.LambaSurface
import com.lamba.app.ui.theme.LambaSurfaceSoft
import components.BackButton
import kotlin.math.max

enum class StatisticsPeriod(
    val title: String
) {
    MONTH("Месяц"),
    HALF_YEAR("6 месяцев"),
    YEAR("Год")
}

private enum class DynamicsChartStyle {
    BAR,
    LINE
}

private enum class MetricIconType {
    EXPENSES,
    MILEAGE,
    FUEL
}

private data class MetricData(
    val title: String,
    val value: String,
    val delta: String,
    val iconType: MetricIconType
)

private data class ChartPoint(
    val label: String,
    val value: Int
)

private data class CategoryData(
    val title: String,
    val percent: Int,
    val amount: String,
    val color: Color
)

private data class StatisticsUiState(
    val periodTitle: String,
    val metrics: List<MetricData>,
    val dynamics: List<ChartPoint>,
    val dynamicsStyle: DynamicsChartStyle,
    val categories: List<CategoryData>,
    val donutCenterValue: String,
    val donutCenterLabel: String
)

private val monthlyStatistics = listOf(
    StatisticsUiState(
        periodTitle = "Май 2025",
        metrics = listOf(
            MetricData("Расходы", "26 200 ₽", "+7% к апрелю", MetricIconType.EXPENSES),
            MetricData("Пробег", "1 338 км", "+4% к апрелю", MetricIconType.MILEAGE),
            MetricData("Бензин", "128 л", "+5% к апрелю", MetricIconType.FUEL)
        ),
        dynamics = listOf(
            ChartPoint("Нед 1", 10),
            ChartPoint("Нед 2", 12),
            ChartPoint("Нед 3", 11),
            ChartPoint("Нед 4", 26)
        ),
        dynamicsStyle = DynamicsChartStyle.BAR,
        categories = listOf(
            CategoryData("Бензин", 54, "14 100 ₽", LambaAccentStrong),
            CategoryData("Обслуживание", 23, "6 000 ₽", LambaAccent),
            CategoryData("Уход", 14, "3 700 ₽", LambaSignal),
            CategoryData("Прочее", 9, "2 400 ₽", LambaAccent.copy(alpha = 0.45f))
        ),
        donutCenterValue = "26 200 ₽",
        donutCenterLabel = "Май"
    ),
    StatisticsUiState(
        periodTitle = "Июнь 2025",
        metrics = listOf(
            MetricData("Расходы", "23 000 ₽", "-12% к маю", MetricIconType.EXPENSES),
            MetricData("Пробег", "1 420 км", "+6% к маю", MetricIconType.MILEAGE),
            MetricData("Бензин", "118 л", "-8% к маю", MetricIconType.FUEL)
        ),
        dynamics = listOf(
            ChartPoint("Нед 1", 11),
            ChartPoint("Нед 2", 13),
            ChartPoint("Нед 3", 9),
            ChartPoint("Нед 4", 23)
        ),
        dynamicsStyle = DynamicsChartStyle.BAR,
        categories = listOf(
            CategoryData("Бензин", 52, "12 000 ₽", LambaAccentStrong),
            CategoryData("Обслуживание", 25, "5 800 ₽", LambaAccent),
            CategoryData("Уход", 15, "3 400 ₽", LambaSignal),
            CategoryData("Прочее", 8, "1 800 ₽", LambaAccent.copy(alpha = 0.45f))
        ),
        donutCenterValue = "23 000 ₽",
        donutCenterLabel = "Июнь"
    ),
    StatisticsUiState(
        periodTitle = "Июль 2025",
        metrics = listOf(
            MetricData("Расходы", "24 800 ₽", "+8% к июню", MetricIconType.EXPENSES),
            MetricData("Пробег", "1 510 км", "+6% к июню", MetricIconType.MILEAGE),
            MetricData("Бензин", "123 л", "+4% к июню", MetricIconType.FUEL)
        ),
        dynamics = listOf(
            ChartPoint("Нед 1", 12),
            ChartPoint("Нед 2", 11),
            ChartPoint("Нед 3", 14),
            ChartPoint("Нед 4", 25)
        ),
        dynamicsStyle = DynamicsChartStyle.BAR,
        categories = listOf(
            CategoryData("Бензин", 51, "12 700 ₽", LambaAccentStrong),
            CategoryData("Обслуживание", 24, "5 900 ₽", LambaAccent),
            CategoryData("Уход", 16, "3 900 ₽", LambaSignal),
            CategoryData("Прочее", 9, "2 300 ₽", LambaAccent.copy(alpha = 0.45f))
        ),
        donutCenterValue = "24 800 ₽",
        donutCenterLabel = "Июль"
    )
)

private val halfYearStatistics = listOf(
    StatisticsUiState(
        periodTitle = "Янв – Июн 2025",
        metrics = listOf(
            MetricData("Расходы", "118 000 ₽", "-9% к пред. 6 мес.", MetricIconType.EXPENSES),
            MetricData("Пробег", "7 850 км", "+5% к пред. 6 мес.", MetricIconType.MILEAGE),
            MetricData("Бензин", "620 л", "-7% к пред. 6 мес.", MetricIconType.FUEL)
        ),
        dynamics = listOf(
            ChartPoint("Янв", 18),
            ChartPoint("Фев", 24),
            ChartPoint("Мар", 20),
            ChartPoint("Апр", 16),
            ChartPoint("Май", 22),
            ChartPoint("Июн", 23)
        ),
        dynamicsStyle = DynamicsChartStyle.BAR,
        categories = listOf(
            CategoryData("Бензин", 50, "59 000 ₽", LambaAccentStrong),
            CategoryData("Обслуживание", 27, "31 860 ₽", LambaAccent),
            CategoryData("Уход", 14, "16 520 ₽", LambaSignal),
            CategoryData("Прочее", 9, "10 620 ₽", LambaAccent.copy(alpha = 0.45f))
        ),
        donutCenterValue = "118 000 ₽",
        donutCenterLabel = "6 мес."
    ),
    StatisticsUiState(
        periodTitle = "Июл – Дек 2025",
        metrics = listOf(
            MetricData("Расходы", "126 500 ₽", "+7% к янв. – июн.", MetricIconType.EXPENSES),
            MetricData("Пробег", "8 210 км", "+5% к янв. – июн.", MetricIconType.MILEAGE),
            MetricData("Бензин", "648 л", "+5% к янв. – июн.", MetricIconType.FUEL)
        ),
        dynamics = listOf(
            ChartPoint("Июл", 19),
            ChartPoint("Авг", 21),
            ChartPoint("Сен", 20),
            ChartPoint("Окт", 22),
            ChartPoint("Ноя", 21),
            ChartPoint("Дек", 24)
        ),
        dynamicsStyle = DynamicsChartStyle.BAR,
        categories = listOf(
            CategoryData("Бензин", 49, "61 900 ₽", LambaAccentStrong),
            CategoryData("Обслуживание", 28, "35 420 ₽", LambaAccent),
            CategoryData("Уход", 14, "17 480 ₽", LambaSignal),
            CategoryData("Прочее", 9, "11 700 ₽", LambaAccent.copy(alpha = 0.45f))
        ),
        donutCenterValue = "126 500 ₽",
        donutCenterLabel = "6 мес."
    )
)

private val yearlyStatistics = listOf(
    StatisticsUiState(
        periodTitle = "2023",
        metrics = listOf(
            MetricData("Расходы", "236 000 ₽", "+2% к 2022", MetricIconType.EXPENSES),
            MetricData("Пробег", "14 580 км", "+4% к 2022", MetricIconType.MILEAGE),
            MetricData("Бензин", "1 205 л", "+2% к 2022", MetricIconType.FUEL)
        ),
        dynamics = listOf(
            ChartPoint("Я", 15),
            ChartPoint("Ф", 16),
            ChartPoint("М", 17),
            ChartPoint("А", 15),
            ChartPoint("М", 18),
            ChartPoint("И", 21),
            ChartPoint("И", 17),
            ChartPoint("А", 16),
            ChartPoint("С", 18),
            ChartPoint("О", 20),
            ChartPoint("Н", 21),
            ChartPoint("Д", 22)
        ),
        dynamicsStyle = DynamicsChartStyle.LINE,
        categories = listOf(
            CategoryData("Бензин", 49, "115 640 ₽", LambaAccentStrong),
            CategoryData("Обслуживание", 25, "59 000 ₽", LambaAccent),
            CategoryData("Уход", 16, "37 760 ₽", LambaSignal),
            CategoryData("Прочее", 10, "23 600 ₽", LambaAccent.copy(alpha = 0.45f))
        ),
        donutCenterValue = "236 000 ₽",
        donutCenterLabel = "2023"
    ),
    StatisticsUiState(
        periodTitle = "2024",
        metrics = listOf(
            MetricData("Расходы", "248 000 ₽", "+5% к 2023", MetricIconType.EXPENSES),
            MetricData("Пробег", "15 620 км", "+7% к 2023", MetricIconType.MILEAGE),
            MetricData("Бензин", "1 240 л", "+3% к 2023", MetricIconType.FUEL)
        ),
        dynamics = listOf(
            ChartPoint("Я", 16),
            ChartPoint("Ф", 17),
            ChartPoint("М", 18),
            ChartPoint("А", 16),
            ChartPoint("М", 20),
            ChartPoint("И", 22),
            ChartPoint("И", 18),
            ChartPoint("А", 17),
            ChartPoint("С", 19),
            ChartPoint("О", 21),
            ChartPoint("Н", 23),
            ChartPoint("Д", 24)
        ),
        dynamicsStyle = DynamicsChartStyle.LINE,
        categories = listOf(
            CategoryData("Бензин", 48, "119 040 ₽", LambaAccentStrong),
            CategoryData("Обслуживание", 26, "63 840 ₽", LambaAccent),
            CategoryData("Уход", 16, "39 680 ₽", LambaSignal),
            CategoryData("Прочее", 10, "25 440 ₽", LambaAccent.copy(alpha = 0.45f))
        ),
        donutCenterValue = "248 000 ₽",
        donutCenterLabel = "2024"
    ),
    StatisticsUiState(
        periodTitle = "2025",
        metrics = listOf(
            MetricData("Расходы", "259 500 ₽", "+5% к 2024", MetricIconType.EXPENSES),
            MetricData("Пробег", "16 080 км", "+3% к 2024", MetricIconType.MILEAGE),
            MetricData("Бензин", "1 260 л", "+2% к 2024", MetricIconType.FUEL)
        ),
        dynamics = listOf(
            ChartPoint("Я", 17),
            ChartPoint("Ф", 18),
            ChartPoint("М", 19),
            ChartPoint("А", 17),
            ChartPoint("М", 21),
            ChartPoint("И", 23),
            ChartPoint("И", 19),
            ChartPoint("А", 18),
            ChartPoint("С", 20),
            ChartPoint("О", 22),
            ChartPoint("Н", 24),
            ChartPoint("Д", 25)
        ),
        dynamicsStyle = DynamicsChartStyle.LINE,
        categories = listOf(
            CategoryData("Бензин", 47, "121 965 ₽", LambaAccentStrong),
            CategoryData("Обслуживание", 27, "70 065 ₽", LambaAccent),
            CategoryData("Уход", 16, "41 520 ₽", LambaSignal),
            CategoryData("Прочее", 10, "25 950 ₽", LambaAccent.copy(alpha = 0.45f))
        ),
        donutCenterValue = "259 500 ₽",
        donutCenterLabel = "2025"
    )
)

private val statisticsPreviewData = mapOf(
    StatisticsPeriod.MONTH to monthlyStatistics,
    StatisticsPeriod.HALF_YEAR to halfYearStatistics,
    StatisticsPeriod.YEAR to yearlyStatistics
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    onBackClick: () -> Unit = {}
) {
    var selectedPeriod by rememberSaveable { mutableStateOf(StatisticsPeriod.MONTH) }
    var selectedMonthIndex by rememberSaveable { mutableStateOf(1) }
    var selectedHalfYearIndex by rememberSaveable { mutableStateOf(0) }
    var selectedYearIndex by rememberSaveable { mutableStateOf(1) }

    val selectedIndex = when (selectedPeriod) {
        StatisticsPeriod.MONTH -> selectedMonthIndex
        StatisticsPeriod.HALF_YEAR -> selectedHalfYearIndex
        StatisticsPeriod.YEAR -> selectedYearIndex
    }

    val periodStates = remember(selectedPeriod) {
        statisticsPreviewData.getValue(selectedPeriod)
    }
    val state = periodStates[selectedIndex]

    fun movePeriod(delta: Int) {
        when (selectedPeriod) {
            StatisticsPeriod.MONTH -> {
                selectedMonthIndex = (selectedMonthIndex + delta)
                    .coerceIn(0, monthlyStatistics.lastIndex)
            }
            StatisticsPeriod.HALF_YEAR -> {
                selectedHalfYearIndex = (selectedHalfYearIndex + delta)
                    .coerceIn(0, halfYearStatistics.lastIndex)
            }
            StatisticsPeriod.YEAR -> {
                selectedYearIndex = (selectedYearIndex + delta)
                    .coerceIn(0, yearlyStatistics.lastIndex)
            }
        }
    }

    Scaffold(
        containerColor = LambaCanvas,
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = LambaCanvas,
                    titleContentColor = LambaInk,
                    navigationIconContentColor = LambaInk
                ),
                title = {
                    Text(
                        text = "Статистика",
                        style = MaterialTheme.typography.titleLarge,
                        color = LambaInk,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                navigationIcon = {
                    BackButton(onClick = onBackClick)
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .pointerInput(selectedPeriod, selectedIndex) {
                    var totalHorizontalDrag = 0f
                    detectHorizontalDragGestures(
                        onHorizontalDrag = { change, dragAmount ->
                            totalHorizontalDrag += dragAmount
                            change.consume()
                        },
                        onDragEnd = {
                            when {
                                totalHorizontalDrag <= -48f -> movePeriod(1)
                                totalHorizontalDrag >= 48f -> movePeriod(-1)
                            }
                            totalHorizontalDrag = 0f
                        },
                        onDragCancel = {
                            totalHorizontalDrag = 0f
                        }
                    )
                },
            contentPadding = PaddingValues(
                start = LambaSpacing.ScreenHorizontal,
                top = 12.dp,
                end = LambaSpacing.ScreenHorizontal,
                bottom = LambaSpacing.ScreenBottom + 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                PeriodTabs(
                    selectedPeriod = selectedPeriod,
                    onPeriodSelected = { selectedPeriod = it }
                )
            }

            item {
                PeriodSelectorRow(
                    periodTitle = state.periodTitle,
                    canGoPrevious = selectedIndex > 0,
                    canGoNext = selectedIndex < periodStates.lastIndex,
                    onPreviousClick = { movePeriod(-1) },
                    onNextClick = { movePeriod(1) }
                )
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    state.metrics.forEach { metric ->
                        MetricCard(
                            metric = metric,
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            item {
                ExpenseDynamicsCard(
                    points = state.dynamics,
                    chartStyle = state.dynamicsStyle
                )
            }

            item {
                CategoryBreakdownCard(
                    totalValue = state.donutCenterValue,
                    totalLabel = state.donutCenterLabel,
                    categories = state.categories
                )
            }
        }
    }
}

@Composable
private fun PeriodTabs(
    selectedPeriod: StatisticsPeriod,
    onPeriodSelected: (StatisticsPeriod) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        color = LambaSurfaceSoft,
        shape = RoundedCornerShape(LambaRadius.Pill)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            StatisticsPeriod.entries.forEach { period ->
                val selected = period == selectedPeriod

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(LambaRadius.Pill))
                        .background(if (selected) LambaAccent else Color.Transparent)
                        .clickable { onPeriodSelected(period) }
                        .padding(horizontal = 10.dp, vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = period.title,
                        style = MaterialTheme.typography.bodyMedium,
                        color = if (selected) Color.White else LambaInkMuted,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Composable
private fun PeriodSelectorRow(
    periodTitle: String,
    canGoPrevious: Boolean,
    canGoNext: Boolean,
    onPreviousClick: () -> Unit,
    onNextClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        PeriodChevronButton(
            chevron = "<",
            enabled = canGoPrevious,
            onClick = onPreviousClick
        )

        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = periodTitle,
                style = MaterialTheme.typography.headlineMedium,
                color = LambaInk,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }

        PeriodChevronButton(
            chevron = ">",
            enabled = canGoNext,
            onClick = onNextClick
        )
    }
}

@Composable
private fun MetricCard(
    metric: MetricData,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.heightIn(min = 132.dp),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = LambaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(34.dp)
                    .clip(RoundedCornerShape(LambaRadius.Medium))
                    .background(LambaAccentSoft),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = metric.iconType.toImageVector(),
                    contentDescription = metric.title,
                    tint = LambaAccentStrong,
                    modifier = Modifier.size(18.dp)
                )
            }

            Text(
                text = metric.title,
                style = MaterialTheme.typography.bodySmall,
                color = LambaInkMuted,
                maxLines = 2
            )

            Text(
                text = metric.value,
                style = MaterialTheme.typography.titleMedium,
                color = LambaInk,
                fontWeight = FontWeight.SemiBold,
                maxLines = 2
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(LambaRadius.Pill))
                    .background(LambaAccentSoft.copy(alpha = 0.8f))
                    .padding(horizontal = 8.dp, vertical = 6.dp)
            ) {
                Text(
                    text = metric.delta,
                    style = MaterialTheme.typography.bodySmall,
                    color = LambaAccentStrong,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun ExpenseDynamicsCard(
    points: List<ChartPoint>,
    chartStyle: DynamicsChartStyle,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = LambaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LambaSpacing.CardPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Динамика расходов",
                style = MaterialTheme.typography.titleMedium,
                color = LambaInk,
                fontWeight = FontWeight.SemiBold
            )

            if (chartStyle == DynamicsChartStyle.LINE) {
                LineChart(points = points)
            } else {
                BarChart(points = points)
            }
        }
    }
}

@Composable
private fun BarChart(
    points: List<ChartPoint>,
    modifier: Modifier = Modifier
) {
    val maxValue = points.maxOfOrNull { it.value }?.toFloat() ?: 1f

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(168.dp),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.Bottom
    ) {
        points.forEachIndexed { index, point ->
            val isSelected = index == points.lastIndex
            val fillFraction = max(point.value / maxValue, 0.16f)

            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = "${point.value}k",
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) LambaAccentStrong else LambaInkMuted,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.72f)
                            .fillMaxHeight(fillFraction)
                            .clip(RoundedCornerShape(topStart = 14.dp, topEnd = 14.dp))
                            .background(
                                if (isSelected) {
                                    Brush.verticalGradient(
                                        colors = listOf(LambaAccent, LambaAccentStrong)
                                    )
                                } else {
                                    Brush.verticalGradient(
                                        colors = listOf(
                                            LambaAccent.copy(alpha = 0.22f),
                                            LambaAccent.copy(alpha = 0.45f)
                                        )
                                    )
                                }
                            )
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = point.label,
                    style = MaterialTheme.typography.bodySmall,
                    color = if (isSelected) LambaInk else LambaInkMuted,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
private fun LineChart(
    points: List<ChartPoint>,
    modifier: Modifier = Modifier
) {
    val maxValue = points.maxOfOrNull { it.value }?.toFloat() ?: 1f
    val minValue = points.minOfOrNull { it.value }?.toFloat() ?: 0f
    var selectedPointIndex by remember(points) { mutableStateOf(points.lastIndex) }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .pointerInput(points) {
                    detectTapGestures { tapOffset ->
                        val pointOffsets = calculateLineChartOffsets(
                            width = size.width.toFloat(),
                            height = size.height.toFloat(),
                            points = points,
                            minValue = minValue,
                            maxValue = maxValue,
                            horizontalPadding = 10.dp.toPx(),
                            topPadding = 12.dp.toPx(),
                            bottomPadding = 16.dp.toPx()
                        )
                        val nearestIndex = pointOffsets.indices.minByOrNull { index ->
                            val dx = pointOffsets[index].x - tapOffset.x
                            val dy = pointOffsets[index].y - tapOffset.y
                            dx * dx + dy * dy
                        } ?: return@detectTapGestures

                        selectedPointIndex = nearestIndex
                    }
                }
        ) {
            val topPadding = 12.dp.toPx()
            val bottomPadding = 16.dp.toPx()
            val pointOffsets = calculateLineChartOffsets(
                width = size.width,
                height = size.height,
                points = points,
                minValue = minValue,
                maxValue = maxValue,
                horizontalPadding = 10.dp.toPx(),
                topPadding = topPadding,
                bottomPadding = bottomPadding
            )

            repeat(3) { index ->
                val y = topPadding + ((size.height - topPadding - bottomPadding) / 2f) * index
                drawLine(
                    color = LambaAccent.copy(alpha = 0.12f),
                    start = Offset(10.dp.toPx(), y),
                    end = Offset(size.width - 10.dp.toPx(), y),
                    strokeWidth = 1.dp.toPx()
                )
            }

            val linePath = Path().apply {
                pointOffsets.forEachIndexed { index, offset ->
                    if (index == 0) moveTo(offset.x, offset.y) else lineTo(offset.x, offset.y)
                }
            }

            val fillPath = Path().apply {
                addPath(linePath)
                lineTo(pointOffsets.last().x, size.height - bottomPadding)
                lineTo(pointOffsets.first().x, size.height - bottomPadding)
                close()
            }

            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(LambaAccent.copy(alpha = 0.20f), Color.Transparent),
                    startY = topPadding,
                    endY = size.height
                )
            )

            drawPath(
                path = linePath,
                color = LambaAccentStrong,
                style = Stroke(
                    width = 4.dp.toPx(),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            pointOffsets.forEachIndexed { index, offset ->
                val isSelected = index == selectedPointIndex

                if (isSelected) {
                    drawCircle(
                        color = LambaAccent.copy(alpha = 0.18f),
                        radius = 11.dp.toPx(),
                        center = offset
                    )
                }

                drawCircle(
                    color = if (isSelected) LambaAccentStrong else LambaAccent,
                    radius = if (isSelected) 6.dp.toPx() else 4.dp.toPx(),
                    center = offset
                )

                drawCircle(
                    color = LambaSurface,
                    radius = if (isSelected) 3.dp.toPx() else 2.dp.toPx(),
                    center = offset
                )
            }

            if (selectedPointIndex in pointOffsets.indices) {
                val selectedOffset = pointOffsets[selectedPointIndex]
                val selectedValue = "${points[selectedPointIndex].value}k"
                val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    color = LambaAccentStrong.toArgb()
                    textAlign = Paint.Align.CENTER
                    textSize = 12.sp.toPx()
                    typeface = android.graphics.Typeface.create(
                        android.graphics.Typeface.DEFAULT,
                        android.graphics.Typeface.BOLD
                    )
                }

                drawContext.canvas.nativeCanvas.drawText(
                    selectedValue,
                    selectedOffset.x,
                    selectedOffset.y - 14.dp.toPx(),
                    textPaint
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            points.forEachIndexed { index, point ->
                val isSelected = index == points.lastIndex

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = point.label,
                        style = MaterialTheme.typography.bodySmall,
                        color = if (isSelected) LambaAccentStrong else LambaInkMuted,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal
                    )
                }
            }
        }
    }
}

@Composable
private fun CategoryBreakdownCard(
    totalValue: String,
    totalLabel: String,
    categories: List<CategoryData>,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = LambaSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LambaSpacing.CardPadding),
            verticalArrangement = Arrangement.spacedBy(18.dp)
        ) {
            Text(
                text = "Категории расходов",
                style = MaterialTheme.typography.titleMedium,
                color = LambaInk,
                fontWeight = FontWeight.SemiBold
            )

            BoxWithConstraints(
                modifier = Modifier.fillMaxWidth()
            ) {
                val stackContent = maxWidth < 340.dp

                if (stackContent) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(18.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        DonutChart(
                            totalValue = totalValue,
                            totalLabel = totalLabel,
                            categories = categories
                        )

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            categories.forEach { category ->
                                CategoryLegendRow(category = category)
                            }
                        }
                    }
                } else {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        DonutChart(
                            totalValue = totalValue,
                            totalLabel = totalLabel,
                            categories = categories
                        )

                        Column(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            categories.forEach { category ->
                                CategoryLegendRow(category = category)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DonutChart(
    totalValue: String,
    totalLabel: String,
    categories: List<CategoryData>,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.size(126.dp),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier.fillMaxSize()
        ) {
            val strokeWidth = 18.dp.toPx()
            val gapDegrees = 4f
            val diameter = size.minDimension
            val arcSize = Size(diameter, diameter)
            val topLeft = Offset(
                x = (size.width - diameter) / 2f,
                y = (size.height - diameter) / 2f
            )

            var startAngle = -90f

            categories.forEach { category ->
                val sweepAngle = (category.percent / 100f) * 360f - gapDegrees

                drawArc(
                    color = category.color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = false,
                    topLeft = topLeft,
                    size = arcSize,
                    style = Stroke(width = strokeWidth, cap = StrokeCap.Round)
                )

                startAngle += sweepAngle + gapDegrees
            }
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = totalValue,
                style = MaterialTheme.typography.bodyMedium,
                color = LambaInk,
                fontWeight = FontWeight.SemiBold,
                textAlign = TextAlign.Center
            )
            Text(
                text = totalLabel,
                style = MaterialTheme.typography.bodySmall,
                color = LambaInkMuted
            )
        }
    }
}

@Composable
private fun CategoryLegendRow(
    category: CategoryData
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Box(
            modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .background(category.color)
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = category.title,
                    style = MaterialTheme.typography.bodyMedium,
                    color = LambaInk,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "${category.percent}%",
                    style = MaterialTheme.typography.bodySmall,
                    color = LambaAccentStrong,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Text(
                text = category.amount,
                style = MaterialTheme.typography.bodySmall,
                color = LambaInkMuted
            )
        }
    }
}

@Composable
private fun PeriodChevronButton(
    chevron: String,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(LambaRadius.Pill),
        color = if (enabled) LambaSurface else LambaSurfaceSoft,
        tonalElevation = 0.dp,
        shadowElevation = 0.dp
    ) {
        IconButton(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.size(42.dp)
        ) {
            Text(
                text = chevron,
                style = MaterialTheme.typography.titleMedium,
                color = if (enabled) LambaAccentStrong else LambaInkMuted
            )
        }
    }
}

private fun MetricIconType.toImageVector(): ImageVector {
    return when (this) {
        MetricIconType.EXPENSES -> Icons.Outlined.AccountBalanceWallet
        MetricIconType.MILEAGE -> Icons.Outlined.Speed
        MetricIconType.FUEL -> Icons.Outlined.LocalGasStation
    }
}

private fun calculateLineChartOffsets(
    width: Float,
    height: Float,
    points: List<ChartPoint>,
    minValue: Float,
    maxValue: Float,
    horizontalPadding: Float,
    topPadding: Float,
    bottomPadding: Float
): List<Offset> {
    val usableHeight = height - topPadding - bottomPadding
    val valueRange = max(maxValue - minValue, 1f)

    return points.mapIndexed { index, point ->
        val x = if (points.size == 1) {
            width / 2f
        } else {
            horizontalPadding + index * ((width - horizontalPadding * 2) / points.lastIndex.toFloat())
        }
        val normalized = (point.value - minValue) / valueRange
        val y = height - bottomPadding - normalized * usableHeight
        Offset(x, y)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEF4F2)
@Composable
private fun StatisticsScreenPreview() {
    LAMBA_MVPv0Theme {
        Surface(color = LambaCanvas) {
            StatisticsScreen()
        }
    }
}
