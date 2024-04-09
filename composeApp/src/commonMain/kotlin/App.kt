import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.FadeTransition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import view.theme.AppTheme
import view.LoadingScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {

    var temp = isSystemInDarkTheme()

    AppTheme() {
//        Navigator(screen = ItemScreen())

        Navigator(screen = LoadingScreen()){navigator ->
            FadeTransition(navigator)
        }






//        val brush = Brush.linearGradient(listOf(Color.Blue, Color.Magenta))
//        Canvas(
//            modifier = Modifier.fillMaxSize(),
//            onDraw = {
//                drawRect(brush)
//            }
//        )
//
//        var showContent by remember { mutableStateOf(false) }
//        Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//            Button(onClick = { showContent = !showContent }) {
//                Text("Click me!")
//            }
//            AnimatedVisibility(showContent) {
//                val greeting = remember { Greeting().greet() }
//                val dataResult = remember { mutableStateListOf<Account>() }
//                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
//                    Image(painterResource(Res.drawable.logo), null)
//                    Text("Compose: $greeting\n")
//                    LaunchedEffect(Unit){
//                        withContext(Dispatchers.IO){
//                            val result = supabase.from("account").select().decodeList<Account>()
//                            dataResult.addAll(result)
//                        }
//
//                    }
//                    Text(dataResult.toList().toString())
//
//                }
//            }
//
//        }

    }
}
