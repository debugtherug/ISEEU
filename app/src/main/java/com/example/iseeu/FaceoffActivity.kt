package com.example.iseeu

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.Collections

class FaceoffActivity : AppCompatActivity() {

    private lateinit var emotionTextView: TextView
    private lateinit var scoreTextView: TextView
    private lateinit var backButton: Button
    private lateinit var emotionImageView: ImageView
    private lateinit var correctButton: Button
    private lateinit var incorrectButton: Button
    private lateinit var timerTextView: TextView
    private lateinit var endGameButton: Button
    private lateinit var timeChangeIndicator: TextView

    private val emotions = listOf(
        "Absent Minded",
        "Angry",
        "Bored",
        "Brave",
        "Confused",
        "Disappointed",
        "Disgusted",
        "Embarrassed",
        "Excited",
        "Frustrated",
        "Happy",
        "Helpful",
        "Impatient",
        "Jealous",
        "Lonely",
        "Nervous",
        "Proud",
        "Sad",
        "Scared",
        "Shy",
        "Silly",
        "Sorry",
        "Surprised",
        "Thankful",
        "Thoughtful",
        "Tired",
        "Unkind",
        "Worried"
    )

    private val emotionImages = arrayOf(
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

    private var shuffledEmotions = emotions.toMutableList()
    private var shuffledImages = emotionImages.copyOf()
    private var currentEmotionIndex = 0
    private var correctGuesses = 0
    private var incorrectGuesses = 0
    private var incorrectTriesForCurrentEmotion = 0
    private var roundActive = true
    private val correctEmotions = mutableListOf<String>()
    private val incorrectEmotions = mutableListOf<String>()

    private var timer: CountDownTimer? = null
    private var timeLeftInMillis: Long = 3 * 60 * 1000 // 2 minutes in milliseconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_faceoff)

        emotionTextView = findViewById(R.id.emotionTextView)
        scoreTextView = findViewById(R.id.scoreTextView)
        backButton = findViewById(R.id.backButton)
        emotionImageView = findViewById(R.id.emotionImageView)
        correctButton = findViewById(R.id.correctButton)
        incorrectButton = findViewById(R.id.incorrectButton)
        timerTextView = findViewById(R.id.timerTextView)
        endGameButton = findViewById(R.id.endGameButton) // Initialize End Game button

        // Shuffle the emotions and images
        shuffleEmotions()

        // Show the game instructions
        showGameInstructions()

        // Back button functionality
        backButton.setOnClickListener {
            val intent = Intent(this, WelcomeActivity::class.java)
            startActivity(intent)
            finish()

        }

        // Initialize the TextView for time change indicator
        timeChangeIndicator = TextView(this)
        timeChangeIndicator.setTextSize(18f)
        timeChangeIndicator.setTextColor(Color.WHITE)
        timeChangeIndicator.setGravity(Gravity.CENTER)
        timeChangeIndicator.visibility = View.GONE
        // Add it to your layout (above or below the timerTextView)
        val layout = findViewById<LinearLayout>(R.id.mainLayout)  // Assuming mainLayout exists
        layout.addView(timeChangeIndicator)

        // Correct button functionality
        correctButton.setOnClickListener {
            if (roundActive) {
                correctGuesses++
                correctEmotions.add(shuffledEmotions[currentEmotionIndex])
                showCorrectFeedback()

                // Add 3 seconds to the timer
                timeLeftInMillis += 3000  // Adding 3 seconds
                restartTimer()  // Restart the timer with updated time
            }
        }

        // Incorrect button functionality
        incorrectButton.setOnClickListener {
            if (roundActive) {
                incorrectGuesses++
                incorrectEmotions.add(shuffledEmotions[currentEmotionIndex])
                showIncorrectFeedback()

                // Subtract 5 seconds from the timer
                if (timeLeftInMillis - 5000 >= 0) {
                    timeLeftInMillis -= 5000  // Subtracting 5 seconds
                    restartTimer()  // Restart the timer with updated time
                }
            }
        }

        // End Game button functionality
        endGameButton.setOnClickListener {
            roundActive = false
            timer?.cancel()  // Stop the timer
            showFinalScore()
        }
    }

    // Helper function to restart the timer with updated time
    private fun restartTimer() {
        timer?.cancel()  // Stop the current timer
        startTimer()  // Restart the timer with updated timeLeftInMillis
    }

    private fun getEmojiResId(emotion: String): Int {
        val emotionIndex =
            emotions.indexOf(emotion) // Get the index of the emotion in the `emotions` list
        return if (emotionIndex != -1) {
            emotionImages[emotionIndex] // Use the index to get the corresponding image from the `emotionImages` array
        } else {
            R.drawable.my_emotions // A fallback emoji in case the emotion is not found
        }
    }


