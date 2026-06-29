package com.lamba.app.screens.home

import com.lamba.app.data.cars.CarResponse

internal fun CarResponse.displayName(): String {
    return listOfNotNull(
        make?.takeIf { it.isNotBlank() },
        model.takeIf { it.isNotBlank() }
    ).joinToString(separator = " ")
}

internal fun Int.formatMileage(): String {
    return toString()
        .reversed()
        .chunked(3)
        .joinToString(separator = " ")
        .reversed()
}
