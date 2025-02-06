package com.example.iseeu

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlin.random.Random

class IcebreakerActivity : AppCompatActivity() {

    private val questionsWithEmotions = listOf(
        "What do I feel like doing when I’m feeling SAD?",
        "How does my body react when I’m feeling ANGRY?",
        "Is there another way to think about feeling DISAPPOINTED?",
        "Is it okay to feel EMBARASSED?",
        "What do I need when I feel DISGUSTED?",
        "What do I hope other people would do for me when I’m feeling FRUSTRATED?",
        "How can I tell if someone is feeling IMPATIENT?",
        "What would you say to someone when they’re feeling JEALOUS?",
        "How can I help when someone is feeling LONELY?",
        "How do you feel when your friend is feeling NERVOUS?",
        "Who can you talk to when you’re feeling PROUD?",
        "Describe a time when you feel SCARED.",
        "When you feel SHY last time, was your response appropriate to the situation?",
        "Can you use an analogy or metaphor to describe when you feel SORRY?",
        "Describe your facial expression when you feel TIRED.",
        "What color does it appear when you feel WORRY?",
        "Describe the last time you felt ABSENT-MINDED. What was the situation like?",
        "Think of a book or movie character when they felt BORED.",
        "When you are feeling CONFUSED, which animal would you associate with this feeling? Why?",
        "Use another word to describe the most intense level of feeling THOUGHTFUL.",
        "What do you avoid doing when you feel SILLY?"
    )

    private val responses: MutableMap<String, MutableList<Pair<String, String>>> = mutableMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_icebreaker)

        val backButton: Button = findViewById(R.id.back_welcome_Button)
        val questionTextView: TextView = findViewById(R.id.icebreakerPlaceholderText)
        val userInputEditText: EditText = findViewById(R.id.userInputEditText)
        val nextQuestionButton: Button = findViewById(R.id.generate_question_button)
        val submitResponseButton: Button = findViewById(R.id.submit_response_button)
        val reviewResponsesButton: Button = findViewById(R.id.review_responses_button)


        // Show the rules dialog when the activity starts
        showRulesDialog()

        // Back button functionality
        backButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Generate and display the first random question
        displayRandomQuestion(questionTextView)

        nextQuestionButton.setOnClickListener {
            displayRandomQuestion(questionTextView)
        }

        // Submit response functionality
        submitResponseButton.setOnClickListener {
            val userInput = userInputEditText.text.toString().trim()
            if (userInput.isNotBlank()) {
                val currentQuestion = questionTextView.text.toString()
                saveResponse(userInput, currentQuestion)
                Toast.makeText(this, "Response submitted!", Toast.LENGTH_SHORT).show()
                userInputEditText.text.clear()
            } else {
                Toast.makeText(this, "Please enter a response before submitting.", Toast.LENGTH_SHORT).show()
            }
        }

        // Review responses button functionality
        reviewResponsesButton.setOnClickListener {
            val intent = Intent(this, ReviewResponsesActivity::class.java)
            intent.putExtra("questions", ArrayList(questionsWithEmotions)) // Pass questions list
            intent.putExtra("responses", HashMap(responses))  // Pass responses as a HashMap
            startActivity(intent)
        }
    }

    private fun showRulesDialog() {
        val rulesMessage = """
            
            This game is to store responses to help train AI and make it become more familiar with you. 🤖💬
            
            Your answers will be stored in a list, which can later be retrieved and used to help the AI get to know you better, providing more personalized responses. 💡💬
            
            Ready to play? Let's get started! 🚀
        """.trimIndent()

        val builder = AlertDialog.Builder(this)
            .setTitle("Icebreaker Game Rules")
            .setMessage(rulesMessage)
            .setCancelable(false) // Prevent dismissing the dialog by tapping outside
            .setPositiveButton("Got it!") { dialog, _ ->
                dialog.dismiss() // Close the dialog when the user clicks "Got it!"
            }

        val dialog = builder.create()
        dialog.show()
    }

    private fun displayRandomQuestion(questionTextView: TextView) {
        val randomQuestion = questionsWithEmotions[Random.nextInt(questionsWithEmotions.size)]
        questionTextView.text = randomQuestion
    }

    private fun saveResponse(response: String, question: String) {
        val timestamp = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault()).format(System.currentTimeMillis())
        responses.computeIfAbsent(question) { mutableListOf() }.add(response to timestamp)

        // Debugging: Log the response to confirm it's saved
        Log.d("IcebreakerActivity", "Response saved: $response for question: $question at $timestamp")
    }
}
