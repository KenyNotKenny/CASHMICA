package view.composable

import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.gotrue.user.UserInfo
import model.EntrytDetail
import model.Seller


var sellerText: String = ""
var seller_id:Int? = null

@Composable
fun UploadTab(sellerList:MutableList<Seller>, userInfo: UserInfo?, item_id: Int){
    var priceText by remember { mutableStateOf("") }
    var dayText by remember { mutableStateOf("") }
    var monthText by remember { mutableStateOf("") }
    var yearText by remember { mutableStateOf("") }
    var sellerListShow by remember { mutableStateOf(false) }

    if(seller_id==null || priceText.isEmpty()){}else{
        var entry: EntrytDetail = EntrytDetail(
            item_id = item_id,
            seller_id = seller_id!! ,
            user_id = userInfo!!.id,
            expired_date = null,
            price = priceText.toInt()
        )

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
                    onValueChange = { },
                    textStyle = TextStyle(
                        fontSize = 24.sp,
                        textAlign = TextAlign.End),)
                Icon(modifier = Modifier.align(Alignment.CenterVertically).fillMaxHeight(0.7f).aspectRatio(1f)
                    .clickable { sellerListShow = !sellerListShow },
                    imageVector =  if(sellerListShow){
                        Icons.Default.KeyboardArrowUp} else{
                        Icons.Default.KeyboardArrowDown}, contentDescription = null)
            }
            AnimatedVisibility(sellerListShow){
                Column{
                    sellerList.forEach { seller ->
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