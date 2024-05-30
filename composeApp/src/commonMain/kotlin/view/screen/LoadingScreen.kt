package view.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.user.UserSession
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import model.SupabaseService
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import viewModel.ThemeViewModel

class LoadingScreen(): Screen {

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val composableScope = rememberCoroutineScope()
        Box(modifier = Modifier.fillMaxSize().clickable {
        }){
            Image( modifier = Modifier.align(Alignment.Center).fillMaxSize(),
                painter = painterResource(DrawableResource("drawable/loading_background_light.png")),
                contentDescription = null,
                contentScale = ContentScale.FillHeight)
            Image( modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.8f),
                painter = painterResource(DrawableResource("drawable/logo_t.png")),
                contentDescription = null)

        }
        composableScope.launch{
            SupabaseService.supabase
            var timeout = 5
            while(timeout>0){
                var session: UserSession?
                try{
                    session = SupabaseService.supabase.auth.currentSessionOrNull()
                    if( session!= null){
                        navigator.replace(MainScreen())
                        return@launch
                    }
                }catch (e: Exception){
                    println("Connection error")
                }
                delay(1000L)
                timeout--
            }
            println("Timeout ")
            navigator.replace(LoginScreen())

        }
    }
}