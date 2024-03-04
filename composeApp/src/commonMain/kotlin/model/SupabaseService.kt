package model

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.serialization.Serializable

class SupabaseService {
    companion object{
        val supabase = createSupabaseClient(
            supabaseUrl = "https://pskpouhbuvzeqdhjtfdu.supabase.co",
            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6InBza3BvdWhidXZ6ZXFkaGp0ZmR1Iiwicm9sZSI6ImFub24iLCJpYXQiOjE3MDkzMDU2NzEsImV4cCI6MjAyNDg4MTY3MX0.Dayhg5OljEZ0mX4mDG8YkWvdmi3wwBystnRr4i0PnqE"
        ) {
            install(Postgrest) {
                defaultSchema = "public" // default: "public"
                propertyConversionMethod = PropertyConversionMethod.CAMEL_CASE_TO_SNAKE_CASE // default: PropertyConversionMethod.CAMEL_CASE_TO_SNAKE_CASE
            }
            install(Auth)
        }

        suspend fun loginEmail(em: String, pw: String): Result<String>{
            try {
                val loginResult = supabase.auth.signInWith(Email){
                    email = em
                    password = pw
                }

//        SupabaseService.supabase.auth.sessionStatus.collect {
//            when(it) {
//                is SessionStatus.Authenticated -> println(it.session.user)
//                SessionStatus.LoadingFromStorage -> println("Loading from storage")
//                SessionStatus.NetworkError -> println("Network error")
//                SessionStatus.NotAuthenticated -> println("Not authenticated")
//            }
//            repeat(5){
//                println("In sesson")
//                delay(2000)
//            }
//            return@collect
//        }
                return Result.success("Login success")


            } catch (e: Exception) {
                // Handle other exceptions
                println("Login fail")
                return Result.failure(Exception("Login fail!"))
            }
        }
        suspend fun signUpEmail(em: String, pw: String): Result<String>{
            try {
                val signUpResult = supabase.auth.signUpWith(Email){
                    email = em
                    password = pw
                }
                return Result.success("Sign up success")


            } catch (e: Exception) {
                // Handle other exceptions
                println("Sign up fail")
                return Result.failure(Exception("Sign up fail!"))
            }
        }
    }


}
@Serializable
data class Account(
    var username: String,
    var password: String,
    var email: String,
    var credit: Int
)