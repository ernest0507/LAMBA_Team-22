package components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lamba.app.R

@Composable
fun CarImage(
    modifier: Modifier = Modifier,
    bodyColor: Color = Color(0xFFB5C0BD),
    bodyType: String? = null,
    colorKey: String? = null
) {
    Image(
        painter = painterResource(id = toCarDrawableRes(bodyType, colorKey)),
        contentDescription = bodyType,
        modifier = modifier
            .fillMaxWidth()
            .height(160.dp),
        contentScale = ContentScale.Fit
    )
}

@DrawableRes
private fun toCarDrawableRes(bodyType: String?, colorKey: String?): Int {
    return when (bodyType.toBodyKey()) {
        "hatchback" -> when (colorKey.toColorKey()) {
            "blue" -> R.drawable.car_hatchback_blue
            "green" -> R.drawable.car_hatchback_green
            "red" -> R.drawable.car_hatchback_red
            "graphite" -> R.drawable.car_hatchback_graphite
            else -> R.drawable.car_hatchback_silver
        }
        "crossover" -> when (colorKey.toColorKey()) {
            "blue" -> R.drawable.car_crossover_blue
            "green" -> R.drawable.car_crossover_green
            "red" -> R.drawable.car_crossover_red
            "graphite" -> R.drawable.car_crossover_graphite
            else -> R.drawable.car_crossover_silver
        }
        "coupe" -> when (colorKey.toColorKey()) {
            "blue" -> R.drawable.car_coupe_blue
            "green" -> R.drawable.car_coupe_green
            "red" -> R.drawable.car_coupe_red
            "graphite" -> R.drawable.car_coupe_graphite
            else -> R.drawable.car_coupe_silver
        }
        "wagon" -> when (colorKey.toColorKey()) {
            "blue" -> R.drawable.car_wagon_blue
            "green" -> R.drawable.car_wagon_green
            "red" -> R.drawable.car_wagon_red
            "graphite" -> R.drawable.car_wagon_graphite
            else -> R.drawable.car_wagon_silver
        }
        "pickup" -> when (colorKey.toColorKey()) {
            "blue" -> R.drawable.car_pickup_blue
            "green" -> R.drawable.car_pickup_green
            "red" -> R.drawable.car_pickup_red
            "graphite" -> R.drawable.car_pickup_graphite
            else -> R.drawable.car_pickup_silver
        }
        "cabriolet" -> when (colorKey.toColorKey()) {
            "blue" -> R.drawable.car_cabriolet_blue
            "green" -> R.drawable.car_cabriolet_green
            "red" -> R.drawable.car_cabriolet_red
            "graphite" -> R.drawable.car_cabriolet_graphite
            else -> R.drawable.car_cabriolet_silver
        }
        else -> when (colorKey.toColorKey()) {
            "blue" -> R.drawable.car_sedan_blue
            "green" -> R.drawable.car_sedan_green
            "red" -> R.drawable.car_sedan_red
            "graphite" -> R.drawable.car_sedan_graphite
            else -> R.drawable.car_sedan_silver
        }
    }
}

private fun String?.toBodyKey(): String {
    return when (this?.trim()?.lowercase()) {
        "sedan",
        "\u0441\u0435\u0434\u0430\u043d",
        "c\u0435\u0434\u0430\u043d",
        "\u0441\u0451\u0434\u0430\u043d",
        "\u0441\u0435\u0434\u0430\u043d",
        "\u0421\u0435\u0434\u0430\u043d".lowercase(),
        "СЃРµРґР°РЅ",
        "РЎРµРґР°РЅ".lowercase() -> "sedan"

        "hatchback",
        "\u0445\u044d\u0442\u0447\u0431\u0435\u043a",
        "\u0445\u0435\u0442\u0447\u0431\u0435\u043a",
        "\u0425\u044d\u0442\u0447\u0431\u0435\u043a".lowercase(),
        "С…СЌС‚С‡Р±РµРє",
        "РҐСЌС‚С‡Р±РµРє".lowercase() -> "hatchback"

        "crossover",
        "\u043a\u0440\u043e\u0441\u0441\u043e\u0432\u0435\u0440",
        "\u041a\u0440\u043e\u0441\u0441\u043e\u0432\u0435\u0440".lowercase(),
        "РєСЂРѕСЃСЃРѕРІРµСЂ",
        "РљСЂРѕСЃСЃРѕРІРµСЂ".lowercase() -> "crossover"

        "coupe",
        "\u043a\u0443\u043f\u0435",
        "\u041a\u0443\u043f\u0435".lowercase(),
        "РєСѓРїРµ",
        "РљСѓРїРµ".lowercase() -> "coupe"

        "wagon",
        "\u0443\u043d\u0438\u0432\u0435\u0440\u0441\u0430\u043b",
        "\u0423\u043d\u0438\u0432\u0435\u0440\u0441\u0430\u043b".lowercase(),
        "СѓРЅРёРІРµСЂСЃР°Р»",
        "РЈРЅРёРІРµСЂСЃР°Р»".lowercase() -> "wagon"

        "pickup",
        "\u043f\u0438\u043a\u0430\u043f",
        "\u041f\u0438\u043a\u0430\u043f".lowercase(),
        "РїРёРєР°Рї",
        "РџРёРєР°Рї".lowercase() -> "pickup"

        "cabriolet",
        "\u043a\u0430\u0431\u0440\u0438\u043e\u043b\u0435\u0442",
        "\u041a\u0430\u0431\u0440\u0438\u043e\u043b\u0435\u0442".lowercase(),
        "РєР°Р±СЂРёРѕР»РµС‚",
        "РљР°Р±СЂРёРѕР»РµС‚".lowercase() -> "cabriolet"

        else -> "sedan"
    }
}

private fun String?.toColorKey(): String {
    return when (this?.trim()?.lowercase()) {
        "blue",
        "\u0441\u0438\u043d\u0438\u0439",
        "\u0421\u0438\u043d\u0438\u0439".lowercase(),
        "СЃРёРЅРёР№",
        "РЎРёРЅРёР№".lowercase() -> "blue"

        "green",
        "\u0437\u0435\u043b\u0435\u043d\u044b\u0439",
        "\u0437\u0435\u043b\u0451\u043d\u044b\u0439",
        "\u0417\u0435\u043b\u0435\u043d\u044b\u0439".lowercase(),
        "\u0417\u0435\u043b\u0451\u043d\u044b\u0439".lowercase(),
        "Р·РµР»РµРЅС‹Р№",
        "Р—РµР»РµРЅС‹Р№".lowercase() -> "green"

        "red",
        "\u043a\u0440\u0430\u0441\u043d\u044b\u0439",
        "\u041a\u0440\u0430\u0441\u043d\u044b\u0439".lowercase(),
        "РєСЂР°СЃРЅС‹Р№",
        "РљСЂР°СЃРЅС‹Р№".lowercase() -> "red"

        "graphite",
        "black",
        "\u0433\u0440\u0430\u0444\u0438\u0442",
        "\u0447\u0435\u0440\u043d\u044b\u0439",
        "\u0447\u0451\u0440\u043d\u044b\u0439",
        "\u0413\u0440\u0430\u0444\u0438\u0442".lowercase(),
        "\u0427\u0435\u0440\u043d\u044b\u0439".lowercase(),
        "\u0427\u0451\u0440\u043d\u044b\u0439".lowercase(),
        "РіСЂР°С„РёС‚",
        "С‡РµСЂРЅС‹Р№",
        "С‡С‘СЂРЅС‹Р№" -> "graphite"

        else -> "silver"
    }
}
