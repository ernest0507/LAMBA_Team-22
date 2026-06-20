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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
                    style = MaterialTheme.typography.headlineMedium,
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
                    RegistrationTextField(
                        label = "Имя",
                        value = name,
                        onValueChange = { name = it },
                        placeholder = "Ваше имя",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next
                        )
                    )

                    RegistrationTextField(
                        label = "Email",
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "name@example.com",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Email,
                            imeAction = ImeAction.Next
                        )
                    )

                    RegistrationPasswordField(
                        label = "Пароль",
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "••••••••",
                        passwordVisible = passwordVisible,
                        onVisibilityToggle = { passwordVisible = !passwordVisible },
                        imeAction = ImeAction.Next
                    )

                    RegistrationPasswordField(
                        label = "Повторите пароль",
                        value = repeatPassword,
                        onValueChange = { repeatPassword = it },
                        placeholder = "••••••••",
                        passwordVisible = repeatPasswordVisible,
                        onVisibilityToggle = { repeatPasswordVisible = !repeatPasswordVisible },
                        imeAction = ImeAction.Done
                    )
                }

                Spacer(modifier = Modifier.height(22.dp))

                Button(
                    onClick = onCreateAccountClick,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp),
                    shape = RoundedCornerShape(LambaRadius.Large),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LambaAccent,
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Создать аккаунт",
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.SemiBold
                    )
                }

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
            RegistrationAccentSpark()
        }
    }
}

@Composable
private fun RegistrationAccentSpark() {
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
private fun RegistrationTextField(
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
            visualTransformation = visualTransformation,
            keyboardOptions = keyboardOptions,
            trailingIcon = trailingContent,
            shape = RoundedCornerShape(LambaRadius.Medium),
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
private fun RegistrationPasswordField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    passwordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    imeAction: ImeAction
) {
    RegistrationTextField(
        label = label,
        value = value,
        onValueChange = onValueChange,
        placeholder = placeholder,
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Password,
            imeAction = imeAction
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

@Composable
private fun RegistrationBackgroundDecoration() {
    Canvas(modifier = Modifier.fillMaxSize()) {
        val mainStroke = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
        val thinStroke = Stroke(width = 1.2.dp.toPx(), cap = StrokeCap.Round)

        val upperLine = Path().apply {
            moveTo(size.width * 0.58f, size.height * 0.18f)
            cubicTo(
                size.width * 0.72f,
                size.height * 0.10f,
                size.width * 0.88f,
                size.height * 0.14f,
                size.width * 1.02f,
                size.height * 0.06f
            )
        }

        val carLine = Path().apply {
            moveTo(size.width * 0.04f, size.height * 0.80f)
            cubicTo(
                size.width * 0.20f,
                size.height * 0.73f,
                size.width * 0.42f,
                size.height * 0.71f,
                size.width * 0.58f,
                size.height * 0.76f
            )
            cubicTo(
                size.width * 0.70f,
                size.height * 0.79f,
                size.width * 0.84f,
                size.height * 0.75f,
                size.width * 0.98f,
                size.height * 0.66f
            )
        }

        drawPath(
            path = upperLine,
            color = LambaAccent.copy(alpha = 0.08f),
            style = thinStroke
        )
        drawPath(
            path = carLine,
            color = LambaAccent.copy(alpha = 0.11f),
            style = mainStroke
        )
        drawCircle(
            color = LambaAccent.copy(alpha = 0.05f),
            radius = size.minDimension * 0.26f,
            center = Offset(size.width * 0.92f, size.height * 0.14f)
        )
        drawCircle(
            color = LambaOutline.copy(alpha = 0.42f),
            radius = size.minDimension * 0.16f,
            center = Offset(size.width * 0.10f, size.height * 0.94f)
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFEEF4F2)
@Composable
private fun RegistrationScreenPreview() {
    LAMBA_MVPv0Theme {
        RegistrationScreen()
    }
}
