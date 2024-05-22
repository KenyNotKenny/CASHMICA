package viewModel

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.Category
import model.SupabaseService

class CategoryViewModel(
    private val savedStateHandle: SavedStateHandle,
    Category: Category.Companion
) : ViewModel() {
    var categoryList = listOf<Category>()
    var categoryNames: List<String> = emptyList()

    init {
        runBlocking {
            launch {
                categoryList = (
                    SupabaseService.supabase
                        .from("category")
                        .select(columns = Columns.ALL) {}
                        .decodeList<Category>()
                )
                categoryNames = categoryList.map { it.name }
            }
        }
    }
}