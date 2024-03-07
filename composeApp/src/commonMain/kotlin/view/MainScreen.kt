package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import model.SummaryPrize
import model.SupabaseService


class MainScreen(changeTheme: () -> Unit): Screen {
    val changeDarkTheme = changeTheme
    val menuList = listOf<@Composable () -> Unit>(
        {DarkThemeToggle()},
        {SignOutButton()}
    )


    @Composable
    override fun Content() {
        val brush = Brush.horizontalGradient(listOf(MaterialTheme.colors.primaryVariant,MaterialTheme.colors.primary))
        val navigator = LocalNavigator.currentOrThrow
        val composableScope = rememberCoroutineScope()
        val itemList = remember { mutableStateListOf<SummaryPrize>() }
        var openMenu by remember { mutableStateOf(false) }
        Column{
            Box(modifier = Modifier.fillMaxWidth().height(100.dp),){
                Canvas(
                    modifier = Modifier.fillMaxSize(),
                    onDraw = {
                        drawRect(brush)
                    }
                )
                Box(modifier = Modifier.fillMaxWidth().height(70.dp).padding(horizontal = 20.dp).align(Alignment.TopCenter)){
                    MenuButton(
                        modifier = Modifier.align(Alignment.CenterStart)
                        ,onClick = {
                            openMenu = !openMenu

                        }
                    )
                    DropdownMenu(
                        expanded = openMenu,
                        onDismissRequest = { openMenu = false },
                        modifier = Modifier.width(220.dp).background(
                            MaterialTheme.colors.background)
                    ) {
                        menuList.forEachIndexed { index, composable ->
                            DropdownMenuItem(onClick = {
//                                if( s == "Theme toggle"){
//                                    changeDarkTheme()
//                                }
//                                if( s == "Sign out"){
//                                    navigator.pop()
////                                    composableScope.launch {
////                                        SupabaseService.supabase.auth.signOut() }
//                                }
                                if(index == 1){
                                    navigator.pop()
                                }
                                openMenu = false
                            }) {
                                composable()
                            }
                        }
                    }
                    Text( modifier = Modifier.align(Alignment.CenterEnd),
                        text="Welcome, ",
                        color = MaterialTheme.colors.onPrimary
                    )
                }


            }
            Column(
                modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.surface),
            ){

                Spacer(modifier = Modifier.size(20.dp))

                LazyColumn{
                    items(itemList){ item ->
                        Spacer(modifier = Modifier.size(20.dp))
                        ItemCard(item.item_name,item.average_prize,
                            navigator = navigator)

                    }
                }

            }
        }

        SearchBar(modifier = Modifier.fillMaxWidth().padding(top = 70.dp).clip(RoundedCornerShape(40.dp))){
            composableScope.launch{
                itemList.clear()
                itemList.addAll(SupabaseService.supabase.from("prize_summary").select(columns = Columns.list("item_id, item_name, average_prize, max_prize, min_prize")).decodeList<SummaryPrize>())
                println("update list")
            }
        }


    }
    @Composable
    fun SearchBar(modifier: Modifier, onClick: () -> Unit){
        var searchBarText by rememberSaveable() { mutableStateOf("") }
        TextField(
            modifier = modifier,
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent)
            ,
            placeholder = { Text(text = "Seach item") },
            value = searchBarText,
            onValueChange = { searchBarText = it},
            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
            singleLine = true,
            trailingIcon = {
                Icon(
                    modifier = Modifier.clickable { onClick() },
                    imageVector  = Icons.Default.Search,
                    contentDescription = "Search icon",
                    tint = MaterialTheme.colors.onBackground
                    )
            },

        )
    }
    @Composable
    fun ItemCard(itemName: String, prize: Int, navigator: Navigator){
        Box(modifier = Modifier.fillMaxWidth().height(180.dp).clip(RoundedCornerShape(30.dp))){
            Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.background)){
                Row (
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Box( modifier = Modifier.fillMaxHeight(1f).aspectRatio(1f)){
                        Box(modifier =  Modifier.fillMaxSize(0.8f).align(Alignment.Center)
                            .clip(RoundedCornerShape(20.dp))){
                            Box( modifier =  Modifier.fillMaxSize().background(Color.LightGray)){
                                Text(text = "Image",
                                    modifier = Modifier.align(Alignment.Center))
                            }
                        }

                    }
                    Box(modifier = Modifier.fillMaxSize()){
                        Box( modifier =  Modifier.fillMaxHeight(0.8f).fillMaxWidth(0.9f).align(
                            Alignment.Center)){
                            Text(text = itemName,
                                fontWeight = FontWeight.Bold,
                                fontSize = 27.sp,
                                color = MaterialTheme.colors.onBackground
//
                            )
                            Box(
                                modifier = Modifier.align(Alignment.BottomStart).fillMaxWidth(0.9f)
                                    .wrapContentHeight()
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    text =  prize.addCommas() +" vnđ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colors.onBackground
                                )

                                Button(onClick = { navigator.push(ItemScreen()) },
                                    modifier= Modifier.size(36.dp).align(Alignment.CenterEnd),  //avoid the oval shape
                                    shape = CircleShape,
                                    contentPadding = PaddingValues(0.dp),  //avoid the little icon
                                    colors = ButtonDefaults.buttonColors(
                                        backgroundColor =  MaterialTheme.colors.primary,
                                        contentColor = MaterialTheme.colors.onPrimary
                                        )
                                ) {
                                    Icon(Icons.Default.Add, contentDescription = "More")
                                }
                            }


                        }
                    }


                }
            }
        }
    }
    @Composable
    fun MenuButton(modifier: Modifier, onClick: () -> Unit){
        Button(onClick = onClick,
            modifier= modifier.size(40.dp),  //avoid the oval shape
            shape = CircleShape,
            contentPadding = PaddingValues(0.dp),  //avoid the little icon
            colors = ButtonDefaults.buttonColors(
                backgroundColor =  MaterialTheme.colors.background,
                contentColor = MaterialTheme.colors.primary
            )
        ) {
            Icon(Icons.Default.Menu, contentDescription = "Menu")
        }
    }
    @Composable
    fun DarkThemeToggle(){
        var checked by remember { mutableStateOf(false) }
        Row(modifier = Modifier.fillMaxSize()
        ){
            Text(
                modifier = Modifier.align(Alignment.CenterVertically),
                text = "Join the dark side")
            Switch(
                colors =  SwitchDefaults.colors(
                    checkedThumbColor = MaterialTheme.colors.primary,
                    checkedTrackColor = MaterialTheme.colors.primary
                    ),
                checked = checked,
                onCheckedChange = {
                    checked = it
                    changeDarkTheme()
                }
            )
        }

    }
    @Composable
    fun SignOutButton(){
        Text("Sign out")
    }

    fun Int.addCommas(): String {
        val numberString = this.toString()
        val reversedString = numberString.reversed()
        val stringBuilder = StringBuilder()

        for ((index, char) in reversedString.withIndex()) {
            stringBuilder.append(char)
            if ((index + 1) % 3 == 0 && (index + 1) != reversedString.length) {
                stringBuilder.append(',')
            }
        }

        return stringBuilder.reverse().toString()
    }

}
