package com.lamba.app.screens.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.AppTheme
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import components.BackButton

private const val ThemeLight = "Светлая"
private const val ThemeDark = "Тёмная"

private enum class SettingsDialogStep {
    LogoutStepOne,
    LogoutStepTwo
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsScreen(
    currentTheme: AppTheme = AppTheme.LIGHT,
    onThemeSelected: (AppTheme) -> Unit = {},
    onBackClick: () -> Unit = {},
    onLogoutConfirmed: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    var isThemeSheetVisible by rememberSaveable { mutableStateOf(false) }
    var activeDialogStep by rememberSaveable { mutableStateOf<SettingsDialogStep?>(null) }

    if (isThemeSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isThemeSheetVisible = false },
            containerColor = colorScheme.surface,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(topStart = LambaRadius.Large, topEnd = LambaRadius.Large)
        ) {
            ThemeSelectionSheet(
                currentTheme = currentTheme,
                onThemeSelected = { theme ->
                    onThemeSelected(theme)
                    isThemeSheetVisible = false
                },
                onCancel = { isThemeSheetVisible = false }
            )
        }
    }

    activeDialogStep?.let { dialogStep ->
        val dialogConfig = dialogStep.toDialogConfig(colorScheme = colorScheme)

        ConfirmationDialog(
            title = dialogConfig.title,
            message = dialogConfig.message,
            confirmText = dialogConfig.confirmText,
            dismissText = dialogConfig.dismissText,
            confirmColor = dialogConfig.confirmColor,
            onDismiss = { activeDialogStep = null },
            onConfirm = {
                when (dialogStep) {
                    SettingsDialogStep.LogoutStepOne -> {
                        activeDialogStep = SettingsDialogStep.LogoutStepTwo
                    }

                    SettingsDialogStep.LogoutStepTwo -> {
                        activeDialogStep = null
                        onLogoutConfirmed()
                    }
                }
            }
        )
    }

    Scaffold(
        containerColor = colorScheme.background
    ) { innerPadding ->
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = colorScheme.background
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(
                    start = LambaSpacing.ScreenHorizontal,
                    top = LambaSpacing.ScreenTop,
                    end = LambaSpacing.ScreenHorizontal,
                    bottom = LambaSpacing.ScreenBottom
                ),
                verticalArrangement = Arrangement.spacedBy(LambaSpacing.CardPadding)
            ) {
                item {
                    SettingsHeader(onBackClick = onBackClick)
                }

                item {
                    Text(
                        text = "Настройте внешний вид приложения и параметры аккаунта.",
                        style = MaterialTheme.typography.bodyMedium,
                        color = colorScheme.onSurfaceVariant
                    )
                }

                item {
                    SettingsSectionTitle(text = "Внешний вид")
                }

                item {
                    ClickableSettingsCard(
                        icon = Icons.Filled.Palette,
                        title = "Тема приложения",
                        description = currentTheme.displayName(),
                        onClick = { isThemeSheetVisible = true }
                    )
                }

                item {
                    SettingsSectionTitle(text = "Аккаунт")
                }

                item {
                    ClickableSettingsCard(
                        icon = Icons.AutoMirrored.Filled.Logout,
                        title = "Выйти из аккаунта",
                        description = "Завершить текущий сеанс.",
                        onClick = { activeDialogStep = SettingsDialogStep.LogoutStepOne }
                    )
                }
            }
        }
    }
}

@Composable
private fun SettingsHeader(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BackButton(onClick = onBackClick)

        Text(
            text = "Настройки приложения",
            modifier = Modifier.weight(1f),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun SettingsSectionTitle(
    text: String
) {
    Text(
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = MaterialTheme.colorScheme.onBackground,
        fontWeight = FontWeight.SemiBold
    )
}

@Composable
private fun ClickableSettingsCard(
    icon: ImageVector,
    title: String,
    description: String,
    showChevron: Boolean = false,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LambaRadius.Large))
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(containerColor = colorScheme.surface),
        border = BorderStroke(1.dp, colorScheme.outlineVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LambaSpacing.CardPadding),
            horizontalArrangement = Arrangement.spacedBy(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SettingsIcon(icon = icon)

            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorScheme.onSurface,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )
            }

            if (showChevron) {
                Text(
                    text = "\u203A",
                    style = MaterialTheme.typography.headlineMedium,
                    color = colorScheme.primary,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}

@Composable
private fun SettingsIcon(
    icon: ImageVector
) {
    val colorScheme = MaterialTheme.colorScheme

    Box(
        modifier = Modifier
            .size(44.dp)
            .clip(RoundedCornerShape(LambaRadius.Medium))
            .background(colorScheme.primaryContainer),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = colorScheme.onPrimaryContainer
        )
    }
}

