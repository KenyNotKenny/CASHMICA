package view.composable

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Checkbox
import androidx.compose.material.CheckboxDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import io.github.jan.supabase.postgrest.from
import kotlinx.coroutines.launch
import model.SubmitableSeller
import model.SupabaseService

@Composable
fun CreateSellerPopup(onDismissRequest: () -> Unit) {
    Dialog(onDismissRequest = { onDismissRequest() }) {
        Card(
            modifier = Modifier
                .height(420.dp)
                .fillMaxWidth(0.8f),
            shape = RoundedCornerShape(30.dp),
        ) {
            val composableScope = rememberCoroutineScope()
            var nameText  by remember { mutableStateOf("") }
            val isLocal  = remember { mutableStateOf(true) }
            var linkOrAddressText  by remember { mutableStateOf("") }
            val seller = SubmitableSeller(name = nameText, address = if(isLocal.value) linkOrAddressText else null, link = if(!isLocal.value) linkOrAddressText else null )
            var isError by remember { mutableStateOf(false) }
            val throwPopup = remember { mutableStateOf(false) }


            Box(Modifier.fillMaxSize()){
                Column(modifier = Modifier.fillMaxSize(0.9f).align(Alignment.Center)) {
                    Text(
                        text = "Add a seller",
                        modifier = Modifier
                            .wrapContentSize(Alignment.TopCenter).padding(10.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 30.sp
                    )
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.8f).padding(10.dp),
                        value = nameText,
                        singleLine = true,
                        maxLines = 1,
                        onValueChange = { nameText = it },
                        shape = RoundedCornerShape(percent = 100),
                        label = {Text("Seller's name")},
                        isError = isError
                    )
                    Row {
                        Text(
                            text = "Local",
                            modifier = Modifier.padding(10.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                        Checkbox(
                            checked = isLocal.value,
                            onCheckedChange = {
                                isLocal.value = it
                                isError = false
                                              },
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary)
                        )
                    }
                    Row {
                        Text(
                            text = "Online",
                            modifier = Modifier.padding(10.dp),
                            textAlign = TextAlign.Center,
                            fontSize = 20.sp
                        )
                        Checkbox(
                            checked = !isLocal.value,
                            onCheckedChange = { isLocal.value = !it },
                            colors = CheckboxDefaults.colors(checkedColor = MaterialTheme.colors.primary)
                        )
                    }
                    OutlinedTextField(
                        modifier = Modifier.fillMaxWidth(0.8f).padding(10.dp),
                        value = linkOrAddressText,
                        singleLine = true,
                        maxLines = 1,
                        onValueChange = {
                            linkOrAddressText = it
                            isError = false
                                        },
                        shape = RoundedCornerShape(percent = 100),
                        label = {Text(text = if(isLocal.value)"Address" else "Link")},
                        isError = isError
                    )
                    Button(
                        modifier = Modifier.align(Alignment.End).padding(10.dp),
                        onClick = {
                            if(nameText.isNotEmpty() && linkOrAddressText.isNotEmpty()){
                                composableScope.launch {
                                    SupabaseService.supabase.from("seller").insert(seller)
                                    throwPopup.value = true
                                }
                            }else{
                                isError = true
                            }
                        },
                        shape = RoundedCornerShape(percent = 100)){
                        Icon(imageVector =   Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = null,
                            tint = Color.White)
                    }
                }

            }
            when{
                throwPopup.value ->{
                    SuccessPopup {
                        throwPopup.value = false
                    }
                }
            }


        }
    }
}