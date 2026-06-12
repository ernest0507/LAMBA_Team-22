package com.lamba.app.screens.assistant

import androidx.compose.foundation.Image
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.ui.graphics.vector.ImageVector


@Composable
fun AiChatScreen() {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2EA))
            .padding(15.dp)
    ) {
        Header()

        HorizontalDivider(color = Color(0xFFE4DDD2))
        Spacer(modifier = Modifier.height(24.dp))

        MessageArea(
            userMessage = "Когда следующее техосбслуживание?",
            assistantMessage = "Следующее ТО рекомендуется через 3 240 км."

        )

        Spacer(modifier = Modifier.weight(1f))
        InputArea()
    }
}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 12.dp, end = 20.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color(0xFFF0EADF), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
            )
        }
        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color(0xFF243F7A), RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("\uD83E\uDD16")
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = "AI-ассистент",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "● в сети",
                color = Color(0xFF2E8B3C)
            )
        }
    }
}

@Composable
fun MessageArea(
    userMessage: String,
    assistantMessage: String
) {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {

        ChatBubble(
            text = userMessage,
            isUser = true
        )
        Spacer(modifier = Modifier.height(16.dp))

        ChatBubble(
            text = assistantMessage,
            isUser = false
        )
    }
}


@Composable
fun ChatBubble(
    text: String,
    isUser: Boolean
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Box(
            modifier = Modifier
                .background(
                    color = if (isUser) Color(0xFF243F7A) else Color(0xFFEDE7DA),
                    shape = RoundedCornerShape(18.dp)
                )
                .padding(16.dp)
        ) {
            Text(
                text = text,
                color = if (isUser) Color.White else Color(0xFF2B2522)
            )
        }
    }
}

@Composable
fun InputArea() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F2EA))
            .padding(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            placeholder = { Text("Введите вопрос...") },
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(14.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Button(
            onClick = {},
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(5.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF243F7A)
            )
        ) {
            Icon(
                imageVector = Icons.Default.Add, contentDescription = "Add"
            )
        }

        Spacer(modifier = Modifier.width(6.dp))

        Button(
            onClick = {},
            modifier = Modifier.size(48.dp),
            shape = RoundedCornerShape(16.dp),
            contentPadding = PaddingValues(0.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xFF243F7A)
            )
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.Send, contentDescription = "Send"
            )
        }


    }


}

