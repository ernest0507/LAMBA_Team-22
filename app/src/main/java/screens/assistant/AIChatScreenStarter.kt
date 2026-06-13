package screens.assistant

import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.ui.unit.sp


@Composable
fun AIChatScreenStarter(
    onQuestionClick: (String) -> Unit,
    onNewChatClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(Color(0xFFF7F2EA))
            .fillMaxSize()
            .padding(20.dp)
    ) {
        Header()
        HorizontalDivider(color = Color(0xFFE4DDD2))
        Spacer(modifier = Modifier.height(48.dp))
        ListOfQueries(
            onQuestionClick = onQuestionClick
        )

        Spacer(modifier = Modifier.height(18.dp))
        NewChatButton(
            onNewChatClick = onNewChatClick
        )
    }


}

@Composable
fun NewChatButton(
    onNewChatClick: () -> Unit
) {
    Button(
        onClick = onNewChatClick,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(16.dp),
        contentPadding = PaddingValues(0.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF243F7A)
        )
    ){
        Text(
            text = "New Chat",
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
              text = "AI Assistant",
              fontSize = 24.sp,
              fontWeight = FontWeight.Bold
          )

            Text(
                text = "Ask a question about your car",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }

    }
}


@Composable
fun ListOfQueries(
    onQuestionClick: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
    )
    {
        Text(
            text = "POPULAR QUESTIONS",
            fontSize = 14.sp,
            color = Color.Gray
        )
        Spacer(modifier = Modifier.height(12.dp))

        QueryCard(
            text = "When is the next maintenance?",
            onQuestionClick = onQuestionClick
        )
        Spacer(modifier = Modifier.height(14.dp))

        QueryCard(
            text = "How much have I spent this year?",
            onQuestionClick = onQuestionClick)
        Spacer(modifier = Modifier.height(14.dp))

        QueryCard(
            text = "What to check before a long trip?",
            onQuestionClick = onQuestionClick)
        Spacer(modifier = Modifier.height(14.dp))

        QueryCard(
            text = "Why has fuel consumption increased?",
            onQuestionClick = onQuestionClick)
        Spacer(modifier = Modifier.height(14.dp))
    }
}


@Composable
fun QueryCard(
    text : String,
    onQuestionClick: (String) -> Unit
    ) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onQuestionClick(text)
            }
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
