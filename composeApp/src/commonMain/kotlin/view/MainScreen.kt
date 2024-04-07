package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Menu
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.viewModelFactory
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import model.SummaryPrize
import model.SupabaseService
import view.composable.ItemSearchBar
import view.composable.ProfilePanel
import viewModel.MainScreenViewModel


class MainScreen : Screen {
    private val menuList = listOf<@Composable () -> Unit>(
        {DarkThemeToggle()},
        {SignOutButton()}
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
        val itemList = remember { mutableStateListOf<SummaryPrize>() }

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
                                if(index == 1){
                                    navigator.push(LoginScreen())
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
                                openMenu = false
                            }) {
                                composable()
                            }
                        }
                    }
                    Box(modifier = Modifier.align(Alignment.CenterEnd)){
                        this@Column.AnimatedVisibility(!firstSearch){
                            Text( modifier = Modifier.align(Alignment.CenterEnd),
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
//                Box(modifier = Modifier.align(Alignment.TopCenter).background(MaterialTheme.colors.primary).height(30.dp).fillMaxWidth())
                Box(modifier = Modifier.align(Alignment.BottomCenter).background(MaterialTheme.colors.surface).height(30.dp).fillMaxWidth())
                ItemSearchBar(modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
                    .height(60.dp)
                    .clip(RoundedCornerShape(100.dp)),
                    viewModel = viewModel,
                    onClick = {
                        composableScope.launch{
                            viewModel.setFirstSearch(false)
                            itemList.clear()
                            itemList.addAll(SupabaseService.supabase
                                .from("prize_summary")
                                .select(columns = Columns.list("item_id(id, name, image), item_name, average_prize, max_prize, min_prize")){
                                    filter {
//                            like("item_name","%"+searchBarText+"%")
                                        ilike("item_name","%"+viewModel.searchBarTextStateFlow.value+"%")
                                    }
                                }
                                .decodeList<SummaryPrize>())
                            println(
                                SupabaseService.getCurrentUser().userMetadata
                            )
                        }
                    })
            }
            Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.surface),
            ){
                Spacer(modifier = Modifier.size(20.dp))
                LazyColumn{
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

//        AnimatedVisibility(itemPanelVisible){
//            ItemDetail()
//        }
//        AnimatedVisibility(itemPanelVisible){
//            ItemPanel(onClickOut = {itemPanelVisible = false})
//        }


    }

    @Composable
    fun ItemCard(summaryPrize: SummaryPrize,navigator: Navigator, viewModel: MainScreenViewModel, onClick: () -> Unit){
        Box(modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clip(RoundedCornerShape(30.dp))){
            Box(modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colors.background)){
                Row (
                    modifier = Modifier.fillMaxSize(),
                    verticalAlignment = Alignment.CenterVertically) {
                    Box( modifier = Modifier
                        .fillMaxHeight(1f)
                        .aspectRatio(1f)){
                        Box(modifier =  Modifier
                            .fillMaxSize(0.8f)
                            .align(Alignment.Center)
                            .clip(RoundedCornerShape(20.dp))){
                            Box( modifier =  Modifier.fillMaxSize().background(Color.Transparent)){
                                if(summaryPrize.item_id.image!= null){
                                    KamelImage(
                                        resource = asyncPainterResource(data = summaryPrize.item_id.image!!),
                                        contentDescription = "description"
                                    )
                                }
                            }
                        }
                    }
                    Box(modifier = Modifier.fillMaxSize()){
                        Box( modifier =  Modifier
                            .fillMaxHeight(0.8f)
                            .fillMaxWidth(0.9f)
                            .align(Alignment.Center)){
                            Text(text = summaryPrize.item_name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 27.sp,
                                color = MaterialTheme.colors.onBackground
                            )
                            Box(
                                modifier = Modifier
                                    .align(Alignment.BottomStart)
                                    .fillMaxWidth(0.9f)
                                    .wrapContentHeight()
                            ) {
                                Text(
                                    modifier = Modifier.align(Alignment.CenterStart),
                                    text =  summaryPrize.average_prize.addCommas() +" vnđ",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 24.sp,
                                    color = MaterialTheme.colors.onBackground
                                )

                                Button(onClick = { onClick()
                                                 navigator.push(ItemScreen( summaryPrize = summaryPrize,
                                                     navigator = navigator,
                                                     userInfo = viewModel.userInfo))},
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
//                    changeDarkTheme()
                }
            )
        }

    }
    @Composable
    fun SignOutButton(){
        Text("Sign out")
    }
    private fun Int.addCommas(): String {
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