    private fun showGameInstructions() {
        val message = """
    🤳 TEAM 1 holds the phone without looking at the screen and watches TEAM 2 act out an emotion.

    🎭 TEAM 2, your job is to act out the emotion, and TEAM 1 has to guess.

    TEAM 2, click:
    ✅ CORRECT if TEAM 1 got it right.
    ❌ INCORRECT if TEAM 1 got it wrong.

    ⏰ You have 3 minutes to play!

    Let’s have fun! 🎉
""".trimIndent()


        val builder = AlertDialog.Builder(this)
        builder.setTitle("Let’s Play!")
        builder.setMessage(message)

        builder.setPositiveButton("Let’s Go! 🚀") { dialog, _ ->
            dialog.dismiss()
            startGame()
            startTimer()  // Start the timer when the game begins
        }
        builder.setCancelable(false) // Prevent the user from dismissing the dialog accidentally
        builder.show()
    }

    private fun startGame() {
        correctGuesses = 0
        incorrectGuesses = 0
        currentEmotionIndex = 0
        roundActive = true
        correctEmotions.clear()
        incorrectEmotions.clear()
        displayNextEmotion()
    }

    private fun shuffleEmotions() {
        // Shuffle the emotions and corresponding images
        val combinedList = emotions.zip(emotionImages).toMutableList()
        combinedList.shuffle()
        shuffledEmotions = combinedList.map { it.first }.toMutableList()
        shuffledImages = combinedList.map { it.second }.toTypedArray()
    }

    private fun displayNextEmotion() {
        if (currentEmotionIndex < shuffledEmotions.size) {
            val emotion = shuffledEmotions[currentEmotionIndex]
            val imageResId =
                shuffledImages[currentEmotionIndex] // Get the image corresponding to the emotion

            emotionTextView.text = emotion
            emotionImageView.setImageResource(imageResId)
        } else {
            roundActive = false
            showFinalScore()
        }
    }

