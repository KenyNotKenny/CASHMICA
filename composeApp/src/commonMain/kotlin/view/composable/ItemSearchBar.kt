package view.composable

import androidx.compose.foundation.clickable
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.text.TextStyle
import viewModel.MainScreenViewModel

@Composable
fun ItemSearchBar(modifier: Modifier, onClick: () -> Unit, viewModel: MainScreenViewModel){
    val searchBarText by viewModel.searchBarTextStateFlow.collectAsState()
    println(searchBarText)
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
        placeholder = { Text(text = "Seach item") },
        value = searchBarText,
        onValueChange = {viewModel.setSearchBarText(it)
        },
        textStyle = TextStyle(color = MaterialTheme.colors.onBackground),
        singleLine = true,
        trailingIcon = {
            Icon(
                modifier = Modifier.clickable { onClick() },
                imageVector  = Icons.Default.Search,
                contentDescription = "Search icon",
                tint = MaterialTheme.colors.onBackground
            )
        },

        )
}

