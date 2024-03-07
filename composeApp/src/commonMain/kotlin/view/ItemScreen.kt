package view

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen

class ItemScreen: Screen {
    @Composable
    override fun Content() {
        Column(modifier = Modifier.fillMaxSize()){
            Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.0f).background(Color.LightGray)){
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
                        text="Ca phe wake up",
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
                            text="20,000 vnd",
                            fontSize = 40.sp,
                            color = MaterialTheme.colors.onBackground)
                    }
                    Box(modifier = Modifier.fillMaxWidth().height(60.dp)) {
                        Text(
                            modifier = Modifier.align(Alignment.CenterStart),
                            text="Lowest ▼",
                            fontSize = 25.sp,
                            color = Color.Gray)
                        Text(
                            modifier = Modifier.align(Alignment.CenterEnd),
                            text="15,000 vnd",
                            fontSize = 30.sp,
                            color = MaterialTheme.colors.onBackground)
                    }
                    LazyColumn {  }

                }


            }
        }
    }
}