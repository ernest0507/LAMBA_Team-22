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
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
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
fun RegistrationScreen(
    onCreateAccountClick: () -> Unit = {},
    onLoginClick: () -> Unit = {}
) {
    var name by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var repeatPassword by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }
    var repeatPasswordVisible by rememberSaveable { mutableStateOf(false) }
    var emailError by rememberSaveable { mutableStateOf<String?>(null) }
    var passwordError by rememberSaveable { mutableStateOf<String?>(null) }
    var repeatPasswordError by rememberSaveable { mutableStateOf<String?>(null) }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = LambaCanvas
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            RegistrationBackgroundDecoration()

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .navigationBarsPadding()
                    .padding(horizontal = LambaSpacing.ScreenHorizontal)
                    .padding(top = LambaSpacing.ScreenTop, bottom = LambaSpacing.ScreenBottom)
            ) {
                RegistrationBrandHeader()

                Spacer(modifier = Modifier.height(40.dp))

                Text(
                    text = "Создать аккаунт",
                    style = MaterialTheme.typography.headlineLarge,
                    color = LambaInk
                )

                Spacer(modifier = Modifier.height(LambaSpacing.Step))

                Text(
                    text = "Сохраните гараж, документы и историю автомобиля.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = LambaInkMuted
                )

                Spacer(modifier = Modifier.height(28.dp))

                Column(
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    SharedLambaTextField(
                        label = "Имя",
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Ваше имя",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        ),
                        leadingContent = {
                            RegistrationFieldIcon(
                                imageVector = Icons.Outlined.Person,
                                contentDescription = "Имя"
                            )
                        }
                    )

                    SharedLambaTextField(
                        label = "Email",
                        value = email,
                        onValueChange = {
                            email = it
                            emailError = if (it.isNotEmpty() && !isEmailValid(it)) {
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
                            RegistrationFieldIcon(
                                imageVector = Icons.Outlined.Email,
                                contentDescription = "Email"
                            )
                        }
                    )

                    RegistrationPasswordField(
                        label = "Пароль",
                        value = password,
                        onValueChange = {
                            password = it
                            passwordError = if (it.isNotEmpty() && !isPasswordValid(it)) {
                                InvalidDataMessage
                            } else {
                                null
                            }
                            repeatPasswordError = if (
                                repeatPassword.isNotEmpty() &&
                                repeatPassword != it
                            ) {
                                PasswordMismatchMessage
                            } else {
                                null
                            }
                        },
                        placeholder = "••••••••",
                        passwordVisible = passwordVisible,
                        onVisibilityToggle = { passwordVisible = !passwordVisible },
                        imeAction = ImeAction.Next,
                        errorMessage = passwordError
                    )

                    RegistrationPasswordField(
                        label = "Повторите пароль",
                        value = repeatPassword,
                        onValueChange = {
                            repeatPassword = it
                            repeatPasswordError = if (
                                it.isNotEmpty() &&
                                it != password
                            ) {
                                PasswordMismatchMessage
                            } else {
                                null
                            }
                        },
                        placeholder = "••••••••",
                        passwordVisible = repeatPasswordVisible,
                        onVisibilityToggle = { repeatPasswordVisible = !repeatPasswordVisible },
                        imeAction = ImeAction.Done,
                        errorMessage = repeatPasswordError
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                ContinueButton(
                    onClick = onCreateAccountClick,
                    text = "Создать аккаунт"
                )

                Spacer(modifier = Modifier.weight(1f))

                RegistrationFooterAction(
                    prompt = "Уже есть аккаунт?",
                    action = "Войти",
                    onClick = onLoginClick
                )
            }
        }
    }
}

@Composable
private fun RegistrationBrandHeader() {
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
            RegistrationAccentSpark()
        }
    }
}

@Composable
private fun RegistrationAccentSpark() {
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
private fun RegistrationFieldIcon(
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
private fun RegistrationPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    passwordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    imeAction: ImeAction,
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
            imeAction = imeAction
        ),
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
        },
        leadingContent = {
            RegistrationFieldIcon(
                imageVector = Icons.Outlined.Lock,
                contentDescription = label
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
private fun RegistrationFooterAction(
    prompt: String,
    action: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 6.dp),
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

private fun isEmailValid(email: String): Boolean {
    return EmailPattern.matches(email)
}

private fun isPasswordValid(password: String): Boolean {
    return password.length == RequiredPasswordLength
}

@Composable
private fun RegistrationBackgroundDecoration() {
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

@Preview(showBackground = true)
@Composable
private fun RegistrationScreenPreview() {
    LAMBA_MVPv0Theme {
        RegistrationScreen()
    }
}

private val EmailPattern = Regex("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")
private const val InvalidDataMessage = "Некорректные данные"
private const val PasswordMismatchMessage = "Пароли не совпадают"
private const val RequiredPasswordLength = 8
