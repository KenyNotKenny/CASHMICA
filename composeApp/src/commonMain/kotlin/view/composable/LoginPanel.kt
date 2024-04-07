package view.composable


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
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonColors
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.viewModelFactory
import io.github.jan.supabase.gotrue.SessionStatus
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch
import model.SupabaseService
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import view.MainScreen
import view.composable.LoginPanel
import viewModel.LoginScreenViewModel

@OptIn(ExperimentalResourceApi::class)
@Composable
fun LoginPanel(viewModel: LoginScreenViewModel){
    var authFail by remember{ mutableStateOf<Boolean>(false) }
    var signUpForm by remember{ mutableStateOf<Boolean>(false) }
    var loading  by remember{ mutableStateOf(false) }
    val navigator = LocalNavigator.currentOrThrow


    Column(Modifier.fillMaxWidth()
    ) {
        Text(
            text = if(!signUpForm)" Login" else " Sign up",
            fontWeight = FontWeight.Bold,
            fontSize = 50.sp,
            color = MaterialTheme.colors.background
        )
        Spacer(Modifier.height(16.dp))
        Box(modifier = Modifier.clip(RoundedCornerShape(40.dp))
            .background(MaterialTheme.colors.background)
            .fillMaxWidth()
        )
        {
            Box(Modifier.fillMaxWidth(0.84f).align(Alignment.Center)){
                Column(
                    modifier = Modifier.align(Alignment.Center),
                    horizontalAlignment = Alignment.CenterHorizontally) {
                    val composableScope = rememberCoroutineScope()
                    Spacer(Modifier.height(16.dp))
                    val displayNameText by viewModel.displayNameTextStateFlow.collectAsState()
                    AnimatedVisibility(signUpForm){
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = displayNameText,
                            onValueChange = { viewModel.setDisplayNameText(it)
                            },
                            colors = TextFieldDefaults.outlinedTextFieldColors(),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                            label = { Text("Display name")},
                            singleLine = true,
                            shape = RoundedCornerShape(30.dp)
                        )
                    }
                    val emailText by viewModel.emailTextStateFlow.collectAsState()
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = emailText,
                        onValueChange = { viewModel.setEmailText(it)
                            authFail = false
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                        label = { Text("E-mail")},
                        singleLine = true,
                        shape = RoundedCornerShape(30.dp),
                        isError = authFail

                    )
                    val passwordText by viewModel.passwordTextStateFlow.collectAsState()
                    var passwordVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth().height(60.dp),
                        value = passwordText,
                        onValueChange = { viewModel.setPasswordText(it)
                            authFail = false


                        },
                        textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                        label = { Text("Password")},
                        singleLine = true,
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        trailingIcon = {
                            val image = if (passwordVisible)
                                "drawable/icon/view.png"
                            else "drawable/icon/hidden.png"

                            // Please provide localized description for accessibility services
                            val description = if (passwordVisible) "Hide password" else "Show password"

                            IconButton(onClick = {passwordVisible = !passwordVisible}){
                                Icon( modifier = Modifier.fillMaxHeight(0.5f).padding(end = 10.dp),
                                    painter = painterResource(DrawableResource(image)),
                                    contentDescription = description,
                                    )
                            }
                        },
                        shape = RoundedCornerShape(30.dp),
                        isError = authFail || (passwordText.length<6 && !passwordText.isEmpty() && signUpForm)

                    )
                    AnimatedVisibility(passwordText.length<6 && !passwordText.isEmpty() && signUpForm){
                        Text("Password must be at least 6 characters",
                            color = MaterialTheme.colors.error)
                    }
                    var confirmPasswordCorrect by remember { mutableStateOf(true) }
                    AnimatedVisibility(signUpForm){
                        Column {
                            var confirmpasswordText by remember { mutableStateOf("") }
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth().height(60.dp),
                                value = confirmpasswordText,
                                onValueChange = { confirmpasswordText = it
                                    authFail = false
                                    confirmPasswordCorrect = (passwordText == confirmpasswordText)},
                                textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
                                label = { Text("Confirm password")},
                                singleLine = true,
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                trailingIcon = {
                                    val image = if (passwordVisible)
                                        "drawable/icon/view.png"
                                    else "drawable/icon/hidden.png"

                                    // Please provide localized description for accessibility services
                                    val description = if (passwordVisible) "Hide password" else "Show password"

                                    IconButton(onClick = {passwordVisible = !passwordVisible}){
                                        Icon( modifier = Modifier.fillMaxHeight(0.5f).padding(end = 10.dp),
                                            painter = painterResource(DrawableResource(image)),
                                            contentDescription = description,
                                        )
                                    }
                                },
                                shape = RoundedCornerShape(30.dp),
                                isError = !confirmPasswordCorrect,

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
                    Spacer(Modifier.height(16.dp))
                    Box(Modifier.fillMaxWidth().wrapContentHeight()){
                        Button(
                            modifier = Modifier.fillMaxWidth().align(Alignment.CenterEnd).height(50.dp),
                            shape = RoundedCornerShape(30.dp),
                            onClick = {
                                loading = true
                                authFail = false
                                composableScope.launch {
                                    if(signUpForm ){
                                        if(confirmPasswordCorrect && passwordText.length > 5){
                                            viewModel.trySignUp(
                                                OnSuccess = {
                                                    navigator.push(MainScreen())
                                                },
                                                OnFailure = {
                                                    authFail = true
                                                    loading = false
                                                }
                                            )
                                        }
                                    }
                                    else{
                                        viewModel.tryLogin(
                                            OnSuccess = {
                                                navigator.push(MainScreen())
                                            },
                                            OnFailure = {
                                                authFail = true
                                                loading = false
                                            }
                                        )
                                    }

                                }
                            }){

                            if(!loading){
                                Text(text = if(!loading){if (signUpForm) "Sign up" else "Login "} else {"Loading "},
                                    fontSize = 18.sp,
                                    color = Color.White)
                                Icon(Icons.AutoMirrored.Filled.ArrowForward,
                                    contentDescription = null,
                                    tint = Color.White)
                            }else{
                                LoadingAnimation(modifier = Modifier.fillMaxHeight().offset(y = (5).dp ),
                                    circleColor = MaterialTheme.colors.background,
                                    circleSize = 10.dp,
                                    travelDistance = 5.dp)
                            }

                        }
                    }
                    Spacer(Modifier.height(16.dp))
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

        }
    }
}