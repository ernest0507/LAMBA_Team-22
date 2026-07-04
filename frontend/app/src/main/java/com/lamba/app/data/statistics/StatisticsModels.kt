package com.lamba.app.data.statistics

import com.google.gson.annotations.SerializedName

data class CarStatisticsResponse(
    val month: List<StatisticsPeriodResponse> = emptyList(),
    @SerializedName("half_year")
    val halfYear: List<StatisticsPeriodResponse> = emptyList(),
    val year: List<StatisticsPeriodResponse> = emptyList()
)

data class StatisticsPeriodResponse(
    @SerializedName("period_key")
    val periodKey: String,
    @SerializedName("period_title")
    val periodTitle: String,
    val metrics: List<StatisticsMetricResponse> = emptyList(),
    val dynamics: List<StatisticsChartPointResponse> = emptyList(),
    @SerializedName("dynamics_style")
    val dynamicsStyle: String,
    val categories: List<StatisticsCategoryResponse> = emptyList(),
    @SerializedName("total_amount")
    val totalAmount: String,
    @SerializedName("total_label")
    val totalLabel: String
)

data class StatisticsMetricResponse(
    val title: String,
    val value: String,
    val delta: String,
    val type: String
)

data class StatisticsChartPointResponse(
    val label: String,
    val value: Int
)

data class StatisticsCategoryResponse(
    val title: String,
    val percent: Int,
    val amount: String,
    val key: String
)
