package view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.navigator.Navigator
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.SummaryPrize
import view.screen.ItemScreen
import viewModel.MainScreenViewModel


@Composable
fun ItemCard(summaryPrize: SummaryPrize, navigator: Navigator, viewModel: MainScreenViewModel, onClick: () -> Unit){
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
                        Text(text = summaryPrize.item_name.truncateString(26),
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
                                fontSize = 22.sp,
                                color = MaterialTheme.colors.onBackground
                            )


                        }
                    }
                }
            }
        }
        Button(onClick = { onClick()
            navigator.push(
                ItemScreen( summaryPrize = summaryPrize,
                    navigator = navigator,
                    userInfo = viewModel.userInfo)
            )},
            modifier= Modifier.height(50.dp).wrapContentWidth().align(Alignment.BottomEnd),  //avoid the oval shape
            shape = RoundedCornerShape(topStart = 30.dp),
            contentPadding = PaddingValues(0.dp),  //avoid the little icon
            colors = ButtonDefaults.buttonColors(
                backgroundColor =  MaterialTheme.colors.primary,
                contentColor = MaterialTheme.colors.onPrimary
            )
        ) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = "More",
                fontSize = 22.sp,
                color = MaterialTheme.colors.background)
            Icon(imageVector =  Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "More",
                tint = MaterialTheme.colors.background)
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
fun String.truncateString( maxLength: Int): String {
    return if (this.length > maxLength) {
        this.substring(0, maxLength - 3) + "..."
    } else {
        this
    }
}