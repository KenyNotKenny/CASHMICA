package viewModel

import com.hoc081098.kmp.viewmodel.SavedStateHandle
import com.hoc081098.kmp.viewmodel.ViewModel
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import kotlinx.serialization.json.put
import model.SupabaseService

class LoginScreenViewModel(
    private val savedStateHandle: SavedStateHandle,
): ViewModel() {
    val emailTextStateFlow = savedStateHandle.getStateFlow("emailText", "")
    val displayNameTextStateFlow = savedStateHandle.getStateFlow("displayNameText", "")
    val passwordTextStateFlow = savedStateHandle.getStateFlow("passwordText", "")
    fun setEmailText( text:String ){
        savedStateHandle["emailText"] = text
    }
    fun setDisplayNameText( text:String ){
        savedStateHandle["displayNameText"] = text
    }
    fun setPasswordText( text:String ){
        savedStateHandle["passwordText"] = text
    }
    suspend fun tryLogin(OnSuccess:() -> Unit, OnFailure:() -> Unit){
        try {
            SupabaseService.supabase.auth.signInWith(Email){
                email = emailTextStateFlow.value
                password = passwordTextStateFlow.value
            }
            println("Login success")
            OnSuccess()
            return
        } catch (e: Exception) {
            println("Login fail")
            OnFailure()
            return
        }

    }
    suspend fun trySignUp(OnSuccess:() -> Unit, OnFailure:() -> Unit){
        try {
            SupabaseService.supabase.auth.signUpWith(Email){
                email = emailTextStateFlow.value
                password = passwordTextStateFlow.value
            }
            SupabaseService.supabase.auth.modifyUser {
                data {
                    put("display_name", displayNameTextStateFlow.value)
                    put("cashmicoin", 0)
                }
            }
            println("Sign up succeed")
            OnSuccess()
            return


        } catch (e: Exception) {
            println("Sign up fail")
            OnFailure()
            return
        }
    }
}