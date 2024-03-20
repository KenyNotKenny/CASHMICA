import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.transitions.ScaleTransition
import cafe.adriel.voyager.transitions.SlideTransition
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import com.example.compose.AppTheme
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import view.ItemScreen
import view.LoginScreen
import view.MainScreen

@OptIn(ExperimentalResourceApi::class)
@Composable
@Preview
fun App() {

    var temp = isSystemInDarkTheme()
    var localDarkTheme by remember { mutableStateOf(temp) }

    AppTheme(useDarkTheme = localDarkTheme) {
//        Navigator(screen = ItemScreen())

        Navigator(screen = LoginScreen(changeTheme = {localDarkTheme = !localDarkTheme})){navigator ->
            ScaleTransition(navigator)
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
