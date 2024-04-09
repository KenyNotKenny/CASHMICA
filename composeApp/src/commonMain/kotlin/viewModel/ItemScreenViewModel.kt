package viewModel

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import model.EntrytDetail
import model.SummaryPrize
import model.SupabaseService

class ItemScreenViewModel (
    private val savedStateHandle: SavedStateHandle,
    val summaryPrize: SummaryPrize
): ViewModel() {
    var entriesList = listOf<EntrytDetail>()

    init {
        runBlocking {
            launch {
                entriesList = (
                        SupabaseService.supabase
                            .from("entry")
                            .select(columns = Columns.list("item_id, seller_id(id, name, address, link), user_id, expired_date, price"))
                            {
                                filter {
                                    eq("item_id", summaryPrize.item_id.id)
                                }
                            }
                            .decodeList<EntrytDetail>()
                        )

                println("update entries")

            }
        }
    }
}