package viewModel

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.coroutines.flow.forEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import model.Category
import model.SummaryPrize
import model.SupabaseService

class MainScreenViewModel (
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    var searchBarTextStateFlow = savedStateHandle.getStateFlow("searchBarText","")
    var firstSearchStateFlow = savedStateHandle.getStateFlow("firstSearch", true)
    var itemListStateFlow = savedStateHandle.getStateFlow("itemList", listOf<SummaryPrize>())
    var userInfo: UserInfo? = null
    var userName: String = "[Username]"
    var cashmicoin: Int = 0
    var categoryList: List<Category> = emptyList()
    var selectedItemIdStateFlow = savedStateHandle.getStateFlow("selectedItemId", listOf<Int>())

    init {
        runBlocking {
            launch {
                userInfo = SupabaseService.getCurrentUser()
                userName = Json.decodeFromJsonElement(userInfo?.userMetadata?.get("display_name")!!)
                cashmicoin =  Json.decodeFromJsonElement(userInfo?.userMetadata?.get("cashmicoin")!!)
                categoryList = SupabaseService.supabase.from("category").select(columns = Columns.ALL).decodeList<Category>()

            }
        }
    }
    fun setSearchBarText(text: String){
        savedStateHandle["searchBarText"] = text
    }
    fun setFirstSearch(value: Boolean){
        savedStateHandle["firstSearch"] = value
    }
    fun setItemList(value: List<SummaryPrize>){
        savedStateHandle["itemList"] = value
    }

    fun setItemId(value: List<Int>){
        savedStateHandle["selectedItemId"] = value
    }
    suspend fun querryForItemList(){
//        println("selectedItemIdStateFlow: "+selectedItemIdStateFlow.value)
        savedStateHandle["itemList"] = (SupabaseService.supabase
            .from("prize_summary")
            .select(columns = Columns.list("item_id(id, name, image, description, category), item_name, average_prize, max_prize, min_prize")){
                filter {
//                            like("item_name","%"+searchBarText+"%")
                    ilike("item_name","%"+searchBarTextStateFlow.value+"%")
                     if (selectedItemIdStateFlow.value.isNotEmpty()) {
                        or {
                            selectedItemIdStateFlow.value.forEach { itemId ->
                                eq("item_id", itemId)
                            }
                        }
                     }
                }
            }
            .decodeList<SummaryPrize>())
    }
}