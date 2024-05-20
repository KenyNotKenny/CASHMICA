package view.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ExposedDropdownMenuBox
import androidx.compose.material.ExposedDropdownMenuDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Tab
import androidx.compose.material.TabPosition
import androidx.compose.material.TabRow
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.readByteArray
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.Category
import model.Seller
import model.SubmitableItem
import model.SupabaseService
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import view.composable.UploadTab

class CreateItemSrceen(private val navigator: Navigator, private val userInfo: UserInfo?):Screen {
    var options = mutableListOf<Category>()
    private var item: SubmitableItem = SubmitableItem(null,null,null,null)
    init {
        runBlocking {
            options = SupabaseService.supabase
                    .from("category")
                    .select( columns = Columns.list("id, name")) {
                    }
                    .decodeList<Category>().toMutableList()


        }
    }

    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        var selectedIndex by remember { mutableStateOf(0) }
        var nameText by remember { mutableStateOf("") }
        var previewImageURL by remember { mutableStateOf("") }
        var error by remember { mutableStateOf(false) }
        println(item.image)

        Box(modifier = Modifier.fillMaxSize()){
            Column(modifier = Modifier.fillMaxSize()){
                Box(modifier = Modifier.fillMaxWidth().aspectRatio(1.0f)){
                    if(previewImageURL.isNotEmpty()){
                        KamelImage(
                            resource = asyncPainterResource(data = previewImageURL),
                            contentDescription = null
                        )
                    }else{
                        Box(Modifier.fillMaxSize().background(MaterialTheme.colors.surface)){
                            Icon(
                                modifier = Modifier.size(160.dp).align(Alignment.Center),
                                painter = painterResource(DrawableResource("drawable/icon/insert-picture-icon.png")),
                                contentDescription = null)
                        }
                    }

                    BackButton(onClick = {navigator.pop()})

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
                        if (selectedIndex != 0) {
                            Text(
                                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.9f),
                                text = nameText,
                                fontSize = 30.sp,
                                fontWeight = FontWeight.Bold,
                            )
                        }


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
                                ItemCreatePanel(imageURL = previewImageURL,onPreviewCheck = { previewImageURL = it}, nameText = nameText, onNameChange = {nameText = it},
                                    isError = error, onUpload = {previewImageURL = it})
                            }else{
                                UploadTab(navigator = navigator ,userInfo = userInfo, submitableItem = item )
                            }
                        }


                    }

                    ItemScreenTabRow(modifier = Modifier.padding(bottom = 30.dp).align(Alignment.CenterHorizontally),
                        selectedIndex = selectedIndex,
                        onClick = { index ->
                            if(index !=0 && (nameText.isEmpty()||previewImageURL.isEmpty())){
                                error = true
                            }
                            else{
                                error = false
                                selectedIndex= index
                                item.name = nameText
                                item.image = previewImageURL
                            }
                            })


                }
            }
        }

    }

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun ItemCreatePanel(imageURL:String, onPreviewCheck:(String) -> Unit,nameText:String, onNameChange:(String)-> Unit, isError:Boolean, onUpload:(String)->Unit){
        var imageText by remember { mutableStateOf(imageURL) }
        var decriptionText by remember { mutableStateOf("")}
        Column(modifier = Modifier.fillMaxWidth()){
            TextField(
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth().height(80.dp),
                value = nameText,
                onValueChange = onNameChange,
                placeholder = { Text(text = "Item name",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold)
                },
                singleLine = true,
                maxLines = 1,
                textStyle = TextStyle( fontWeight = FontWeight.Bold, fontSize = 30.sp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                isError = isError
            )
            TextField(
                modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth().height(80.dp),
                value = decriptionText,
                onValueChange = { decriptionText = it
                                item.description = decriptionText},
                placeholder = { Text(text = "Description",
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold)
                },
                textStyle = TextStyle( fontSize = 25.sp),
                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                isError = isError
            )

            var expanded by remember { mutableStateOf(false) }
            var selectedOptionText by remember { mutableStateOf(options[0]) }

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = {
                    expanded = !expanded
                }
            ) {
                TextField(
                    modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth().height(80.dp),
                    readOnly = true,
                    value = selectedOptionText.name,
                    onValueChange = { },
                    label = { Text("Category") },
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded
                        )
                    },
                    colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
                    )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = {
                        expanded = false
                    }
                ) {
                    options.forEach { selectionOption ->
                        DropdownMenuItem(
                            onClick = {
                                selectedOptionText = selectionOption
                                expanded = false
                                item.category = selectedOptionText.id
                            }
                        ){
                            Text(
                                modifier = Modifier.fillMaxWidth().height(80.dp),
                                text = selectionOption.name)
                        }
                    }
                }
            }
            Spacer(Modifier.size(10.dp))
            Text(
                text = "Select image from your device ",
                color = Color.Black,
                fontSize = 24.sp)
//            TextField(
//                modifier = Modifier.fillMaxWidth().height(60.dp),
//                value = imageText,
//                onValueChange = { imageText = it },
//                placeholder = { Text(text = "https:/example.[png/jpg]",
//                    fontSize = 20.sp,)
//                },
//                textStyle = TextStyle(
//                    fontSize = 20.sp,
//                ),
//                colors = TextFieldDefaults.textFieldColors(backgroundColor = Color.Transparent),
//                isError = isError
//
//            )
            Spacer(Modifier.size(10.dp))
            val scope = rememberCoroutineScope()
            val context = LocalPlatformContext.current
            val pickerLauncher = rememberFilePickerLauncher(
                type = FilePickerFileType.Image,
                selectionMode = FilePickerSelectionMode.Single,
                onResult = { files ->
                    scope.launch {
                        files.firstOrNull()?.let { file ->
                            // Do something with the selected file
                            // You can get the ByteArray of the file
                            val bucket = SupabaseService.supabase.storage.from("item_image")
                            bucket.upload("$nameText.png", file.readByteArray(context), upsert = true)
                            onUpload(SupabaseService.supabase.storage.from("item_image").publicUrl("$nameText.png"))
                        }
                    }
                }
            )
            Button(modifier = Modifier.align(Alignment.End).height(60.dp),
                shape = RoundedCornerShape(percent = 100),
                onClick = {
//                    onPreviewCheck(imageText)
                    pickerLauncher.launch()
                }){
                Text(text = "Upload image",
                    color = MaterialTheme.colors.background,
                    fontSize = 20.sp,)
            }
            AnimatedVisibility(isError){
                Text("Please enter item name and image",
                    color = MaterialTheme.colors.error)
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
                    color = Color(0xFF8F00FF)
                )
            }
        }
    }

    @Composable
    fun ItemScreenTabRow(modifier: Modifier,selectedIndex:Int, onClick: (index:Int) -> Unit){

        val list = listOf("Item", "Price")

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