@Composable
private fun ThemeSelectionSheet(
    currentTheme: AppTheme,
    onThemeSelected: (AppTheme) -> Unit,
    onCancel: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = LambaSpacing.ScreenHorizontal)
            .padding(top = 8.dp, bottom = 32.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(
            text = "Выберите тему",
            style = MaterialTheme.typography.titleMedium,
            color = colorScheme.onSurface,
            fontWeight = FontWeight.SemiBold
        )

        ThemeOptionRow(
            title = ThemeLight,
            selected = currentTheme == AppTheme.LIGHT,
            onClick = { onThemeSelected(AppTheme.LIGHT) }
        )

        HorizontalDivider(
            color = colorScheme.outlineVariant,
            thickness = 1.dp
        )

        ThemeOptionRow(
            title = ThemeDark,
            selected = currentTheme == AppTheme.DARK,
            onClick = { onThemeSelected(AppTheme.DARK) }
        )

        TextButton(
            onClick = onCancel,
            modifier = Modifier.align(Alignment.End)
        ) {
            Text(
                text = "Отмена",
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.onPrimaryContainer,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

@Composable
private fun ThemeOptionRow(
    title: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LambaRadius.Medium))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        RadioButton(
            selected = selected,
            onClick = onClick
        )

        Text(
            text = title,
            style = MaterialTheme.typography.bodyLarge,
            color = colorScheme.onSurface
        )
    }
}

@Composable
private fun ConfirmationDialog(
    title: String,
    message: String,
    confirmText: String,
    dismissText: String,
    confirmColor: Color,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    AlertDialog(
        onDismissRequest = onDismiss,
        containerColor = colorScheme.surface,
        shape = RoundedCornerShape(LambaRadius.Large),
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
                color = colorScheme.onSurface,
                fontWeight = FontWeight.SemiBold
            )
        },
        text = {
            Text(
                text = message,
                style = MaterialTheme.typography.bodyMedium,
                color = colorScheme.onSurfaceVariant
            )
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = dismissText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = colorScheme.onSurfaceVariant
                )
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = confirmText,
                    style = MaterialTheme.typography.bodyLarge,
                    color = confirmColor,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    )
}

private data class SettingsDialogConfig(
    val title: String,
    val message: String,
    val confirmText: String,
    val dismissText: String,
    val confirmColor: Color
)

@Composable
private fun SettingsDialogStep.toDialogConfig(
    colorScheme: androidx.compose.material3.ColorScheme
): SettingsDialogConfig {
    return when (this) {
        SettingsDialogStep.LogoutStepOne -> SettingsDialogConfig(
            title = "Выйти из аккаунта?",
            message = "Для повторного входа потребуется снова авторизоваться.",
            confirmText = "Продолжить",
            dismissText = "Отмена",
            confirmColor = colorScheme.onPrimaryContainer
        )

        SettingsDialogStep.LogoutStepTwo -> SettingsDialogConfig(
            title = "Подтвердите выход",
            message = "Вы действительно хотите выйти из аккаунта?",
            confirmText = "Да, выйти",
            dismissText = "Нет",
            confirmColor = colorScheme.error
        )
    }
}

private fun AppTheme.displayName(): String {
    return when (this) {
        AppTheme.LIGHT -> ThemeLight
        AppTheme.DARK -> ThemeDark
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEF4F2)
@Composable
private fun AppSettingsScreenPreview() {
    LAMBA_MVPv0Theme {
        AppSettingsScreen(currentTheme = AppTheme.LIGHT)
    }
}
