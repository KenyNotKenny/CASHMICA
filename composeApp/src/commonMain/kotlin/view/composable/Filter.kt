package view.composable

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.hoc081098.kmp.viewmodel.compose.kmpViewModel
import com.hoc081098.kmp.viewmodel.createSavedStateHandle
import com.hoc081098.kmp.viewmodel.viewModelFactory
import model.Item
import viewModel.FilterViewModel
import viewModel.MainScreenViewModel

@Composable
fun Filter(modifier: Modifier,viewModel: MainScreenViewModel) {
    var expanded by remember { mutableStateOf(false) }
    val viewModel1: FilterViewModel = kmpViewModel(
        factory = viewModelFactory {
            FilterViewModel(savedStateHandle = createSavedStateHandle(), Item)
        }
    )

//    val viewModel: MainScreenViewModel = kmpViewModel(
//        factory = viewModelFactory {
//            MainScreenViewModel(savedStateHandle = createSavedStateHandle())
//        }
//    )

    val categoryList by remember { mutableStateOf(viewModel.categoryList) }

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
            categoryList.forEach { category ->
                DropdownMenuItem(
                    onClick = {
                        viewModel1.setSelectedCategory(category.id)
                        println("selectedCategoryStateFlow: "+viewModel1.selectedCategoryStateFlow.value)
                        println("itemListStateFlow: "+viewModel1.itemListStateFlow.value)
                        viewModel.setItemId(viewModel1.itemListStateFlow.value)
                        expanded = false
                    }
                ) {
                    Text(text = category.name)
                }
            }
        }
    }
}