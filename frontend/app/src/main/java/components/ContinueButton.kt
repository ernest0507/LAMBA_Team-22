package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaRadius

@Composable
fun ContinueButton(
    onClick: () -> Unit,
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true
) {
    val shape = RoundedCornerShape(LambaRadius.Medium)
    val colorScheme = MaterialTheme.colorScheme
    val backgroundBrush = if (enabled) {
        Brush.horizontalGradient(
            colors = listOf(colorScheme.primary, colorScheme.onPrimaryContainer)
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(colorScheme.outlineVariant, colorScheme.outlineVariant)
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .shadow(
                elevation = 12.dp,
                shape = shape,
                ambientColor = colorScheme.primary.copy(alpha = 0.16f),
                spotColor = colorScheme.onPrimaryContainer.copy(alpha = 0.12f)
            )
            .clip(shape)
            .background(backgroundBrush)
    ) {
        Button(
            onClick = onClick,
            enabled = enabled,
            modifier = Modifier.fillMaxSize(),
            shape = shape,
            colors = ButtonDefaults.buttonColors(
                containerColor = androidx.compose.ui.graphics.Color.Transparent,
                contentColor = colorScheme.onPrimary,
                disabledContainerColor = androidx.compose.ui.graphics.Color.Transparent,
                disabledContentColor = colorScheme.onSurfaceVariant
            )
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.titleMedium
            )
        }
    }
}
