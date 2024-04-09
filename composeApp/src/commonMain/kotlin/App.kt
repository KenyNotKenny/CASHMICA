
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import org.jetbrains.compose.ui.tooling.preview.Preview
import view.theme.AppTheme
import view.screen.LoadingScreen

@Composable
@Preview
fun App() {

    AppTheme {

        Navigator(screen = LoadingScreen()){ navigator ->
            FadeTransition(navigator)
        }

    }
}
