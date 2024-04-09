package view.screen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import cafe.adriel.voyager.core.screen.Screen
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.viewModelFactory
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import view.composable.LoginPanel
import viewModel.LoginScreenViewModel

class LoginScreen: Screen {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val viewModel: LoginScreenViewModel = kmpViewModel(
            factory = viewModelFactory {
                LoginScreenViewModel(savedStateHandle = createSavedStateHandle())
            }
        )

        Canvas(modifier = Modifier.fillMaxSize(), onDraw = {
            drawRect(
                Brush.linearGradient(listOf(Color(0xFFc680ff), Color(0xFF8600ee)))

            )
        })
        Image( modifier = Modifier.fillMaxSize(),
            painter = painterResource(DrawableResource("drawable/loading_background_icon.png")),
            contentDescription = null,
            contentScale = ContentScale.FillHeight,)
        Box(Modifier.fillMaxSize()){
            Image( modifier = Modifier.fillMaxHeight(0.18f).fillMaxWidth().align(Alignment.BottomCenter),
                painter = painterResource(DrawableResource("drawable/bg_corner.png")),
                contentDescription = null,
                contentScale = ContentScale.FillBounds,
                )
        }
        Box (Modifier.fillMaxSize()){
            Box(modifier = Modifier.wrapContentHeight()
                .fillMaxWidth(0.8f)
                .align(Alignment.Center)){
                LoginPanel( viewModel = viewModel)
            }

        }

    }
}
