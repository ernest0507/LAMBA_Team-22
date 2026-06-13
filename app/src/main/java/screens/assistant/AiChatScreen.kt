package screens.assistant

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.foundation.lazy.items

@Composable
fun AiChatScreen(
    userMessage: String,
) {
    val message = remember {
        mutableStateListOf<ChatMessage>()
    }

    LaunchedEffect(userMessage) {
        if (userMessage.isNotBlank()) {
            message.add(
                ChatMessage(
                    text = userMessage,
                    type = MessageType.USER
                )
            )
        }

        message.add(
            ChatMessage(
                text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Commodo cupidatat adipiscing sint id laboris sunt sed eiusmod do non ea lorem nostrud duis dolore. ",
                type = MessageType.ASSISTANT
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F2EA))
            .padding(15.dp)
    ) {
        ChatHeader()

        HorizontalDivider(color = Color(0xFFF7F2EA))
        Spacer(modifier = Modifier.height(24.dp))

        MessageArea(
            messages = message,
            modifier = Modifier.weight(1f)
        )

        InputArea(onSendMessage = {
            text ->
            if (text.isNotBlank()) {
                message.add(
                    ChatMessage(
                        text = text,
                        type = MessageType.USER
                    )
                )

                message.add(
                    ChatMessage(
                        text = "This is a temporary AI assistant response for MVP",
                        type = MessageType.ASSISTANT
                    )
                )
            }
        })
    }
}

@Composable
fun ChatHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 30.dp, start = 12.dp, end = 20.dp, bottom = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .background(Color(0xFFEDE7DA), RoundedCornerShape(14.dp)),
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
                text = "AI Assistant",
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "● online",
                color = Color(0xFF2E8B3C)
            )
        }
    }
}

@Composable
fun MessageArea(
    messages: List<ChatMessage>,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier.fillMaxWidth()
    ) {

        items(messages) { message ->
            ChatBubble(
                text = message.text,
                type = message.type
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

enum class MessageType {
    USER,
    ASSISTANT,
    ERROR
}

@Composable
fun BotAvatar() {
    Box(
        modifier = Modifier
            .size(44.dp)
            .padding(top = 2.dp)
            .background(Color(0xFF243F7A), RoundedCornerShape(14.dp)),
        Alignment.Center
    )
    {
        Text("\uD83E\uDD16")
    }

}

@Composable
fun InputArea(
    onSendMessage: (String) -> Unit
) {

    var message by remember { mutableStateOf("") }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {

        OutlinedTextField(
            value = message,
            onValueChange = {
                message = it
            },
            modifier = Modifier.weight(1f)
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
            onClick = {
                onSendMessage(message)
                message = ""
                      },
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

@Composable
fun ChatBubble(
    text: String,
    type: MessageType
) {
    when (type) {
        MessageType.ASSISTANT -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start
            ) {
                BotAvatar()
                Spacer(modifier = Modifier.width(8.dp))
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFFEDE7DA),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = text,
                        color = Color(0xFF2B2522)
                    )
                }
            }
        }

        MessageType.USER -> {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Box(
                    modifier = Modifier
                        .background(
                            color = Color(0xFF243F7A),
                            shape = RoundedCornerShape(18.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = text,
                        color = Color.White
                    )
                }
            }
        }

        MessageType.ERROR -> {
            Column(
                modifier = Modifier
                    .fillMaxHeight()
            ) {
                Row(
                    modifier = Modifier
                        .padding(end = 48.dp)
                        .fillMaxWidth(),

                    horizontalArrangement = Arrangement.Start
                ) {
                    BotAvatar()
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFFFF7E7E),
                                shape = RoundedCornerShape(18.dp)
                            )
                            .padding(16.dp)

                    ) {
                        Text(
                            text = "Something went wrong. Failed to connect to the server"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .padding(start = 40.dp, end = 36.dp),
                    shape = RoundedCornerShape(16.dp),
                    contentPadding = PaddingValues(0.dp),
                ) {
                    Text("⟲ Retry")
                }
            }

        }
    }
}


data class ChatMessage(
    val text : String,
    val type : MessageType
)
