package com.example.snake

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class EndGameActivity: AppCompatActivity() {


    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_end_game)
        val endGameTextView: TextView = findViewById(R.id.end_game)

        val score = intent.getIntExtra("SCORE", 0)
        endGameTextView.text = "You lose! Your score: $score"

        val tryAgainButton: Button = findViewById(R.id.try_again_button)
        tryAgainButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val exitButton: Button = findViewById(R.id.exit_button)
        exitButton.setOnClickListener {
            finishAffinity()
        }
    }
}