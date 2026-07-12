package components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaRadius
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack


@Composable
fun BackButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val colorScheme = MaterialTheme.colorScheme

    Button(
        onClick = onClick,
        modifier = modifier.height(44.dp),
        shape = RoundedCornerShape(LambaRadius.Medium),
        border = BorderStroke(
            width = 1.dp,
            color = colorScheme.outlineVariant
        ),
        colors = ButtonDefaults.buttonColors(
            containerColor = colorScheme.surface,
            contentColor = colorScheme.onPrimaryContainer
        ),
        contentPadding = PaddingValues(
            horizontal = 14.dp,
            vertical = 0.dp
        )
    ) {
        Icon(
            imageVector = Icons.Filled.ArrowBack,
            contentDescription = "Назад",
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(8.dp))

        Text(
            text = "Назад",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}
