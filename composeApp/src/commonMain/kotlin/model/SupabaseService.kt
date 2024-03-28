package model

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.providers.builtin.Email
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.postgrest.query.Columns
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.put

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

//        val supabaseAlt = createSupabaseClient(
//            supabaseUrl = "https://zvukdugznrucavucofvn.supabase.co",
//            supabaseKey = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6Inp2dWtkdWd6bnJ1Y2F2dWNvZnZuIiwicm9sZSI6ImFub24iLCJpYXQiOjE3MTA4OTU2MTMsImV4cCI6MjAyNjQ3MTYxM30.oT92ZKhAYZDxQqkWHMGMnKyyvxXyKcbAGo9rmp4Kw-Y"
//        ) {
//            install(Postgrest) {
//                defaultSchema = "public" // default: "public"
//                propertyConversionMethod = PropertyConversionMethod.CAMEL_CASE_TO_SNAKE_CASE // default: PropertyConversionMethod.CAMEL_CASE_TO_SNAKE_CASE
//            }
//            install(Auth)
//        }


        suspend fun loginEmail(em: String, pw: String): Result<String>{
            try {
                val loginResult = supabase.auth.signInWith(Email){
                    email = em
                    password = pw
                }
//                supabase.auth.modifyUser {
//                    data {
//                        put("display_name", "KenyNotKenny")
//                    }
//                }

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
        suspend fun signUpEmail(em: String, pw: String, name: String): Result<String>{
            try {
                val signUpResult = supabase.auth.signUpWith(Email){
                    email = em
                    password = pw
                }
                supabase.auth.modifyUser {
                    data {
                        put("display_name", name)
                        put("cashmicoin", 0)

                    }
                }

                return Result.success("Sign up success")


            } catch (e: Exception) {
                // Handle other exceptions
                println("Sign up fail")
                return Result.failure(Exception("Sign up fail!"))
            }
        }
        suspend fun logOut(){
            try {
                supabase.auth.signOut()
            }
            catch (e: Exception){}
            finally {

            }
        }
        suspend fun getCurrentUser():UserInfo = SupabaseService.supabase.auth.retrieveUserForCurrentSession(updateSession = true)


    }


}
@Serializable
data class SummaryPrize(
    var item_id: ItemImage,
    var item_name: String,
    var average_prize: Int,
    var max_prize: Int,
    var min_prize: Int,
)
@Serializable
data class EntrytDetail(
    var item_id: Int,
    val seller_id: Int,
    val user_id: String,
    val expired_date: LocalDate?,
    val price: Int,
)
@Serializable
data class ItemImage(
    val id: Int,
    val image: String?
)
@Serializable
data class Seller(
    val id: Int,
    val name: String,
    val address: String?,
    val link: String?,
)
@Serializable
data class Entry(
    val id: Int,
    val name: String,
    val address: String?,
    val link: String?,
)
@Serializable
data class Test(
    val id: Int,
    val name: String,
)