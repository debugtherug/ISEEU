package com.example.iseeu

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class EmotionTrackerActivity : AppCompatActivity() {

    private lateinit var backButton: Button
    private lateinit var calendarView: CalendarView
    private lateinit var emotionSpinner: Spinner // Spinner for emotion selection
    private lateinit var emotionImageView: ImageView // ImageView to show emotion image
    private val emotions = listOf("Select Emotion", "Happy", "Sad", "Angry", "Excited", "Scared", "Nervous", "Bored", "Frustrated", "Shy", "Worried", "Tired", "Lonely", "Brave", "Confused", "Disappointed", "Disgusted", "Embarassed", "Helpful", "Impatient", "Jealous", "Proud", "Silly", "Sorry", "Surprised", "Thankful", "Thoughtful", "Unkind", "Absent Minded")
    private val emotionImages = mapOf(
        "Happy" to R.drawable.happy, // Replace with your actual drawable resources
        "Sad" to R.drawable.sad,
        "Angry" to R.drawable.angry, // Example: angry.png
        "Excited" to R.drawable.excited,
        "Scared" to R.drawable.scared,
        "Nervous" to R.drawable.nervous,
        "Bored" to R.drawable.bored,
        "Frustrated" to R.drawable.frustrated,
        "Shy" to R.drawable.shy,
        "Worried" to R.drawable.worried,
        "Tired" to R.drawable.tired,
        "Lonely" to R.drawable.lonely,
        "Brave" to R.drawable.brave,
        "Confused" to R.drawable.confused,
        "Disappointed" to R.drawable.disappointed,
        "Disgusted" to R.drawable.disgusted,
        "Embarassed" to R.drawable.embarassed,
        "Helpful" to R.drawable.helpful,
        "Impatient" to R.drawable.impatient,
        "Jealous" to R.drawable.jealous,
        "Proud" to R.drawable.proud,
        "Silly" to R.drawable.silly,
        "Sorry" to R.drawable.sorry,
        "Surprised" to R.drawable.surprised,
        "Thankful" to R.drawable.thankful,
        "Thoughtful" to R.drawable.thoughtful,
        "Unkind" to R.drawable.unkind,
        "Absent Minded" to R.drawable.absent_minded
    )
    private val emotionData = mutableMapOf<String, String>() // Stores date and emotion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_emotion_tracker)

        calendarView = findViewById(R.id.calendarView)
        emotionSpinner = findViewById(R.id.emotionSpinner)
        backButton = findViewById(R.id.back_welcome_Button)
        emotionImageView = findViewById(R.id.emotionImageView) // ImageView to show selected emotion

        // Setup Spinner with emotion names
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, emotions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        emotionSpinner.adapter = adapter

        // Set the default selection to "Select Emotion"
        emotionSpinner.setSelection(0)

        // Set default image to placeholder (R.drawable.my_emotions)
        emotionImageView.setImageResource(R.drawable.my_emotions)

        // Load saved data (from SharedPreferences)
        loadEmotionData()

        // Handle emotion selection from Spinner
        emotionSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parentView: AdapterView<*>, selectedView: View, position: Int, id: Long) {
                val selectedEmotion = emotions[position]

                // If "Select Emotion" is selected, do nothing
                if (selectedEmotion == "Select Emotion") {
                    emotionImageView.setImageResource(R.drawable.my_emotions) // Show default image
                    return
                }

                val selectedDate = getCurrentDate()

                // Save emotion data and update the calendar
                emotionData[selectedDate] = selectedEmotion
                saveEmotionData()

                // Update the emotion image
                emotionImageView.setImageResource(emotionImages[selectedEmotion] ?: R.drawable.my_emotions)
                Toast.makeText(this@EmotionTrackerActivity, "Emotion saved for today!", Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(parentView: AdapterView<*>) {
                // Do nothing
            }
        }

        // Handle calendar date selection
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val selectedDate = getFormattedDate(year, month, dayOfMonth)

            // Check if there's an emotion saved for the selected date
            val emotion = emotionData[selectedDate]
            if (emotion != null) {
                // Set the corresponding emotion image
                emotionImageView.setImageResource(emotionImages[emotion] ?: R.drawable.my_emotions)
            } else {
                // No emotion saved, set a default or clear image
                emotionImageView.setImageResource(R.drawable.my_emotions)
            }
        }

        // Back button functionality
        backButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    // Function to get the current date in a formatted string
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date(calendarView.date))
    }

    // Function to get formatted date from calendar view selection
    private fun getFormattedDate(year: Int, month: Int, dayOfMonth: Int): String {
        val formattedMonth = if (month + 1 < 10) "0${month + 1}" else (month + 1).toString()
        val formattedDay = if (dayOfMonth < 10) "0$dayOfMonth" else dayOfMonth.toString()
        return "$year-$formattedMonth-$formattedDay"
    }

    // Function to save emotion data to SharedPreferences
    private fun saveEmotionData() {
        val sharedPref = getSharedPreferences("EmotionTracker", MODE_PRIVATE)
        with(sharedPref.edit()) {
            emotionData.forEach { (date, emotion) ->
                putString(date, emotion)
            }
            apply()
        }
    }

    // Function to load emotion data from SharedPreferences
    private fun loadEmotionData() {
        val sharedPref = getSharedPreferences("EmotionTracker", MODE_PRIVATE)
        sharedPref.all.forEach { entry ->
            emotionData[entry.key] = entry.value.toString()
        }
    }
}
