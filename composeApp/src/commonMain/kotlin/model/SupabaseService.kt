package model

import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.auth
import io.github.jan.supabase.gotrue.user.UserInfo
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.postgrest.PropertyConversionMethod
import io.github.jan.supabase.postgrest.from
import io.github.jan.supabase.storage.Storage
import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromJsonElement
import kotlinx.serialization.json.put
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
        suspend fun ChangeCashmicoin(amount:Int){
            var coin:Int  = Json.decodeFromJsonElement(getCurrentUser()?.userMetadata?.get("cashmicoin")!!)
            SupabaseService.supabase.auth.updateUser {
                data {
                    put("cashmicoin", coin + amount )
                }
            }
        }
        suspend fun setApproval(id: Int,amount: Int){
            supabase.from("verification").update(
                {
                    VerifyEntry::approval_count setTo amount
                }
            ) {
                filter {
                    eq("id", id)
                }
            }
        }
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
data class PendingEntry(
    val id:Int,
    var item_id: Item,
    val user_id: String,
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
    val image: String?,
    val description: String?,
    var category: Int?,
)
@Serializable
data class SubmitableItem(
    var name: String?,
    var image: String?,
    var description: String?,
    var category: Int?,
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
@Serializable
data class Category(
    val id: Int,
    val name: String,
)
@Serializable
data class VerifyEntry(
    val pending_entry_id: PendingEntry,
    var approval_count: Int,

)
