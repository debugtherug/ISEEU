package com.example.iseeu

import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val images = arrayOf(
    R.drawable.absent_minded,
    R.drawable.angry,
    R.drawable.bored,
    R.drawable.brave,
    R.drawable.confused,
    R.drawable.disappointed,
    R.drawable.disgusted,
    R.drawable.embarassed,
    R.drawable.excited,
    R.drawable.frustrated,
    R.drawable.happy,
    R.drawable.helpful,
    R.drawable.impatient,
    R.drawable.jealous,
    R.drawable.lonely,
    R.drawable.nervous,
    R.drawable.proud,
    R.drawable.sad,
    R.drawable.scared,
    R.drawable.shy,
    R.drawable.silly,
    R.drawable.sorry,
    R.drawable.surprised,
    R.drawable.thankful,
    R.drawable.thoughtful,
    R.drawable.tired,
    R.drawable.unkind,
    R.drawable.worried
)

val imageDescriptions = arrayOf(
    R.string.image1_description,
    R.string.image2_description,
    R.string.image3_description,
    R.string.image4_description,
    R.string.image5_description,
    R.string.image6_description,
    R.string.image7_description,
    R.string.image8_description,
    R.string.image9_description,
    R.string.image10_description,
    R.string.image11_description,
    R.string.image12_description,
    R.string.image13_description,
    R.string.image14_description,
    R.string.image15_description,
    R.string.image16_description,
    R.string.image17_description,
    R.string.image18_description,
    R.string.image19_description,
    R.string.image20_description,
    R.string.image21_description,
    R.string.image22_description,
    R.string.image23_description,
    R.string.image24_description,
    R.string.image25_description,
    R.string.image26_description,
    R.string.image27_description,
    R.string.image28_description
)

val amatic_sc_FontFamily = FontFamily(
    Font(R.font.amatic_sc_regular), // Regular
    Font(R.font.amatic_sc_bold)     // Bold
)

val dressed_book_FontFamily = FontFamily(
    Font(R.font.dressed_book)      // Regular (assuming there's only one version)
)

val libre_baskerville_FontFamily = FontFamily(
    Font(R.font.libre_baskerville_regular),  // Regular
    Font(R.font.libre_baskerville_bold),     // Bold
    Font(R.font.libre_baskerville_italic)    // Italic
)

val mulish_FontFamily = FontFamily(
    Font(R.font.mulish_regular),        // Regular
    Font(R.font.mulish_bold),           // Bold
    Font(R.font.mulish_italic),         // Italic
    Font(R.font.mulish_light),          // Light
    Font(R.font.mulish_bold_italic),    // Bold Italic
    Font(R.font.mulish_light_italic)    // Light Italic
)

val myriad_pro_FontFamily = FontFamily(
    Font(R.font.myriad_pro_regular),    // Regular
    Font(R.font.myriad_pro_bold),       // Bold
    Font(R.font.myriad_pro_light)       // Light
)

