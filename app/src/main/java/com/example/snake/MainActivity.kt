package com.example.snake

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.view.View.OnClickListener
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), OnClickListener {

    var gameView: GameView? = null
    var scoreText: TextView? = null
    private var score_image: ImageView? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        gameView = findViewById<GameView>(R.id.game_view)
        scoreText = findViewById<TextView>(R.id.score_text)
        score_image = findViewById<ImageView>(R.id.score_image)
        gameView?.setScoreTextView(scoreText)
    }

    override fun onClick(p0: View?) {
        if (p0!!.id == R.id.btn_left &&
            gameView!!.snake.direct != Direction.right) {
            gameView!!.snake.moveTo(Direction.left)
        }
        if (p0.id == R.id.btn_right &&
            gameView!!.snake.direct != Direction.left) {
            gameView!!.snake.moveTo(Direction.right)
        }
        if (p0.id == R.id.btn_up &&
            gameView!!.snake.direct != Direction.down) {
            gameView!!.snake.moveTo(Direction.up)
        }
        if (p0.id == R.id.btn_down &&
            gameView!!.snake.direct != Direction.up) {
            gameView!!.snake.moveTo(Direction.down)
        }
    }
}