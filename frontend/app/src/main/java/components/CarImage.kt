package components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

private val CarStroke = Color(0xFF5F6C70)
private val CarWindow = Color(0xFF263A42)
private val WheelOuter = Color(0xFF263034)
private val WheelInner = Color(0xFFBEC8C9)
private val LightFront = Color(0xFFF6F7F5)
private val LightRear = Color(0xFFE8A33B)
private val Shadow = Color(0x1F243034)

@Composable
fun CarImage(
    modifier: Modifier = Modifier,
    bodyColor: Color = Color(0xFFB5C0BD)
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(132.dp)
    ) {
        val w = size.width
        val h = size.height
        val scale = minOf(w / 320f, h / 118f)
        val carW = 300f * scale
        val carH = 104f * scale
        val left = (w - carW) / 2f
        val top = (h - carH) / 2f + 4f * scale

        fun x(value: Float) = left + value * scale
        fun y(value: Float) = top + value * scale

        drawRoundRect(
            color = Shadow,
            topLeft = Offset(x(34f), y(104f)),
            size = androidx.compose.ui.geometry.Size(238f * scale, 8f * scale),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12f * scale, 12f * scale)
        )

        val body = Path().apply {
            moveTo(x(5f), y(79f))
            lineTo(x(20f), y(79f))
            cubicTo(x(25f), y(61f), x(43f), y(51f), x(69f), y(47f))
            lineTo(x(108f), y(41f))
            lineTo(x(140f), y(13f))
            lineTo(x(221f), y(13f))
            cubicTo(x(241f), y(13f), x(255f), y(25f), x(269f), y(42f))
            cubicTo(x(288f), y(49f), x(300f), y(64f), x(305f), y(81f))
            lineTo(x(296f), y(98f))
            lineTo(x(260f), y(98f))
            cubicTo(x(256f), y(76f), x(238f), y(62f), x(214f), y(62f))
            cubicTo(x(190f), y(62f), x(172f), y(76f), x(168f), y(98f))
            lineTo(x(98f), y(98f))
            cubicTo(x(94f), y(76f), x(76f), y(62f), x(52f), y(62f))
            cubicTo(x(29f), y(62f), x(12f), y(76f), x(8f), y(98f))
            lineTo(x(0f), y(98f))
            lineTo(x(0f), y(84f))
            cubicTo(x(0f), y(81f), x(2f), y(80f), x(5f), y(79f))
            close()
        }

        drawPath(body, bodyColor)
        drawPath(body, CarStroke, style = Stroke(width = 2.1f * scale))

        val frontWindow = Path().apply {
            moveTo(x(142f), y(18f))
            lineTo(x(184f), y(18f))
            lineTo(x(184f), y(43f))
            lineTo(x(115f), y(43f))
            close()
        }
        val rearWindow = Path().apply {
            moveTo(x(189f), y(18f))
            lineTo(x(218f), y(18f))
            cubicTo(x(232f), y(19f), x(241f), y(29f), x(251f), y(43f))
            lineTo(x(189f), y(43f))
            close()
        }

        drawPath(frontWindow, CarWindow)
        drawPath(rearWindow, CarWindow)
        drawLine(
            color = Color.White.copy(alpha = 0.34f),
            start = Offset(x(187f), y(18f)),
            end = Offset(x(187f), y(43f)),
            strokeWidth = 1.4f * scale
        )

        drawLine(
            color = CarStroke.copy(alpha = 0.4f),
            start = Offset(x(100f), y(98f)),
            end = Offset(x(166f), y(98f)),
            strokeWidth = 1.4f * scale
        )

        drawRoundRect(
            color = LightFront,
            topLeft = Offset(x(20f), y(63f)),
            size = androidx.compose.ui.geometry.Size(34f * scale, 6f * scale),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f * scale, 3f * scale)
        )
        drawRoundRect(
            color = bodyColor,
            topLeft = Offset(x(1f), y(84f)),
            size = androidx.compose.ui.geometry.Size(13f * scale, 13f * scale),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(1.5f * scale, 1.5f * scale)
        )
        drawLine(
            color = CarStroke,
            start = Offset(x(1f), y(84f)),
            end = Offset(x(14f), y(84f)),
            strokeWidth = 2.1f * scale
        )
        drawRoundRect(
            color = LightFront,
            topLeft = Offset(x(268f), y(61f)),
            size = androidx.compose.ui.geometry.Size(23f * scale, 5f * scale),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f * scale, 3f * scale)
        )
        drawRoundRect(
            color = LightRear,
            topLeft = Offset(x(274f), y(73f)),
            size = androidx.compose.ui.geometry.Size(21f * scale, 5f * scale),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(3f * scale, 3f * scale)
        )

        listOf(Offset(x(52f), y(96f)), Offset(x(214f), y(96f))).forEach { center ->
            drawCircle(WheelOuter, radius = 25f * scale, center = center)
            drawCircle(Color(0xFF10191C), radius = 17f * scale, center = center)
            drawCircle(WheelInner, radius = 10.5f * scale, center = center)
        }
    }
}
