package com.example.snake

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Rect
import android.os.CountDownTimer
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.TextView

class GameView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    var snake: Snake = Snake(this)
    var food: Food? = null
    var scoreText: TextView? = null
    var gameOver = false
    private var backgroundBitmap: Bitmap? = null

    var timer = object : CountDownTimer(Long.MAX_VALUE, 500) {
        override fun onTick(p0: Long) {
            if (!gameOver) {
                snake.move(width, height, food!!)
                if (!snake.flag) endGame()
                if (snake.checkCollisionWithItself()) endGame()
                invalidate()
            }
        }

        override fun onFinish() {

        }
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawBackground(canvas)
        snake.draw(canvas)
        food?.draw(canvas)
    }

    private fun drawBackground(canvas: Canvas) {
        backgroundBitmap?.let {
            canvas.drawBitmap(it, null, Rect(0, 0, width, height), null)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event!!.x.toInt() / 100 * 100f
        val y = event.y.toInt() / 100 * 100f
        snake.x = x
        snake.y = y
        return true
    }

    override fun onSizeChanged(width: Int, height: Int, oldWidth: Int, oldHeight: Int) {
        super.onSizeChanged(width, height, oldWidth, oldHeight)
        backgroundBitmap = BitmapFactory.decodeResource(
            resources, R.drawable.snake_background).let {
            Bitmap.createScaledBitmap(it, width, height, true)
        }
        food = Food(width, height, snake.size, this)
        food!!.createFood(snake.body)
        timer.start()
    }

    fun setScoreTextView(scoreTextView: TextView?) {
        scoreText = scoreTextView
    }

    @SuppressLint("SetTextI18n")
    fun updateScore(score: Int) {
        scoreText?.text = "Score: $score"
    }

    private fun endGame() {
        gameOver = true
    }
}