@Composable
fun BakingScreen(
    bakingViewModel: BakingViewModel = viewModel()
) {
    val selectedImage = remember { mutableIntStateOf(-1) }
    val placeholderPrompt = stringResource(R.string.prompt_placeholder)
    var prompt by rememberSaveable { mutableStateOf("") }
    val uiState by bakingViewModel.uiState.collectAsState()
    val context = LocalContext.current

    // State to store the entire conversation
    val conversation = remember { mutableStateListOf<Pair<String, Boolean>>() }
    var isAIResponded by rememberSaveable { mutableStateOf(false) }

    // State to handle if a new chat needs to start
    var isNewChat by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Chatbot title
        Text(
            text = stringResource(R.string.ai_chatbot_title),
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.padding(16.dp)
        )

        // Reset chat logic if it's a new chat
        if (isNewChat) {
            selectedImage.intValue = -1
            conversation.clear()
            prompt = ""
            isAIResponded = false
            isNewChat = false // Reset after starting new chat
        }

        // Top section to display selected emotion
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            if (selectedImage.intValue >= 0) {
                Image(
                    painter = painterResource(images[selectedImage.intValue]),
                    contentDescription = stringResource(imageDescriptions[selectedImage.intValue]),
                    modifier = Modifier.size(120.dp)
                )
            } else {
                Text(
                    text = stringResource(R.string.select_emotion_prompt),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Normal, // Apply font weight
                        letterSpacing = 1.sp,
                        color = MaterialTheme.colorScheme.primary,
                        fontFamily = myriad_pro_FontFamily // Apply the custom font
                    ),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(16.dp)
                )
            }
        }

        // Chat message display
        val scrollState = rememberScrollState()
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            conversation.forEach { (message, isUser) ->
                ChatBubble(message = message, isUser = isUser)
            }

            if (uiState is UiState.Loading) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.padding(16.dp))
                }
            }

            if (uiState is UiState.Error) {
                val errorMessage = (uiState as UiState.Error).errorMessage
                ChatBubble(message = errorMessage, isUser = false, error = true)
            }

            if (uiState is UiState.Success && !isAIResponded) {
                val aiMessage = (uiState as UiState.Success).outputText
                conversation.add(Pair(aiMessage, false))  // Add AI response
                isAIResponded = true
            }
        }

        // Input and send button (only show if emotion is selected and AI has responded)
        if (selectedImage.intValue >= 0 && isAIResponded) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                TextField(
                    value = prompt,
                    label = { Text(stringResource(R.string.label_prompt)) },
                    onValueChange = { prompt = it },
                    modifier = Modifier
                        .weight(0.8f)
                        .padding(end = 16.dp)
                        .align(Alignment.CenterVertically)
                )
                Button(
                    onClick = {
                        val emotionDescription = context.getString(imageDescriptions[selectedImage.intValue])

                        val therapistPrompt = """
                            You are a humorous and supportive therapist that specializes and uses scripts in cognitive behavioral therapy and family-based attachment.
                            The user feels $emotionDescription.
                            Your job is to guide the user through their negative emotions with therapeutic strategies and validate their
                            positive emotions with mindfulness, the goal is to make the user feel better.
                            Listen carefully and provide thoughtful, validating responses,
                            focusing on why the user might feel this way and offering encouragement or advice.
                            Give two to three sentences only or one short paragraph that contains
                            strategies or techniques with a question to ask the user as a follow-up for the next response (only keep asking the question if the user is still engaged and does not indicate that they want to finish the chat),
                            do not repeat strategies or comments and do not repeat positive reinforcement comments.
                            Ask the user how they feel based on positive responses to make sure that the
                            negative emotions have been resolved in order to finish the chat, they can ask again if they need additional support.
                            Stop asking further questions for positive emotions once the user says thank you or indicates that they are done with the conversation jut end the conversation,
                            by highlighting their accomplishments, share their positive emotions and help them apply these strategies in other areas in life and suggest to click on the "New Chat" button to re-evaluate their emotional state being.
                            Stop asking further questions for negative emotions once the user feels better or says thank you and suggest to click on the "New Chat" button to re-evaluate their emotional state being.
                          
                            
                           

                            User: "$prompt"
                            Therapist:
                        """.trimIndent()

                        val bitmap = BitmapFactory.decodeResource(context.resources, images[selectedImage.intValue])
                        bakingViewModel.sendPrompt(bitmap, therapistPrompt)

                        conversation.add(Pair(prompt, true))  // Add user's message to the conversation
                        prompt = ""  // Clear input
                        isAIResponded = false
                    },
                    enabled = prompt.isNotEmpty(),
                    modifier = Modifier.align(Alignment.CenterVertically)
                ) {
                    Text(text = stringResource(R.string.action_go))
                }
            }
        }

        // Image selection section (only visible when no image is selected)
        if (selectedImage.intValue == -1) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(3),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(456.dp)
            ) {
                itemsIndexed(images) { index, image ->
                    val description = stringResource(imageDescriptions[index])
                    var imageModifier = Modifier
                        .padding(8.dp)
                        .requiredSize(100.dp)
                        .clickable {
                            selectedImage.intValue = index

                            // Trigger AI response directly when emotion is selected
                            initiateAIResponse(
                                context = context,
                                index = index,
                                conversation = conversation,
                                bakingViewModel = bakingViewModel
                            )
                        }

                    // Highlight the selected image
                    if (index == selectedImage.intValue) {
                        imageModifier = imageModifier.border(
                            BorderStroke(4.dp, MaterialTheme.colorScheme.primary)
                        )
                    }

                    Image(
                        painter = painterResource(image),
                        contentDescription = description,
                        modifier = imageModifier
                    )
                }
            }
        }

        // Back Button and New Chat Button
        Row(
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 6.dp)
        ) {
            Button(
                onClick = {
                    val intent = Intent(context, WelcomeActivity::class.java)
                    context.startActivity(intent)
                },
                modifier = Modifier
                    .padding(end = 8.dp)
            ) {
                Text(text = "Back")
            }

            // New Chat Button (only show when emotion is selected and after a chat has happened)
            if (selectedImage.intValue >= 0 && isAIResponded) {
                Button(
                    onClick = {
                        isNewChat = true // Reset the chat state
                    },
                    modifier = Modifier
                ) {
                    Text(text = "New Chat")
                }
            }
        }
    }
}