    private fun startTimer() {
        timer = object : CountDownTimer(timeLeftInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInMillis = millisUntilFinished
                updateTimer()
            }

            override fun onFinish() {
                roundActive = false
                showFinalScore()
            }
        }.start()
    }

    private fun updateTimer() {
        val minutes = (timeLeftInMillis / 1000) / 60  // Get minutes
        val seconds = (timeLeftInMillis / 1000) % 60 // Get seconds

        // Format time to always show two digits for seconds
        val formattedTime = String.format("%02d:%02d", minutes, seconds)
        timerTextView.text = "Time Left: $formattedTime"
    }

    private fun showCorrectFeedback() {
        // Show success Toast message
        Toast.makeText(this, "Correct! You gained 3 seconds!", Toast.LENGTH_SHORT).show()

        // Bounce animation for correct answer (scale up and down)
        emotionImageView.animate().scaleX(1.5f).scaleY(1.5f).setDuration(300).withEndAction {
            emotionImageView.animate().scaleX(1f).scaleY(1f).setDuration(300)
        }

        // Change background color to green for a correct guess
        val mainLayout = findViewById<View>(R.id.mainLayout)
        mainLayout.setBackgroundColor(Color.parseColor("#28a745"))
        emotionTextView.setTextColor(Color.WHITE)

        // Force layout redraw
        mainLayout.requestLayout()

        // Add a small delay before moving to the next emotion
        emotionImageView.postDelayed({
            // Reset background color after the delay
            mainLayout.setBackgroundColor(Color.WHITE)
            emotionTextView.setTextColor(Color.BLACK)
            moveToNextEmotion()
        }, 1000)  // Delay set to 1 second
    }

    private fun showIncorrectFeedback() {
        // Show error Toast message
        Toast.makeText(this, "Incorrect! You lost 5 seconds!!", Toast.LENGTH_SHORT).show()

        // Aggressive wiggle animation for incorrect answer (shake left-right)
        emotionImageView.animate().translationX(50f).setDuration(100).withEndAction {
            emotionImageView.animate().translationX(-50f).setDuration(100).withEndAction {
                emotionImageView.animate().translationX(30f).setDuration(100).withEndAction {
                    emotionImageView.animate().translationX(-30f).setDuration(100).withEndAction {
                        emotionImageView.animate().translationX(0f).setDuration(100)
                    }
                }
            }
        }

        // Change background color to red for an incorrect guess
        val mainLayout = findViewById<View>(R.id.mainLayout)
        mainLayout.setBackgroundColor(Color.parseColor("#dc3545"))
        emotionTextView.setTextColor(Color.WHITE)

        // Force layout redraw
        mainLayout.requestLayout()

        // Add a small delay before moving to the next emotion
        emotionImageView.postDelayed({
            // Reset background color after the delay
            mainLayout.setBackgroundColor(Color.WHITE)
            emotionTextView.setTextColor(Color.BLACK)
            moveToNextEmotion()
        }, 1000)  // Delay set to 1 second
    }

    private fun moveToNextEmotion() {
        currentEmotionIndex++
        if (currentEmotionIndex < shuffledEmotions.size) {
            displayNextEmotion()
        } else {
            roundActive = false
            showFinalScore()
        }
    }

    private fun showFinalScore() {
        // Stop the timer if the game ends
        timer?.cancel()

        // Hide game elements on final score page
        emotionImageView.visibility = View.GONE
        emotionTextView.visibility = View.GONE
        correctButton.visibility = View.GONE
        incorrectButton.visibility = View.GONE
        timerTextView.visibility = View.GONE

        // Show score text view
        scoreTextView.visibility = View.VISIBLE

        // Display the final score
        scoreTextView.text = "Game Over! 🎮\n\n" +
                "🎯 Correct: $correctGuesses\n" +
                "❌ Incorrect: $incorrectGuesses"

        // Clear previous views in the score container
        val correctEmotionsContainer = findViewById<LinearLayout>(R.id.correctEmotionsContainer)
        val incorrectEmotionsContainer = findViewById<LinearLayout>(R.id.incorrectEmotionsContainer)

        correctEmotionsContainer.removeAllViews()
        incorrectEmotionsContainer.removeAllViews()

        // Make the containers visible
        correctEmotionsContainer.visibility = View.VISIBLE
        incorrectEmotionsContainer.visibility = View.VISIBLE

        // Add header for Correct Emotions
        val correctHeaderText = TextView(this)
        correctHeaderText.text = "✅ Correct\n"
        correctHeaderText.textSize = 18f
        correctHeaderText.setTextColor(Color.BLACK)
        correctHeaderText.gravity = android.view.Gravity.CENTER
        correctEmotionsContainer.addView(correctHeaderText)

        // Create a horizontal layout for correct emotions (emoji row)
        val correctEmotionsRow = LinearLayout(this)
        correctEmotionsRow.orientation = LinearLayout.HORIZONTAL
        correctEmotionsRow.gravity = Gravity.CENTER
        correctEmotionsContainer.addView(correctEmotionsRow)

        // Add correct emotions emoji images to the row
        if (correctEmotions.isNotEmpty()) {
            correctEmotions.forEach { emotion ->
                val emojiImageView = ImageView(this)
                emojiImageView.setImageResource(getEmojiResId(emotion)) // Get emoji image for the emotion
                val layoutParams = LinearLayout.LayoutParams(150, 150) // Fixed size for the images
                layoutParams.setMargins(30, 10, 30, 10) // Add some margin for better spacing
                emojiImageView.layoutParams = layoutParams

                correctEmotionsRow.addView(emojiImageView)  // Add to the horizontal row
            }
        } else {
            val noCorrectText = TextView(this)
            noCorrectText.text = "No correct emotions guessed 😔"
            correctEmotionsRow.addView(noCorrectText)  // Add to the row
        }

        // Add header for Incorrect Emotions
        val incorrectHeaderText = TextView(this)
        incorrectHeaderText.text = "❌ Incorrect\n"
        incorrectHeaderText.textSize = 18f
        incorrectHeaderText.setTextColor(Color.BLACK)
        incorrectHeaderText.gravity = android.view.Gravity.CENTER
        incorrectEmotionsContainer.addView(incorrectHeaderText)

        // Create a horizontal layout for incorrect emotions (emoji row)
        val incorrectEmotionsRow = LinearLayout(this)
        incorrectEmotionsRow.orientation = LinearLayout.HORIZONTAL
        incorrectEmotionsRow.gravity = Gravity.CENTER
        incorrectEmotionsContainer.addView(incorrectEmotionsRow)

        // Add incorrect emotions emoji images to the row
        if (incorrectEmotions.isNotEmpty()) {
            incorrectEmotions.forEach { emotion ->
                val emojiImageView = ImageView(this)
                emojiImageView.setImageResource(getEmojiResId(emotion)) // Get emoji image for the emotion
                val layoutParams = LinearLayout.LayoutParams(150, 150) // Fixed size for the images
                layoutParams.setMargins(30, 10, 30, 10) // Add some margin for better spacing
                emojiImageView.layoutParams = layoutParams

                incorrectEmotionsRow.addView(emojiImageView)  // Add to the horizontal row
            }
        } else {
            val noIncorrectText = TextView(this)
            noIncorrectText.text = "No incorrect emotions guessed 😎"
            incorrectEmotionsRow.addView(noIncorrectText)  // Add to the row
        }

        // Show End Game and Back Button
        endGameButton.visibility = View.VISIBLE
        backButton.visibility = View.VISIBLE
    }
}