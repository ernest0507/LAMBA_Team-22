package com.lamba.app.screens.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.lamba.app.common.LoadingOverlay
import components.ContinueButton
import components.LambaTextField as SharedLambaTextField
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import com.lamba.app.ui.theme.LambaError
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing

@Composable
fun LoginScreen(
    isLoading: Boolean = false,
    authErrorMessage: String? = null,
    onLoginClick: (email: String, password: String) -> Unit = { _, _ -> },
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    val colorScheme = MaterialTheme.colorScheme
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    val canSubmit = email.isNotBlank() &&
        password.length >= RequiredPasswordLength &&
        emailError == null &&
        passwordError == null &&
        !isLoading

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AuthBackgroundDecoration()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(horizontal = LambaSpacing.ScreenHorizontal)
                    .padding(top = LambaSpacing.ScreenTop, bottom = LambaSpacing.ScreenBottom)
            ) {
                AuthBrandHeader()

                Spacer(modifier = Modifier.height(AuthHeaderGap))

                Text(
                    text = "Вход в LAMBA",
                    style = MaterialTheme.typography.headlineLarge,
                    color = colorScheme.onBackground
                )

                Spacer(modifier = Modifier.height(LambaSpacing.Step))

                Text(
                    text = "Продолжите работу с цифровым двойником автомобиля.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(AuthSectionGap))

                Column(
                    verticalArrangement = Arrangement.spacedBy(AuthFieldGap)
                ) {
                    SharedLambaTextField(
                        label = "Email",
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = if (it.isNotEmpty() && !EmailPattern.matches(it)) {
                                InvalidDataMessage
                            } else {
                                null
                            }
                        },
                        placeholder = "name@example.com",
                        isError = emailError != null,
                        errorMessage = emailError,
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        ),
                        leadingContent = {
                            AuthFieldIcon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = "Email"
                            )
                        }
                    )

                    AuthPasswordField(
                        label = "Пароль",
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = if (it.isNotEmpty() && it.length < RequiredPasswordLength) {
                                InvalidDataMessage
                            } else {
                                null
                            }
                        },
                        placeholder = "••••••••",
                        passwordVisible = passwordVisible,
                        onVisibilityToggle = { passwordVisible = !passwordVisible },
                        errorMessage = passwordError
                    )
                }

                Spacer(modifier = Modifier.height(AuthInlineGap))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(
                        onClick = onForgotPasswordClick,
                        contentPadding = AuthTextButtonPadding
                    ) {
                        Text(
                            text = "Забыли пароль?",
                            style = MaterialTheme.typography.bodyMedium,
                            color = colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(LambaSpacing.CardPadding))

                if (!authErrorMessage.isNullOrBlank()) {
                    Text(
                        text = authErrorMessage,
                        style = MaterialTheme.typography.bodySmall,
                        color = LambaError
                    )

                    Spacer(modifier = Modifier.height(AuthFieldGap))
                }

                ContinueButton(
                    onClick = {
                        if (canSubmit) {
                            onLoginClick(email.trim(), password)
                        }
                    },
                    text = "Войти",
                    enabled = canSubmit
                )

                Spacer(modifier = Modifier.weight(1f))

                AuthFooterAction(
                    prompt = "Нет аккаунта?",
                    action = "Зарегистрироваться",
                    enabled = !isLoading,
                    onClick = onRegisterClick
                )
            }
        }
        if (isLoading) {
            LoadingOverlay(
                title = "Выполняется вход",
                message = "Пожалуйста, подождите. Ваши данные обрабатываются"
            )
        }
    }
}

@Composable
private fun AuthBrandHeader() {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "LAMBA",
            style = MaterialTheme.typography.titleMedium,
            color = colorScheme.onBackground,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = colorScheme.primaryContainer.copy(alpha = 0.85f),
                    shape = RoundedCornerShape(LambaRadius.Medium)
                ),
            contentAlignment = Alignment.Center
        ) {
            AccentSpark()
        }
    }
}

@Composable
private fun AccentSpark() {
    val colorScheme = MaterialTheme.colorScheme

    Canvas(modifier = Modifier.size(15.dp)) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val longRadius = size.minDimension * 0.38f
        val shortRadius = size.minDimension * 0.20f

        drawLine(
            color = colorScheme.primary,
            start = Offset(center.x, center.y - longRadius),
            end = Offset(center.x, center.y + longRadius),
            strokeWidth = 1.6.dp.toPx()
        )
        drawLine(
            color = colorScheme.primary,
            start = Offset(center.x - longRadius, center.y),
            end = Offset(center.x + longRadius, center.y),
            strokeWidth = 1.6.dp.toPx()
        )
        drawLine(
            color = colorScheme.primary,
            start = Offset(center.x - shortRadius, center.y - shortRadius),
            end = Offset(center.x + shortRadius, center.y + shortRadius),
            strokeWidth = 1.2.dp.toPx()
        )
        drawLine(
            color = colorScheme.primary,
            start = Offset(center.x - shortRadius, center.y + shortRadius),
            end = Offset(center.x + shortRadius, center.y - shortRadius),
            strokeWidth = 1.2.dp.toPx()
        )
    }
}

@Composable
private fun AuthFieldIcon(
    imageVector: androidx.compose.ui.graphics.vector.ImageVector,
    contentDescription: String
) {
    Icon(
        imageVector = imageVector,
        contentDescription = contentDescription,
        tint = MaterialTheme.colorScheme.onSurfaceVariant
    )
}

@Composable
private fun AuthPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    passwordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    errorMessage: String? = null
) {
    SharedLambaTextField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        isError = errorMessage != null,
        errorMessage = errorMessage,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        leadingContent = {
            AuthFieldIcon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = "Пароль"
            )
        },
        trailingContent = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    imageVector = if (passwordVisible) {
                        Icons.Outlined.VisibilityOff
                    } else {
                        Icons.Outlined.Visibility
                    },
                    contentDescription = if (passwordVisible) {
                        "Скрыть пароль"
                    } else {
                        "Показать пароль"
                    },
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    )
}

@Composable
private fun AuthFooterAction(
    prompt: String,
    action: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = AuthInlineGap),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = prompt,
            style = MaterialTheme.typography.bodyMedium,
            color = colorScheme.onSurfaceVariant
        )

        Text(
            text = " $action",
            style = MaterialTheme.typography.bodyMedium,
            color = if (enabled) colorScheme.primary else colorScheme.onSurfaceVariant,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(enabled = enabled, onClick = onClick)
        )
    }
}

@Composable
private fun AuthBackgroundDecoration() {
    val colorScheme = MaterialTheme.colorScheme

    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = colorScheme.primaryContainer.copy(alpha = 0.42f),
            radius = size.minDimension * 0.23f,
            center = Offset(size.width * 0.94f, size.height * 0.08f)
        )
        drawCircle(
            color = colorScheme.outlineVariant.copy(alpha = 0.24f),
            radius = size.minDimension * 0.15f,
            center = Offset(size.width * 0.10f, size.height * 0.97f)
        )
    }
}

private val AuthHeaderGap = 40.dp
private val AuthSectionGap = 32.dp
private val AuthFieldGap = 16.dp
private val AuthInlineGap = 6.dp
private val AuthTextButtonPadding = PaddingValues(
    horizontal = 4.dp,
    vertical = 0.dp
)
private val EmailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
private const val InvalidDataMessage = "Некорректные данные"
private const val RequiredPasswordLength = 8

//@Preview(showBackground = true)
//@Composable
//private fun LoginScreenPreview() {
//    LAMBA_MVPv0Theme {
//        LoginScreen()
//    }
//}
