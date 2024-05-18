package model

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import io.github.jan.supabase.storage.Storage
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlin.time.Duration.Companion.seconds

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
            install(Storage) {
                transferTimeout = 90.seconds // Default: 120 seconds
            }
        }
        suspend fun getCurrentUser():UserInfo = supabase.auth.retrieveUserForCurrentSession(updateSession = true)
    }
}
@Serializable
data class SummaryPrize(
    var item_id: Item,
    var item_name: String,
    var average_prize: Int,
    var max_prize: Int,
    var min_prize: Int,
)
@Serializable
data class EntrytDetail(
    var item_id: Int,
    val seller_id: Seller,
    val user_id: String,
    val expired_date: LocalDate?,
    val price: Int,
)
@Serializable
data class SubmitableEntry(
    var item_id: Int?,
    val seller_id: Int,
    val user_id: String,
    val expired_date: LocalDate?,
    val price: Int,
)
@Serializable
data class Item(
    val id: Int,
    val name: String,
    val image: String?
)
@Serializable
data class SubmitableItem(
    var name: String?,
    var image: String?
)
@Serializable
data class Seller(
    val id: Int,
    val name: String,
    val address: String?,
    val link: String?,
)
@Serializable
data class SubmitableSeller(
    val name: String,
    val address: String? = null,
    val link: String? = null,
)
