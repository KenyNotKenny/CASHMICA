package view.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.viewModelFactory
import io.github.jan.supabase.gotrue.user.UserInfo
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.SummaryPrize
import view.composable.DetailTab
import view.composable.UploadImagePopup
import view.composable.UploadTab
import viewModel.ItemScreenViewModel

class ItemScreen(private val summaryPrize: SummaryPrize, private val navigator: Navigator, private val userInfo: UserInfo?): Screen {


    @Composable
    override fun Content() {
//        val composableScope = rememberCoroutineScope()
//        composableScope.launch{
//
//        }
        val viewModel: ItemScreenViewModel = kmpViewModel(
            factory = viewModelFactory {
                ItemScreenViewModel(savedStateHandle = createSavedStateHandle(), summaryPrize)
            }
        )
        val throwChangeImagePopup = remember { mutableStateOf(false) }
        var selectedIndex by remember { mutableStateOf(0) }

        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.fillMaxSize()){
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)){
                    if(summaryPrize.item_id.image!= null){
                        KamelImage(
                            resource = asyncPainterResource(data = summaryPrize.item_id.image!!),
                            contentDescription = "description"
                        )
                    }

//                Button(onClick = { navigator.pop()}){
//                    Icon(Icons.Default.Close,contentDescription = null)
//                }

                    BackButton(onClick = {navigator.pop()})
                    if(selectedIndex ==1){
                        Text(modifier = Modifier.align(Alignment.TopEnd).padding(10.dp).clickable {
                                                                                                  throwChangeImagePopup.value = true
                        },
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
                                DetailTab(viewModel = viewModel)
                            }else{
                                UploadTab(navigator = navigator , userInfo = userInfo , item_id = summaryPrize.item_id.id)
                            }
                        }


                    }

                    ItemScreenTabRow(modifier = Modifier.padding(bottom = 30.dp).align(Alignment.CenterHorizontally),
                        selectedIndex = selectedIndex,
                        onClick = { index -> selectedIndex= index })


                }
            }
        }
        when{
            throwChangeImagePopup.value ->{
                UploadImagePopup(onDismissRequest =  {
                    throwChangeImagePopup.value = false
                    navigator.pop() }, item_id = summaryPrize.item_id.id)
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

}
