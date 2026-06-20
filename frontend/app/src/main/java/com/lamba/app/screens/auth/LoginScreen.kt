package com.lamba.app.screens.auth

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lamba.app.ui.theme.LAMBA_MVPv0Theme
import com.lamba.app.ui.theme.LambaAccent
import com.lamba.app.ui.theme.LambaAccentSoft
import com.lamba.app.ui.theme.LambaCanvas
import com.lamba.app.ui.theme.LambaInk
import com.lamba.app.ui.theme.LambaInkMuted
import com.lamba.app.ui.theme.LambaOutline
import com.lamba.app.ui.theme.LambaRadius
import com.lamba.app.ui.theme.LambaSpacing
import com.lamba.app.ui.theme.LambaSurface

@Composable
fun LoginScreen(
    onLoginClick: () -> Unit = {},
    onRegisterClick: () -> Unit = {},
    onForgotPasswordClick: () -> Unit = {}
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

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
                    style = MaterialTheme.typography.headlineMedium,
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
                    AuthTextField(
                        label = "Email",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "name@example.com",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    AuthPasswordField(
                        label = "Пароль",
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "••••••••",
                        passwordVisible = passwordVisible,
                        onVisibilityToggle = { passwordVisible = !passwordVisible }
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

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

                Spacer(modifier = Modifier.height(18.dp))

                Button(
                    onClick = onLoginClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(AuthButtonHeight),
                    shape = RoundedCornerShape(LambaRadius.Large),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LambaAccent
                    )
                ) {
                    Text(
                        text = "Войти",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

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
            style = MaterialTheme.typography.titleMedium.copy(
                letterSpacing = 1.6.sp
            ),
            color = LambaInk,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        Box(
            modifier = Modifier
                .size(30.dp)
                .background(
                    color = LambaAccentSoft,
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
    Canvas(modifier = Modifier.size(16.dp)) {
        val strokeWidth = 1.8.dp.toPx()
        val center = Offset(size.width / 2f, size.height / 2f)
        val longRadius = size.minDimension * 0.38f
        val shortRadius = size.minDimension * 0.22f

        drawLine(
            brush = SolidColor(LambaAccent),
            start = Offset(center.x, center.y - longRadius),
            end = Offset(center.x, center.y + longRadius),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            brush = SolidColor(LambaAccent),
            start = Offset(center.x - longRadius, center.y),
            end = Offset(center.x + longRadius, center.y),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            brush = SolidColor(LambaAccent),
            start = Offset(center.x - shortRadius, center.y - shortRadius),
            end = Offset(center.x + shortRadius, center.y + shortRadius),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
        drawLine(
            brush = SolidColor(LambaAccent),
            start = Offset(center.x - shortRadius, center.y + shortRadius),
            end = Offset(center.x + shortRadius, center.y - shortRadius),
            strokeWidth = strokeWidth,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun AuthTextField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    keyboardOptions: KeyboardOptions,
    trailingContent: @Composable (() -> Unit)? = null,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = LambaInkMuted
        )

        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            textStyle = MaterialTheme.typography.bodyLarge.copy(color = LambaInk),
            placeholder = {
                Text(
                    text = placeholder,
                    style = MaterialTheme.typography.bodyLarge,
                    color = LambaInkMuted
                )
            },
            shape = RoundedCornerShape(LambaRadius.Medium),
            keyboardOptions = keyboardOptions,
            visualTransformation = visualTransformation,
            trailingIcon = trailingContent,
            colors = TextFieldDefaults.colors(
                focusedTextColor = LambaInk,
                unfocusedTextColor = LambaInk,
                disabledTextColor = LambaInk,
                errorTextColor = LambaInk,
                focusedContainerColor = LambaSurface,
                unfocusedContainerColor = LambaSurface,
                disabledContainerColor = LambaSurface,
                errorContainerColor = LambaSurface,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                errorIndicatorColor = Color.Transparent,
                cursorColor = LambaAccent
            )
        )
    }
}

@Composable
private fun AuthPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    passwordVisible: Boolean,
    onVisibilityToggle: () -> Unit
) {
    AuthTextField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = ImeAction.Done
        ),
        trailingContent = {
            Text(
                text = if (passwordVisible) "Скрыть" else "Показать",
                style = MaterialTheme.typography.bodySmall,
                color = LambaAccent,
                fontWeight = FontWeight.Medium,
                modifier = Modifier.clickable(onClick = onVisibilityToggle)
            )
        },
        visualTransformation = if (passwordVisible) {
            VisualTransformation.None
        } else {
            PasswordVisualTransformation()
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

@Composable
private fun AuthBackgroundDecoration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val wideStroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        val softStroke = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)

        val roofLine = Path().apply {
            moveTo(size.width * 0.08f, size.height * 0.78f)
            cubicTo(
                size.width * 0.24f,
                size.height * 0.71f,
                size.width * 0.42f,
                size.height * 0.68f,
                size.width * 0.60f,
                size.height * 0.73f
            )
            cubicTo(
                size.width * 0.72f,
                size.height * 0.76f,
                size.width * 0.82f,
                size.height * 0.73f,
                size.width * 0.92f,
                size.height * 0.64f
            )
        }

        val roadLine = Path().apply {
            moveTo(size.width * 0.16f, size.height * 0.88f)
            cubicTo(
                size.width * 0.40f,
                size.height * 0.84f,
                size.width * 0.70f,
                size.height * 0.92f,
                size.width * 0.94f,
                size.height * 0.86f
            )
        }

        drawPath(
            path = roofLine,
            color = LambaAccent.copy(alpha = 0.10f),
            style = wideStroke
        )
        drawPath(
            path = roadLine,
            color = LambaInk.copy(alpha = 0.08f),
            style = softStroke
        )
        drawCircle(
            color = LambaAccent.copy(alpha = 0.05f),
            radius = size.minDimension * 0.30f,
            center = Offset(size.width * 0.92f, size.height * 0.16f)
        )
        drawCircle(
            color = LambaOutline.copy(alpha = 0.45f),
            radius = size.minDimension * 0.20f,
            center = Offset(size.width * 0.06f, size.height * 0.96f)
        )
    }
}

private val AuthHeaderGap = 40.dp
private val AuthSectionGap = 32.dp
private val AuthFieldGap = 16.dp
private val AuthButtonHeight = 58.dp
private val AuthTextButtonPadding = androidx.compose.foundation.layout.PaddingValues(
    horizontal = 4.dp,
    vertical = 0.dp
)

@Preview(showBackground = true, backgroundColor = 0xFFEEF4F2)
@Composable
private fun LoginScreenPreview() {
    LAMBA_MVPv0Theme {
        LoginScreen()
    }
}
