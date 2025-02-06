package com.example.iseeu

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class WelcomeActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_welcome)

        // Button to proceed to Faceoff Game
        val faceoffButton = findViewById<Button>(R.id.faceoffButton)
        faceoffButton.setOnClickListener {
            startActivity(Intent(this, FaceoffActivity::class.java))
            finish()
        }

        // Button to proceed to Icebreaker Game
        val icebreakerButton = findViewById<Button>(R.id.icebreakerButton)
        icebreakerButton.setOnClickListener {
            startActivity(Intent(this, IcebreakerActivity::class.java))
            finish()
        }

        // Button to proceed to Chatbot
        val startButton = findViewById<Button>(R.id.startButton)
        startButton.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        // Button to proceed to EmotionTracker
        val emotionTrackerButton = findViewById<Button>(R.id.emotionTrackerButton)
        emotionTrackerButton.setOnClickListener {
            startActivity(Intent(this, EmotionTrackerActivity::class.java))
            finish()
        }

    }
}