package com.example.iseeu

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import android.util.Log

class ReviewResponsesActivity : AppCompatActivity() {

    private lateinit var questions: List<String>
    private lateinit var responses: Map<String, List<Pair<String, String>>>
    private lateinit var spinner: Spinner
    private lateinit var selectedQuestionTextView: TextView
    private lateinit var responsesListTextView: TextView
    private lateinit var backButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_responses)

        // Get the questions and responses from the intent
        questions = intent.getStringArrayListExtra("questions") ?: emptyList()
        responses = intent.getSerializableExtra("responses") as? Map<String, List<Pair<String, String>>> ?: emptyMap()

        // Log to debug if responses are passed correctly
        Log.d("ReviewResponsesActivity", "Questions received: $questions")
        Log.d("ReviewResponsesActivity", "Responses received: $responses")

        // Initialize views
        spinner = findViewById(R.id.questions_spinner)
        selectedQuestionTextView = findViewById(R.id.selected_question)
        responsesListTextView = findViewById(R.id.responses_list)
        backButton = findViewById(R.id.back_button)

        // Ensure questions list is not empty before setting up Spinner
        if (questions.isEmpty()) {
            selectedQuestionTextView.text = "No questions available."
            responsesListTextView.text = "No responses available."
        } else {
            // Set up the Spinner with questions
            val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, questions)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter

            // Handle Spinner item selection
            spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parentView: AdapterView<*>, view: View?, position: Int, id: Long) {
                    val selectedQuestion = questions[position]
                    selectedQuestionTextView.text = selectedQuestion

                    // Display responses for the selected question
                    val responsesForSelectedQuestion = responses[selectedQuestion]
                    if (responsesForSelectedQuestion != null && responsesForSelectedQuestion.isNotEmpty()) {
                        val responseText = responsesForSelectedQuestion.joinToString("\n") { response ->
                            "Response: ${response.first} (${response.second})"
                        }
                        responsesListTextView.text = responseText
                    } else {
                        responsesListTextView.text = "No responses saved for this question."
                    }
                }

                override fun onNothingSelected(parentView: AdapterView<*>) {
                    selectedQuestionTextView.text = "No question selected."
                    responsesListTextView.text = ""
                }
            }
        }

        // Back button functionality
        backButton.setOnClickListener {
            finish() // This will close the ReviewResponsesActivity and go back to IcebreakerActivity
        }
    }
}
