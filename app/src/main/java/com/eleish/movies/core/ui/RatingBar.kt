package com.eleish.movies.core.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.VectorPainter
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.eleish.movies.R

@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    count: Int = 5,
    rating: Float,
    spacing: Dp = 0.dp
) {

    val imagePainter = ImageVector.vectorResource(id = R.drawable.star_rate_empty)
    val imageFullPainter = ImageVector.vectorResource(id = R.drawable.star_rate_filled)
    val size = imagePainter.defaultWidth

    val space = LocalDensity.current.run { spacing.toPx() }
    val spacesSum = spacing * (count - 1)
    val totalWidth = size * count + spacesSum

    val painter = rememberVectorPainter(image = imagePainter)
    val painterFull = rememberVectorPainter(image = imageFullPainter)

    Box(
        modifier
            .width(totalWidth)
            .height(size)
            .drawBehind {
                drawRating(count, rating, painter, painterFull, space)
            })
}

private fun DrawScope.drawRating(
    count: Int = 5,
    rating: Float,
    backgroundPainter: VectorPainter,
    foregroundPainter: VectorPainter,
    space: Float
) {

    val imageSize = backgroundPainter.intrinsicSize.width
    val ratingRounded = rating.toInt()

    fun calculateStarOffset(index: Int): Float {
        return imageSize * index + space * index
    }

    for (i in 0 until count) {
        val start = calculateStarOffset(i)
        with(backgroundPainter) {
            translate(left = start) {
                draw(backgroundPainter.intrinsicSize)
            }
        }
    }

    drawWithLayer {
        for (i in 0 until count) {
            val start = calculateStarOffset(i)
            // Destination
            with(foregroundPainter) {
                translate(left = start) {
                    draw(foregroundPainter.intrinsicSize)
                }
            }
        }

        val start = rating * imageSize + ratingRounded * space
        val end = imageSize * count + space * (count - 1)
        val size = end - start

        // Source
        drawRect(
            Color.Transparent,
            topLeft = Offset(start, 0f),
            size = Size(width = size, height = imageSize),
            blendMode = BlendMode.SrcIn
        )
    }
}

private fun DrawScope.drawWithLayer(block: DrawScope.() -> Unit) {
    with(drawContext.canvas.nativeCanvas) {
        val checkPoint = saveLayer(null, null)
        block()
        restoreToCount(checkPoint)
    }
}