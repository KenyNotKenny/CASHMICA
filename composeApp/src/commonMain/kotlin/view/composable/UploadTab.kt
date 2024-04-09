package view.composable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import model.Item
import model.Seller
import model.SubmitableEntry
import model.SubmitableItem
import model.SupabaseService

@Composable
fun UploadTab(navigator: Navigator , userInfo: UserInfo?, item_id: Int? = null, submitableItem:SubmitableItem? = null ){
    val sellerList = remember { mutableStateListOf<Seller>()}
    var seller_id by remember { mutableStateOf<Int?>(null) }
    val composableScope = rememberCoroutineScope()
    var priceText by remember { mutableStateOf("") }
    var sellerText by remember { mutableStateOf("") }
    var dayText by remember { mutableStateOf("") }
    var monthText by remember { mutableStateOf("") }
    var yearText by remember { mutableStateOf("") }
    var sellerListShow by remember { mutableStateOf(false) }
    var submitButtonVisible by remember { mutableStateOf(false) }
    val throwPopup = remember { mutableStateOf(false) }
    val createSellerPopup = remember { mutableStateOf(false) }
    var entry = SubmitableEntry(
        item_id = item_id,
        seller_id = 1 ,
        user_id = userInfo!!.id,
        expired_date = LocalDate(dayOfMonth = 1, monthNumber = 1, year = 1),
        price = 0
    )
    println("$priceText;$dayText$seller_id")
    if(seller_id==null || priceText.isEmpty()|| dayText.isEmpty() || monthText.isEmpty()|| yearText.isEmpty()){
        submitButtonVisible = false
    }else{
        entry = SubmitableEntry(
            item_id = item_id,
            seller_id = seller_id!! ,
            user_id = userInfo.id,
            expired_date = LocalDate(dayOfMonth = dayText.toInt(), monthNumber = monthText.toInt(), year = yearText.toInt()),
            price = priceText.toInt()
        )
        submitButtonVisible = true
        println(entry.toString())
    }

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
                    onValueChange = {
                        sellerText = it
                        composableScope.launch {
                            sellerList.clear()
                            sellerList.addAll(
                                SupabaseService.supabase
                                    .from("seller")
                                    .select( columns = Columns.list("id, name, address, link")) {
                                        filter {
                                            ilike("name", "%$sellerText%")
                                        }
                                    }
                                    .decodeList<Seller>()
                            )
                            println("got seller:"+sellerList.toList())
                            sellerListShow = true


                        }
                                    },
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        textAlign = TextAlign.End),)
                Icon(modifier = Modifier.align(Alignment.CenterVertically).fillMaxHeight(0.7f).aspectRatio(1f)
                    .clickable {
                        sellerListShow = !sellerListShow
                               },
                    imageVector =  if(sellerListShow){
                        Icons.Default.KeyboardArrowUp} else{
                        Icons.Default.KeyboardArrowDown}, contentDescription = null)
            }
            AnimatedVisibility(sellerListShow){
                Column{
                    sellerList.toList().toSet().forEach { seller ->
                        Column(modifier = Modifier.fillMaxWidth().wrapContentHeight()){
                            Spacer(modifier = Modifier.size(5.dp))
                            Column(modifier = Modifier.fillMaxWidth().height(70.dp).clickable {
                                sellerText = seller.name
                                seller_id = seller.id
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
            Row(modifier = Modifier.align(Alignment.CenterHorizontally).padding(top = 10.dp)) {
                Text(
                    text = "Seller isn't listed? ",
                    color = Color.Gray,
                    fontSize = 20.sp)
                Text(
                    modifier = Modifier.clickable { createSellerPopup.value = true },
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

            AnimatedVisibility(submitButtonVisible){
                Box(Modifier.fillMaxWidth()){
                    Box(
                        modifier = Modifier.align(Alignment.Center).height(60.dp).width(160.dp).clip(
                            RoundedCornerShape(30.dp)).align(Alignment.Center)
                    ){
                        Box(modifier = Modifier.fillMaxSize().background(Color(0xFF8F00FF)).clickable {
                            composableScope.launch {
                                if(item_id != null){
                                    SupabaseService.supabase.from("entry").insert(entry)
                                    throwPopup.value = true
                                }else{
                                    if( submitableItem != null){
                                        SupabaseService.supabase.from("item").insert(submitableItem)
                                        entry.item_id = SupabaseService.supabase.from("item").select(columns = Columns.ALL){
                                            filter { submitableItem.name?.let { eq("name", value = it) } }
                                        }.decodeSingle<Item>().id
                                        println(entry)
                                        SupabaseService.supabase.from("entry").insert(entry)

                                    }
                                }
                            }
                        }){
                            Text(
                                modifier = Modifier.align(Alignment.Center),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 22.sp,
                                text = "Submit ↥")
                        }
                    }
                }
            }
            Spacer(Modifier.height(10.dp))
        }
    }
    when{
        throwPopup.value ->{
            SuccessPopup {
                throwPopup.value = false
                navigator.pop() }
        }
        createSellerPopup.value ->{
            CreateSellerPopup {
                createSellerPopup.value = false
            }
        }
    }
}