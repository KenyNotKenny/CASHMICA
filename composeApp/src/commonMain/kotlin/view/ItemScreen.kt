package view

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Divider
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.Icon
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Close
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
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.kamel.core.utils.URI
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import io.ktor.http.Url
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.LocalDate
import model.EntrytDetail
import model.Seller
import model.SummaryPrize
import model.SupabaseService
import model.Test

class ItemScreen(private val summaryPrize: SummaryPrize,private val navigator: Navigator): Screen {
    val tempList = listOf(1,2,3,4,5,6,7)
    var entriesList = mutableListOf<EntrytDetail>()
    var sellerList = mutableListOf<Seller>()
    var sellerText: String = ""

    init {
        runBlocking {
            launch {
                entriesList.clear()
                entriesList.addAll(
                    SupabaseService.supabase
                        .from("entry")
                        .select(columns = Columns.list("seller_id, price, expired_date"))
                        {
                            filter {
                                eq("item_id", summaryPrize.item_id)
                            }
                        }
                        .decodeList<EntrytDetail>())
                println("update entries")
                sellerList.clear()
                sellerList.addAll(
                    SupabaseService.supabase
                        .from("seller")
                        .select( columns = Columns.list("id, name, address, link")) {  }
                        .decodeList<Seller>()
                )
                println("got seller")
                SupabaseService.supabaseAlt.auth.signInWith(Email){
                    email = "keny7503@gmail.com"
                    password = "wasd1234"
                }
                SupabaseService.supabaseAlt.auth.signOut()
                println(SupabaseService.supabaseAlt.from("class").select(columns = Columns.list("id, name")) {  }.decodeList<Test>())


            }
        }
    }

