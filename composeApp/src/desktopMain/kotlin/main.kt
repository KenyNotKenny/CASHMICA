import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import java.awt.Toolkit

fun main() = application {
    val windowHeight = (Toolkit.getDefaultToolkit().screenSize.height/2) - 30
    Window(onCloseRequest = ::exitApplication,
        title = "CASHMICA",
        state = WindowState(width = windowHeight.dp, height = (windowHeight*(20/9)).dp),
        resizable = true,
        icon = painterResource("drawable/logo.png")) {
        App()
        Toolkit.getDefaultToolkit().screenSize.height
    }
}