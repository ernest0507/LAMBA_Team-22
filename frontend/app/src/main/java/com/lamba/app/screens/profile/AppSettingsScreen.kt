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
import androidx.compose.material.icons.automirrored.filled.Chat
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.DeleteOutline
import androidx.compose.material.icons.filled.History
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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
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
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import components.BackButton
import kotlinx.coroutines.launch

private const val ThemeLight = "Светлая"
private const val ThemeDark = "Тёмная"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppSettingsScreen(
    onBackClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    var selectedTheme by rememberSaveable { mutableStateOf(ThemeLight) }
    var useCarDataForAi by rememberSaveable { mutableStateOf(true) }
    var usePersonalizedAnswers by rememberSaveable { mutableStateOf(true) }
    var saveChatHistory by rememberSaveable { mutableStateOf(true) }
    var isThemeSheetVisible by rememberSaveable { mutableStateOf(false) }
    var showResetStepOneDialog by rememberSaveable { mutableStateOf(false) }
    var showResetStepTwoDialog by rememberSaveable { mutableStateOf(false) }
    var showLogoutDialog by rememberSaveable { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    if (isThemeSheetVisible) {
        ModalBottomSheet(
            onDismissRequest = { isThemeSheetVisible = false },
            containerColor = colorScheme.surface,
            tonalElevation = 0.dp,
            shape = RoundedCornerShape(topStart = LambaRadius.Large, topEnd = LambaRadius.Large)
        ) {
            ThemeSelectionSheet(
                selectedTheme = selectedTheme,
                onThemeSelected = { selectedTheme = it },
                onCancel = { isThemeSheetVisible = false }
            )
        }
    }

    if (showResetStepOneDialog) {
        ConfirmationDialog(
            title = "Сбросить данные приложения?",
            message = "Будут удалены локальные настройки приложения.",
            confirmText = "Продолжить",
            dismissText = "Отмена",
            confirmColor = colorScheme.onPrimaryContainer,
            onDismiss = { showResetStepOneDialog = false },
            onConfirm = {
                showResetStepOneDialog = false
                showResetStepTwoDialog = true
            }
        )
    }

    if (showResetStepTwoDialog) {
        ConfirmationDialog(
            title = "Вы уверены?",
            message = "Это действие нельзя отменить.",
            confirmText = "Сбросить",
            dismissText = "Отмена",
            confirmColor = colorScheme.error,
            onDismiss = { showResetStepTwoDialog = false },
            onConfirm = {
                showResetStepTwoDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Настройки приложения сброшены")
                }
            }
        )
    }

    if (showLogoutDialog) {
        ConfirmationDialog(
            title = "Выйти из аккаунта?",
            message = "После выхода потребуется снова выполнить вход.",
            confirmText = "Выйти",
            dismissText = "Отмена",
            confirmColor = colorScheme.error,
            onDismiss = { showLogoutDialog = false },
            onConfirm = {
                showLogoutDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("Вы вышли из аккаунта")
                }
            }
        )
    }

    Scaffold(
        containerColor = colorScheme.background,
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
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
                        text = "Настройте внешний вид приложения и параметры конфиденциальности.",
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
                        description = selectedTheme,
                        onClick = { isThemeSheetVisible = true }
                    )
                }

                item {
                    SettingsSectionTitle(text = "Конфиденциальность")
                }

                item {
                    PrivacyToggleCard(
                        icon = Icons.Filled.AutoAwesome,
                        title = "Использовать данные автомобиля\nдля рекомендаций ИИ",
                        description = "ИИ сможет учитывать пробег, историю обслуживания и расходы.",
                        checked = useCarDataForAi,
                        onCheckedChange = { useCarDataForAi = it }
                    )
                }

                item {
                    PrivacyToggleCard(
                        icon = Icons.AutoMirrored.Filled.Chat,
                        title = "Персонализированные ответы",
                        description = "ИИ сможет использовать информацию о вашем автомобиле при ответах.",
                        checked = usePersonalizedAnswers,
                        onCheckedChange = { usePersonalizedAnswers = it }
                    )
                }

                item {
                    PrivacyToggleCard(
                        icon = Icons.Filled.History,
                        title = "Сохранять историю чата",
                        description = "История переписки будет доступна после повторного входа.",
                        checked = saveChatHistory,
                        onCheckedChange = { saveChatHistory = it }
                    )
                }

                item {
                    SettingsSectionTitle(text = "Управление данными")
                }

                item {
                    ClickableSettingsCard(
                        icon = Icons.Filled.DeleteOutline,
                        title = "Сбросить данные приложения",
                        description = "Удалить локальные настройки приложения.",
                        onClick = { showResetStepOneDialog = true }
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
                        onClick = { showLogoutDialog = true }
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
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onBackground,
            maxLines = 1,
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
        }
    }
}

@Composable
private fun PrivacyToggleCard(
    icon: ImageVector,
    title: String,
    description: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Card(
        modifier = Modifier.fillMaxWidth(),
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
            verticalAlignment = Alignment.Top
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

            Switch(
                checked = checked,
                onCheckedChange = onCheckedChange,
                colors = SwitchDefaults.colors(
                    checkedThumbColor = colorScheme.onPrimary,
                    checkedTrackColor = colorScheme.primary,
                    uncheckedThumbColor = colorScheme.surface,
                    uncheckedTrackColor = colorScheme.outlineVariant
                )
            )
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
    selectedTheme: String,
    onThemeSelected: (String) -> Unit,
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
            selected = selectedTheme == ThemeLight,
            onClick = { onThemeSelected(ThemeLight) }
        )

        HorizontalDivider(
            color = colorScheme.outlineVariant,
            thickness = 1.dp
        )

        ThemeOptionRow(
            title = ThemeDark,
            selected = selectedTheme == ThemeDark,
            onClick = { onThemeSelected(ThemeDark) }
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

@Preview(showBackground = true, backgroundColor = 0xFFEEF4F2)
@Composable
private fun AppSettingsScreenPreview() {
    LAMBA_MVPv0Theme {
        AppSettingsScreen()
    }
}