private fun initiateAIResponse(
    context: Context,
    index: Int,
    conversation: SnapshotStateList<Pair<String, Boolean>>,
    bakingViewModel: BakingViewModel
) {
    val emotionDescription = context.getString(imageDescriptions[index])

    val therapistPrompt = """
                            You are a humorous and supportive therapist that specializes and uses scripts in cognitive behavioral therapy and family-based attachment.
                            The user feels $emotionDescription.
                            Your job is to guide the user through their negative emotions with therapeutic strategies and validate their
                            positive emotions with mindfulness, the goal is to make the user feel better.
                            Listen carefully and provide thoughtful, validating responses,
                            focusing on why the user might feel this way and offering encouragement or advice.
                            Give two to three sentences only or one short paragraph that contains
                            strategies or techniques with a question to ask the user as a follow-up for the next response (only keep asking the question if the user is still engaged and does not indicate that they want to finish the chat),
                            do not repeat strategies or comments and do not repeat positive reinforcement comments.
                            Ask the user how they feel based on positive responses to make sure that the
                            negative emotions have been resolved in order to finish the chat, they can ask again if they need additional support.
                            Stop asking further questions for positive emotions once the user says thank you or indicates that they are done with the conversation jut end the conversation,
                            by highlighting their accomplishments, share their positive emotions and help them apply these strategies in other areas in life and suggest to click on the "New Chat" button to re-evaluate their emotional state being.
                            Stop asking further questions for negative emotions once the user feels better or says thank you and suggest to click on the "New Chat" button to re-evaluate their emotional state being.
                          
                            
                           

                            User: ""
                            Therapist:
                        """.trimIndent()

    val bitmap = BitmapFactory.decodeResource(context.resources, images[index])

    // Trigger the AI with the emotion-based prompt
    bakingViewModel.sendPrompt(bitmap, therapistPrompt)
}


@Composable
fun ChatBubble(message: String, isUser: Boolean, error: Boolean = false) {
    val backgroundColor = when {
        isUser -> Color(0xFF007AFF) // User Chat Bubble Color
        error -> Color(0xFFFFCDD2) // Error Chat Bubble Color
        else -> Color(0xFF37ACA8) // AI Chat Bubble Color
    }
    val textColor = if (error) Color.Black else Color.White
    val shape = if (isUser) {
        RoundedCornerShape(16.dp, 16.dp, 0.dp, 16.dp) // User messages have rounded corners on the right
    } else {
        RoundedCornerShape(16.dp, 16.dp, 16.dp, 0.dp) // AI messages have rounded corners on all sides
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (isUser) Arrangement.End else Arrangement.Start
    ) {
        Surface(
            color = backgroundColor,
            shape = shape,
            modifier = Modifier
                .padding(8.dp)
                .border(
                    width = 1.dp,
                    color = if (error) Color.Red else Color.Transparent,
                    shape = shape
                )
        ) {
            Text(
                text = message,
                color = textColor,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontFamily = myriad_pro_FontFamily, // Apply custom font family
                    fontStyle = if (error) FontStyle.Italic else FontStyle.Normal,
                    fontSize = 18.sp
                )
            )
        }
    }
}
