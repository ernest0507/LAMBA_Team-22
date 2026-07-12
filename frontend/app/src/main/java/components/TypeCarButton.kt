package components

import android.R
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaRadius


@Composable
fun TypeCarButton(
    text: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Button(
        onClick = onClick,
        modifier = modifier
            .height(48.dp),
        shape = RoundedCornerShape(LambaRadius.Small),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) colorScheme.onPrimaryContainer else colorScheme.outlineVariant
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (selected) colorScheme.primaryContainer.copy(alpha = 0.28f) else colorScheme.surface,
            contentColor = if (selected) colorScheme.onPrimaryContainer else colorScheme.onSurface
        ),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        Text(
            text = text,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 1
        )
    }
}
