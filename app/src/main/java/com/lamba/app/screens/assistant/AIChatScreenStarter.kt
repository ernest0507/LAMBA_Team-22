package com.lamba.app.screens.assistant

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
import androidx.compose.material3.Icon
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.unit.sp

@Composable
fun AIChatScreenStarter() {
    Column(
        modifier = Modifier
            .background(Color(0xFFF7F2EA))
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Header()
        HorizontalDivider(color = Color(0xFFE4DDD2))
        Spacer(modifier = Modifier.height(48.dp))
        ListOfQueries()

        Spacer(modifier = Modifier.height(18.dp))
        NewChatButton()
    }


}

@Composable
fun NewChatButton() {
    Button(
        onClick = {},
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF243F7A)
        )
    ){
        Text(
            text = "Новый чат",
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )
    }
}

@Composable
fun Header() {
    Row(
       modifier = Modifier
           .fillMaxWidth()
           .padding(top = 35.dp, start = 10.dp, end= 20.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(modifier = Modifier
            .size(52.dp)
            .background(Color(0xFFF0EADF),  RoundedCornerShape(14.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back"
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Box(
            modifier = Modifier
                .size(52.dp)
                .background(Color(0xFF243F7A), RoundedCornerShape(32.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text("\uD83E\uDD16")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column{
          Text(
              text = "AI-ассистент",
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold
          )

            Text(
                text = "Задайте вопрос об автомобиле",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

    }
}


@Composable
fun ListOfQueries() {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        Text(
            text = "ПОПУЛЯРНЫЕ ВОПРОСЫ",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))

        QueryCard(text = "Когда следующее техобслуживание?")
        Spacer(modifier = Modifier.height(14.dp))

        QueryCard(text = "Сколько я потратил в этом году?")
        Spacer(modifier = Modifier.height(14.dp))

        QueryCard(text = "Что проверить перед дальней поездкой?")
        Spacer(modifier = Modifier.height(14.dp))

        QueryCard(text = "Почему вырос расход топлива?")
        Spacer(modifier = Modifier.height(14.dp))

    }
}


@Composable
fun QueryCard(text : String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0xFFEDE7DA),
                shape = RoundedCornerShape(20.dp)
            ) .padding(20.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(52.dp)
                .background(
                    color = Color(0xFF243F7A),
                    shape = RoundedCornerShape(16.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Text("\uD83D\uDD27")
        }

        Spacer(modifier = Modifier.width(16.dp))

        Text(
            text = text,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}













