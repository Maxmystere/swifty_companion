package tech.maxdevcoveries.swiftycompanion.lib

import android.content.Context
import androidx.core.content.ContextCompat.getString
import com.google.gson.Gson
import okhttp3.Interceptor
import okhttp3.Response
import retrofit2.Retrofit
import tech.maxdevcoveries.swiftycompanion.R

class FortyTwoAuthInterceptor(private val context: Context) : Interceptor {
    private val sessionManager = FortyTwoSessionManager(context)

    private var accessToken = sessionManager.fetchAuthToken()

    private fun tryRegenerateToken(chain: Interceptor.Chain): Response? {
        // Generate request with retrofit
        val retrofitGetToken = Retrofit.Builder()
            .baseUrl(FORTY_TWO_BASE_URL)
            .build()
            .create(FortyTwoService::class.java)
            .getAccessToken(getString(context, R.string.forty_two_client_id), getString(context, R.string.forty_two_client_secret))

        // Pass request to okhttp chain
        val tokenResponse = chain.proceed(retrofitGetToken.request())

        // Return invalid response
        if (!tokenResponse.isSuccessful || tokenResponse.body == null) {
            return tokenResponse
        }
        val gson = Gson()
        val responseToken = gson.fromJson(tokenResponse.body!!.string(), AccessToken::class.java)
        accessToken = responseToken.accessToken
        sessionManager.saveAuthToken(responseToken.accessToken)

        // Return null since there is no invalid response
        return null
    }

    override fun intercept(chain: Interceptor.Chain): Response {

        // Get or generate the token
        if (accessToken == null)
        {
            val errorResponse = tryRegenerateToken(chain)
            if (errorResponse != null)
            {
                return errorResponse
            }
        }

        val requestBuilder = chain.request().newBuilder()

        requestBuilder.removeHeader("Authorization")
        requestBuilder.addHeader("Authorization", "Bearer $accessToken")

        var response = chain.proceed(requestBuilder.build())
        when (response.code)
        {
            // Regenerate token if they became invalid
            401 -> {
                val errorResponse = tryRegenerateToken(chain)
                if (errorResponse != null)
                {
                    return errorResponse
                }
                requestBuilder.addHeader("Authorization", "Bearer $accessToken")
                response = chain.proceed(requestBuilder.build())
            }
        }
        return response
    }
}
