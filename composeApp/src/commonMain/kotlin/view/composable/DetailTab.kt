package view.composable

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import viewModel.ItemScreenViewModel


@Composable
fun DetailTab(viewModel: ItemScreenViewModel){
    Box(modifier = Modifier.fillMaxSize()){
        Column(modifier = Modifier) {
            Box(modifier = Modifier.fillMaxWidth().height(60.dp)) {
                Text(
                    modifier = Modifier.align(Alignment.CenterEnd),
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
            LazyColumn {
                items(viewModel.entriesList){ entry ->
                    Box(modifier = Modifier.fillMaxWidth().height(80.dp)) {
                        Text(
                            modifier = Modifier.align(Alignment.BottomStart),
                            text= entry.seller_id.name,
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
//            Text(
//                modifier = Modifier.align(Alignment.Center),
//                text= "Buy premium to unlock this feature",
//                fontSize = 20.sp,
//                color = MaterialTheme.colors.onBackground)
    }

}