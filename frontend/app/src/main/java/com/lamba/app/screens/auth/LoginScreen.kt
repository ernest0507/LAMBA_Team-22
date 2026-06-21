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
import components.ContinueButton
import components.LambaTextField as SharedLambaTextField
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import com.lamba.app.ui.theme.LambaAccent
import com.lamba.app.ui.theme.LambaAccentSoft
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaOutline
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LambaCanvas
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
                    color = LambaInk
                )

                Spacer(modifier = Modifier.height(LambaSpacing.Step))

                Text(
                    text = "Продолжите работу с цифровым двойником автомобиля.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LambaInkMuted
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
                            passwordError = if (it.isNotEmpty() && it.length != RequiredPasswordLength) {
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
                            color = LambaAccent,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                Spacer(modifier = Modifier.height(LambaSpacing.CardPadding))

                ContinueButton(
                    onClick = onLoginClick,
                    text = "Войти"
                )

                Spacer(modifier = Modifier.weight(1f))

                AuthFooterAction(
                    prompt = "Нет аккаунта?",
                    action = "Зарегистрироваться",
                    onClick = onRegisterClick
                )
            }
        }
    }
}

@Composable
private fun AuthBrandHeader() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "LAMBA",
            style = MaterialTheme.typography.titleMedium,
            color = LambaInk,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(32.dp)
                .background(
                    color = LambaAccentSoft.copy(alpha = 0.85f),
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
    Canvas(modifier = Modifier.size(15.dp)) {
        val center = Offset(size.width / 2f, size.height / 2f)
        val longRadius = size.minDimension * 0.38f
        val shortRadius = size.minDimension * 0.20f

        drawLine(
            color = LambaAccent,
            start = Offset(center.x, center.y - longRadius),
            end = Offset(center.x, center.y + longRadius),
            strokeWidth = 1.6.dp.toPx()
        )
        drawLine(
            color = LambaAccent,
            start = Offset(center.x - longRadius, center.y),
            end = Offset(center.x + longRadius, center.y),
            strokeWidth = 1.6.dp.toPx()
        )
        drawLine(
            color = LambaAccent,
            start = Offset(center.x - shortRadius, center.y - shortRadius),
            end = Offset(center.x + shortRadius, center.y + shortRadius),
            strokeWidth = 1.2.dp.toPx()
        )
        drawLine(
            color = LambaAccent,
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
        tint = LambaInkMuted
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
                    tint = LambaInkMuted
                )
            }
        }
    )
}

@Composable
private fun AuthFooterAction(
    prompt: String,
    action: String,
    onClick: () -> Unit
) {
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
            color = LambaInkMuted
        )

        Text(
            text = " $action",
            style = MaterialTheme.typography.bodyMedium,
            color = LambaAccent,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.clickable(onClick = onClick)
        )
    }
}

@Composable
private fun AuthBackgroundDecoration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawCircle(
            color = LambaAccentSoft.copy(alpha = 0.42f),
            radius = size.minDimension * 0.23f,
            center = Offset(size.width * 0.94f, size.height * 0.08f)
        )
        drawCircle(
            color = LambaOutline.copy(alpha = 0.18f),
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
