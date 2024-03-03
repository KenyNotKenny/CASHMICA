package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class MainScreen: Screen {
    @Composable
    override fun Content() {
        val brush = Brush.linearGradient(listOf(Color(0xFF2200C6), Color(0xFF740EE1)))
        val navigator = LocalNavigator.currentOrThrow
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawRect(brush)
            }
        )
        Button(onClick = {navigator.pop()},
            colors = ButtonDefaults.buttonColors(Color.Red)){
            Text("Log out")
        }
    }
}