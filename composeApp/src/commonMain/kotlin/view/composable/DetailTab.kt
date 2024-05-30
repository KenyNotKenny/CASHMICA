package view.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import model.SupabaseService
import viewModel.ItemScreenViewModel


@Composable
fun DetailTab(viewModel: ItemScreenViewModel){
    val composableScope = rememberCoroutineScope()

    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Column(modifier = Modifier.fillMaxWidth()) {
                var shorten by remember { mutableStateOf(true) }
                Text(
                    modifier = Modifier
                        .padding(10.dp)
                        .clickable { shorten = !shorten },
                    text = viewModel.summaryPrize.item_id.description?.let {
                        if (shorten) {
                            it.truncateString(23) + " Read more"
                        } else {
                            it
                        }
                    } ?: "",
                    fontSize = 18.sp,
                    color = MaterialTheme.colors.onSurface
                )
                Text(
                    modifier = Modifier.align(Alignment.End),
                    text= viewModel.summaryPrize.average_prize.addCommas()+" vnđ",
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
                    text= viewModel.summaryPrize.min_prize.addCommas() +" vnd",
                    fontSize = 30.sp,
                    color = MaterialTheme.colors.onBackground)
            }
            var visible by remember { mutableStateOf(false) }

            Box {
                Box(modifier = if (visible) Modifier else Modifier.blur(30.dp)) {
                    LazyColumn(modifier = Modifier.height(200.dp)) {
                        items(viewModel.entriesList) { entry ->
                            Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
                                Text(
                                    modifier = Modifier.align(Alignment.BottomStart),
                                    text = entry.seller_id.name,
                                    fontSize = 25.sp,
                                    color = Color.Gray
                                )
                                Text(
                                    modifier = Modifier.align(Alignment.TopEnd),
                                    text = entry.price.addCommas() + " vnđ",
                                    fontSize = 30.sp,
                                    color = MaterialTheme.colors.onBackground
                                )
                            }
                        }
                    }
                }
                if (!visible) {

                    Text(
                        modifier = Modifier.align(Alignment.Center).clickable { visible = true
                            composableScope.launch {
                                SupabaseService.ChangeCashmicoin(-1)

                            }
                                                                              },
                        text = "Use 1 CASHMICOIN to see detail",
                        fontSize = 20.sp,
                        color = MaterialTheme.colors.onBackground
                    )
                }
            }


        }

    }

}