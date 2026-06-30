package components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lamba.app.R

@Composable
fun CarImage(
    modifier: Modifier = Modifier,
    bodyColor: Color = Color(0xFFB5C0BD),
    bodyType: String? = null
) {
    Image(
        painter = painterResource(id = bodyType.toCarDrawableRes()),
        contentDescription = bodyType,
        modifier = modifier
            .fillMaxWidth()
            .height(132.dp),
        contentScale = ContentScale.Fit,
        colorFilter = ColorFilter.tint(
            color = bodyColor,
            blendMode = BlendMode.Modulate
        )
    )
}

@DrawableRes
private fun String?.toCarDrawableRes(): Int {
    return when (this?.trim()?.lowercase()) {
        "седан" -> R.drawable.car_body_sedan
        "хэтчбек" -> R.drawable.car_body_hatchback
        "кроссовер" -> R.drawable.car_body_crossover
        "купе" -> R.drawable.car_body_coupe
        "универсал" -> R.drawable.car_body_wagon
        "пикап" -> R.drawable.car_body_pickup
        else -> R.drawable.car_body_sedan
    }
}
