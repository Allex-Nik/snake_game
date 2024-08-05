package com.example.snake

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.Rect
import android.widget.TextView

class Snake(private val gameView: GameView) {

    var x = 100f
    var y = 100f
    var size = 100f
    var body = mutableListOf<Pair<Float, Float>>()
    var direct: Direction = Direction.stop
    var score = 0
    var flag = true

    private val snakeHeadBitmap: Bitmap = BitmapFactory.decodeResource(
        gameView.resources, R.drawable.snake_head4).let {
        Bitmap.createScaledBitmap(it, size.toInt(), size.toInt(), false)
    }
    private val snakeBodyBitmap: Bitmap = BitmapFactory.decodeResource(
        gameView.resources, R.drawable.snake_segment3).let {
        Bitmap.createScaledBitmap(it, size.toInt(), size.toInt(), false)
    }
    private val snakeTailBitmap: Bitmap = BitmapFactory.decodeResource(
        gameView.resources, R.drawable.snake_tail4).let {
        Bitmap.createScaledBitmap(it, size.toInt(), size.toInt(), false)
    }
    private val snakeTurnRightUpBitmap: Bitmap = BitmapFactory.decodeResource(
        gameView.resources, R.drawable.snake_right_up2).let {
        Bitmap.createScaledBitmap(it, size.toInt(), size.toInt(), false)
    }
    private val snakeTurnLeftUpBitmap: Bitmap = BitmapFactory.decodeResource(
        gameView.resources, R.drawable.snake_left_up2).let {
        Bitmap.createScaledBitmap(it, size.toInt(), size.toInt(), false)
    }
    private val snakeTurnRightDownBitmap: Bitmap = BitmapFactory.decodeResource(
        gameView.resources, R.drawable.snake_right_down3).let {
        Bitmap.createScaledBitmap(it, size.toInt(), size.toInt(), false)
    }
    private val snakeTurnLeftDownBitmap: Bitmap = BitmapFactory.decodeResource(
        gameView.resources, R.drawable.snake_left_down4).let {
        Bitmap.createScaledBitmap(it, size.toInt(), size.toInt(), false)
    }


    init {
        body.add(Pair(x, y))
    }

    fun draw(canvas: Canvas) {
        val head = body.first()
        drawRotatedBitmap(canvas, snakeHeadBitmap,
            head.first, head.second, getRotationAngle(direct))

        for (i in 1 until body.size - 1) {
            val prev_segment = body[i - 1]
            val segment = body[i]
            val next_segment = body[i + 1]

            val turnBitmap = getTurnBitmap(prev_segment, segment, next_segment)
            if (turnBitmap != null) {
                canvas.drawBitmap(turnBitmap, segment.first, segment.second, null)
            } else {
                drawRotatedBitmap(
                    canvas, snakeBodyBitmap,
                    segment.first, segment.second, getRotationAngle(getDirection(prev_segment, segment))
                )
            }
        }
        if (body.size > 1) {
            val tail = body.last()
            val beforeTail = body[body.size - 2]
            drawRotatedBitmap(
                canvas, snakeTailBitmap,
                tail.first, tail.second, getRotationAngle(getDirection(beforeTail, tail))
            )
        }
    }

    private fun getTurnBitmap(prevSegment: Pair<Float, Float>,
                              segment: Pair<Float, Float>, nextSegment: Pair<Float, Float>): Bitmap? {
        val prevDirection = getDirection(prevSegment, segment)
        val nextDirection = getDirection(segment, nextSegment)

        return when {
            prevDirection == Direction.up &&
                    nextDirection == Direction.left -> snakeTurnLeftUpBitmap
            prevDirection == Direction.up &&
                    nextDirection == Direction.right -> snakeTurnRightUpBitmap
            prevDirection == Direction.right &&
                    nextDirection == Direction.up -> snakeTurnLeftDownBitmap
            prevDirection == Direction.right &&
                    nextDirection == Direction.down -> snakeTurnLeftUpBitmap
            prevDirection == Direction.down &&
                    nextDirection == Direction.left -> snakeTurnLeftDownBitmap
            prevDirection == Direction.down &&
                    nextDirection == Direction.right -> snakeTurnRightDownBitmap
            prevDirection == Direction.left &&
                    nextDirection == Direction.up -> snakeTurnRightDownBitmap
            prevDirection == Direction.left &&
                    nextDirection == Direction.down -> snakeTurnRightUpBitmap
            else -> null
        }
    }

    private fun getDirection(from: Pair<Float, Float>, to: Pair<Float, Float>): Direction {
        return when {
            to.first > from.first -> Direction.right
            to.first < from.first -> Direction.left
            to.second < from.second -> Direction.up
            to.second > from.second -> Direction.down
            else -> Direction.stop
        }
    }


    private fun drawRotatedBitmap(canvas: Canvas,
                                  bitmap: Bitmap, x: Float, y: Float, angle: Float) {
        val matrix = Matrix()
        matrix.postTranslate(-bitmap.width / 2f, -bitmap.height / 2f)
        matrix.postRotate(angle)
        matrix.postTranslate(x + size / 2, y + size / 2)
        canvas.drawBitmap(bitmap, matrix, null)
    }

    private fun getRotationAngle(direction: Direction): Float {
        return when (direction) {
            Direction.left -> 180f
            Direction.right -> 0f
            Direction.up -> 270f
            Direction.down -> 90f
            else -> 0f
        }
    }

    fun moveTo(d : Direction): Direction {
        direct = d
        return direct
    }

    fun move(screenWidth: Int, screenHeight: Int, food: Food) {
        val head = when (direct) {
            Direction.left -> Pair(x - size, y)
            Direction.right -> Pair(x + size, y)
            Direction.up -> Pair(x, y - size)
            Direction.down -> Pair(x, y + size)
            else -> Pair(x, y)
        }

        if (checkCollisionWithWalls(screenWidth, screenHeight, head)) {
            flag = false
            return
        }

        x = head.first
        y = head.second

        body.add(0, head)

        if (!eatFood(food)) {
            body.removeAt(body.size - 1)
        } else {
            food.createFood(body)
            score++
            gameView.updateScore(score)
        }
    }

    private fun eatFood(food: Food) : Boolean {
        return (x == food.x && y == food.y)
    }

    private fun checkCollisionWithWalls(screenWidth: Int, screenHeight: Int, head: Pair<Float, Float>) : Boolean {
        return (head.first < 0 || head.first + size > screenWidth ||
                head.second < 0 || head.second + size > screenHeight)
    }

    fun checkCollisionWithItself() : Boolean {
        val head = Pair(x, y)
        return body.subList(1, body.size).contains(head)
    }
}