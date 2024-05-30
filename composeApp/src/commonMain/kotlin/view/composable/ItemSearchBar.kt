package view.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import viewModel.MainScreenViewModel

@Composable
fun ItemSearchBar(modifier: Modifier, onClick: () -> Unit, viewModel: MainScreenViewModel){
    val searchBarText by viewModel.searchBarTextStateFlow.collectAsState()
    println(searchBarText)
    Column{
        TextField(
            modifier = modifier.onKeyEvent {
                if (it.key == Key.Enter){
                    onClick()
                    true
                }else{false}},
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = MaterialTheme.colors.background,
//                disabledTextColor = Color.Transparent,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent)
            ,
            placeholder = { Text(text = "Search item") },
            value = searchBarText,
            onValueChange = {viewModel.setSearchBarText(it)
            },
            textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
            singleLine = true,
            trailingIcon = {
                Row{
                    Icon(
                        modifier = Modifier.clickable { onClick() }.align(Alignment.CenterVertically),
                        imageVector  = Icons.Default.Search,
                        contentDescription = "Search icon",
                        tint = MaterialTheme.colors.onBackground
                    )
                    Filter(modifier = Modifier.align(Alignment.CenterVertically).background(Color.White))

                }

            },
        )

    }

}