    @Composable
    override fun Content() {
//        val composableScope = rememberCoroutineScope()
//        composableScope.launch{
//
//        }
        var selectedIndex by remember { mutableStateOf(0) }

        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.fillMaxSize()){
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)){

                    KamelImage(
                        resource = asyncPainterResource(data = "https://product.hstatic.net/200000401369/product/loc-4-hop-thuc-uong-lua-mach-it-duong-milo-180ml_50eba1f822fb4c69b7f325c2c00e6361.jpg"),
                        contentDescription = "description"
                    )
//                Button(onClick = { navigator.pop()}){
//                    Icon(Icons.Default.Close,contentDescription = null)
//                }

                    BackButton(onClick = {navigator.pop()})
                    if(selectedIndex ==1){
                        Text(modifier = Modifier.align(Alignment.TopEnd).padding(10.dp),
                            text = "✎ change image",
                            fontSize = 20.sp,
                            color = Color(0xFF8F00FF))
                    }
                    Box(modifier = Modifier.fillMaxWidth()
                        .height(40.dp)
                        .clip(RoundedCornerShape(topEndPercent = 100, topStartPercent = 100))
                        .align(Alignment.BottomCenter)){
                        Box(Modifier.background(Color(0xFFF4E7FF)).fillMaxSize())
                    }
                }

                Column(modifier = Modifier
                    .fillMaxSize()
                    .background(Brush.verticalGradient(listOf(Color(0xFFF4E7FF),Color.White))),){



                    Column(modifier = Modifier.fillMaxWidth(0.9f).align(Alignment.CenterHorizontally).weight(1f)){
                        Text(
                            modifier = Modifier.align(Alignment.CenterHorizontally),
                            text= summaryPrize.item_name,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colors.onBackground)
                        Spacer(Modifier.height(20.dp))
                        AnimatedContent(targetState =  selectedIndex,
                            transitionSpec = {
                                if(selectedIndex==0){
                                    (slideInHorizontally(initialOffsetX = {offset -> -offset}).togetherWith(slideOutHorizontally(targetOffsetX = { offset -> offset})))

                                }else{
                                    (slideInHorizontally(initialOffsetX = {offset -> offset}).togetherWith(slideOutHorizontally(targetOffsetX = { offset -> -offset})))
                                }

                            }

                        ){ selectedTab ->
                            if(selectedTab==0){
                                DetailTab()
                            }else{
                                UploadTab()
                            }
                        }


                    }

                    ItemScreenTabRow(modifier = Modifier.padding(bottom = 30.dp).align(Alignment.CenterHorizontally),
                        selectedIndex = selectedIndex,
                        onClick = { index -> selectedIndex= index })


                }
            }
        }

    }

    @Composable
    fun BackButton(onClick:()->Unit){
        Box(modifier = Modifier.height(60.dp).width(120.dp).clip(RoundedCornerShape(bottomEnd = 30.dp))){
            Box(modifier = Modifier.background(Color(0xFFF4E7FF)).fillMaxSize()
                .clickable(onClick = {onClick()})){
                Text(modifier = Modifier.align(Alignment.Center),
                    text = "←back",
                    fontFamily = FontFamily.SansSerif,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.sp,
                    color = Color(0xFF8F00FF))
            }
        }
    }
    @Composable
    fun ItemScreenTabRow(modifier: Modifier,selectedIndex:Int, onClick: (index:Int) -> Unit){

        val list = listOf("Detail", "Upload")

        Box(modifier = modifier
            .height(70.dp)
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(percent = 50))
            .border(width = 2.dp, color = Color(0xFF8F00FF), shape = RoundedCornerShape(percent = 50))){
            TabRow( modifier = Modifier.fillMaxSize(),
                selectedTabIndex = selectedIndex,
                backgroundColor = Color.White,

                indicator = { tabPositions: List<TabPosition> ->
                    Box {}
                }
            ) {
                list.forEachIndexed { index, text ->
                    val selected = selectedIndex == index
                    Tab(
                        modifier = if (selected) Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                Color(0xFF8F00FF)
                            )
                        else Modifier
                            .clip(RoundedCornerShape(50))
                            .background(
                                Color.White
                            ),
                        selected = selected,
                        onClick = {
                            onClick(index)
                                  },
                        text = {
                            Text(modifier = Modifier.align(Alignment.Center),
                                text = text,
                                fontFamily = FontFamily.SansSerif,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.sp,
                                color = if(selected){Color.White} else{ Color(0xFF8F00FF)})
                        }
                    )
                }
            }
        }

    }

    @Composable
    fun DetailTab(){
        Box(modifier =Modifier.fillMaxSize()){
            Column(modifier = Modifier.blur(10.dp)) {
                Box(modifier = Modifier.fillMaxWidth().height(60.dp)) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        text= summaryPrize.average_prize.addCommas()+" vnđ",
                        fontSize = 35.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onBackground)
                }
                Box(modifier = Modifier.fillMaxWidth().height(60.dp)) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterStart)
                            .clickable {  },
                        text="Lowest ▼",
                        fontSize = 25.sp,
                        color = Color.Gray)
                    Text(
                        modifier = Modifier.align(Alignment.CenterEnd),
                        text= summaryPrize.min_prize.addCommas() +" vnd",
                        fontSize = 30.sp,
                        color = MaterialTheme.colors.onBackground)
                }
                LazyColumn {
                    items(entriesList){ entry ->
                        Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
                            Text(
                                modifier = Modifier.align(Alignment.BottomStart),
                                text= entry.seller_id.toString(),
                                fontSize = 25.sp,
                                color = Color.Gray)
                            Text(
                                modifier = Modifier.align(Alignment.TopEnd),
                                text= entry.price.addCommas()+" vnđ",
                                fontSize = 30.sp,
                                color = MaterialTheme.colors.onBackground)
                        }
                    }
                }
            }
            Text(
                modifier = Modifier.align(Alignment.Center),
                text= "Buy premium to unlock this feature",
                fontSize = 20.sp,
                color = MaterialTheme.colors.onBackground)
        }

    }


    @Composable
    fun UploadTab(){
        var priceText by remember { mutableStateOf("") }
        var dayText by remember { mutableStateOf("") }
        var monthText by remember { mutableStateOf("") }
        var yearText by remember { mutableStateOf("") }
        var sellerListShow by remember { mutableStateOf(true)}

        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                Row(modifier = Modifier.height(70.dp).fillMaxWidth()){
                    Text(modifier = Modifier.align(Alignment.CenterVertically),
                        text = "Price: ",
                        color = Color.Black,
                        fontSize = 24.sp)
                    CustomTextField(modifier = Modifier.weight(1f),
                        value = priceText,
                        onValueChange = { if(it.all { char -> char.isDigit() }){ priceText=it}},
                        textStyle = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 26.sp,
                            textAlign = TextAlign.End),)
                    Text(modifier = Modifier.align(Alignment.CenterVertically),
                        text = "vnđ",
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        fontSize = 26.sp)
                }
                Row(modifier = Modifier.height(70.dp).fillMaxWidth()){
                    Text(modifier = Modifier.align(Alignment.CenterVertically),
                        text = "Seller: ",
                        color = Color.Black,
                        fontSize = 24.sp)
                    CustomTextField(modifier = Modifier.weight(1f),
                        value = sellerText,
                        onValueChange = { },
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            textAlign = TextAlign.End),)
                    Icon(modifier = Modifier.align(Alignment.CenterVertically).fillMaxHeight(0.7f).aspectRatio(1f)
                        .clickable { sellerListShow = !sellerListShow },
                        imageVector =  if(sellerListShow){Icons.Default.KeyboardArrowUp} else{Icons.Default.KeyboardArrowDown}, contentDescription = null)
                }
                AnimatedVisibility(sellerListShow){
                    Column{
                        sellerList.forEach { seller ->
                            Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()){
                                Spacer(modifier = Modifier.size(5.dp))
                                Column(modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
                                    sellerText = seller.name
                                    sellerListShow = false }){
                                    Text(
                                        text = seller.name,
                                        color = Color.Gray,
                                        fontSize = 20.sp)
                                    if(seller.link != null){
                                        Text(
                                            modifier = Modifier.align(Alignment.End).fillMaxWidth(0.9f),
                                            text = seller.link,
                                            color = Color.Gray,
                                            textAlign = TextAlign.End,
                                            fontSize = 20.sp)
                                    }
                                    if(seller.address != null){
                                        Text(
                                            modifier = Modifier.align(Alignment.End).fillMaxWidth(0.9f),
                                            text = seller.address,
                                            color = Color.Gray,
                                            textAlign = TextAlign.End,
                                            fontSize = 20.sp)
                                    }

                                }

                                Divider()
                            }
                        }
                    }

                }
                Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp),) {
                    Text(
                        text = "Seller isn't listed? ",
                        color = Color.Gray,
                        fontSize = 20.sp)
                    Text(
                        modifier = Modifier.clickable {  },
                        text = "Add seller",
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF8F00FF),
                        fontSize = 20.sp)

                }
                Row(modifier = Modifier.height(70.dp).fillMaxWidth()){
                    Text(modifier = Modifier.align(Alignment.CenterVertically),
                        text = "Price expire date: ",
                        color = Color.Black,
                        fontSize = 24.sp)
                    CustomTextField(
                        modifier = Modifier.width(60.dp),
                        value = dayText,
                        onValueChange = { if(it.all { char -> char.isDigit() }&& it.length<=2){ dayText=it}},
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            textAlign = TextAlign.End),)
                    Text(modifier = Modifier.align(Alignment.CenterVertically),
                        text = "/",
                        color = Color(0xFF8F00FF),
                        fontSize = 26.sp)
                    CustomTextField(
                        modifier = Modifier.width(60.dp),
                        value = monthText,
                        onValueChange = { if(it.all { char -> char.isDigit() }&& it.length<=2){ monthText=it}},
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            textAlign = TextAlign.End),)
                    Text(modifier = Modifier.align(Alignment.CenterVertically),
                        text = "/",
                        color = Color(0xFF8F00FF),
                        fontSize = 26.sp)
                    CustomTextField(
                        modifier = Modifier.width(120.dp),
                        value = yearText,
                        onValueChange = { if(it.all{ char -> char.isDigit() } && it.length<=4){ yearText=it}},
                        textStyle = TextStyle(
                            fontSize = 24.sp,
                            textAlign = TextAlign.End),)
                }

                Spacer(Modifier.height(10.dp))
            }
        }
    }

    @Composable
    fun CustomTextField(modifier: Modifier = Modifier, value: String, onValueChange: (String) -> Unit,textStyle: TextStyle){
        TextField( modifier = modifier.height(80.dp),
            singleLine = true,
            maxLines = 1,
            value = value,
            onValueChange = {onValueChange(it)},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.Transparent,
                unfocusedIndicatorColor = Color(0xFF8F00FF)
            ),
            textStyle = textStyle,
            keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number),
        )
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
