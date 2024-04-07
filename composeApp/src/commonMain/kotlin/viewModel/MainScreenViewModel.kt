package viewModel

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import io.github.jan.supabase.gotrue.user.UserInfo
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import model.SupabaseService

class MainScreenViewModel (
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    var searchBarTextStateFlow = savedStateHandle.getStateFlow("searchBarText","")
    var firstSearchStateFlow = savedStateHandle.getStateFlow("firstSearch", true)
    var userInfo: UserInfo? = null
    var userName: String = "[Username]"
    var cashmicoin: Int = 0


    init {
        runBlocking {
            launch {
                userInfo = SupabaseService.getCurrentUser()
                userName = Json.decodeFromJsonElement(userInfo?.userMetadata?.get("display_name")!!)
                cashmicoin =  Json.decodeFromJsonElement(userInfo?.userMetadata?.get("cashmicoin")!!)

            }
        }
    }
    fun setSearchBarText(text: String){
        savedStateHandle["searchBarText"] = text
    }
    fun setFirstSearch(value: Boolean){
        savedStateHandle["firstSearch"] = value
    }

}