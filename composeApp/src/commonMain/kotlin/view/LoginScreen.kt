package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch
import model.SupabaseService
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import kotlin.random.Random

class LoginScreen(changeTheme: () -> Unit): Screen {
    val changeDarkTheme = changeTheme
    var randomX = 0.5f
    var randomY = 0.5f
    val randomBackground = {
        randomX = Random.nextFloat()
        randomY = Random.nextFloat()}
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {

        var authFail by remember{mutableStateOf<Boolean>(false)}
        var signUpForm by remember{mutableStateOf<Boolean>(false)}
        var loading  by remember{ mutableStateOf(false)}



        val navigator = LocalNavigator.currentOrThrow
        println(randomX)
        BackgroundAnimation(randomX = randomX, randomY= randomY)
        Box (Modifier.fillMaxSize(),){
            Box(modifier = Modifier.clip(RoundedCornerShape(40.dp))
                .background(MaterialTheme.colors.background)
                .fillMaxWidth(0.8F)
                .wrapContentHeight()
                .align(Alignment.Center)
                ,
            )
            {
                Column(
                    modifier = Modifier.align(Alignment.Center).fillMaxWidth(0.8f),
                    horizontalAlignment = Alignment.CenterHorizontally) {

                    val composableScope = rememberCoroutineScope()


                    Spacer(Modifier.height(20.dp))


                    Text(
                        text = if(!signUpForm)"Login" else "Sign up",
                        style = MaterialTheme.typography.h3,
                        color = MaterialTheme.colors.primary
                    )
                    var emailText by remember { mutableStateOf("") }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = emailText,
                        onValueChange = { emailText = it
                                        authFail = false
                                        randomBackground()
                        },
                        colors = if(authFail) TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = MaterialTheme.colors.error )
                        else TextFieldDefaults.outlinedTextFieldColors(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                        label = { Text("E-mail")},
                        singleLine = true
                    )
                    var passwordText by remember { mutableStateOf("") }
                    var passwordVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = passwordText,
                        onValueChange = { passwordText = it
                                        authFail = false
                                        randomBackground()

                                        },
                        textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                        label = { Text("Password")},
                        singleLine = true,
                        colors = if(authFail) TextFieldDefaults.outlinedTextFieldColors(
                            unfocusedBorderColor = MaterialTheme.colors.error )
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
                    var confirmPasswordCorrect by remember { mutableStateOf(true) }
                    AnimatedVisibility(signUpForm){
                        Column {
                            var confirmpasswordText by remember { mutableStateOf("") }
                            var confirmpasswordVisible by remember { mutableStateOf(false) }
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = confirmpasswordText,
                                onValueChange = { confirmpasswordText = it
                                    authFail = false
                                    confirmPasswordCorrect = (passwordText == confirmpasswordText)},
                                textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                                label = { Text("Confirm password")},
                                singleLine = true,
                                colors = if(!confirmPasswordCorrect) TextFieldDefaults.outlinedTextFieldColors(
                                    unfocusedBorderColor = MaterialTheme.colors.error  )
                                else TextFieldDefaults.outlinedTextFieldColors(),
                                visualTransformation = if (confirmpasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    val image = if (confirmpasswordVisible)
                                        Icons.Filled.KeyboardArrowDown
                                    else Icons.Filled.KeyboardArrowUp

                                    // Please provide localized description for accessibility services
                                    val description = if (confirmpasswordVisible) "Hide password" else "Show password"

                                    IconButton(onClick = {confirmpasswordVisible = !confirmpasswordVisible}){
                                        Icon(imageVector  = image, description)
                                    }
                                }
                            )

                            AnimatedVisibility(!confirmPasswordCorrect){
                                Text("Password don't match",
                                    color = MaterialTheme.colors.error)
                            }
                        }


                    }
                    AnimatedVisibility(authFail){
                        Text("Wrong e-mail or password",
                            color = MaterialTheme.colors.error)
                    }
                    Spacer(Modifier.height(20.dp))
                    Box(Modifier.fillMaxWidth().wrapContentHeight()){
                        Button(
                            modifier = Modifier.wrapContentWidth().align(Alignment.CenterEnd),
                            onClick = {
                                loading = true
                                authFail = false
                                composableScope.launch {
                                    if(signUpForm && confirmPasswordCorrect){
                                        var signUpResult = SupabaseService.signUpEmail(emailText,passwordText)
                                        if (signUpResult.isSuccess){
                                            navigator.push(MainScreen(changeTheme = {changeDarkTheme()}))
                                        }else{
                                            authFail = true
                                            loading = false
                                        }
                                    }
                                    else{
                                        var loginResult = SupabaseService.loginEmail(emailText,passwordText)
                                        if (loginResult.isSuccess){
                                            navigator.push(MainScreen(changeTheme = {changeDarkTheme()}))
                                        }else{
                                            authFail = true
                                            loading = false

                                        }
                                    }

                                }
                            }){
                            Text(text = if(!loading){if (signUpForm) "Sign up →" else "Login →"} else {"Loading ↻"} )
                        }
                    }
                    Spacer(Modifier.height(10.dp))
                    Row{
                        Text(
                            text = if(signUpForm)"Return to " else "Don't have an account? ",
                            color = MaterialTheme.colors.onBackground)
                        Text(
                            modifier = Modifier.clickable { signUpForm = !signUpForm },
                            text = if (signUpForm) "Login" else "Sign up",
                            color = MaterialTheme.colors.primary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                    Spacer(Modifier.height(20.dp))



                }
            }
//            Image(painter = painterResource(DrawableResource("drawable/logo.png")),
//                contentDescription = null)

        }



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