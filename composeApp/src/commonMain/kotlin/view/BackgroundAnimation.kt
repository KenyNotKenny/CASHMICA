package view

import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.StartOffset
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

@Composable
fun BackgroundAnimation(randomX: Float = 0.5f,randomY: Float =0.5f){
//    var boxSize by remember { mutableStateOf(IntSize.Zero) }
//    val infiniteTransition = rememberInfiniteTransition()
//
//
//    val centerX by infiniteTransition.animateFloat(
//        initialValue = (boxSize.width).toFloat()/2,
//        targetValue = (boxSize.width).toFloat()*randomX, // Random value for x center
//        animationSpec = infiniteRepeatable(
//            animation = tween(5000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        )
//    )
//    val centerY by infiniteTransition.animateFloat(
//        initialValue = (boxSize.height).toFloat()/2,
//        targetValue = (boxSize.height).toFloat()*randomY, // Random value for y center
//        animationSpec = infiniteRepeatable(
//            animation = tween(5000, easing = LinearEasing),
//            repeatMode = RepeatMode.Reverse
//        )
//    )
//
//    Box(
//        modifier = Modifier
//            .fillMaxSize()
//            .onGloballyPositioned { coordinates ->
//                boxSize = IntSize(coordinates.size.width, coordinates.size.height)
//            }
//            .background(
//                Brush.radialGradient(
//                    center = Offset(centerX, centerY),
//                    radius = 1000f,
//                    colors = listOf(MaterialTheme.colors.primaryVariant, Color.Transparent)
//                )
//            )
//    )





        val infiniteTransition = rememberInfiniteTransition()
        val color1 by infiniteTransition.animateColor(
            initialValue = MaterialTheme.colors.primaryVariant,
            targetValue = MaterialTheme.colors.background,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,

            )
        )
        val color2 by infiniteTransition.animateColor(
            initialValue = MaterialTheme.colors.primaryVariant,
            targetValue = MaterialTheme.colors.background,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(1000)
            )
        )
        val color3 by infiniteTransition.animateColor(
            initialValue = MaterialTheme.colors.primaryVariant,
            targetValue = MaterialTheme.colors.background,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(2000)
            )
        )
        val color4 by infiniteTransition.animateColor(
            initialValue = MaterialTheme.colors.primaryVariant,
            targetValue = MaterialTheme.colors.background,
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse,
                initialStartOffset = StartOffset(3000)
            )
        )
        Canvas(
                modifier = Modifier.fillMaxSize(),
        onDraw = {
            drawRect(Brush.linearGradient(listOf(color1,color2,color3,color4)))
        }
        )


}

