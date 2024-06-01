package view.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.viewModelFactory
import io.github.jan.supabase.gotrue.auth
import kotlinx.coroutines.launch
import model.SupabaseService
import view.composable.Filter
import view.composable.ItemCard
import view.composable.ItemSearchBar
import view.composable.ProfilePanel
import viewModel.MainScreenViewModel
import viewModel.ThemeViewModel


class MainScreen() : Screen {
    private val menuList = listOf<@Composable () -> Unit>(
        {DarkThemeToggle()},
        {EarnCashmicoinButton()},
        {SignOutButton()},
    )

    @Composable
    override fun Content() {
        val viewModel: MainScreenViewModel = kmpViewModel(
            factory = viewModelFactory {
                MainScreenViewModel(savedStateHandle = createSavedStateHandle())
            }
        )
        val firstSearch by viewModel.firstSearchStateFlow.collectAsState()
        val brush = Brush.horizontalGradient(listOf(MaterialTheme.colors.primary,MaterialTheme.colors.primary))
        val navigator = LocalNavigator.currentOrThrow
        val composableScope = rememberCoroutineScope()
        val itemList by viewModel.itemListStateFlow.collectAsState()

        var openMenu by remember { mutableStateOf(false) }
        var itemPanelVisible by remember { mutableStateOf(false) }
        Column(modifier = Modifier.background(MaterialTheme.colors.primary)){
            Box(modifier = Modifier.animateContentSize().fillMaxWidth().heightIn(min = 80.dp).fillMaxHeight(if(firstSearch) 0.6f else 0.0f)
            ){
                Canvas(
                    modifier = Modifier.fillMaxSize(),
                    onDraw = {
                        drawRect(brush)
                    }
                )
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(horizontal = 20.dp)
                    .align(Alignment.TopCenter)){
                    MenuButton(
                        modifier = Modifier.align(Alignment.CenterStart)
                        ,onClick = {
                            openMenu = !openMenu
                        }
                    )
                    DropdownMenu(
                        expanded = openMenu,
                        onDismissRequest = { openMenu = false },
                        modifier = Modifier.width(220.dp)
                            .background(MaterialTheme.colors.background)
                    ) {
                        menuList.forEachIndexed { index, composable ->
                            DropdownMenuItem(onClick = {
                                when(index){
                                    1 ->{
                                        navigator.push(VerifyEntryScreen(navigator))
                                    }
                                    2 ->{
                                        navigator.replace(LoginScreen())
                                        composableScope.launch {
                                            try {
                                                SupabaseService.supabase.auth.signOut()
                                                SupabaseService.supabase.auth.clearSession()
                                            }
                                            catch (e: Exception){
                                                println("Sign out error")
                                            }
                                        }
                                    }

                                }
                                openMenu = false
                            }) {
                                composable()
                            }
                        }
                    }
                    Box(modifier = Modifier.align(Alignment.CenterEnd)){
                        this@Column.AnimatedVisibility(!firstSearch){
                            Text( modifier = Modifier.align(Alignment.CenterEnd),
                                fontSize = 16.sp,
                                text=("Welcome, "+ viewModel.userName)+"! You have "+viewModel.cashmicoin +" CASHMICOIN",
                                color = MaterialTheme.colors.background
                            )
                        }
                    }
                }
                if(firstSearch){
                    Box(Modifier.fillMaxWidth().fillMaxSize(0.9f).align(Alignment.BottomCenter)){
                        ProfilePanel(modifier = Modifier.fillMaxSize(), userName =  viewModel.userName, cashmicoin = viewModel.cashmicoin)

                    }
                }



            }
            Box(Modifier.fillMaxWidth().height(60.dp)){
                Box(modifier = Modifier.align(Alignment.BottomCenter).background(MaterialTheme.colors.surface).height(30.dp).fillMaxWidth())
                Row{
                    ItemSearchBar(modifier = Modifier.fillMaxWidth().align(Alignment.CenterVertically)
                        .height(60.dp)
                        .clip(RoundedCornerShape(100.dp)),
                        viewModel = viewModel,
                        onClick = {
                            composableScope.launch{
                                viewModel.setFirstSearch(false)
                                viewModel.querryForItemList()
                                println("selectedItemIdStateFlow: "+viewModel.selectedItemIdStateFlow.value)
                            }
                        })

                }

            }
            Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.surface),
            ){
                Spacer(modifier = Modifier.size(20.dp))
                AnimatedVisibility(!firstSearch){
                    LazyColumn(modifier = Modifier.padding(horizontal = 10.dp)){
                        items(itemList){ item ->
                            Spacer(modifier = Modifier.size(20.dp))
                            ItemCard(summaryPrize = item, navigator = navigator, viewModel = viewModel, onClick = { itemPanelVisible = true })
                        }
                        item {
                            Spacer(modifier = Modifier.size(20.dp))
                            Box(Modifier.fillMaxWidth()){
                                Button(
                                    modifier = Modifier.height(60.dp).wrapContentWidth().align(Alignment.Center),
                                    shape = RoundedCornerShape(30.dp),
                                    colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
                                    onClick = { navigator.push(CreateItemSrceen(navigator,viewModel.userInfo)) }){
                                    Text(
                                        text ="Add item",
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colors.background
                                    )
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
    fun DarkThemeToggle(themeViewModel: ThemeViewModel? = null){
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
//                    changeDarkTheme()

                }
            )
        }
    }
    @Composable
    fun SignOutButton(){
        Text("Sign out")
    }
    @Composable
    fun EarnCashmicoinButton(){
        Text(
            text ="Earn CASHMICOIN")
    }
}
