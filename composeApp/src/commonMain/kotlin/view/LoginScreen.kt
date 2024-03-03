package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import model.SupabaseService

class LoginScreen: Screen {
    @Composable
    override fun Content() {
        val infiniteTransition = rememberInfiniteTransition()
        // Creates a Color animation as a part of the [InfiniteTransition].
        val color by infiniteTransition.animateColor(
            initialValue = MaterialTheme.colors.primaryVariant,
            targetValue = MaterialTheme.colors.onPrimary,
            animationSpec = infiniteRepeatable(
                // Linearly interpolate between initialValue and targetValue every 1000ms.
                animation = tween(5000, easing = FastOutSlowInEasing),
                // Once [TargetValue] is reached, starts the next iteration in reverse (i.e. from
                // TargetValue to InitialValue). Then again from InitialValue to TargetValue. This
                // [RepeatMode] ensures that the animation value is *always continuous*.
                repeatMode = RepeatMode.Reverse
            )
        )
        val colorAlt by infiniteTransition.animateColor(
            initialValue = MaterialTheme.colors.onPrimary,
            targetValue = MaterialTheme.colors.primaryVariant,
//            initialValue = Color(0xFF2200C6),
//            targetValue = Color(0xFF740EE1),
            animationSpec = infiniteRepeatable(
                // Linearly interpolate between initialValue and targetValue every 1000ms.
                animation = tween(5000, easing = FastOutSlowInEasing),
                // Once [TargetValue] is reached, starts the next iteration in reverse (i.e. from
                // TargetValue to InitialValue). Then again from InitialValue to TargetValue. This
                // [RepeatMode] ensures that the animation value is *always continuous*.
                repeatMode = RepeatMode.Reverse
            )
        )
        var authFail by remember{mutableStateOf<Boolean>(false)}

        val navigator = LocalNavigator.currentOrThrow
        Canvas(
            modifier = Modifier.fillMaxSize(),
            onDraw = {
                drawRect(Brush.linearGradient(listOf(color, colorAlt)))
            }
        )
        Box (Modifier.fillMaxSize(),){
            Box(modifier = Modifier.clip(RoundedCornerShape(40.dp))
                .background(MaterialTheme.colors.background)
                .fillMaxWidth(0.8F)
                .aspectRatio(1f)
                .align(Alignment.Center)
                ,
            )
            {
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    val composableScope = rememberCoroutineScope()



                    Text("Login|Register",
                        style = MaterialTheme.typography.h4,
                        color = MaterialTheme.colors.primary
                    )
                    var emailText by remember { mutableStateOf("") }
                    Spacer(Modifier.fillMaxHeight(0.1f))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        value = emailText,
                        onValueChange = { emailText = it
                                        authFail = false},
                        colors = if(authFail) TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = Color.Red  )
                        else TextFieldDefaults.outlinedTextFieldColors(),
                        textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                        label = { Text("E-mail")},
                        singleLine = true
                    )
                    var passwordText by remember { mutableStateOf("") }
                    var passwordVisible by remember { mutableStateOf(false) }
                    Spacer(Modifier.fillMaxHeight(0.1f))
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        value = passwordText,
                        onValueChange = { passwordText = it
                                        authFail = false},
                        label = { Text("Password")},
                        singleLine = true,
                        colors = if(authFail) TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = Color.Red  )
                        else TextFieldDefaults.outlinedTextFieldColors(),
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                Icons.Filled.KeyboardArrowDown
                            else Icons.Filled.KeyboardArrowUp

                            // Please provide localized description for accessibility services
                            val description = if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = {passwordVisible = !passwordVisible}){
                                Icon(imageVector  = image, description)
                            }
                        }
                    )
                    AnimatedVisibility(authFail){
                        Text("Wrong e-mail or password",
                            color = Color.Red)
                    }
                    Spacer(Modifier.fillMaxHeight(0.1f))
                    Row(modifier = Modifier.fillMaxWidth(0.8f),
                        horizontalArrangement = Arrangement.Center){
                        Button(
                            modifier = Modifier.wrapContentWidth(),
                            onClick = {
                                runBlocking {
                                    launch {
                                        testSignIn()
//                                        authCheck()
                                    }
                                }
                            }){
                            Text("Create new account",
                                maxLines = 1)
                        }
                        Spacer(Modifier.fillMaxWidth(0.05f))
                        Button(
                            modifier = Modifier.wrapContentWidth(),
                            onClick = {
                                authFail = false
                                composableScope.launch {
                                    var loginResult = SupabaseService.loginEmail(emailText,passwordText)
                                    if (loginResult.isSuccess){
                                        navigator.push(MainScreen())
                                    }else{
                                        authFail = true
                                    }
                                }
                            }){
                            Text("Login")
                        }
                    }


                }
            }
        }



//        Button(onClick = {navigator.push( MainScreen())}){
//            Text("Login")
//        }
    }
}

suspend fun testSignIn(){

    try {
        val signUpResult = SupabaseService.supabase.auth.signUpWith(Email){
            email = "keny7503@gmail.com"
            password = "asda"
        }

//        if (signUpResult.isSuccessful) {
//            val user = signUpResult.data.user!!
//            return
//        } else {
//            // Handle sign-up failure
//            return
//        }


    } catch (e: Exception) {
        // Handle other exceptions
        println("Sign up fail")
        return
    }

}
suspend fun login(paseword: String){

    try {
        val signUpResult = SupabaseService.supabase.auth.signInWith(Email){
            email = "keny7503@gmail.com"
            password = paseword
        }

//        if (signUpResult.isSuccessful) {
//            val user = signUpResult.data.user!!
//            return
//        } else {
//            // Handle sign-up failure
//            return
//        }

//        SupabaseService.supabase.auth.sessionStatus.collect {
//            when(it) {
//                is SessionStatus.Authenticated -> println(it.session.user)
//                SessionStatus.LoadingFromStorage -> println("Loading from storage")
//                SessionStatus.NetworkError -> println("Network error")
//                SessionStatus.NotAuthenticated -> println("Not authenticated")
//            }
//            repeat(5){
//                println("In sesson")
//                delay(2000)
//            }
//            return@collect
//        }

        val session = SupabaseService.supabase.auth.currentSessionOrNull()

    } catch (e: Exception) {
        // Handle other exceptions
        println("Login fail")
        return
    }

}
suspend fun authCheck(){
    SupabaseService.supabase.auth.sessionStatus.collect {
        when(it) {
            is SessionStatus.Authenticated -> println(it.session.user)
            SessionStatus.LoadingFromStorage -> println("Loading from storage")
            SessionStatus.NetworkError -> println("Network error")
            SessionStatus.NotAuthenticated -> println("Not authenticated")
        }
    }
}