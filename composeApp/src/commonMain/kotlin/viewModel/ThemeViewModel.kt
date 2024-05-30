package viewModel

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import model.SummaryPrize

class ThemeViewModel(
    private val savedStateHandle: SavedStateHandle
): ViewModel() {
    var isDarkThemeStateFlow = savedStateHandle.getStateFlow("isDarkTheme", false)
    fun setDarkTheme(value: Boolean){
        savedStateHandle["isDarkTheme"] = value
    }

}