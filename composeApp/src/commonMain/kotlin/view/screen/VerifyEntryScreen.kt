package view.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import io.github.jan.supabase.storage.storage
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.SupabaseService
import model.VerifyEntry
import org.jetbrains.compose.resources.ExperimentalResourceApi
import view.composable.addCommas

class VerifyEntryScreen(private val navigator: Navigator): Screen {
    var verifyEntryList = mutableListOf<VerifyEntry>()
    init {
        runBlocking {
            verifyEntryList = SupabaseService.supabase.from("verification").select(columns = Columns.list("pending_entry_id(id, item_id(id, name, image, description, category), user_id, price), approval_count")).decodeList<VerifyEntry>().toMutableList()
        }
    }
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    override fun Content() {
        val composableScope = rememberCoroutineScope()

        Box(modifier = Modifier.fillMaxSize()){
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(verifyEntryList) { entry ->
                    var visible by remember { mutableStateOf(true) }
                    AnimatedVisibility(visible){
                        Box(modifier = Modifier.padding(20.dp)){
                            Box(modifier = Modifier.fillMaxWidth().clip(RoundedCornerShape(40.dp))){
                                Column(modifier = Modifier.fillMaxSize().background(MaterialTheme.colors.surface)) {
                                    KamelImage(
                                        modifier = Modifier.height(200.dp),
                                        resource = asyncPainterResource(data = SupabaseService.supabase.storage.from("entry_image").publicUrl("${entry.pending_entry_id.id}.png")),
                                        contentDescription = "image",
                                    )
                                    Text(
                                        text = entry.pending_entry_id.item_id.name,
                                        fontSize = 25.sp,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = entry.pending_entry_id.price.addCommas() + " vnđ",
                                        fontSize = 30.sp,
                                        color = MaterialTheme.colors.onBackground
                                    )
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Button(
                                            modifier = Modifier.fillMaxWidth(0.5f),
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFF57777)),
                                            onClick = {
                                                composableScope.launch {
                                                    SupabaseService.setApproval(entry.pending_entry_id.id,entry.approval_count-1)
                                                    SupabaseService.ChangeCashmicoin(1)
                                                }
                                                visible = false
                                            }){
                                            Text("Reject")
                                        }
                                        Button(
                                            modifier = Modifier.fillMaxWidth(),
                                            colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFF77F5C8)),
                                            onClick = {
                                                composableScope.launch {
                                                    SupabaseService.setApproval(entry.pending_entry_id.id,entry.approval_count+1)
                                                    SupabaseService.ChangeCashmicoin(1)
                                                }
                                                visible = false

                                            }){
                                            Text("Approve")
                                        }
                                    }


                                }
                            }
                        }
                    }



                }
            }
            Box(modifier = Modifier.align(Alignment.TopStart)){
                BackButton(onClick = {navigator.pop()})
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
}