package view

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import model.EntrytDetail
import model.SummaryPrize
import model.SupabaseService

class ItemScreen(private val summaryPrize: SummaryPrize,private val navigator: Navigator): Screen {
    val tempList = listOf(1,2,3,4,5,6,7)
    var entriesList = mutableListOf<EntrytDetail>()

    @Composable
    override fun Content() {
        val composableScope = rememberCoroutineScope()
        composableScope.launch{
            entriesList.clear()
            entriesList.addAll(
                SupabaseService.supabase
                .from("entry")
                .select(columns = Columns.list("seller_id, price, expired_date"))
                .decodeList<EntrytDetail>())
            println("update list")
        }
        var expandLowprize by remember { mutableStateOf(false) }
        Column(modifier = Modifier.fillMaxSize()){
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.0f).background(Color.LightGray)){
                Button(onClick = { navigator.pop()}){
                    Icon(Icons.Default.Close,contentDescription = null)
                }
                Box(modifier = Modifier.fillMaxWidth()
                    .height(40.dp)
                    .clip(RoundedCornerShape(topEndPercent = 100, topStartPercent = 100))
                    .align(Alignment.BottomCenter)){
                    Box(Modifier.background(MaterialTheme.colors.background).fillMaxSize())
                }
            }
            Box(modifier = Modifier.fillMaxSize()){
                Column(modifier = Modifier.fillMaxWidth(0.9f).align(Alignment.TopCenter)) {
                    Text(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        text= summaryPrize.item_name,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colors.onBackground)
                    Box(modifier = Modifier.fillMaxWidth().height(60.dp)) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text="Prize:",
                            fontSize = 30.sp,
                            color = Color.Gray)
                        Text(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            text= summaryPrize.average_prize.addCommas()+" vnđ",
                            fontSize = 40.sp,
                            color = MaterialTheme.colors.onBackground)
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(60.dp)) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterStart)
                                .clickable { expandLowprize = !expandLowprize  },
                            text="Lowest ▼",
                            fontSize = 25.sp,
                            color = Color.Gray)
                        Text(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            text= summaryPrize.min_prize.addCommas() +" vnd",
                            fontSize = 30.sp,
                            color = MaterialTheme.colors.onBackground)
                    }
                    AnimatedVisibility(expandLowprize){
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


                }


            }
        }
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