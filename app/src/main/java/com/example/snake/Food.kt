package com.example.snake

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import kotlin.random.Random


class Food(private val screenWidth : Int, private val screenHeight : Int,
           private val segmentSize: Float, private val gameView: GameView) {
    var x : Float = 0f
    var y : Float = 0f
    private val size : Float = segmentSize
    private val foodBitmap: Bitmap = BitmapFactory.decodeResource(
        gameView.resources, R.drawable.food1)

    fun draw(canvas: Canvas) {
        canvas.drawBitmap(foodBitmap, null,
            Rect(x.toInt(), y.toInt(), (x + size).toInt(), (y + size).toInt()), null)
    }

    fun createFood(snakeBody: List<Pair<Float, Float>>) {
        do {
            x = Random.nextInt(0, (screenWidth / size).toInt()) * size
            y = Random.nextInt(0, (screenHeight / size).toInt()) * size
        } while (snakeBody.contains(Pair(x, y)))
    }
}