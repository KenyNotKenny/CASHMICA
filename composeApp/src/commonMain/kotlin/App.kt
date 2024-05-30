
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.viewModelFactory
import org.jetbrains.compose.ui.tooling.preview.Preview
import view.theme.AppTheme
import view.screen.LoadingScreen
import viewModel.MainScreenViewModel
import viewModel.ThemeViewModel

@Composable
@Preview
fun App() {

    val isDarkTheme by remember { mutableStateOf(false) }

    AppTheme(
        useDarkTheme = isDarkTheme
    ) {
        Navigator(screen = LoadingScreen()){ navigator ->
            FadeTransition(navigator)
        }

    }
}
