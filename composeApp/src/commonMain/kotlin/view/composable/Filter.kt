package view.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.viewModelFactory
import model.Category
import viewModel.CategoryViewModel

@Composable
fun Filter(modifier: Modifier) {
    var expanded by remember { mutableStateOf(false) }
    val viewModel: CategoryViewModel = kmpViewModel(
        factory = viewModelFactory {
            CategoryViewModel(savedStateHandle = createSavedStateHandle(), Category)
        }
    )
    Box(
//            .wrapContentSize(LineHeightStyle.Alignment.TopEnd)
    ) {
        IconButton(onClick = { expanded = !expanded }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                contentDescription = "More",
                tint = Color.Black
            )
        }

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            viewModel.categoryNames.forEach{
                item->
                DropdownMenuItem(
                    onClick = {
                        expanded=false
                    }
                ){
                    Text(text = item)
                }
            }
        }
    }
}