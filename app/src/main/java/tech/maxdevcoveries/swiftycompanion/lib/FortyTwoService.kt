package tech.maxdevcoveries.swiftycompanion.lib

import com.google.gson.annotations.SerializedName
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path

const val FORTY_TWO_BASE_URL = "https://api.intra.42.fr"

data class AccessToken(
    @SerializedName("access_token")
    val accessToken:String,
    @SerializedName("token_type")
    val tokenType:String,
)

interface FortyTwoService {
    @Headers("Accept: application/json")
    @FormUrlEncoded
    @POST("oauth/token")
    fun getAccessToken(
        @Field("client_id") clientId:String,
        @Field("client_secret") clientSecret:String,
        @Field("grant_type") grantType:String = "client_credentials",
    ): Call<ResponseBody>

    @GET("v2/users/{id}")
    suspend fun getUserInfo(@Path("id") userId:String): FortyTwoUser
}
