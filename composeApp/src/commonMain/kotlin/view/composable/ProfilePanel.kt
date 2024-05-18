package view.composable

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import io.github.jan.supabase.storage.storage
import io.kamel.image.KamelImage
import io.kamel.image.asyncPainterResource
import model.SupabaseService
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun ProfilePanel(modifier: Modifier, userName:String , cashmicoin:Int){
    val url = SupabaseService.supabase.storage.from("item_image").publicUrl("milo.jpg")
    Column(modifier = modifier,
        verticalArrangement = Arrangement.Bottom){
        Image( modifier = Modifier.align(Alignment.CenterHorizontally).fillMaxWidth(0.6f),
            painter = painterResource(DrawableResource("drawable/logo_t.png")),
            contentDescription = null)

        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text= "Welcome, $userName!",
            color = MaterialTheme.colors.background,
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold
        )
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text= "You have $cashmicoin CASHMICOIN",
            color = MaterialTheme.colors.background,
            fontSize = 30.sp,
            fontWeight = FontWeight.Bold

        )
        Spacer(Modifier.size(20.dp))
        Text(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            text= "Check price",
            color = MaterialTheme.colors.background,
            fontSize = 20.sp,
        )
        Icon(modifier = Modifier.align(Alignment.CenterHorizontally).size(50.dp),
            imageVector =  Icons.Default.KeyboardArrowDown,
            contentDescription = null,
            tint = Color.White)
    }
}