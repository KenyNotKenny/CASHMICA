package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
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
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch
import model.SupabaseService

class MainScreen: Screen {
    @Composable
    override fun Content() {
        val brush = Brush.horizontalGradient(listOf(MaterialTheme.colors.primaryVariant,MaterialTheme.colors.primary))
        val navigator = LocalNavigator.currentOrThrow
        Column{
            Canvas(
                modifier = Modifier.fillMaxWidth().height(100.dp),
                onDraw = {
                    drawRect(brush)
                }
            )
            Box(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background),
            ){


            }
        }
        SearchBar(modifier = Modifier.fillMaxWidth().padding(top = 70.dp).clip(RoundedCornerShape(40.dp)))

//        val composableScope = rememberCoroutineScope()
//        Button(onClick = {
//            navigator.pop()
//            composableScope.launch {
//                SupabaseService.supabase.auth.signOut()
//            }
//                         },
//            colors = ButtonDefaults.buttonColors(Color.Red)){
//            Text("Log out")
//        }
    }
    @Composable
    fun SearchBar(modifier: Modifier){
        var searchBarText by rememberSaveable() { mutableStateOf("") }
        TextField(
            modifier = modifier,
            colors = TextFieldDefaults.outlinedTextFieldColors(
                backgroundColor = MaterialTheme.colors.background  )
            ,
            placeholder = { Text(text = "Seach item") },
            value = searchBarText,
            onValueChange = { searchBarText = it},
            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
            singleLine = true,
            trailingIcon = {
                Icon(imageVector  = Icons.Default.Search,
                    contentDescription = "Search icon",
                    tint = MaterialTheme.colors.onBackground
                    )
            },

        )
    }
}