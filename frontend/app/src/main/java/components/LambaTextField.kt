package components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.graphics.Color
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing


@Composable
fun LambaTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    minHeight: Dp = 58.dp,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    singleLine: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    leadingContent: @Composable (() -> Unit)? = null,
    trailingContent: @Composable (() -> Unit)? = null
) {
    val fieldShape = RoundedCornerShape(LambaRadius.Medium)
    val colorScheme = MaterialTheme.colorScheme
    val borderColor = when {
        isError -> colorScheme.error.copy(alpha = 0.7f)
        else -> colorScheme.outlineVariant
    }

    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(LambaSpacing.Step))

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(min = minHeight)
                .clip(fieldShape)
                .background(colorScheme.surface, fieldShape)
                .border(
                    width = 1.dp,
                    color = borderColor,
                    shape = fieldShape
                ),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            },
            isError = isError,
            singleLine = singleLine,
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            leadingIcon = leadingContent,
            trailingIcon = trailingContent,
            textStyle = MaterialTheme.typography.bodyMedium.copy(color = colorScheme.onSurface),
            shape = fieldShape,
            colors = TextFieldDefaults.colors(
                focusedTextColor = colorScheme.onSurface,
                unfocusedTextColor = colorScheme.onSurface,
                focusedContainerColor = colorScheme.surface,
                unfocusedContainerColor = colorScheme.surface,
                disabledContainerColor = colorScheme.surface,
                errorContainerColor = colorScheme.surface,
                focusedLeadingIconColor = colorScheme.onSurfaceVariant,
                unfocusedLeadingIconColor = colorScheme.onSurfaceVariant,
                focusedTrailingIconColor = colorScheme.onSurfaceVariant,
                unfocusedTrailingIconColor = colorScheme.onSurfaceVariant,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent
            )
        )
        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.bodySmall,
                color = colorScheme.error,
                modifier = Modifier.padding(top = LambaSpacing.Step / 2)
            )
        }
    }
}
