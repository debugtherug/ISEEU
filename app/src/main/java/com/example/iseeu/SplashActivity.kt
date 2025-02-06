package com.example.iseeu

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.iseeu.ui.theme.ISEEUTheme

class SplashActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Use your existing theme
        setContent {
            ISEEUTheme {
                // Display the splash screen layout
                SplashScreen()
            }
        }

        // Transition to MainActivity after a delay
        Handler().postDelayed({
            startActivity(Intent(this, WelcomeActivity::class.java))
            finish()
        }, 2500) // 2.5 seconds delay
    }
}

@Composable
fun SplashScreen() {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(id = R.drawable.iseeu_splash),
            contentDescription = null, // Provide a description if necessary for accessibility
            modifier = Modifier.fillMaxSize() // Adjust as needed for size and alignment
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSplashScreen() {
    ISEEUTheme {
        SplashScreen()
    }
}
