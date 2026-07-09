package com.lamba.app.screens.profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import com.lamba.app.ui.theme.LambaAccent
import com.lamba.app.ui.theme.LambaAccentSoft
import com.lamba.app.ui.theme.LambaAccentStrong
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaOutlineSoft
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import com.lamba.app.ui.theme.LambaSurface
import components.BackButton

@Composable
fun ProfileScreen(
    onBackClick: () -> Unit = {},
    onVehicleDataClick: () -> Unit = {},
    onNotificationsClick: () -> Unit = {},
    onAppSettingsClick: () -> Unit = {},
    onHelpClick: () -> Unit = {},
    onSignOutClick: () -> Unit = {}
) {
    val menuItems = listOf(
        ProfileMenuItem(
            iconLabel = "А",
            title = "Данные автомобиля",
            subtitle = "Год, пробег, VIN, цвет кузова",
            onClick = onVehicleDataClick
        ),
        ProfileMenuItem(
            iconLabel = "У",
            title = "Уведомления",
            subtitle = "ТО, страховка, расходы",
            onClick = onNotificationsClick
        ),
        ProfileMenuItem(
            iconLabel = "Н",
            title = "Настройки приложения",
            subtitle = "Тема, единицы, приватность",
            onClick = onAppSettingsClick
        ),
        ProfileMenuItem(
            iconLabel = "?",
            title = "Помощь",
            subtitle = "FAQ и поддержка",
            onClick = onHelpClick
        ),
        ProfileMenuItem(
            iconLabel = "!",
            title = "Sign out",
            subtitle = "End the current session",
            onClick = onSignOutClick
        )
    )

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LambaCanvas
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                start = LambaSpacing.ScreenHorizontal,
                top = LambaSpacing.ScreenTop,
                end = LambaSpacing.ScreenHorizontal,
                bottom = LambaSpacing.ScreenBottom
            ),
            verticalArrangement = Arrangement.spacedBy(LambaSpacing.CardPadding)
        ) {
            item {
                ProfileHeader(onBackClick = onBackClick)
            }

            item {
                ProfileUserCard()
            }

            item {
                ProfileMenuCard(items = menuItems)
            }

            item {
                ProfileFooter()
            }
        }
    }
}

@Composable
private fun ProfileHeader(
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        BackButton(onClick = onBackClick)

        Text(
            text = "Профиль",
            style = MaterialTheme.typography.titleLarge,
            color = LambaInk,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
private fun ProfileUserCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(
            containerColor = LambaSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(LambaSpacing.CardPadding),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(LambaAccentSoft),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Н",
                    style = MaterialTheme.typography.headlineMedium,
                    color = LambaAccentStrong,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Никита",
                    style = MaterialTheme.typography.titleMedium,
                    color = LambaInk,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = "Toyota Corolla · 48 230 км",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LambaInkMuted
                )
            }
        }
    }
}

@Composable
private fun ProfileMenuCard(
    items: List<ProfileMenuItem>
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(LambaRadius.Large),
        colors = CardDefaults.cardColors(
            containerColor = LambaSurface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEachIndexed { index, item ->
                ProfileMenuRow(item = item)

                if (index != items.lastIndex) {
                    HorizontalDivider(
                        modifier = Modifier.padding(horizontal = LambaSpacing.CardPadding),
                        thickness = 1.dp,
                        color = LambaOutlineSoft
                    )
                }
            }
        }
    }
}

@Composable
private fun ProfileMenuRow(
    item: ProfileMenuItem
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(LambaRadius.Medium))
            .clickable(onClick = item.onClick)
            .padding(horizontal = LambaSpacing.CardPadding, vertical = 14.dp),
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(LambaRadius.Medium))
                .background(LambaAccentSoft),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = item.iconLabel,
                style = MaterialTheme.typography.titleMedium,
                color = LambaAccentStrong,
                fontWeight = FontWeight.SemiBold
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = item.title,
                style = MaterialTheme.typography.bodyLarge,
                color = LambaInk,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                text = item.subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = LambaInkMuted
            )
        }

        Text(
            text = "\u203A",
            style = MaterialTheme.typography.headlineMedium,
            color = LambaAccent,
            fontWeight = FontWeight.Normal
        )
    }
}

@Composable
private fun ProfileFooter() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = LambaSpacing.Step, bottom = LambaSpacing.Step),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "LAMBA · Версия 1.0.0",
            style = MaterialTheme.typography.bodySmall,
            color = LambaInkMuted
        )
    }
}

private data class ProfileMenuItem(
    val iconLabel: String,
    val title: String,
    val subtitle: String,
    val onClick: () -> Unit
)

@Preview(showBackground = true, backgroundColor = 0xFFEEF4F2)
@Composable
private fun ProfileScreenPreview() {
    LAMBA_MVPv0Theme {
        ProfileScreen()
    }
}
