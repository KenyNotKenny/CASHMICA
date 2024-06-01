package viewModel

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.Category
import model.Item
import model.ItemId
import model.SupabaseService

class FilterViewModel(
    private val savedStateHandle: SavedStateHandle,
    Item: Item.Companion,
) : ViewModel() {
    var categoryListStateFlow = savedStateHandle.getStateFlow("categoryList", listOf<Category>())
    var itemListStateFlow = savedStateHandle.getStateFlow("itemList", listOf<Int>())
    var selectedCategoryStateFlow = savedStateHandle.getStateFlow("selectedCategory", 0)

    init {
        runBlocking {
            launch {
                val categories = SupabaseService.supabase
                    .from("category")
                    .select(columns = Columns.ALL)
                    .decodeList<Category>()
                savedStateHandle["categoryList"] = categories
            }
        }
    }

    fun setSelectedCategory(categoryId: Int) {
        savedStateHandle["selectedCategory"] = categoryId
        queryItemsByCategory(categoryId)
    }

    private fun queryItemsByCategory(categoryId: Int) {
        runBlocking {
            launch {
                val itemIds = SupabaseService.supabase
                    .from("item")
                    .select(columns = Columns.list("id")) {
                        filter {
                            eq("category", categoryId)
                        }
                    }
                    .decodeList<ItemId>()
                    .map { it.id }
                savedStateHandle["itemList"] = itemIds
            }
        }
    }
}
