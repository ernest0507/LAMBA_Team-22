package components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutocompleteField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    suggestions: List<String>,
    otherLabel: String = "Другое",
    modifier: Modifier = Modifier,
    minHeight: Dp = 58.dp,
    isError: Boolean = false,
    errorMessage: String? = null
) {
    var expanded by remember { mutableStateOf(false) }
    val fieldShape = RoundedCornerShape(LambaRadius.Medium)
    val colorScheme = MaterialTheme.colorScheme

    val filtered = remember(value, suggestions) {
        if (value.isBlank()) suggestions + otherLabel
        else (suggestions.filter { it.contains(value, ignoreCase = true) } + otherLabel)
            .distinct()
    }

    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(LambaSpacing.Step))

        ExposedDropdownMenuBox(
            expanded = expanded && filtered.isNotEmpty(),
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = value,
                onValueChange = {
                    onValueChange(it)
                    expanded = true
                },
                modifier = Modifier
                    .menuAnchor(type = MenuAnchorType.PrimaryEditable, enabled = true)
                    .fillMaxWidth()
                    .heightIn(min = minHeight)
                    .clip(fieldShape)
                    .background(colorScheme.surface, fieldShape)
                    .border(
                        width = 1.dp,
                        color = if (isError) colorScheme.error.copy(alpha = 0.7f) else colorScheme.outlineVariant,
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
                singleLine = true,
                textStyle = MaterialTheme.typography.bodyMedium.copy(color = colorScheme.onSurface),
                shape = fieldShape,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = colorScheme.onSurface,
                    unfocusedTextColor = colorScheme.onSurface,
                    focusedContainerColor = colorScheme.surface,
                    unfocusedContainerColor = colorScheme.surface,
                    disabledContainerColor = colorScheme.surface,
                    errorContainerColor = colorScheme.surface,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                    errorIndicatorColor = Color.Transparent
                )
            )

            ExposedDropdownMenu(
                expanded = expanded && filtered.isNotEmpty(),
                onDismissRequest = { expanded = false },
                containerColor = colorScheme.surface,
                shape = RoundedCornerShape(LambaRadius.Medium)
            ) {
                filtered.forEach { option ->
                    if (option == otherLabel) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 12.dp)
                        ) {
                            Text(
                                text = option,
                                style = MaterialTheme.typography.bodySmall,
                                color = colorScheme.onSurfaceVariant
                            )
                        }
                    } else {
                        DropdownMenuItem(
                            text = {
                                Text(
                                    text = option,
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = colorScheme.onSurface
                                )
                            },
                            onClick = {
                                onValueChange(option)
                                expanded = false
                            }
                        )
                    }
                }
            }
        }

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
